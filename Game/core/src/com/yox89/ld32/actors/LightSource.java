package com.yox89.ld32.actors;

import box2dLight.ConeLight;
import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.yox89.ld32.Physics;
import com.yox89.ld32.raytracing.Direction;
import com.yox89.ld32.raytracing.LightColor;
import com.yox89.ld32.raytracing.RayDispatcher.Ray;
import com.yox89.ld32.raytracing.RayDispatcher.RayRequest;
import com.yox89.ld32.screens.TiledLevelScreen;
import com.yox89.ld32.util.Assets;
import com.yox89.ld32.util.Collision;
import com.yox89.ld32.util.PhysicsUtil;
import com.yox89.ld32.util.PhysicsUtil.BodyParams;
import com.yox89.ld32.util.Ui;

public class LightSource extends PhysicsActor implements Disposable {

	private static final boolean DEBUG_LASERS = false;

	private LightColor mColor;

	private PointLight mLight;

	private ShapeRenderer mShapeRenderer;

	private Direction[] mDirections;

	private final Physics mPhysics;

	private Array<Ray> res = null;
	private Array<ConeLight> mRayCones = new Array<ConeLight>();

	private boolean mCanBeActivated;

	private Texture mTexture;

	public LightSource(final TiledLevelScreen levelScreen, final Ui ui,
			Physics physics, LightColor color, Direction... lightDirections) {
		setTouchable(Touchable.enabled);

		mPhysics = physics;
		mColor = color;
		mDirections = lightDirections;
		setTouchable(Touchable.enabled);

		mCanBeActivated = true;
		Texture img;
		if (mColor == LightColor.GREEN) {
			img = new Texture("green_lantern.png");
		} else {
			img = new Texture("red_lantern.png");
		}
		this.mTexture = img;
		mTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		initPhysicsBody(PhysicsUtil.createBody(new BodyParams(physics.world) {

			@Override
			public BodyType getBodyType() {
				return BodyType.StaticBody;
			}

			@Override
			public short getCollisionType() {
				return Collision.WORLD;
			}

			@Override
			public short getCollisionMask() {
				return Collision.WORLD | Collision.PLAYER;
			}

			@Override
			public void setShape(PolygonShape ps) {
				ps.setAsBox(1f / 2, 1f / 2, new Vector2(1f / 2, 1f / 2), 0f);
			}
		}));
		mLight = new PointLight(physics.rayHandler, 10, mColor.toColor(), 1f,
				4f, 4f);
		mLight.setSoftnessLength(.2f);

		mShapeRenderer = new ShapeRenderer();
		mShapeRenderer.setAutoShapeType(true);

		setSize(1f, 1f);

		addListener(new InputListener() {

			public boolean touchDown(InputEvent event, float x, float y,
					int pointer, int button) {
				if (!levelScreen.mouseIsInRangeOfPlayer()) {
					return false;
				}
				if (!mCanBeActivated) {
					return false;
				}
				mCanBeActivated = false;

				final Array<RayRequest> reqs = new Array<RayRequest>();
				final Vector2 lightPos = new Vector2(mLight.getPosition());
				for (Direction dir : mDirections) {
					reqs.add(new RayRequest(mColor, lightPos, dir));
				}

				Assets.laser.play();

				res = mPhysics.rayDispatcher.dispatch(reqs);
				for (Ray ray : res) {
					final float dist = new Vector2(ray.src).sub(ray.dst).len() * 2;
					final ConeLight cone = new ConeLight(mPhysics.rayHandler,
							10, new Color(ray.color.toColor()), dist, x, y,
							ray.direction.getAngleDegrees(), 50 / dist);

					cone.setPosition(ray.src);
					mRayCones.add(cone);
				}
				ui.removeFluffText();

				final Color blockColor = mColor.toColor();
				addAction(Actions.sequence(Actions.fadeIn(.2f), Actions.color(
						new Color(blockColor.r / 2, blockColor.g / 2,
								blockColor.b / 2, 0f), .3f), Actions
						.run(new Runnable() {

							@Override
							public void run() {
								disposeRays();
								mLight.setActive(false);

								for (Actor a : getStage().getActors()) {
									if (a instanceof LightSource
											&& ((LightSource) a).mCanBeActivated) {
										return;
									}
								}

								levelScreen.onNoMoreLights();
							}

						})));
				return true;
			};
		});
	}

	public boolean canBeAtivated() {
		return mCanBeActivated;
	}

	@Override
	public void act(float delta) {
		mLight.setPosition(getX() + getWidth() / 2, getY() + getHeight() / 2);
		if (mRayCones.size > 0) {
			final float targetAlpha = getColor().a;
			for (ConeLight cl : mRayCones) {
				Color c = cl.getColor();
				c.a = targetAlpha;
				cl.setColor(c);
			}
		}
		super.act(delta);
	}

	private void disposeRays() {
		if (res != null) {
			for (ConeLight cl : mRayCones) {
				cl.remove();
				cl.dispose();
			}
			mRayCones.clear();
			res = null;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		final Color c = getColor();
		batch.setColor(new Color(c.r, c.g, c.b, 1f));
		batch.draw(mTexture, getX(), getY(), getOriginX() - getWidth() / 2,
				getOriginY() - getHeight() / 2, getWidth(), getHeight(),
				getScaleX(), getScaleY(), getRotation(), 0, 0,
				mTexture.getWidth(), mTexture.getHeight(), false, false);
		batch.setColor(Color.WHITE);

		// if (DEBUG_LASERS) {
		// if (mDirections != null && mDirections.length != 0) {
		// batch.end();
		// mShapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
		// mShapeRenderer.begin();
		//
		// final float LIGHT_LEN = 10f;
		// mShapeRenderer.setColor(new Color(mColor.toColor()).sub(0f, 0f,
		// 0f, .25f));
		//
		// for (Direction d : mDirections) {
		// switch (d) {
		// case EAST:
		// mShapeRenderer.rect(getX() + getWidth(), getY(),
		// LIGHT_LEN, getHeight());
		// break;
		// case SOUTH:
		// mShapeRenderer.rect(getX(), getY() - LIGHT_LEN,
		// getWidth(), LIGHT_LEN);
		// break;
		// case WEST:
		// mShapeRenderer.rect(getX() - LIGHT_LEN, getY(),
		// LIGHT_LEN, getHeight());
		// break;
		// case NORTH:
		// mShapeRenderer.rect(getX(), getY() + getHeight(),
		// getWidth(), LIGHT_LEN);
		// break;
		// default:
		// System.err
		// .println("Illegal direction for light source "
		// + d);
		// break;
		// }
		// }
		// if (res != null) {
		// mShapeRenderer.setColor(Color.MAGENTA);
		// Gdx.gl.glLineWidth(.1f);
		// mShapeRenderer.set(ShapeType.Line);
		//
		// for (Ray ray : res) {
		// mShapeRenderer.line(ray.src, ray.dst);
		// }
		// }
		//
		// mShapeRenderer.end();
		// batch.begin();
		// }
		// }

	}

	@Override
	public void dispose() {
		mLight.dispose();
		disposeRays();
	}
}

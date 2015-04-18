package com.yox89.ld32.actors;

import box2dLight.ConeLight;
import box2dLight.PointLight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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
import com.yox89.ld32.util.Collision;
import com.yox89.ld32.util.PhysicsUtil;
import com.yox89.ld32.util.PhysicsUtil.BodyParams;

public class LightSource extends TexturedPhysicsActor implements Disposable {

	private LightColor mColor;

	private PointLight mLight;

	private ShapeRenderer mShapeRenderer;

	private Direction[] mDirections;

	private final Physics mPhysics;

	private Array<Ray> res = null;
	private Array<ConeLight> mRayCones = new Array<ConeLight>();

	public LightSource(Physics physics, LightColor color,
			Direction... lightDirections) {
		setTouchable(Touchable.enabled);

		mPhysics = physics;
		mColor = color;
		mDirections = lightDirections;

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
				final Array<RayRequest> reqs = new Array<RayRequest>();
				final Vector2 lightPos = new Vector2(mLight.getPosition());
				for (Direction dir : mDirections) {
					reqs.add(new RayRequest(mColor, lightPos, dir));
				}

				res = mPhysics.rayDispatcher.dispatch(reqs);
				for (Ray ray : res) {
					final float dist = new Vector2(ray.src).sub(ray.dst).len() * 2;
					final ConeLight cone = new ConeLight(mPhysics.rayHandler,
							10, ray.color.toColor(), dist, x, y,
							ray.direction.getAngleDegrees(), 100 / dist);

					cone.setPosition(ray.src);
					mRayCones.add(cone);
				}

				addAction(Actions.delay(1f, Actions.run(new Runnable() {

					@Override
					public void run() {
						disposeRays();
					}

				})));
				return true;
			};
		});
	}

	@Override
	public void act(float delta) {
		mLight.setPosition(getX() + getWidth() / 2, getY() + getHeight() / 2);
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
	protected String getTextureName() {
		switch (mColor) {
		case GREEN:
			return "tileGreen_01";
		case RED:
		default:
			return "tileRed_01";
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);

		if (mDirections != null && mDirections.length != 0) {
			batch.end();
			mShapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
			mShapeRenderer.begin();

			final float LIGHT_LEN = 10f;
			mShapeRenderer.setColor(new Color(mColor.toColor()).sub(0f, 0f, 0f,
					.25f));

			for (Direction d : mDirections) {
				switch (d) {
				case EAST:
					mShapeRenderer.rect(getX() + getWidth(), getY(), LIGHT_LEN,
							getHeight());
					break;
				case SOUTH:
					mShapeRenderer.rect(getX(), getY() - LIGHT_LEN, getWidth(),
							LIGHT_LEN);
					break;
				case WEST:
					mShapeRenderer.rect(getX() - LIGHT_LEN, getY(), LIGHT_LEN,
							getHeight());
					break;
				case NORTH:
					mShapeRenderer.rect(getX(), getY() + getHeight(),
							getWidth(), LIGHT_LEN);
					break;
				default:
					System.err.println("Illegal direction for light source "
							+ d);
					break;
				}
			}
			if (res != null) {
				mShapeRenderer.setColor(Color.MAGENTA);
				Gdx.gl.glLineWidth(.1f);
				mShapeRenderer.set(ShapeType.Line);

				for (Ray ray : res) {
					mShapeRenderer.line(ray.src, ray.dst);
				}
			}

			mShapeRenderer.end();
			batch.begin();
		}
	}

	@Override
	public void dispose() {
		mLight.dispose();
		disposeRays();
	}
}

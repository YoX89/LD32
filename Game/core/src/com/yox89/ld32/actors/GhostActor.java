package com.yox89.ld32.actors;

import java.util.ArrayList;

import box2dLight.ConeLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.yox89.ld32.Physics;
import com.yox89.ld32.particles.ParticleEffect;
import com.yox89.ld32.particles.ParticlePool;
import com.yox89.ld32.raytracing.RayDispatcher.Dispatcher;
import com.yox89.ld32.raytracing.RayDispatcher.Ray;
import com.yox89.ld32.raytracing.RayDispatcher.RayRequest;
import com.yox89.ld32.raytracing.RayDispatcher.RayTarget;
import com.yox89.ld32.screens.TiledLevelScreen;
import com.yox89.ld32.util.Assets;
import com.yox89.ld32.util.Collision;
import com.yox89.ld32.util.PhysicsUtil;
import com.yox89.ld32.util.PhysicsUtil.BodyParams;

public class GhostActor extends PhysicsActor implements Disposable, RayTarget, GamePositioned {

	public final boolean isNotMoving;

	private static final float SPEED = 3f;
	private final Texture mTexture;
	private float angleDegree;

	private final TiledLevelScreen mOwner;

	private ConeLight mLightVision;

	public GhostActor(TiledLevelScreen owner, Physics physicsWorld,
			ArrayList<Vector2> positions, float angleDegree) {
		isNotMoving = positions.size() <= 1;
		this.angleDegree = angleDegree;
		mOwner = owner;
		final Texture img = new Texture("ghost_sprite.png");
		this.mTexture = img;
		Color color = getColor();
		color.a = 0.8f;
		this.setColor(color);
		
		mLightVision = new ConeLight(physicsWorld.rayHandler, 10, new Color(1,
				0, 1, .75f), 6.25f, 0f, 0f, 0f, 30f);
		mLightVision.setXray(true);

		final Vector2[] polygonShape = this.getShapeVertices();

		this.initPhysicsBody(PhysicsUtil.createBody(new BodyParams(
				physicsWorld.world) {

			@Override
			public BodyType getBodyType() {
				return BodyType.DynamicBody;
			}

			@Override
			public short getCollisionType() {
				return Collision.GHOST_VISION;
			}

			@Override
			public short getCollisionMask() {
				return Collision.PLAYER;
			}

			@Override
			public void setShape(PolygonShape ps) {
				ps.set(polygonShape);
			}

			@Override
			public boolean isSensor() {
				return true;
			}

		}));
		this.setSize(1f, 1f);

		assert (positions.size() > 0) : "The ghost must have at least one position";
		Vector2 initialPosition = positions.get(0);

		this.setPosition(initialPosition.x, initialPosition.y);

		this.setupActions(positions);

		final FixtureDef ghostBody = new FixtureDef();
		ghostBody.filter.categoryBits = Collision.GHOST_BODY;
		ghostBody.filter.maskBits = Collision.NONE;
		ghostBody.isSensor = true;

		final CircleShape cs = new CircleShape();
		if (isNotMoving) {
			final Vector2 off = new Vector2(0.5f, 0.5f);
			if (angleDegree < 90) {
				off.set(.5f, .5f);
			} else if (angleDegree < 180) {
				off.set(.5f, -.5f);
			} else if (angleDegree < 270) {
				off.set(-.5f, -.5f);
			} else {
				off.set(-.5f, .5f);
			}
			cs.setPosition(off);
		}

		cs.setRadius(.45f);
		ghostBody.shape = cs;


		getPhysicsBody().createFixture(ghostBody).setUserData(this);
		cs.dispose();
	}

	@Override
	public void act(float delta) {
		super.act(delta);
		mLightVision.setDirection(getRotation());
		if (isNotMoving) {
			mLightVision.setPosition(getX() + getWidth() / 2, getY()
					+ getHeight() / 2);
		} else {
			mLightVision.setPosition(getX(), getY());
		}
	}

	private final Vector2 gamePosition = new Vector2(-1, -1);

	@Override
	public Vector2 getGamePosition() {
		return gamePosition;
	}

	@Override
	public void setPosition(float x, float y) {
		gamePosition.set(x, y);
		super.setPosition(x, y);
	}

	private Vector2[] getShapeVertices() {
		Vector2[] vertices = new Vector2[3];
		float offset = 0f;
		if (isNotMoving) {
			offset = 0.5f;
		}
		if (angleDegree < 90) {
			vertices[0] = new Vector2(0f + offset, 0f + offset);
			vertices[1] = new Vector2(4f + offset, -2f + offset);
			vertices[2] = new Vector2(4f + offset, 2f + offset);
		} else if (angleDegree < 180) {
			vertices[0] = new Vector2(0f + offset, 0f - offset);
			vertices[1] = new Vector2(4f + offset, -2f - offset);
			vertices[2] = new Vector2(4f + offset, 2f - offset);
		} else if (angleDegree < 270) {
			vertices[0] = new Vector2(0f - offset, 0f - offset);
			vertices[1] = new Vector2(4f - offset, -2f - offset);
			vertices[2] = new Vector2(4f - offset, 2f - offset);
		} else {
			vertices[0] = new Vector2(0f - offset, 0f + offset);
			vertices[1] = new Vector2(4f - offset, -2f + offset);
			vertices[2] = new Vector2(4f - offset, 2f + offset);
		}
		return vertices;
	}

	private void setupActions(ArrayList<Vector2> positions) {

		if (positions.size() > 1) {
			Vector2 startPosition = positions.get(0);
			Vector2 currentPosition = startPosition;

			Action[] actions = new Action[positions.size()];

			for (int i = 1; i < positions.size(); i++) {
				Vector2 newPosition = positions.get(i);

				final Vector2 diff = new Vector2(newPosition.x
						- currentPosition.x, newPosition.y - currentPosition.y);
				float length = diff.len();

				final float duration = length / SPEED;

				actions[i - 1] = Actions.sequence(Actions.parallel(
						Actions.run(new Runnable() {

							@Override
							public void run() {
								RotateToAction rotateToAction = getRotateActionUsingClosestDirection(
										diff.angle(), duration / 4);
								addAction(rotateToAction);
							}
						}), Actions.moveTo(newPosition.x, newPosition.y,
								duration)));
				diff.angle();

				currentPosition = newPosition;
			}

			final Vector2 diff = new Vector2(startPosition.x
					- currentPosition.x, startPosition.y - currentPosition.y);
			float length = diff.len();
			final float duration = length / SPEED;

			actions[actions.length - 1] = Actions.parallel(
					Actions.run(new Runnable() {

						@Override
						public void run() {
							RotateToAction rotateToAction = getRotateActionUsingClosestDirection(
									diff.angle(), duration / 4);
							addAction(rotateToAction);
						}
					}), Actions.moveTo(startPosition.x, startPosition.y,
							duration));

			SequenceAction moveActions = Actions.sequence(actions);

			RotateToAction rotateToAction = getRotateActionUsingClosestDirection(
					diff.angle(), 0f);
			addAction(rotateToAction);
			this.addAction(Actions.forever(moveActions));
		}
		
		float duration = MathUtils.random() * 2f + 1f;
		float lowAlpha = Math.min(0.8f, MathUtils.random());
		
		this.addAction(Actions.forever(Actions.sequence(Actions.alpha(lowAlpha, duration), Actions.alpha(0.8f, duration))));
	}

	public RotateToAction getRotateActionUsingClosestDirection(
			float targetAngle, float duration) {
		float curr = getRotation();
		if (curr >= 360f) {
			curr %= 360f;
		} else if (curr <= -360f) {
			curr = 360f + (curr % -360f);
		}
		setRotation(curr);

		final float diff = targetAngle - curr;

		if (Math.abs(diff) > 180f) {
			setRotation(curr - 360f);
		}
		return Actions.rotateTo(targetAngle, duration);
	}

	boolean mIsHit = false;

	@Override
	public void onHitWithRay(Ray ray, Dispatcher dispatcher) {
		mIsHit = true;
		addAction(Actions.removeActor());

		ParticleEffect pe = ParticlePool.get();
		float ang = getRotation();

		Vector2 sides = new Vector2(getWidth(), getHeight());
		sides.rotate(ang);
		if (isNotMoving) {
			pe.setPosition(getX(), getY());
		} else {
			pe.setPosition(getX() - getWidth() / 2, getY() - getHeight() / 2);
		}
		pe.setSize(getWidth(), getHeight());

		TextureRegion ectoplasmRegion = new TextureRegion(Assets.ectoplasm);
		
		pe.init(ectoplasmRegion, 200.0f, 25, .5f);
		getParent().addActor(pe);
		Assets.beaver_death.play();

		if (mLightVision.isActive()) {
			mLightVision.setActive(false);
			mLightVision.remove();
		}

		Array<RayRequest> reqs = new Array<RayRequest>();
		reqs.add(new RayRequest(ray.color, ray.dst, ray.direction));
		dispatcher.dispatch(reqs);

		for (Actor a : getStage().getActors()) {
			if (a instanceof GhostActor && !((GhostActor) a).mIsHit) {
				return;
			}
		}
		mOwner.onAllGhostsDead();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		float x, y, originX, originY;

		if (isNotMoving) {
			x = getX();
			y = getY();
			originX = getOriginX() + getWidth() / 2;
			originY = getOriginY() + getHeight() / 2;
		} else {
			x = getX() - getWidth() / 2;
			y = getY() - getHeight() / 2;
			originX = getOriginX() + getWidth() / 2;
			originY = getOriginY() + getHeight() / 2;
		}
		
		batch.setColor(getColor());
		
		batch.draw(mTexture, x, y, originX, originY, getWidth(), getHeight(),
				getScaleX(), getScaleY(), getRotation() + 180f, 0, 0,
				mTexture.getWidth(), mTexture.getHeight(), false, false);
	}

	@Override
	public void dispose() {
		mTexture.dispose();
		mLightVision.dispose();
	}
}

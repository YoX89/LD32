package com.yox89.ld32.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.yox89.ld32.util.Collision;
import com.yox89.ld32.util.PhysicsUtil.BodyParams;

public class PlayerActor extends PhysicsActor {

	private static final float ROTATION_UP = 90;
	private static final float ROTATION_DOWN = -90;
	private static final float ROTATION_RIGHT = 0;
	private static final float ROTATION_LEFT = 180;

	private Texture bodyTexture;
	private float speed;
	private float angularSpeed;

	public PlayerActor(Texture bodyTexture,World world) {
		this.bodyTexture = bodyTexture;
		this.speed = 10f;
		this.angularSpeed = 3;
		setSize(5f, 5f);
		
		
		
		initPhysicsBody(createBody(world, BodyType.StaticBody, Collision.WORLD, (short)(Collision.WORLD | Collision.PLAYER)));

	}

	@Override
	public void act(float delta) {
		super.act(delta);
		if (Gdx.input.isKeyPressed(Keys.UP)) {
			moveBy(0f, delta * speed);
			rotateTowards(ROTATION_UP, angularSpeed);
		} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
			moveBy(0f, delta * -speed);
			rotateTowards(ROTATION_DOWN, angularSpeed);
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
			moveBy(delta * speed, 0f);
			rotateTowards(ROTATION_RIGHT, angularSpeed);
		} else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
			moveBy(delta * -speed, 0f);
			rotateTowards(ROTATION_LEFT, angularSpeed);
		}
	}

	private void rotateTowards(float rotationGoal, float rotationChange) {
		float currentRotation = getRotation() % 360;
		if (currentRotation > 180) {
			currentRotation = -180 + (currentRotation - 180);
		} else if (currentRotation < -180) {
			currentRotation = 180 + (currentRotation + 180);
		}
		
		boolean leftSemi = currentRotation < -90 || currentRotation > 90;
		boolean lowerSemi = currentRotation < 0;
		int clockWise = 1;
		
		
		if (rotationGoal == ROTATION_UP) {
			clockWise = leftSemi? -1:1;
		} else if (rotationGoal == ROTATION_DOWN) {
			clockWise = leftSemi? 1:-1;
		}else if (rotationGoal == ROTATION_LEFT) {
			clockWise = lowerSemi? -1:1;
		}else if (rotationGoal == ROTATION_RIGHT) {
			clockWise = lowerSemi? 1:-1;
		}
		
		rotateBy(rotationChange*clockWise);

	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(bodyTexture, getX(), getY(),
				getOriginX() + (getWidth() / 2), getOriginY()
						+ (getHeight() / 2), getWidth(), getHeight(),
				getScaleX(), getScaleY(), getRotation(), 0, 0,
				bodyTexture.getWidth(), bodyTexture.getHeight(), false, false);
	}

	
	public static Body createBody(World world, BodyType bodyType, short collisionType, short collisionMask) {
		final BodyDef bd = new BodyDef();
		bd.type = bodyType;
		bd.position.set(0.0f, 10.0f);
		bd.linearDamping = 0.2f;

		final Body body = world.createBody(bd);

		final FixtureDef fd = new FixtureDef();
		fd.density = 0.0f;
		fd.filter.categoryBits = collisionType;

		fd.filter.maskBits = collisionMask;

		fd.restitution = 0.0f;
		fd.friction = 0.0f;

		final CircleShape shape = new CircleShape();
		fd.shape = shape;

		body.createFixture(fd);

		shape.dispose();

		return body;
	}
}

package com.yox89.ld32.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.yox89.ld32.Physics;
import com.yox89.ld32.util.Collision;

public class PlayerActor extends PhysicsActor {

	private static final float ROTATION_UP = 90;
	private static final float ROTATION_DOWN = -90;
	private static final float ROTATION_RIGHT = 0;
	private static final float ROTATION_LEFT = 180;

	private float speed;
	private float angularSpeed;
	private Animation animation;
	private float rotationPriority = 0f;

	public PlayerActor(Physics physics) {
		this.animation = setupAnimation();
		this.speed = 10f;
		this.angularSpeed = 10f;
		setSize(2.3f, 2.3f);
		initPhysicsBody(createBody(physics, BodyType.DynamicBody,
				Collision.PLAYER, (short) (Collision.WORLD | Collision.GHOST)));

	}

	private Animation setupAnimation() {
		int frameRows = 4;
		int frameColumns = 4;
		Texture walkSheet = new Texture(
				Gdx.files.internal("animation_sheet2.png"));
		TextureRegion[][] tmp = TextureRegion.split(walkSheet,
				walkSheet.getWidth() / frameColumns, walkSheet.getHeight()
						/ frameRows); // #10
		TextureRegion[] walkFrames = new TextureRegion[frameColumns * frameRows];
		int index = 0;
		for (int i = 0; i < frameRows; i++) {
			for (int j = 0; j < frameColumns; j++) {
				walkFrames[index++] = tmp[i][j];
			}
		}
		animation = new Animation(2.25f, walkFrames);
		animation.setPlayMode(PlayMode.LOOP);
		return animation;

	}

	@Override
	public void act(float delta) {
		super.act(delta);

		final Vector2 movement = new Vector2();

		if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
			movement.y++;
			rotationPriority = ROTATION_UP;
		}

		if (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) {
			movement.y--;
			rotationPriority = ROTATION_DOWN;
		}

		if (Gdx.input.isKeyPressed(Keys.RIGHT)
				|| Gdx.input.isKeyPressed(Keys.D)) {
			movement.x++;
			rotationPriority = ROTATION_RIGHT;
		}

		if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
			movement.x--;
			rotationPriority = ROTATION_LEFT;
		}

		movement.nor().scl(delta * speed);
		rotateTowards(rotationPriority, angularSpeed);
		moveBy(movement.x, movement.y);
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
			clockWise = leftSemi ? -1 : 1;
		} else if (rotationGoal == ROTATION_DOWN) {
			clockWise = leftSemi ? 1 : -1;
		} else if (rotationGoal == ROTATION_LEFT) {
			clockWise = lowerSemi ? -1 : 1;
		} else if (rotationGoal == ROTATION_RIGHT) {
			clockWise = lowerSemi ? 1 : -1;
		}
		rotationChange = (Math.abs(rotationGoal - currentRotation) < rotationChange) ? 1
				: rotationChange;

		rotateBy(rotationChange * clockWise);

	}

	float stateTime = 0f;

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(animation.getKeyFrame(++stateTime), getX()
				- (getWidth() / 2), getY() - (getHeight() / 2), getOriginX()
				+ (getWidth() / 2), getOriginY() + (getHeight() / 2),
				getWidth(), getHeight(), getScaleX(), getScaleY(),
				getRotation(), true);
	}

	public static Body createBody(Physics physics, BodyType bodyType,
			short collisionType, short collisionMask) {
		final BodyDef bd = new BodyDef();
		bd.type = bodyType;
		bd.position.set(0.0f, 10.0f);
		bd.linearDamping = 0.2f;

		final Body body = physics.world.createBody(bd);

		final FixtureDef fd = new FixtureDef();
		fd.density = 0.0f;
		fd.filter.categoryBits = collisionType;

		fd.filter.maskBits = collisionMask;

		fd.restitution = 0.0f;
		fd.friction = 0.0f;

		final CircleShape shape = new CircleShape();
		shape.setRadius(0.4f);
		fd.shape = shape;

		body.createFixture(fd);

		shape.dispose();

		return body;

	}
}

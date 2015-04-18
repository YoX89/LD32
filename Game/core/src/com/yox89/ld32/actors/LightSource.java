package com.yox89.ld32.actors;

import box2dLight.PointLight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Disposable;
import com.yox89.ld32.Physics;
import com.yox89.ld32.util.Collision;
import com.yox89.ld32.util.PhysicsUtil;
import com.yox89.ld32.util.PhysicsUtil.BodyParams;

public class LightSource extends TexturedPhysicsActor implements Disposable {

	public enum LightColor {
		GREEN, RED;

		public Color toColor() {
			switch (this) {
			case GREEN:
				return Color.GREEN;
			case RED:
			default:
				return Color.RED;
			}
		}
	}

	public enum Direction {
		LEFT, RIGHT, UP, DOWN
	}

	private LightColor mColor;

	private PointLight mLight;

	private ShapeRenderer mShapeRenderer;

	private Direction[] mDirections;

	public LightSource(Physics physics, LightColor color,
			Direction... lightDirections) {
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

		setSize(1f, 1f);
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		mLight.setPosition(getX() + getWidth() / 2, getY() + getHeight() / 2);
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

		if (mDirections != null && mDirections.length != 0 && Gdx.graphics.getFrameId() % 120 < 60) {
			batch.end();
			mShapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
			mShapeRenderer.begin(ShapeType.Filled);

			final float LIGHT_LEN = 10f;
			mShapeRenderer.setColor(mColor.toColor().sub(0f, 0f, 0f, .25f));
			
			for (Direction d : mDirections) {
				switch (d) {
				case RIGHT:
					mShapeRenderer.rect(getX() + getWidth(), getY(), LIGHT_LEN,
							getHeight());
					break;
				case DOWN:
					mShapeRenderer.rect(getX(), getY() - LIGHT_LEN, getWidth(),
							LIGHT_LEN);
					break;
				case LEFT:
					mShapeRenderer.rect(getX() - LIGHT_LEN, getY(), LIGHT_LEN,
							getHeight());
					break;
				case UP:
					mShapeRenderer.rect(getX(), getY() + getHeight(),
							getWidth(), LIGHT_LEN);
					break;

				}
			}
			mShapeRenderer.end();
			batch.begin();
		}
	}

	@Override
	public void dispose() {
		mLight.dispose();
	}
}

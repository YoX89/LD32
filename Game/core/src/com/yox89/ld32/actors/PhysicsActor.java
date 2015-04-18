package com.yox89.ld32.actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class PhysicsActor extends Actor {

	protected Body mPhysicsBody;

	protected PhysicsActor() {
	}

	protected void initPhysicsBody(Body b) {
		this.mPhysicsBody = b;
		b.setUserData(this);
		for (Fixture f : b.getFixtureList()) {
			f.setUserData(this);
		}
	}

	public void act(float delta) {
		super.act(delta);

	}

	@Override
	public boolean remove() {
		if (super.remove()) {
			mPhysicsBody.getWorld().destroyBody(mPhysicsBody);

			return true;
		}
		return false;
	}

	@Override
	public float getX() {
		return mPhysicsBody.getPosition().x;
	}

	@Override
	public float getY() {
		return mPhysicsBody.getPosition().y;
	}

	@Override
	public float getX(int alignment) {
		return getX();
	}

	@Override
	public float getY(int alignment) {
		return getY();
	}

	@Override
	public void setX(float x) {
		mPhysicsBody.setTransform(x, getY(), mPhysicsBody.getAngle());
	}

	@Override
	public void setY(float y) {
		mPhysicsBody.setTransform(getX(), y, mPhysicsBody.getAngle());
	}

	@Override
	public void setPosition(float x, float y) {
		mPhysicsBody.setTransform(x, y, mPhysicsBody.getAngle());
	}

	@Override
	public void moveBy(float x, float y) {
		setPosition(getX() + x, getY() + y);
	}

	@Override
	public void setPosition(float x, float y, int alignment) {
		setPosition(x, y);
	}

	/**
	 * Converts the coordinates given in the parent's coordinate system to this
	 * actor's coordinate system.
	 */
	public Vector2 parentToLocalCoordinates(Vector2 parentCoords) {
		final float rotation = getRotation();
		final float scaleX = getScaleX();
		final float scaleY = getScaleY();
		final float childX = getX();
		final float childY = getY();
		if (rotation == 0) {
			if (scaleX == 1 && scaleY == 1) {
				parentCoords.x -= childX;
				parentCoords.y -= childY;
			} else {
				final float originX = getOriginX();
				final float originY = getOriginY();
				parentCoords.x = (parentCoords.x - childX - originX) / scaleX
						+ originX;
				parentCoords.y = (parentCoords.y - childY - originY) / scaleY
						+ originY;
			}
		} else {
			final float cos = (float) Math.cos(rotation
					* MathUtils.degreesToRadians);
			final float sin = (float) Math.sin(rotation
					* MathUtils.degreesToRadians);
			final float originX = getOriginX();
			final float originY = getOriginY();
			final float tox = parentCoords.x - childX - originX;
			final float toy = parentCoords.y - childY - originY;
			parentCoords.x = (tox * cos + toy * sin) / scaleX + originX;
			parentCoords.y = (tox * -sin + toy * cos) / scaleY + originY;
		}
		return parentCoords;
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (touchable && getTouchable() != Touchable.enabled)
			return null;
		return x >= 0 && x < getWidth() && y >= 0 && y < getHeight() ? this
				: null;
	}

	@Override
	public void rotateBy(float amountInDegrees) {
		setRotation(getRotation() + amountInDegrees);
	}

	@Override
	public float getRotation() {
		return mPhysicsBody.getAngle() * MathUtils.radiansToDegrees;
	}

	@Override
	public void setRotation(float rot) {
		mPhysicsBody.setTransform(mPhysicsBody.getPosition(), rot
				* MathUtils.degreesToRadians);
	}

	public void draw(SpriteBatch batch, float parentAlpha, TextureRegion tr) {
		batch.draw(tr, getX(), getY(), getOriginX(), getOriginY(), getWidth(),
				getHeight(), getScaleX(), getScaleY(), getRotation());
	}
}
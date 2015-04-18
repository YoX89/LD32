package com.yox89.ld32.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.yox89.ld32.util.Assets;
import com.yox89.ld32.util.Collision;
import com.yox89.ld32.util.PhysicsUtil;
import com.yox89.ld32.util.PhysicsUtil.BodyParams;

public abstract class TexturedPhysicsActor extends PhysicsActor {

	public TexturedPhysicsActor() {
	}
	
	protected abstract String getTextureName();

	@Override
	public void draw(Batch batch, float parentAlpha) {
		final TextureRegion region = Assets.get(getTextureName());
		batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),
				getWidth(), getHeight(), getScaleX(), getScaleY(),
				getRotation());
	}

}

package com.yox89.ld32.actors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.yox89.ld32.util.Assets;
import com.yox89.ld32.util.Collision;
import com.yox89.ld32.util.PhysicsUtil;
import com.yox89.ld32.util.PhysicsUtil.BodyParams;

public class Wall extends TexturedPhysicsActor {

	public Wall(World world, final float width, final float height) {
		setTouchable(Touchable.enabled);
		
		initPhysicsBody(PhysicsUtil.createBody(new BodyParams(world) {

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
				ps.setAsBox(width / 2, height / 2, new Vector2(width / 2,
						height / 2), 0f);
			}
		}));
		setSize(width, height);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		final TextureRegion region = Assets.get(getTextureName());
		
		for (int i = 0; i < getWidth(); i++) {
			for (int j = 0; j < getHeight(); j++) {
				batch.draw(region, getX() + i, getY() + j, getOriginX(), getOriginY(), 1f, 1f, getScaleX(),
						getScaleY(), getRotation());
			}
		}
	}

	@Override
	protected String getTextureName() {
		return "tileGrey_01";
	}
}

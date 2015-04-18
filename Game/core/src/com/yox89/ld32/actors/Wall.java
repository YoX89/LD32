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

public class Wall extends PhysicsActor {

	public static final float WALL_SIZE = 4f;

	public Wall(World world) {

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
				ps.setAsBox(WALL_SIZE / 2, WALL_SIZE / 2, new Vector2(
						WALL_SIZE / 2, WALL_SIZE / 2), 0f);
			}
		}));
		setSize(WALL_SIZE, WALL_SIZE);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		final TextureRegion region = Assets.get("tileGrey_01");
		batch.draw(region, getX(), getY(), getOriginX(),
				getOriginY(), getWidth(), getHeight(), getScaleX(),
				getScaleY(), getRotation());
	}

}

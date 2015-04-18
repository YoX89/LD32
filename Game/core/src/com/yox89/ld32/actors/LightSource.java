package com.yox89.ld32.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.yox89.ld32.util.Collision;
import com.yox89.ld32.util.PhysicsUtil;
import com.yox89.ld32.util.PhysicsUtil.BodyParams;

public class LightSource extends TexturedPhysicsActor {

	public enum LightColor {
		GREEN, RED
	}

	private LightColor mColor;

	public LightSource(World world, LightColor color) {
		mColor = color;

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
				ps.setAsBox(1f / 2, 1f / 2, new Vector2(1f / 2, 1f / 2), 0f);
			}
		}));
		setSize(1f, 1f);
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
}

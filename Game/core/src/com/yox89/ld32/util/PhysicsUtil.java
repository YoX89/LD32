package com.yox89.ld32.util;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public abstract class PhysicsUtil {

	public static class BodyParams {

		private World mWorld;
		private BodyType mBodyType;
		private short mCollisionType;
		private short mCollisionMask;

		public BodyParams(World world) {
			this(world, BodyType.StaticBody, Collision.WORLD,
					(short) (Collision.WORLD | Collision.PLAYER));
		}

		public BodyParams(World world, BodyType bodyType, short collisionType,
				short collisionMask) {
			mWorld = world;
			mBodyType = bodyType;
			mCollisionType = collisionType;
			mCollisionMask = collisionMask;
		}

		public BodyType getBodyType() {
			return mBodyType;
		}

		public short getCollisionType() {
			return mCollisionType;
		}

		public short getCollisionMask() {
			return mCollisionMask;
		}

		public void setShape(PolygonShape ps) {
			ps.setAsBox(.5f, .5f, new Vector2(.5f, .5f), 0f);
		}

		public boolean isSensor() {
			return false;
		}
	}

	public static Body createBody(BodyParams params) {
		final BodyDef bd = new BodyDef();
		bd.type = params.getBodyType();
		bd.position.set(0.0f, 10.0f);
		bd.linearDamping = 0.2f;

		final Body body = params.mWorld.createBody(bd);

		final FixtureDef fd = new FixtureDef();
		fd.density = 0.0f;
		fd.filter.categoryBits = params.getCollisionType();

		fd.filter.maskBits = params.getCollisionMask();

		fd.isSensor = params.isSensor();

		fd.restitution = 0.0f;
		fd.friction = 0.0f;

		final PolygonShape ps = new PolygonShape();
		params.setShape(ps);
		fd.shape = ps;

		body.createFixture(fd);

		ps.dispose();

		return body;
	}
}

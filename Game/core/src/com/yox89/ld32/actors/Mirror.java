package com.yox89.ld32.actors;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.yox89.ld32.Physics;
import com.yox89.ld32.raytracing.Direction;
import com.yox89.ld32.raytracing.RayDispatcher.Dispatcher;
import com.yox89.ld32.raytracing.RayDispatcher.Ray;
import com.yox89.ld32.raytracing.RayDispatcher.RayRequest;
import com.yox89.ld32.raytracing.RayDispatcher.RayTarget;
import com.yox89.ld32.util.Collision;
import com.yox89.ld32.util.PhysicsUtil;
import com.yox89.ld32.util.PhysicsUtil.BodyParams;

public class Mirror extends TexturedPhysicsActor implements RayTarget {

	public Mirror(Physics physics) {

		initPhysicsBody(PhysicsUtil.createBody(new BodyParams(physics.world) {

			@Override
			public BodyType getBodyType() {
				return BodyType.StaticBody;
			}

			@Override
			public short getCollisionType() {
				return Collision.MIRROR;
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
		return "tileBlue_14";
	}

	@Override
	public void onHitWithRay(Ray ray, Dispatcher dispatcher) {
		System.out.println("got hit");
		Array<RayRequest> reqs = new Array<RayRequest>();
		reqs.add(new RayRequest(ray.color, ray.dst, Direction.SOUTH));
		reqs.add(new RayRequest(ray.color, ray.dst, Direction.NORTH));
		dispatcher.dispatch(reqs);
	}
}

package com.yox89.ld32.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.yox89.ld32.Physics;
import com.yox89.ld32.raytracing.Direction;
import com.yox89.ld32.raytracing.RayDispatcher.Dispatcher;
import com.yox89.ld32.raytracing.RayDispatcher.Ray;
import com.yox89.ld32.raytracing.RayDispatcher.RayRequest;
import com.yox89.ld32.raytracing.RayDispatcher.RayTarget;
import com.yox89.ld32.util.Collision;
import com.yox89.ld32.util.PhysicsUtil;
import com.yox89.ld32.util.PhysicsUtil.BodyParams;

public class Mirror extends PhysicsActor implements RayTarget, Disposable {

	private Texture mTexture;

	public Mirror(Physics physics) {
		mTexture = new Texture(Gdx.files.internal("mirror.png"));
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
				ps.setAsBox(1f / 2, 1f / 2, new Vector2(0f, 0f), 0f);
			}
		}));
		setSize(1f, 1f);
		// setOrigin(1f, 1f);
		addAction(Actions.forever(Actions.rotateBy(360f, 25f)));
	}

	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x + .5f, y + .5f);
	}

	@Override
	public void onHitWithRay(Ray ray, Dispatcher dispatcher) {
		Array<RayRequest> reqs = new Array<RayRequest>();

		final Direction currDir = Direction.fromAngle(getRotation());
		if (currDir.add45().isParallell(ray.direction)) {
			reqs.add(new RayRequest(ray.color, ray.dst, ray.direction.sub90()));
		} else if (currDir.sub45().isParallell(ray.direction)) {
			reqs.add(new RayRequest(ray.color, ray.dst, ray.direction.add90()));
		}
		if (reqs.size > 0) {
			dispatcher.dispatch(reqs);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(mTexture, getX() - .5f, getY() - .5f, .5f, .5f, getWidth(),
				getHeight(), getScaleX(), getScaleY(), getRotation(), 0, 0,
				mTexture.getWidth(), mTexture.getHeight(), false, false);
	}

	@Override
	public void dispose() {
		mTexture.dispose();
	}
}

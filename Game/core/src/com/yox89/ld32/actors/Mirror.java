package com.yox89.ld32.actors;

import box2dLight.PointLight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.yox89.ld32.Physics;
import com.yox89.ld32.raytracing.Direction;
import com.yox89.ld32.raytracing.RayDispatcher.Dispatcher;
import com.yox89.ld32.raytracing.RayDispatcher.Ray;
import com.yox89.ld32.raytracing.RayDispatcher.RayRequest;
import com.yox89.ld32.raytracing.RayDispatcher.RayTarget;
import com.yox89.ld32.util.Collision;
import com.yox89.ld32.util.MirrorInventory;
import com.yox89.ld32.util.PhysicsUtil;
import com.yox89.ld32.util.PhysicsUtil.BodyParams;

public class Mirror extends PhysicsActor implements RayTarget, Disposable,
		GamePositioned {

	public static final int TYPE_90_DEG = 0, TYPE_SPLITTER = 1;

	private Texture mTexture;

	private Vector2 gamePosition = new Vector2(-1, -1);

	private final int mType;
	private PointLight mLight;

	public Mirror(Physics physics, int type) {
		setTouchable(Touchable.enabled);

		mType = type;

		mTexture = new Texture(
				Gdx.files
						.internal(type == TYPE_SPLITTER ? "mirror_splitter.png"
								: "mirror.png"));
		mTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
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
		mLight = new PointLight(physics.rayHandler, 5, new Color(1f, 1f, 1f,
				0.75f), 1.5f, 0f, 0f);
	}

	@Override
	public boolean remove() {
		if (super.remove()) {
			mLight.setActive(false);

			return true;
		}
		return false;
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		mLight.setPosition(getX(), getY());
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		return super.hit(x + .5f, y + .5f, touchable);
	}

	@Override
	public Vector2 getGamePosition() {
		return gamePosition;
	}

	@Override
	public void setPosition(float x, float y) {
		gamePosition.set(x, y);
		super.setPosition(x + .5f, y + .5f);
	}

	@Override
	public void onHitWithRay(Ray ray, Dispatcher dispatcher) {
		Array<RayRequest> reqs = new Array<RayRequest>();

		final Direction currDir = Direction.fromAngle(getRotation());

		if (mType == TYPE_SPLITTER) {
			reqs.add(new RayRequest(ray.color, ray.dst, currDir));
			reqs.add(new RayRequest(ray.color, ray.dst, currDir.add90()));
			reqs.add(new RayRequest(ray.color, ray.dst, currDir.add90().add90()));
			reqs.add(new RayRequest(ray.color, ray.dst, currDir.sub90()));
		} else {
			if (currDir.add45().isParallell(ray.direction)) {
				reqs.add(new RayRequest(ray.color, ray.dst, ray.direction
						.sub90()));
			} else if (currDir.sub45().isParallell(ray.direction)) {
				reqs.add(new RayRequest(ray.color, ray.dst, ray.direction
						.add90()));
			}
		}
		if (reqs.size > 0) {
			dispatcher.dispatch(reqs);
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.setColor(Color.WHITE);
		batch.draw(mTexture, getX() - .5f, getY() - .5f, .5f, .5f, getWidth(),
				getHeight(), getScaleX(), getScaleY(), getRotation(), 0, 0,
				mTexture.getWidth(), mTexture.getHeight(), false, false);
	}

	@Override
	public void dispose() {
		mTexture.dispose();
		mLight.remove();
		mLight.dispose();
	}

	public String getHumanReadableType() {
		switch (mType) {
		case TYPE_90_DEG:
			return MirrorInventory.MIRROR_TYPE_NORMAL;
		case TYPE_SPLITTER:
			return MirrorInventory.MIRROR_TYPE_SPLITTER;
		default:
			return "" + mType;
		}

	}
}

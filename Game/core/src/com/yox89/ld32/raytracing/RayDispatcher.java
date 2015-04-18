package com.yox89.ld32.raytracing;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class RayDispatcher {

	public interface Dispatcher {
		void dispatch(Array<RayRequest> rays);
	}

	private World mWorld;

	public RayDispatcher(World world) {
		mWorld = world;
	}

	public Array<Ray> dispatch(Array<RayRequest> rays) {
		final Array<Ray> ret = new Array<Ray>();
		for (final RayRequest req : rays) {

			final Vector2 src = req.position;
			final Vector2 dst = new Vector2(src);

			final Vector2 ang = new Vector2(40f, 0f);
			ang.rotate(req.direction.getAngleDegrees());
			dst.add(ang);

			final Ray[] res = new Ray[1];
			mWorld.rayCast(new RayCastCallback() {

				float minFraction = 999999f;

				@Override
				public float reportRayFixture(Fixture fixture, Vector2 point,
						Vector2 normal, float fraction) {

					if (fraction < minFraction) {
						res[0] = new Ray(req.direction, req.color, new Vector2(
								src), new Vector2(point), fixture);
						minFraction = fraction;
					}
					return fraction * 0.9f;
				}
			}, src, dst);

			final Ray ray = res[0];
			if (ray != null) {
				ret.add(ray);
				final Object usd = ray.hit.getUserData();
				if (usd instanceof RayTarget) {

					((RayTarget) usd).onHitWithRay(ray, new Dispatcher() {

						@Override
						public void dispatch(Array<RayRequest> rays) {
							ret.addAll(RayDispatcher.this.dispatch(rays));
						}
					});
				}
			}
		}
		return ret;
	}

	public static class RayRequest {
		public final LightColor color;
		public final Vector2 position;
		public final Direction direction;

		public RayRequest(LightColor color, Vector2 position,
				Direction direction) {
			this.color = color;
			this.position = position;
			this.direction = direction;
		}
	}

	public interface RayTarget {
		void onHitWithRay(Ray ray, Dispatcher dispatcher);
	}

	public static class Ray {
		public final Direction direction;
		public final LightColor color;
		public final Vector2 src, dst;

		private final Fixture hit;

		private Ray(Direction direction, LightColor color, Vector2 src,
				Vector2 dst, Fixture hit) {
			this.direction = direction;
			this.color = color;
			this.src = src;
			this.dst = dst;
			this.hit = hit;
		}

		@Override
		public String toString() {
			return color.toString() + " Ray " + direction;
		}
	}
}

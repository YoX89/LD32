package com.yox89.ld32;

import box2dLight.RayHandler;

import com.badlogic.gdx.physics.box2d.World;
import com.yox89.ld32.raytracing.RayDispatcher;

public class Physics {

	public final World world;
	public final RayHandler rayHandler;
	public final RayDispatcher rayDispatcher;

	public Physics(World world, RayHandler rayHandler,
			RayDispatcher rayDispatcher) {
		this.world = world;
		this.rayHandler = rayHandler;
		this.rayDispatcher = rayDispatcher;
	}
}

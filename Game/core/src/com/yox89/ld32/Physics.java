package com.yox89.ld32;

import box2dLight.RayHandler;

import com.badlogic.gdx.physics.box2d.World;

public class Physics {

	public final World world;
	public final RayHandler rayHandler;

	public Physics(World world, RayHandler rayHandler) {
		this.world = world;
		this.rayHandler = rayHandler;
	}
}

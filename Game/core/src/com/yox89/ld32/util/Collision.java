package com.yox89.ld32.util;

public class Collision {

	public static final short NONE = 0,
			PLAYER = 1 << 0,
			WORLD = 1 << 1,
			GHOST_VISION = 1 << 2,
			GHOST_BODY = 1 << 3,
			MIRROR = 1 << 3;
}

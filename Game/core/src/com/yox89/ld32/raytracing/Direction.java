package com.yox89.ld32.raytracing;

import com.badlogic.gdx.math.Vector2;

public enum Direction {
	WEST, EAST, NORTH, SOUTH, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST;

	public float getAngleDegrees() {
		switch (this) {
		default:
		case EAST:
			return 0;
		case NORTHEAST:
			return 45;
		case NORTH:
			return 90;
		case NORTHWEST:
			return 135;
		case WEST:
			return 180;
		case SOUTHWEST:
			return 215;
		case SOUTH:
			return 270;
		case SOUTHEAST:
			return 315;
		}
	}
	public Vector2 getAngleVec2() {
		switch (this) {
		default:
		case EAST:
			return new Vector2(1, 0);
		case NORTHEAST:
			return new Vector2(1, 1);
		case NORTH:
			return new Vector2(0, 1);
		case NORTHWEST:
			return new Vector2(-1, 1);
		case WEST:
			return new Vector2(-1, 0);
		case SOUTHWEST:
			return new Vector2(-1, -1);
		case SOUTH:
			return new Vector2(0, -1);
		case SOUTHEAST:
			return new Vector2(1, -1);
		}
	}

}
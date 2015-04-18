package com.yox89.ld32.raytracing;

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

}
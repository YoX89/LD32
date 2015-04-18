package com.yox89.ld32.raytracing;

import com.badlogic.gdx.math.Vector2;

public enum Direction {
	WEST, EAST, NORTH, SOUTH, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST;

	public static Direction fromAngle(float degrees) {
		while (degrees < 0f) {
			degrees += 360f;
		}
		float rounded = 45f * Math.round(degrees / 45f);
		rounded %= 360f;
		for (Direction d : Direction.values()) {
			if (Math.abs(d.getAngleDegrees() - rounded) < .01f) {
				return d;
			}
		}
		System.out.println("Unable to determine angle for " + degrees);
		return Direction.EAST;
	}

	public boolean isParallell(Direction other) {
		return this == other || this == (other.add90().add90());
	}

	public Direction add90() {
		return fromAngle(getAngleDegrees() + 90f);
	}

	public Direction add45() {
		return fromAngle(getAngleDegrees() + 45f);
	}
	public Direction sub45() {
		return fromAngle(getAngleDegrees() - 45f);
	}

	public Direction sub90() {
		return fromAngle(getAngleDegrees() - 90f);
	}

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
			return 225;
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
package com.yox89.ld32.raytracing;

import com.badlogic.gdx.math.Vector2;

public class Direction {
	public static final Direction WEST = new Direction();
	public static final Direction EAST = new Direction();
	public static final Direction NORTH = new Direction();
	public static final Direction SOUTH = new Direction();
	public static final Direction NORTHEAST = new Direction();

	public static final Direction NORTHWEST = new Direction();
	public static final Direction SOUTHEAST = new Direction();
	public static final Direction SOUTHWEST = new Direction();

	private static Direction[] VALUES;
	static {
		VALUES = new Direction[] { WEST, EAST, NORTH, SOUTH, NORTHEAST,
				NORTHWEST, SOUTHEAST, SOUTHWEST };
	}

	public Direction() {
	}

	public static Direction fromAngle(float degrees) {
		while (degrees < 0f) {
			degrees += 360f;
		}
		float rounded = 45f * Math.round(degrees / 45f);
		rounded %= 360f;
		for (Direction d : VALUES) {
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
		if (this == EAST) {
			return 0;
		} else if (this == NORTHEAST) {
			return 45;
		} else if (this == NORTH) {
			return 90;
		} else if (this == NORTHWEST) {
			return 135;
		} else if (this == WEST) {
			return 180;
		} else if (this == SOUTHWEST) {
			return 225;
		} else if (this == SOUTH) {
			return 270;
		} else if (this == SOUTHEAST) {
			return 315;
		} else {
			System.err.println("Unknown direction " + this);
			return 0;
		}
	}

	public Vector2 getAngleVec2() {
		if (this == EAST) {
			return new Vector2(1, 0);
		} else if (this == NORTHEAST) {
			return new Vector2(1, 1);
		} else if (this == NORTH) {
			return new Vector2(0, 1);
		} else if (this == NORTHWEST) {
			return new Vector2(-1, 1);
		} else if (this == WEST) {
			return new Vector2(-1, 0);
		} else if (this == SOUTHWEST) {
			return new Vector2(-1, -1);
		} else if (this == SOUTH) {
			return new Vector2(0, -1);
		} else if (this == SOUTHEAST) {
			return new Vector2(1, -1);
		} else {
			System.err.println("Unknown direction " + this);
			return new Vector2(1, 0);
		}
	}

}
package com.yox89.ld32.raytracing;

import com.badlogic.gdx.graphics.Color;

public enum LightColor {
	GREEN, RED;

	public Color toColor() {
		switch (this) {
		case GREEN:
			return Color.GREEN;
		case RED:
		default:
			return Color.RED;
		}
	}
}
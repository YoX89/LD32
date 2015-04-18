package com.yox89.ld32.actors;

import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.yox89.ld32.Physics;

public class Torch extends Actor implements Disposable {

	private static final float LIGHT_BASE_DIST = 10f;

	private PointLight mLight;

	float flickerTime;

	public Torch(Physics physics) {
		mLight = new PointLight(physics.rayHandler, 30, new Color(1f, .7f, .2f,
				1f), 12f, 2f, 2f);
		mLight.setSoftnessLength(.5f);

		setSize(1f, 1f);

		setOrigin(.5f, .5f);
	}

	@Override
	public void act(float delta) {
		super.act(delta);

		flickerTime -= delta;
		if (flickerTime <= 0f) {
			setScaleX(MathUtils.random(.95f, 1.05f));
			flickerTime = .1f;
			mLight.setDistance(LIGHT_BASE_DIST * getScaleX());
		}
		mLight.setPosition(getX() + .5f, getY() + .5f);
	}

	@Override
	public void dispose() {
		mLight.dispose();
	}
}

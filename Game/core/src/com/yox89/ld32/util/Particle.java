package com.yox89.ld32.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Particle extends Actor implements Poolable {

	private TextureRegion sprite;
	private ParticlePool pool;

	public Particle() {

	}

	public void init(TextureRegion sprite, ParticlePool pool) {
		this.sprite = sprite;
		this.pool = pool;
	}

	public boolean remove() {
		if (super.remove()) {
			if (pool != null) {
				pool.free(this);
				pool = null;
			}
			return true;
		}
		return false;
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Color c = batch.getColor();
		batch.setColor(getColor());
		batch.draw(sprite, getX(), getY(), getOriginX(), getOriginY(),
				getWidth(), getHeight(), getScaleX(), getScaleY(),
				getRotation());
		batch.setColor(c);
	}

	@Override
	public void reset() {
		clearActions();
		setColor(Color.WHITE);
		sprite = Assets.smiley;
	}

}
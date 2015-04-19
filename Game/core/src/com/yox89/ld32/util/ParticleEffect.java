package com.yox89.ld32.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.badlogic.gdx.utils.Pools;

public class ParticleEffect extends Group {

	private float timePerParticle;
	private float buffer;
	private int numParticles;
	private float size;
	private TextureRegion sprite;
	public Pool<ParticleEffect> pool;

	private static final float PTM_RATIO = 1f;

	public ParticleEffect() {
		
	}
	
	public void reset() {
		clearActions();
		setColor(Color.WHITE);
		init(Assets.smiley, 0.001f, 100);
	}

	public boolean remove() {
		if (super.remove()) {
			if (pool != null) {
				pool.free(this);
				pool = null;

				ParticlePool pool = Assets.particlePool;
				for (Actor a : getChildren()) {
					pool.free((Particle) a);
				}
			}
			return true;
		}
		return false;
	}

	public void init(TextureRegion sprite, float particlesPerSecond,
			int numParticles) {
		init(sprite, particlesPerSecond, numParticles, PTM_RATIO);
	}

	public void init(TextureRegion sprite, float particlesPerSecond,
			int numParticles, float size) {
		this.sprite = sprite;
		this.timePerParticle = 1.0f / particlesPerSecond;
		this.numParticles = numParticles;
		this.size = size;
	}

	private void spawn() {
		ParticlePool pool = Assets.particlePool;
		Particle p = pool.obtain();
		p.init(sprite, pool);
		p.setColor(getColor());

		Vector2 mv = new Vector2(PTM_RATIO * 2.0f, 0.0f);
		mv.rotate(MathUtils.random(0.0f, 360.0f));

		p.setPosition(getX() + getWidth() / 2 - size / 2, getY() + getHeight()
				/ 2 - size / 2);
		p.setSize(size, size);
		p.addAction(Actions.sequence(Actions.parallel(
				Actions.moveBy(mv.x, mv.y, 1.0f, Interpolation.exp5Out),
				Actions.fadeOut(1.0f)), Actions.removeActor()));

		getParent().addActor(p);
	}

	public void act(float dt) {
		super.act(dt);

		if (numParticles > 0) {

			buffer += dt;

			while (buffer >= timePerParticle) {
				spawn();
				buffer -= timePerParticle;

				if (--numParticles == 0) {
					addAction(Actions.delay(1.0f, Actions.removeActor()));
					break;
				}
			}
		}
	}

}
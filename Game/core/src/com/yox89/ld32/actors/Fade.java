package com.yox89.ld32.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public class Fade extends Actor implements Disposable {

	private ShapeRenderer mRenderer;

	public Fade() {
		mRenderer = new ShapeRenderer();
		setColor(new Color(0, 0, 0, 1f));
		setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.end();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		mRenderer.setColor(getColor());
		mRenderer.begin(ShapeType.Filled);
		mRenderer.rect(0, 0, getWidth(), getHeight());
		mRenderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);

		batch.begin();
	}

	@Override
	public void dispose() {
		mRenderer.dispose();
	}
}

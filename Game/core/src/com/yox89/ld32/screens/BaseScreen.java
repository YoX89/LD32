package com.yox89.ld32.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

public abstract class BaseScreen extends InputAdapter implements Screen {

	protected Stage mGameStage, mUiStage;
	protected Array<Disposable> mDisposables;

	protected static final float GAME_WORLD_WIDTH = 64f;
	protected static final float GAME_WORLD_HEIGHT = 48f;

	@Override
	public void show() {
		mDisposables = new Array<Disposable>();
		mGameStage = manage(new Stage(new ScalingViewport(Scaling.none,
				GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT)));
		mUiStage = manage(new Stage());

		Gdx.input.setInputProcessor(this);

		init(mGameStage, mUiStage);
	}

	protected abstract void init(Stage game, Stage ui);

	protected <T extends Disposable> T manage(T res) {
		mDisposables.add(res);
		return res;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(.3f, .3f, .3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mGameStage.act(delta);
		mGameStage.draw();

		mUiStage.act(delta);
		mUiStage.draw();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		for (Disposable d : mDisposables) {
			d.dispose();
		}
		mDisposables.clear();
	}

}

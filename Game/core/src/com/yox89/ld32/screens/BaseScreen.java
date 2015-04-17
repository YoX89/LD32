package com.yox89.ld32.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public abstract class BaseScreen extends InputAdapter implements Screen {

	protected Stage mStage;
	protected Array<Disposable> mDisposables;
	Texture img;

	@Override
	public void show() {
		mDisposables = new Array<Disposable>();
		mStage = new Stage();

		Gdx.input.setInputProcessor(this);
		
		init(mStage);
		
	}
	
	protected abstract void init(Stage stage);
	
	protected <T extends Disposable> T manage(T res) {
		mDisposables.add(res);
		return res;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		mStage.act(delta);
		mStage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		for (Disposable d : mDisposables) {
			d.dispose();
		}
		mDisposables.clear();
		mStage.dispose();
	}

}

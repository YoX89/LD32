package com.yox89.ld32.screens;

import box2dLight.Light;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yox89.ld32.Physics;
import com.yox89.ld32.actors.Fade;
import com.yox89.ld32.raytracing.RayDispatcher;
import com.yox89.ld32.util.Collision;

public abstract class BaseScreen extends InputAdapter implements Screen {

	protected Stage mGameStage, mUiStage;
	protected Array<Disposable> mDisposables;

	protected int GAME_WORLD_WIDTH = 32;
	protected int GAME_WORLD_HEIGHT = 24;

	private World mWorld;
	private Box2DDebugRenderer mPhysicsDebugger;
	private float mPhysicsUpdateBuf;
	private RayHandler mRayHandler;
	private RayDispatcher mRayDispatcher;
	private static final float PHYSICS_TICK_DT = 1f / 60f;

	public BaseScreen() {
		mDisposables = new Array<Disposable>();
	}

	@Override
	public void show() {
		mGameStage = manage(new Stage(new StretchViewport(GAME_WORLD_WIDTH,
				GAME_WORLD_HEIGHT)));
		mUiStage = manage(new Stage());

		mWorld = manage(new World(new Vector2(0f, 0f), false));
		mRayHandler = manage(new RayHandler(mWorld));

		mPhysicsDebugger = new Box2DDebugRenderer();
		mRayDispatcher = new RayDispatcher(mWorld);

		Light.setContactFilter((short) ~0, (short) 0, (short) ~Collision.GHOST);

		Gdx.input.setInputProcessor(new InputMultiplexer(this, mUiStage,
				mGameStage));

		init(mGameStage, mUiStage, new Physics(mWorld, mRayHandler,
				mRayDispatcher));

		final Fade fade = new Fade();
		fade.getColor().a = 1f;
		fade.addAction(Actions.sequence(Actions.fadeOut(.25f),
				Actions.removeActor()));
		mUiStage.addActor(fade);

	}

	protected abstract void init(Stage game, Stage ui, Physics physicsWorld);

	public <T extends Disposable> T manage(T res) {
		mDisposables.add(res);
		return res;
	}

	@Override
	public void render(float delta) {
		mPhysicsUpdateBuf += delta;
		while (mPhysicsUpdateBuf >= PHYSICS_TICK_DT) {
			mPhysicsUpdateBuf -= PHYSICS_TICK_DT;
			mWorld.step(PHYSICS_TICK_DT, 0, 4);
		}

		Gdx.gl.glClearColor(.3f, .3f, .3f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		final Viewport vp = mGameStage.getViewport();
		vp.apply();
		mGameStage.act(delta);
		mGameStage.draw();
		final Matrix4 gameProj = mGameStage.getCamera().combined;
		mPhysicsDebugger.render(mWorld, gameProj);
		mRayHandler.useCustomViewport(vp.getScreenX(), vp.getScreenY(),
				vp.getScreenWidth(), vp.getScreenHeight());
		mRayHandler.setCombinedMatrix(gameProj);
		mRayHandler.setAmbientLight(new Color(.7f, .7f, .7f, .125f));
		mRayHandler.updateAndRender();

		mUiStage.getViewport().apply();
		mUiStage.act(delta);
		mUiStage.draw();
	}

	protected void switchScreen(Runnable onSwitch) {
		final Fade fade = new Fade();
		fade.getColor().a = 0f;
		fade.addAction(Actions.sequence(Actions.fadeIn(.25f),
				Actions.run(onSwitch)));
		mUiStage.addActor(fade);
	}

	@Override
	public void resize(int width, int height) {

		// mGameStage.getViewport().update(width, height);
		// mGameStage.getViewport().apply(true);
		// mGameStage.getCamera().update(true);

		final float wr = GAME_WORLD_WIDTH / ((float) width);
		final float hr = GAME_WORLD_HEIGHT / ((float) height);

		final float r = Math.max(hr, wr);

		final float sw = r * width;
		final float sh = r * height;

		final Viewport vp = mGameStage.getViewport();
		vp.setWorldSize(sw, sh);
		vp.update(width, height);
		vp.apply();
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

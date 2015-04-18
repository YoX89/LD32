package com.yox89.ld32.screens;

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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.yox89.ld32.Physics;
import com.yox89.ld32.raytracing.RayDispatcher;

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
		mGameStage = manage(new Stage(new ScalingViewport(Scaling.fit,
				GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT)));
		mUiStage = manage(new Stage());

		mWorld = manage(new World(new Vector2(0f, 0f), false));
		mRayHandler = manage(new RayHandler(mWorld));

		mPhysicsDebugger = new Box2DDebugRenderer();
		mRayDispatcher = new RayDispatcher(mWorld);

		// TODO figure out how to filter out player for only some lights.. It
		// looks bad when the player is filtered away from torches.
		// Light.setContactFilter((short)~0, (short)0,
		// (short)~Collision.PLAYER);

		Gdx.input.setInputProcessor(new InputMultiplexer(this, mUiStage,
				mGameStage));

		init(mGameStage, mUiStage, new Physics(mWorld, mRayHandler,
				mRayDispatcher));
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

		mGameStage.act(delta);
		mGameStage.draw();
		final Matrix4 gameProj = mGameStage.getCamera().combined;
//		mPhysicsDebugger.render(mWorld, gameProj);
		mRayHandler.setCombinedMatrix(gameProj);
		mRayHandler.setAmbientLight(new Color(.7f, .7f, .7f, .1f));
		mRayHandler.updateAndRender();

		mUiStage.act(delta);
		mUiStage.draw();

	}

	@Override
	public void resize(int width, int height) {
		mGameStage.getViewport().setScreenBounds(0, 0, width, height);
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

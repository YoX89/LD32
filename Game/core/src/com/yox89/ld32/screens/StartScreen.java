package com.yox89.ld32.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.yox89.ld32.Gajm;
import com.yox89.ld32.Physics;
import com.yox89.ld32.actors.Torch;
import com.yox89.ld32.util.PhysicsUtil;
import com.yox89.ld32.util.PhysicsUtil.BodyParams;

public class StartScreen extends BaseScreen {
	private Stage game;
	private Gajm gajm;

	public StartScreen(Gajm gajm) {
		super();
		this.gajm = gajm;
	}

	@Override
	protected void init(Stage game, Stage ui, Physics physics) {
		this.game = game;
		final Texture img = manage(new Texture("StartButtonEng.png"));
		img.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		final StartGameButtonActor startBtn = new StartGameButtonActor(img,
				physics.world,
				Math.min(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT) / 5f);
		game.addActor(startBtn);
		startBtn.setPosition(GAME_WORLD_WIDTH / 2 - startBtn.getWidth() / 2,
				GAME_WORLD_HEIGHT / 2);

		PhysicsUtil.createBody(new BodyParams(physics.world) {

			@Override
			public void setShape(PolygonShape ps) {
				ps.setAsBox(startBtn.getWidth() / 2, startBtn.getHeight() / 2f);
			}
		}).setTransform(startBtn.getX() + startBtn.getWidth() / 2,
				startBtn.getY() + startBtn.getHeight() / 2, 0f);

		Torch startBtnTorch = new Torch(physics, Color.MAGENTA);
		startBtnTorch.setPosition(startBtn.getX() + startBtn.getWidth() / 2f,
				startBtn.getY() + startBtn.getHeight() / 2);
		game.addActor(startBtnTorch);

		Torch torchUpCorner = new Torch(physics);
		torchUpCorner.setPosition(GAME_WORLD_WIDTH - 1, GAME_WORLD_HEIGHT - 1);
		game.addActor(new Torch(physics));
		game.addActor(torchUpCorner);

		final Label titleLbl = new Label("LD32 Work in progress",
				new LabelStyle(manage(new BitmapFont()), Color.CYAN));
		titleLbl.setPosition(
				Gdx.graphics.getWidth() / 2 - titleLbl.getMinWidth(), 400);
		titleLbl.setFontScale(2f);

		final Label copyLbl = new Label(
				"Made by: Kevlanche, Jonathan Hagberg, "
						+ "\n \t \t Adam Nilsson, Marie Versland in ~24 hours so far",
				new LabelStyle(manage(new BitmapFont()), Color.CYAN));
		copyLbl.setFontScale(0.9f);
		copyLbl.setPosition(300, 50);

		ui.addActor(titleLbl);
		ui.addActor(copyLbl);

	}

	private class StartGameButtonActor extends Actor {

		private final Texture mTexture;

		public StartGameButtonActor(Texture tex, World world, final float size) {
			mTexture = tex;
			setSize(size * 2, size);

			addListener(new InputListener() {

				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					gajm.setScreen(new TiledLevelScreen(gajm, 1));
					return true;
				};

				@Override
				public void enter(InputEvent event, float x, float y,
						int pointer, Actor fromActor) {
					setColor(Color.WHITE);
					super.enter(event, x, y, pointer, fromActor);
				}

				@Override
				public void exit(InputEvent event, float x, float y,
						int pointer, Actor toActor) {
					setColor(Color.LIGHT_GRAY);
					super.exit(event, x, y, pointer, toActor);
				}
			});
			setColor(Color.LIGHT_GRAY);

		}

		@Override
		public void act(float delta) {
			super.act(delta);

			if (Gdx.input.isKeyPressed(Keys.J)){
				gajm.setScreen(new TiledLevelScreen(gajm,5));
			}
			
			if (Gdx.input.isKeyPressed(Keys.K)){
				gajm.setScreen(new TiledLevelScreen(gajm,6));
			}
			
			if (Gdx.input.isKeyPressed(Keys.M)){
				gajm.setScreen(new TiledLevelScreen(gajm,7));
			}
			
			if (Gdx.input.isKeyPressed(Keys.L)){
				gajm.setScreen(new TiledLevelScreen(gajm,8));
			}
			
			if (Gdx.input.isKeyPressed(Keys.O)) {
				gajm.setScreen(new TiledLevelScreen(gajm, 0));
			}

			if (Gdx.input.isKeyPressed(Keys.U)) {
				gajm.setScreen(new TiledLevelScreen(gajm, 2));
			}

			if (Gdx.input.isKeyPressed(Keys.T)) {
				gajm.setScreen(new TiledLevelScreen(gajm, 3));
			}
			
			if (Gdx.input.isKeyPressed(Keys.N)) {
				gajm.setScreen(new TiledLevelScreen(gajm, 4));
			}
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.setColor(getColor());
			batch.draw(mTexture, getX(), getY(), getOriginX(), getOriginY(),
					getWidth(), getHeight(), getScaleX(), getScaleY(),
					getRotation(), 0, 0, mTexture.getWidth(),
					mTexture.getHeight(), false, false);
		}
	}

}

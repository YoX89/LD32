package com.yox89.ld32.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.yox89.ld32.Gajm;
import com.yox89.ld32.Physics;

public class StartScreen extends BaseScreen {
	private Stage game;
	private Gajm gajm;

	public StartScreen(Gajm gajm) {
		super();
		this.gajm= gajm;
	}
	@Override
	protected void init(Stage game, Stage ui, Physics physics) {
		this.game = game;
		final Texture img = manage(new Texture("startButton.png"));
		img.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		final StartGameButtonActor actor = new StartGameButtonActor(img, physics.world,
				Math.min(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT) / 5f);
		game.addActor(actor);


		actor.setPosition(GAME_WORLD_WIDTH / 2 - actor.getWidth() / 2,
				GAME_WORLD_HEIGHT / 2);

		final Label label = new Label("Hello, I am ui", new LabelStyle(
				new BitmapFont(), Color.CYAN)) {

			@Override
			public void act(float delta) {
				super.act(delta);

//				setText(String
//						.format("%.0f | %.0f", actor.getX(), actor.getY()));
			}
		};
		label.setText("Välkommen till spöke på vift");
		label.setPosition(200, 400);

		ui.addActor(label);

	}

	private class StartGameButtonActor extends Actor {

		private final Texture mTexture;

		public StartGameButtonActor(Texture tex, World world, final float size) {
			mTexture = tex;
			setSize(size*2, size);
			
			addListener(new InputListener() {

				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					gajm.setScreen(new LevelScreen(1));
					return true;
				};
			});
		}

		@Override
		public void act(float delta) {
			super.act(delta);

			
			if (Gdx.input.isKeyPressed(Keys.U)){
				gajm.setScreen(new LevelScreen(1));
			}
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.draw(mTexture, getX(), getY(), getOriginX(), getOriginY(),
					getWidth(), getHeight(), getScaleX(), getScaleY(),
					getRotation(), 0, 0, mTexture.getWidth(),
					mTexture.getHeight(), false, false);
		}
	}

}

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
import com.yox89.ld32.actors.Torch;

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
		final Texture img = manage(new Texture("startButtonEng.png"));
		img.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		final StartGameButtonActor actor = new StartGameButtonActor(img, physics.world,
				Math.min(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT) / 5f);
		game.addActor(actor);

		Torch torchUpCorner = new Torch(physics);
		torchUpCorner.setPosition(GAME_WORLD_WIDTH-1, GAME_WORLD_HEIGHT-1);
		game.addActor(new Torch(physics));
		game.addActor(torchUpCorner);
		

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
		label.setText("Welcome to ghost in the house");
		label.setPosition(50, 400);
		label.setFontScale(2f);
		
		
		
		final Label label2 = new Label("Hello, I am ui", new LabelStyle(
				new BitmapFont(), Color.CYAN)) {

			
			@Override
			public void act(float delta) {
				super.act(delta);

//				setText(String
//						.format("%.0f | %.0f", actor.getX(), actor.getY()));
			}
		};
		label2.setText("Coopyright: Anton Risberg, Jonathan Hagberg, "
				+ "\n \t \t \t Adam Nilsson, Marie Versland");
		label2.setFontScale(0.9f);
		label2.setPosition(300, 50);

		ui.addActor(label);
		ui.addActor(label2);

	}

	private class StartGameButtonActor extends Actor {

		private final Texture mTexture;

		public StartGameButtonActor(Texture tex, World world, final float size) {
			mTexture = tex;
			setSize(size*2, size);
			
			addListener(new InputListener() {

				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					gajm.setScreen(new TiledLevelScreen(gajm, 1));
					return true;
				};
			});
		}

		@Override
		public void act(float delta) {
			super.act(delta);

			if (Gdx.input.isKeyPressed(Keys.O)){
				gajm.setScreen(new TiledLevelScreen(gajm,0));
			}
			
			if (Gdx.input.isKeyPressed(Keys.U)){
				gajm.setScreen(new TiledLevelScreen(gajm,2));
			}
			
			if (Gdx.input.isKeyPressed(Keys.T)){
				gajm.setScreen(new TiledLevelScreen(gajm,3));
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

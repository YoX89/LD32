package com.yox89.ld32.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.yox89.ld32.ShadyActor;

public class ExampleScreen extends BaseScreen {
	private Stage game;
	
	@Override
	protected void init(Stage game, Stage ui) {
		this.game = game;
		final Texture img = manage(new Texture("badlogic.jpg"));
		img.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		final ShadyActor shady = manage(new ShadyActor(img));
		shady.setBounds(0, 0, 20, 20);
		game.addActor(shady);

		final ExampleActor actor = new ExampleActor(img);
		game.addActor(actor);

		final float size = Math.min(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT) / 3f;
		actor.setSize(size, size);
		actor.setPosition(GAME_WORLD_WIDTH / 2 - size, GAME_WORLD_HEIGHT / 2);

		final float mvx = GAME_WORLD_WIDTH / 4;
		actor.addAction(Actions.forever(Actions.sequence(
				Actions.moveBy(mvx, 0f, 2f, Interpolation.bounce),
				Actions.moveBy(-mvx, 0f, 2f, Interpolation.bounce))));

		final Label label = new Label("Hello, I am ui", new LabelStyle(
				new BitmapFont(), Color.CYAN)) {

			@Override
			public void act(float delta) {
				super.act(delta);

				setText(String
						.format("%.0f | %.0f", actor.getX(), actor.getY()));
			}
		};
		label.setPosition(50, 45);

		ui.addActor(label);
		
		setUpUsAdamTheCrime(img);
	}

	private void setUpUsAdamTheCrime(Texture img) {
		final AdamActor actor = new AdamActor();
		game.addActor(actor);
		
		final float mvx = GAME_WORLD_WIDTH / 4;
		actor.addAction(Actions.forever(
				Actions.sequence(
				Actions.moveBy(mvx*2, 0f, 4f, Interpolation.sine),
				Actions.moveBy(0f, mvx, 4f, Interpolation.sineOut),
				Actions.moveBy(- mvx*2, 0f, 4f, Interpolation.circleIn),
				Actions.moveBy(0f, -mvx, 4f, Interpolation.pow2)
				
						)));
		
		final float size = Math.min(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT) / 5f;
		actor.setSize(size, size);
		actor.setPosition(GAME_WORLD_WIDTH / 4 , GAME_WORLD_HEIGHT / 4);
		
		
	}

	private class ExampleActor extends Actor {

		private final Texture mTexture;

		public ExampleActor(Texture tex) {
			mTexture = tex;
		}

		@Override
		public void act(float delta) {
			super.act(delta);

			if (Gdx.input.isKeyPressed(Keys.UP)) {
				moveBy(0f, delta * 10f);
			} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
				moveBy(0f, delta * -10f);
			}

			if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
				addAction(Actions.rotateBy(90f, .25f, Interpolation.sine));
			}
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.draw(mTexture, getX(), getY(), getOriginX(), getOriginY(),
					getWidth(), getHeight(), getScaleX(), getScaleY(),
					getRotation(), 0, 0, mTexture.getWidth(),
					mTexture.getHeight(), false, true);
		}
	}
	
	private class AdamActor extends Actor {

		private final Texture mTexture;
		private float magicMultiplier;

		public AdamActor() {
			final Texture img = manage(new Texture("epl11737fig2.jpg"));
			img.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			mTexture = img;
			magicMultiplier = 1f;
		}

	

		public void increaseSpeed(float f){
			magicMultiplier += f;
		}
		
		@Override
		public void act(float delta) {
			if(Gdx.input.isKeyJustPressed(Keys.A)){
				addAction(Actions.rotateBy(-11f, .25f, Interpolation.circleOut));
				increaseSpeed(0.3f);
			}
			super.act(delta*magicMultiplier);


		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.draw(mTexture, getX(), getY(), getOriginX() + (getWidth()/2), getOriginY() + (getHeight()/2),
					getWidth(), getHeight(), getScaleX(), getScaleY(),
					getRotation(), 0, 0, mTexture.getWidth(),
					mTexture.getHeight(), false, false);
		}
	}
}

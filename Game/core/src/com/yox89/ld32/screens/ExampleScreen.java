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

public class ExampleScreen extends BaseScreen {

	@Override
	protected void init(Stage game, Stage ui) {
		final Texture img = manage(new Texture("badlogic.jpg"));
		img.setFilter(TextureFilter.Linear, TextureFilter.Linear);

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
		label.setPosition(50, 50);

		ui.addActor(label);
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
					mTexture.getHeight(), false, false);
		}
	}
}

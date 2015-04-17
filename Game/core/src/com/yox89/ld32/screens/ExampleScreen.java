package com.yox89.ld32.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class ExampleScreen extends BaseScreen {

	Texture img;

	@Override
	protected void init(Stage stage) {
		img = manage(new Texture("badlogic.jpg"));

		ExampleActor actor = new ExampleActor(img);
		stage.addActor(actor);

		actor.setPosition(200, 200);
		actor.setSize(128, 128);
		
		actor.addAction(Actions.forever(Actions.sequence(
				Actions.moveBy(100f, 0f, 2f, Interpolation.sine),
				Actions.moveBy(-100f, 0f, 2f, Interpolation.sine))));

	}

	private class ExampleActor extends Actor {

		private final Texture mTexture;

		public ExampleActor(Texture tex) {
			mTexture = tex;
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

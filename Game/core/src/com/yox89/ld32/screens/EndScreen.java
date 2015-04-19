package com.yox89.ld32.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.yox89.ld32.Gajm;
import com.yox89.ld32.Physics;
import com.yox89.ld32.actors.Torch;

public class EndScreen extends BaseScreen {
	private Stage game;
	private Gajm gajm;

	private boolean mAcceptTouches;

	public EndScreen(Gajm gajm) {
		this.gajm = gajm;
	}

	@Override
	protected void init(Stage game, Stage ui, Physics physics) {
		this.game = game;
		final Texture img = manage(new Texture("StartButtonEng.png"));
		img.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		Torch torchUpCorner = new Torch(physics, 0);
		torchUpCorner.setPosition(GAME_WORLD_WIDTH - 1, GAME_WORLD_HEIGHT - 1);
		game.addActor(new Torch(physics, 180));
		game.addActor(torchUpCorner);

		final String msg = "Done! All the ghosts have been wiped out and order has been restored to the land.\n"
				+ "\n"
				+ "Thank you for playing.";
		final Label titleLbl = new Label(msg, new LabelStyle(
				manage(new BitmapFont()), Color.WHITE));
		titleLbl.setAlignment(Align.center);
		titleLbl.setPosition(
				Gdx.graphics.getWidth() / 2 ,
				Gdx.graphics.getHeight() * 0.7f, Align.center);
		titleLbl.setFontScale(1.2f);

		final Label copyLbl = new Label(
				"Press anywhere to go back to the menu", new LabelStyle(
						manage(new BitmapFont()), Color.WHITE));
		copyLbl.setAlignment(Align.center);
		copyLbl.setFontScale(0.9f);
		copyLbl.setPosition((Gdx.graphics.getWidth() - copyLbl.getMinWidth()
				/ copyLbl.getFontScaleX()) / 2, 100);

		ui.addActor(titleLbl);
		ui.addActor(copyLbl);

		copyLbl.setVisible(false);
		Action blink = Actions.forever(Actions.sequence(
				Actions.delay(.5f, Actions.visible(true)),
				Actions.delay(.5f, Actions.visible(false))));
		copyLbl.addAction(Actions.sequence(Actions.delay(3f),
				Actions.run(new Runnable() {

					@Override
					public void run() {
						mAcceptTouches = true;
					}
				}), blink));

	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (mAcceptTouches) {
			mAcceptTouches = false;
			switchScreen(new Runnable() {

				@Override
				public void run() {
					gajm.setScreen(new StartScreen(gajm));
				}
			});
		}
		return super.touchDown(screenX, screenY, pointer, button);
	}
}

package com.yox89.ld32.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.yox89.ld32.Gajm;
import com.yox89.ld32.Physics;
import com.yox89.ld32.actors.Torch;

public class EndScreen extends BaseScreen {
	private Stage game;
	private Gajm gajm;

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

		final Label titleLbl = new Label("Thanks for playing!", new LabelStyle(
				manage(new BitmapFont()), Color.WHITE));
		titleLbl.setPosition(
				Gdx.graphics.getWidth() / 2 - titleLbl.getMinWidth(),
				Gdx.graphics.getHeight() * 0.7f);
		titleLbl.setFontScale(2f);

		final Label copyLbl = new Label(
				"Check back at the end of LD32 for the full version",
				new LabelStyle(manage(new BitmapFont()), Color.WHITE));
		copyLbl.setAlignment(Align.center);
		copyLbl.setFontScale(0.9f);
		copyLbl.setPosition((Gdx.graphics.getWidth() - copyLbl.getMinWidth()
				/ copyLbl.getFontScaleX()) / 2, 100);

		ui.addActor(titleLbl);
		ui.addActor(copyLbl);
	}
}

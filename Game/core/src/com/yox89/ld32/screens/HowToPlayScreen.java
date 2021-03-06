package com.yox89.ld32.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.yox89.ld32.Gajm;
import com.yox89.ld32.Physics;
import com.yox89.ld32.actors.Torch;

public class HowToPlayScreen extends BaseScreen {
	private Stage game;
	private Gajm gajm;

	public HowToPlayScreen(Gajm gajm) {
		this.gajm = gajm;
	}

	@Override
	protected void init(Stage game, Stage ui, Physics physics) {
		this.game = game;

		final String helpMsg = "Place mirrors by clicking near you in the levels.\n"
				+ "Click them again to rotate them.\n"
				+ "Each level is won by destroying all ghosts visible on the screen.\n"
				+ "Click the gemstones to activate them.\n"
				+ "If you position your mirrors correctly the light will bounce and spread to all the ghosts\n"
				+ "\n"
				+ "Move around using WASD or the arrow keys\n"
				+ "Press 'R' to restart the current level.\n"
				+ "Press 'Q' to go to the main menu.\n"
				+ "\n"
				+ "Press anywhere to return";

		Torch torchUpCorner = new Torch(physics, 0);
		torchUpCorner.setPosition(GAME_WORLD_WIDTH - 1, GAME_WORLD_HEIGHT - 1);
		game.addActor(new Torch(physics, 180));
		game.addActor(torchUpCorner);


		final Label helpLbl = new Label(
				helpMsg,
				new LabelStyle(manage(new BitmapFont()), Color.WHITE));
		helpLbl.setAlignment(Align.center);
		helpLbl.setFontScale(1.2f);
		helpLbl.setPosition((Gdx.graphics.getWidth() - helpLbl.getMinWidth()
				/ helpLbl.getFontScaleX()) / 2, Gdx.graphics.getHeight()/2 - helpLbl.getMinHeight()/(2 * helpLbl.getFontScaleY()));

		ui.addActor(helpLbl);
	}
	
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		switchScreen(new Runnable() {
			
			@Override
			public void run() {
				gajm.setScreen(new StartScreen(gajm));
			}
		});
		return true;
	}
}

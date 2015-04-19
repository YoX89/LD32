package com.yox89.ld32;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.yox89.ld32.screens.StartScreen;
import com.yox89.ld32.util.Assets;

public class Gajm extends Game {

	public static int maxClearedLevel = -1;

	@Override
	public void create() {
		try {
			Assets.init();
		} catch (IOException e) {
			throw new GdxRuntimeException(e);
		}

		Assets.bg_music.setLooping(true);
		Assets.bg_music.play();

		setScreen(new StartScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();

		Assets.dispose();
	}
}

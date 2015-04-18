package com.yox89.ld32;

import java.io.IOException;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.yox89.ld32.screens.ExampleScreen;
import com.yox89.ld32.util.Assets;

public class Gajm extends Game {

	@Override
	public void create() {
		try {
			Assets.init();
		} catch (IOException e) {
			throw new GdxRuntimeException(e);
		}
		setScreen(new ExampleScreen());
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		Assets.dispose();
	}
}

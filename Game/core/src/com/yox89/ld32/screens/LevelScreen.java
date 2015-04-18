package com.yox89.ld32.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.yox89.ld32.actors.LightSource;
import com.yox89.ld32.actors.LightSource.LightColor;
import com.yox89.ld32.actors.Wall;

public class LevelScreen extends BaseScreen {

	private static final int WALL = 0x000000FF;
	private static final int EMPTY = 0xFFFFFFFF;
	private static final int RED_LAZER = 0xFF0000FF;
	private static final int GREEN_LAZER = 0x00FF00FF;
	
	private final Pixmap mLevelSrc;

	public LevelScreen(int level) {
		mLevelSrc = manage(new Pixmap(Gdx.files.internal("levels/" + level
				+ ".png")));
		GAME_WORLD_WIDTH = mLevelSrc.getWidth();
		GAME_WORLD_HEIGHT = mLevelSrc.getHeight();
	}

	@Override
	protected void init(Stage game, Stage ui, World physicsWorld) {

		for (int x = 0; x < GAME_WORLD_WIDTH; x++) {
			for (int y = 0; y < GAME_WORLD_HEIGHT; y++) {
				final int rgb = mLevelSrc.getPixel(x, y);

				switch (rgb) {
				case EMPTY:
					// Do nothing
					break;
				case WALL:
					add(game, new Wall(physicsWorld), x, GAME_WORLD_HEIGHT - y
							- 1);
					break;
				case RED_LAZER:
					add(game, new LightSource(physicsWorld, LightColor.RED), x,
							GAME_WORLD_HEIGHT - y - 1);
					break;

				case GREEN_LAZER:
					add(game, new LightSource(physicsWorld, LightColor.GREEN), x,
							GAME_WORLD_HEIGHT - y - 1);
					break;
				}
			}
		}
	}

	private void add(Stage game, Actor actor, int x, int y) {
		actor.setPosition(x, y);
		game.addActor(actor);
	}

}

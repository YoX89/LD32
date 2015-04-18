package com.yox89.ld32.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.yox89.ld32.Physics;
import com.yox89.ld32.actors.LightSource;
import com.yox89.ld32.actors.Mirror;
import com.yox89.ld32.actors.PlayerActor;
import com.yox89.ld32.actors.Torch;
import com.yox89.ld32.actors.Wall;
import com.yox89.ld32.raytracing.Direction;
import com.yox89.ld32.raytracing.LightColor;

public class LevelScreen extends BaseScreen {

	private static final int WALL = 0x000000FF;
	private static final int EMPTY = 0xFFFFFFFF;
	private static final int RED_LAZER = 0xFF0000FF;
	private static final int GREEN_LAZER = 0x00FF00FF;
	private static final int TORCH = 0xFFFF00FF;
	private static final int MIRROR = 0x0000FFFF;

	private final Pixmap mLevelSrc;

	public LevelScreen(int level) {
		mLevelSrc = manage(new Pixmap(Gdx.files.internal("levels/" + level
				+ ".png")));
		GAME_WORLD_WIDTH = mLevelSrc.getWidth();
		GAME_WORLD_HEIGHT = mLevelSrc.getHeight();
	}

	@Override
	protected void init(Stage game, Stage ui, Physics physics) {
		final PlayerActor player = new PlayerActor(physics);
		game.addActor(player);

		player.setPosition(GAME_WORLD_WIDTH / 2 - player.getWidth() / 2,
				GAME_WORLD_HEIGHT / 2);
		for (int x = 0; x < GAME_WORLD_WIDTH; x++) {
			for (int y = 0; y < GAME_WORLD_HEIGHT; y++) {
				final int rgb = pixelAt(x, y);

				switch (rgb) {
				case EMPTY:
					// Do nothing
					break;
				case WALL:
					add(game, new Wall(physics.world), x, GAME_WORLD_HEIGHT - y
							- 1);
					break;
				case RED_LAZER:
					add(game, new LightSource(physics, LightColor.RED,
							parseLightDirection(x, y)), x, GAME_WORLD_HEIGHT
							- y - 1);
					break;
				case GREEN_LAZER:
					add(game, new LightSource(physics, LightColor.GREEN,
							parseLightDirection(x, y)), x, GAME_WORLD_HEIGHT
							- y - 1);
					break;
				case TORCH:
					add(game, new Torch(physics), x, GAME_WORLD_HEIGHT - y - 1);
					break;
				case MIRROR:
					add(game, new Mirror(physics), x, GAME_WORLD_HEIGHT - y - 1);
					break;
				}
			}
		}
	}
	private int pixelAt(int x, int y) {
		if (x >= 0 && y >= 0 && x < mLevelSrc.getWidth()
				&& y < mLevelSrc.getHeight()) {
			return mLevelSrc.getPixel(x, y);
		} else {
			return WALL;
		}
	}

	private Direction[] parseLightDirection(int x, int y) {
		Array<Direction> dirs = new Array<Direction>(4);
		if (pixelAt(x + 1, y) == EMPTY) {
			dirs.add(Direction.EAST);
		}
		if (pixelAt(x - 1, y) == EMPTY) {
			dirs.add(Direction.WEST);
		}
		if (pixelAt(x, y + 1) == EMPTY) {
			dirs.add(Direction.SOUTH);
		}
		if (pixelAt(x, y - 1) == EMPTY) {
			dirs.add(Direction.NORTH);
		}
		return dirs.toArray(Direction.class);
	}

	private void add(Stage game, Actor actor, int x, int y) {
		if (actor instanceof Disposable) {
			manage((Disposable) actor);
		}
		actor.setPosition(x, y);
		game.addActor(actor);
	}

}

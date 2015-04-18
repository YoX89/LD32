package com.yox89.ld32.screens;

import java.util.ArrayList;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.yox89.ld32.Physics;
import com.yox89.ld32.actors.GhostActor;
import com.yox89.ld32.actors.LightSource;
import com.yox89.ld32.actors.PlayerActor;
import com.yox89.ld32.actors.Torch;
import com.yox89.ld32.actors.Wall;
import com.yox89.ld32.raytracing.Direction;
import com.yox89.ld32.raytracing.LightColor;

public class TiledLevelScreen extends BaseScreen {

	private static final String WALL = "Wall";
	private static final String RED_LASER = "RedLaser";
	private static final String GREEN_LASER = "GreenLaser";
	private static final String GHOST = "Ghost";
	private static final String TORCH = "Torch";

	private final MapLayer mObjectLayer;
	private boolean[][] mLightNotAllowed;

	public TiledLevelScreen(int level) {
		TiledMap levelMap = new TmxMapLoader().load("levels/level" + level
				+ ".tmx");

		TiledMapTileLayer levelMapLayer = (TiledMapTileLayer) levelMap
				.getLayers().get("Tile Layer");

		GAME_WORLD_HEIGHT = levelMapLayer.getHeight();
		GAME_WORLD_WIDTH = levelMapLayer.getWidth();

		this.mLightNotAllowed = new boolean[GAME_WORLD_WIDTH][GAME_WORLD_HEIGHT];

		MapLayer objectLayer = (MapLayer) levelMap.getLayers().get(
				"Object Layer");
		this.mObjectLayer = objectLayer;
	}

	@Override
	protected void init(Stage game, Stage ui, Physics physics) {
		final PlayerActor player = new PlayerActor(physics);
		game.addActor(player);

		player.setPosition(GAME_WORLD_WIDTH / 2 - player.getWidth() / 2,
				GAME_WORLD_HEIGHT / 2);
		
		MapObjects mapObjects = this.mObjectLayer.getObjects();

		for (MapObject mapObject : mapObjects) {
			MapProperties mapProperties = mapObject.getProperties();

			float x = mapProperties.get("x", Float.class);
			float y = mapProperties.get("y", Float.class);

			float width = mapProperties.get("width", Float.class);
			float height = mapProperties.get("height", Float.class);

			String type = mapProperties.get("type", String.class);

			if (type.equals(WALL)) {

				for (int i = (int) x; i < x + width; i++) {
					for (int j = (int) y; j < y + height; j++) {
						this.mLightNotAllowed[i][j] = true;
					}
				}

				add(game, new Wall(physics.world, width, height), x, y);
			} else if (type.equalsIgnoreCase(TORCH)) {
				add(game, new Torch(physics), x, y);
			} else if (type.equals(RED_LASER)) {
				add(game, new LightSource(physics, LightColor.RED,
						parseLightDirection((int) x, (int) y)), x, y);
			} else if (type.equals(GREEN_LASER)) {
				add(game, new LightSource(physics, LightColor.GREEN,
						parseLightDirection((int) x, (int) y)), x, y);
			} else if (type.equals(GHOST)) {
				ArrayList<Vector2> positions = new ArrayList<Vector2>();
				positions.add(new Vector2(x, y));
				add(game, new GhostActor(physics, positions), x, y);
			}
		}
	}

	private boolean lightAllowedAtTile(int x, int y) {
		if (x >= 0 && y >= 0 && x < GAME_WORLD_WIDTH && y < GAME_WORLD_HEIGHT) {

			return !this.mLightNotAllowed[x][y];
		}

		return false;
	}

	private Direction[] parseLightDirection(int x, int y) {
		Array<Direction> dirs = new Array<Direction>(4);
		if (this.lightAllowedAtTile(x + 1, y)) {
			dirs.add(Direction.EAST);
		}
		if (this.lightAllowedAtTile(x - 1, y)) {
			dirs.add(Direction.WEST);
		}
		if (this.lightAllowedAtTile(x, y + 1)) {
			dirs.add(Direction.SOUTH);
		}
		if (this.lightAllowedAtTile(x, y - 1)) {
			dirs.add(Direction.NORTH);
		}
		return dirs.toArray(Direction.class);
	}

	private void add(Stage game, Actor actor, float x, float y) {
		if (actor instanceof Disposable) {
			manage((Disposable) actor);
		}
		actor.setPosition(x, y);
		game.addActor(actor);
	}

}

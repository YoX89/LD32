package com.yox89.ld32.screens;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.yox89.ld32.CollisionManager;
import com.yox89.ld32.CollisionManager.CollisionManagerListener;
import com.yox89.ld32.Gajm;
import com.yox89.ld32.Physics;
import com.yox89.ld32.actors.GhostActor;
import com.yox89.ld32.actors.LightSource;
import com.yox89.ld32.actors.Mirror;
import com.yox89.ld32.actors.PlayerActor;
import com.yox89.ld32.actors.Torch;
import com.yox89.ld32.actors.Wall;
import com.yox89.ld32.raytracing.Direction;
import com.yox89.ld32.raytracing.LightColor;
import com.yox89.ld32.util.Ui;

public class TiledLevelScreen extends BaseScreen implements
		CollisionManagerListener {

	private static final String WALL = "Wall";
	private static final String RED_LASER = "RedLaser";
	private static final String GREEN_LASER = "GreenLaser";
	private static final String MIRROR = "Mirror";
	private static final String GHOST = "Ghost";
	private static final String TORCH = "Torch";
	private static final String PLAYER = "Player";

	private final MapLayer mObjectLayer;
	private boolean[][] mLightNotAllowed;
	private CollisionManager mCollisionManager;

	private ShapeRenderer mFocusRenderer;
	private Actor mFocus;
	final Vector2 mLastHoverCoords = new Vector2(-1f, -1f);

	protected int mNumberRemainingMirrors;
	protected int mNumberTotalMirrors;

	private Physics mPhysics;

	private PlayerActor mPlayer;

	private final Gajm mGajm;
	private final int mLevelId;
	private Ui ui;

	public TiledLevelScreen(Gajm gajm, int level) {

		mGajm = gajm;
		mLevelId = level;
		mFocusRenderer = manage(new ShapeRenderer());

		mNumberRemainingMirrors = 5;
		mNumberTotalMirrors = 5;

		final TiledMap levelMap = new TmxMapLoader().load("levels/demo_level_"
				+ level + ".tmx");

		TiledMapTileLayer levelMapLayer = (TiledMapTileLayer) levelMap
				.getLayers().get("Tile Layer");

		GAME_WORLD_HEIGHT = levelMapLayer.getHeight();
		GAME_WORLD_WIDTH = levelMapLayer.getWidth();

		this.mLightNotAllowed = new boolean[GAME_WORLD_WIDTH][GAME_WORLD_HEIGHT];

		MapLayer objectLayer = (MapLayer) levelMap.getLayers().get(
				"Object Layer");
		this.mObjectLayer = objectLayer;

		levelMap.dispose();
	}

	@Override
	protected void init(Stage game, Stage uiStage, Physics physics) {

		ui = new Ui(this, game, uiStage, mNumberTotalMirrors,
				mObjectLayer.getProperties());

		mPhysics = physics;

		mCollisionManager = new CollisionManager(physics.world);
		mCollisionManager.mCollisionManagerListener = this;

		parseMap(game, physics, true);
		parseMap(game, physics, false);
	}

	private void parseMap(Stage game, Physics physics, boolean wallOnly) {
		MapObjects mapObjects = this.mObjectLayer.getObjects();

		for (MapObject mapObject : mapObjects) {
			MapProperties mapProperties = mapObject.getProperties();

			float x = mapProperties.get("x", Float.class);
			float y = mapProperties.get("y", Float.class);

			float width = mapProperties.get("width", Float.class);
			float height = mapProperties.get("height", Float.class);

			String type = mapProperties.get("type", String.class);

			if (type == null) {
				System.err.println("Null type on " + mapProperties);
				continue;
			}
			if (wallOnly) {
				if (type.equals(WALL)) {

					for (int i = (int) x; i < x + width; i++) {
						for (int j = (int) y; j < y + height; j++) {
							this.mLightNotAllowed[i][j] = true;
						}
					}

					add(game, new Wall(physics.world, width, height), x, y);
				}
			} else {
				if (type.equalsIgnoreCase(TORCH)) {
					add(game, new Torch(physics), x, y);
				} else if (type.equals(PLAYER)) {
					PlayerActor playerActor = new PlayerActor(physics, ui,
							mapProperties);
					add(game, playerActor, x, y);
					mPlayer = playerActor;
				} else if (type.equals(RED_LASER)) {
					add(game, new LightSource(this, physics, LightColor.RED,
							parseLightDirection((int) x, (int) y)), x, y);
				} else if (type.equals(GREEN_LASER)) {
					add(game, new LightSource(this, physics, LightColor.GREEN,
							parseLightDirection((int) x, (int) y)), x, y);
				} else if (type.equals(GHOST)) {
					if (mapObject instanceof PolylineMapObject) {
						PolylineMapObject ghostObject = (PolylineMapObject) mapObject;

						ArrayList<Vector2> path = getPathForGhost(ghostObject);
						Vector2 startPosition = path.get(0);

						GhostActor ghostActor = new GhostActor(this, physics,
								path, 0);

						add(game, ghostActor, startPosition.x, startPosition.y);
					} else {
						Vector2 startPosition = new Vector2(x, y);
						ArrayList<Vector2> path = new ArrayList<Vector2>();
						path.add(startPosition);
						float angleDegree = Float.parseFloat(mapProperties.get(
								"AngleDegrees").toString());
						GhostActor ghostActor = new GhostActor(this, physics,
								path, angleDegree);

						ghostActor.setRotation(angleDegree);

						add(game, ghostActor, startPosition.x, startPosition.y);
					}
				} else if (type.equals(MIRROR)) {
					add(game, new Mirror(physics, Mirror.TYPE_90_DEG), x, y);
				}
			}
		}
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		mGameStage.screenToStageCoordinates(mLastHoverCoords.set(screenX,
				screenY));
		mFocus = mGameStage.hit(mLastHoverCoords.x, mLastHoverCoords.y, true);
		for (Actor a : mGameStage.getActors()) {
			if (a instanceof Mirror
					&& new Vector2(((Mirror) a).gamePosition).sub(
							new Vector2((int) mLastHoverCoords.x,
									(int) mLastHoverCoords.y)).len() < .05f) {
				// Don't know why mFocus isn't this mirror.. but let's force it
				// in.
				mFocus = (Mirror) a;
			}
		}
		return false;
	}

	public boolean mouseIsInRangeOfPlayer() {
		final boolean[] foundWall = new boolean[1];
		final Vector2 dst;
		if (mFocus != null) {
			dst = new Vector2(mFocus.getX() + mFocus.getWidth() / 2,
					mFocus.getY() + mFocus.getHeight() / 2);
		} else {
			dst = mLastHoverCoords;
		}
		mPhysics.world.rayCast(new RayCastCallback() {

			@Override
			public float reportRayFixture(Fixture fixture, Vector2 point,
					Vector2 normal, float fraction) {
				if (fixture.getUserData() instanceof Wall) {
					foundWall[0] = true;
					return 0;
				}
				return 1;
			}
		}, new Vector2(mPlayer.getX(), mPlayer.getY()), dst);
		if (foundWall[0]) {
			return false;
		}
		return new Vector2(mPlayer.getX(), mPlayer.getY())
				.sub(mLastHoverCoords).len() < 5;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.R) {

			switchScreen(new Runnable() {

				@Override
				public void run() {
					mGajm.setScreen(new TiledLevelScreen(mGajm, mLevelId));
				}
			});

			return true;
		} else if (keycode == Keys.E) {
			if (mFocus instanceof Mirror) {
				mFocus.remove();
				mNumberRemainingMirrors++;
				updateMirrorsLabel();
				return true;
			}
			return true;
		} else if (keycode == Keys.Q) {

			switchScreen(new Runnable() {

				@Override
				public void run() {
					mGajm.setScreen(new StartScreen(mGajm));
				}
			});
			return true;
		}
		return super.keyDown(keycode);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		mouseMoved(screenX, screenY);
		if (mouseIsInRangeOfPlayer()) {
			if (mFocus == null && mNumberRemainingMirrors > 0) {
				mNumberRemainingMirrors--;
				updateMirrorsLabel();
				final Mirror mirror = new Mirror(mPhysics, Mirror.TYPE_90_DEG);
				add(mGameStage, mirror, (int) mLastHoverCoords.x,
						(int) mLastHoverCoords.y);
				mFocus = mirror;
				return true;
			} else if (mFocus instanceof Mirror) {
				mFocus.rotateBy(45f);
				return true;
			}
		}
		return false;
	}

	private void updateMirrorsLabel() {
		ui.setMirrorsLeftText(mNumberRemainingMirrors + "");
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		if (mouseIsInRangeOfPlayer()
				&& (mFocus instanceof Mirror || mFocus instanceof LightSource || mNumberRemainingMirrors > 0)) {
			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

			mFocusRenderer.setProjectionMatrix(mGameStage.getCamera().combined);
			mFocusRenderer.begin(ShapeType.Filled);
			mFocusRenderer.setColor(1f, 1f, 1f, .15f);
			mFocusRenderer.rect((int) mLastHoverCoords.x,
					(int) mLastHoverCoords.y, 1f, 1f);
			mFocusRenderer.end();

			Gdx.gl.glDisable(GL20.GL_BLEND);
		}
	}

	private ArrayList<Vector2> getPathForGhost(PolylineMapObject ghostObject) {
		Polyline polyline = ghostObject.getPolyline();

		ArrayList<Vector2> positions = new ArrayList<Vector2>();

		float vertices[] = polyline.getTransformedVertices();

		for (int i = 0; i < vertices.length; i = i + 2) {
			Vector2 position = new Vector2(vertices[i], vertices[i + 1]);
			positions.add(position);
		}

		return positions;
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
			dirs.add(Direction.NORTH);
		}
		if (this.lightAllowedAtTile(x, y - 1)) {
			dirs.add(Direction.SOUTH);
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

	@Override
	public void playerDiscoveredByGhost(final GhostActor ghost) {
		ghost.clearActions();

		final Vector2 diff = new Vector2(ghost.getX() - mPlayer.getX(),
				ghost.getY() - mPlayer.getY());
		final float length = diff.len();

		final float duration = length / 5f;

		Gdx.input.setInputProcessor(null);
		mPlayer.mBlockInput = true;
		ParallelAction parallelAction = Actions.parallel(
				Actions.run(new Runnable() {

					@Override
					public void run() {
						RotateToAction rotateToAction = ghost
								.getRotateActionUsingClosestDirection(
										diff.angle() - 180, duration / 4);
						ghost.addAction(rotateToAction);
					}
				}), Actions.moveTo(mPlayer.getX(), mPlayer.getY(), duration));

		RunnableAction runnableAction = Actions.run(new Runnable() {

			@Override
			public void run() {
				loseGame();
			}
		});

		SequenceAction sequenceAction = Actions.sequence(parallelAction,
				runnableAction);
		ghost.addAction(sequenceAction);
	}

	public void loseGame() {
		switchScreen(new Runnable() {

			@Override
			public void run() {
				mGajm.setScreen(new TiledLevelScreen(mGajm, mLevelId));
			}
		});
	}

	public void onNoMoreLights() {
		if (mDidEnd) {
			return;
		}
		final Label msg = new Label(
				"No more laser charges\nPress 'R' to restart", new LabelStyle(
						manage(new BitmapFont()), Color.CYAN));
		msg.setAlignment(Align.center);
		msg.getColor().a = 0f;
		msg.setPosition(mUiStage.getWidth() / 2 - msg.getMinWidth() / 2, 50f);
		mUiStage.addActor(msg);
		msg.addAction(Actions.delay(1f, Actions.fadeIn(1f)));
	}

	private boolean mDidEnd;

	public void onAllGhostsDead() {
		if (mDidEnd) {
			return;
		}
		mDidEnd = true;
		Gajm.maxClearedLevel = Math.max(Gajm.maxClearedLevel, mLevelId);
		mUiStage.addAction(Actions.delay(1f, Actions.run(new Runnable() {

			@Override
			public void run() {
				switchScreen(new Runnable() {

					@Override
					public void run() {
						if (mLevelId == 4) {
							mGajm.setScreen(new EndScreen(mGajm));
						} else {
							mGajm.setScreen(new TiledLevelScreen(mGajm,
									mLevelId + 1));
						}

					}
				});

			}
		})));
	}

}

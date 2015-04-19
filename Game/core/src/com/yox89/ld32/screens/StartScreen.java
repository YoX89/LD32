package com.yox89.ld32.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Align;
import com.yox89.ld32.Gajm;
import com.yox89.ld32.Physics;
import com.yox89.ld32.actors.Torch;
import com.yox89.ld32.util.Assets;
import com.yox89.ld32.util.PhysicsUtil;
import com.yox89.ld32.util.PhysicsUtil.BodyParams;

public class StartScreen extends BaseScreen {
	private Stage game;
	private Gajm gajm;

	public StartScreen(Gajm gajm) {
		this.gajm = gajm;
	}

	@Override
	protected void init(Stage game, Stage ui, Physics physics) {
		this.game = game;
		final Texture img = manage(new Texture("StartButtonEng.png"));
		img.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		final StartGameButtonActor startBtn = new StartGameButtonActor(img,
				physics.world,
				Math.min(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT) / 5f);
		game.addActor(startBtn);
		startBtn.setPosition(GAME_WORLD_WIDTH / 2 - startBtn.getWidth() / 2,
				GAME_WORLD_HEIGHT / 2);

		final Vector2 startPos = new Vector2(startBtn.getX()
				+ startBtn.getWidth() / 2, startBtn.getY());
		game.stageToScreenCoordinates(startPos);
		ui.screenToStageCoordinates(startPos);

		final float BTN_SIDE = 48f;
		final float PADDING = .2f * BTN_SIDE;
		for (int i = 0; i < 16; i++) {
			final Vector2 pos = new Vector2(startPos.x, startPos.y - 1.5f
					* BTN_SIDE);
			pos.x += (PADDING + BTN_SIDE) * ((i % 4) - 2);
			pos.y -= (PADDING + BTN_SIDE) * (i / 4);
			final LevelJumpButton skip = new LevelJumpButton(i + 1);
			skip.setPosition(pos.x + PADDING / 2, pos.y + PADDING / 2);
			skip.setSize(BTN_SIDE, BTN_SIDE);
			ui.addActor(skip);
		}

		final HelpButton howToPlay = new HelpButton();
		howToPlay.setSize(3 * BTN_SIDE, BTN_SIDE);
		howToPlay.setPosition(Gdx.graphics.getWidth() * 7 / 8,
				Gdx.graphics.getHeight() / 10f, Align.center);
		ui.addActor(howToPlay);

		PhysicsUtil.createBody(new BodyParams(physics.world) {

			@Override
			public void setShape(PolygonShape ps) {
				ps.setAsBox(startBtn.getWidth() / 2, startBtn.getHeight() / 2f);
			}
		}).setTransform(startBtn.getX() + startBtn.getWidth() / 2,
				startBtn.getY() + startBtn.getHeight() / 2, 0f);

		Torch startBtnTorch = new Torch(physics, Color.MAGENTA, 0, false);
		startBtnTorch.setPosition(startBtn.getX() + startBtn.getWidth() / 2f,
				startBtn.getY() + startBtn.getHeight() / 2);
		game.addActor(startBtnTorch);

		Torch torchUpCorner = new Torch(physics, 0);
		torchUpCorner.setPosition(GAME_WORLD_WIDTH - 1, GAME_WORLD_HEIGHT - 1);
		game.addActor(new Torch(physics, 270));
		game.addActor(torchUpCorner);

		final float ghostSize = Math.min(Gdx.graphics.getWidth() / 4,
				Gdx.graphics.getHeight() / 4);
		final Actor rightGhost = new Ghost(ghostSize, false);
		rightGhost.setPosition(Gdx.graphics.getWidth() * 9f / 10f - ghostSize,
				Gdx.graphics.getHeight() / 2f);

		final Actor leftGhost = new Ghost(ghostSize, true);
		leftGhost.setPosition(Gdx.graphics.getWidth() / 10f,
				Gdx.graphics.getHeight() / 2f);

		final Label titleLbl = new Label("Mirror Mirror", new LabelStyle(
				manage(new BitmapFont()), Color.WHITE));
		titleLbl.setPosition(
				Gdx.graphics.getWidth() / 2 - titleLbl.getMinWidth(),
				Gdx.graphics.getHeight() * 0.75f);
		titleLbl.setFontScale(2f);

		final Label copyLbl = new Label(
				"Made by: Kevlanche, Jonathan Hagberg, "
						+ "\nAdam Nilsson & Marie Versland for LD32 in 48 hours",
				new LabelStyle(manage(new BitmapFont()), Color.CYAN));
		copyLbl.setAlignment(Align.center);
		copyLbl.setFontScale(0.9f);
		copyLbl.setPosition((Gdx.graphics.getWidth() - copyLbl.getMinWidth()
				/ copyLbl.getFontScaleX()) / 2, 30);

		ui.addActor(rightGhost);
		ui.addActor(leftGhost);

		ui.addActor(new MuteButton());

		ui.addActor(titleLbl);
		ui.addActor(copyLbl);
	}

	private class Ghost extends Actor {

		private final Texture mTexture;
		private final float mSize;
		private final boolean mFlipHorizontal;

		public Ghost(float size, boolean flipHorizontal) {
			mTexture = manage(new Texture("story_ghost.png"));
			mTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);

			mSize = size;

			mFlipHorizontal = flipHorizontal;

			float deltaX = mFlipHorizontal ? mSize / 5f : -mSize / 5f;
			
			addAction(Actions.forever(Actions.sequence(Actions.moveBy(
					deltaX, 0f, 1.0f, Interpolation.swing), Actions
					.moveBy(-deltaX, 0f, 1.0f, Interpolation.swing))));
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.setColor(getColor());
			batch.draw(mTexture, getX(), getY(), mSize, mSize, 0, 0,
					mTexture.getWidth(), mTexture.getHeight(), mFlipHorizontal,
					false);
		}
	}

	private class StartGameButtonActor extends Actor {

		private final Texture mTexture;

		public StartGameButtonActor(Texture tex, World world, final float size) {
			mTexture = tex;
			setSize(size * 2, size);

			addListener(new InputListener() {

				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					switchScreen(new Runnable() {
						@Override
						public void run() {
							gajm.setScreen(new TiledLevelScreen(gajm, 1));
						}
					});
					return true;
				};

				@Override
				public void enter(InputEvent event, float x, float y,
						int pointer, Actor fromActor) {
					setColor(Color.WHITE);
					super.enter(event, x, y, pointer, fromActor);
				}

				@Override
				public void exit(InputEvent event, float x, float y,
						int pointer, Actor toActor) {
					setColor(Color.LIGHT_GRAY);
					super.exit(event, x, y, pointer, toActor);
				}
			});
			setColor(Color.LIGHT_GRAY);

		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.setColor(getColor());
			batch.draw(mTexture, getX(), getY(), getOriginX(), getOriginY(),
					getWidth(), getHeight(), getScaleX(), getScaleY(),
					getRotation(), 0, 0, mTexture.getWidth(),
					mTexture.getHeight(), false, false);

		}
	}

	private class MuteButton extends Actor {

		public MuteButton() {
			final float size = Math.min(Gdx.graphics.getWidth(),
					Gdx.graphics.getHeight()) / 8f;
			setSize(size, size);
			setY(Gdx.graphics.getHeight() - getHeight());

			addListener(new InputListener() {

				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					Assets.setSoundEnabled(!Assets.isSoundEnabled());
					return true;
				};

				@Override
				public void enter(InputEvent event, float x, float y,
						int pointer, Actor fromActor) {
					setColor(Color.WHITE);
					super.enter(event, x, y, pointer, fromActor);
				}

				@Override
				public void exit(InputEvent event, float x, float y,
						int pointer, Actor toActor) {
					setColor(Color.LIGHT_GRAY);
					super.exit(event, x, y, pointer, toActor);
				}
			});
			setColor(Color.LIGHT_GRAY);

		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.enableBlending();
			batch.setColor(getColor());
			final Texture tex = Assets.isSoundEnabled() ? Assets.sound_on
					: Assets.sound_off;
			batch.draw(tex, getX(), getY(), getOriginX(), getOriginY(),
					getWidth(), getHeight(), getScaleX(), getScaleY(),
					getRotation(), 0, 0, tex.getWidth(), tex.getHeight(),
					false, false);

		}
	}

	private class LevelJumpButton extends Label {

		public LevelJumpButton(final int levelId) {
			super(String.valueOf(levelId), new LabelStyle(
					manage(new BitmapFont()), Color.WHITE));
			setAlignment(Align.center);

			final boolean enabled = Gajm.maxClearedLevel >= levelId - 1;
			if (!enabled) {
				setColor(Color.DARK_GRAY);
			} else {
				addListener(new InputListener() {

					public boolean touchDown(InputEvent event, float x,
							float y, int pointer, int button) {
						switchScreen(new Runnable() {
							@Override
							public void run() {
								gajm.setScreen(new TiledLevelScreen(gajm,
										levelId));
							}
						});
						return true;
					};

					@Override
					public void enter(InputEvent event, float x, float y,
							int pointer, Actor fromActor) {
						setColor(Color.WHITE);
						super.enter(event, x, y, pointer, fromActor);
					}

					@Override
					public void exit(InputEvent event, float x, float y,
							int pointer, Actor toActor) {
						setColor(Color.LIGHT_GRAY);
						super.exit(event, x, y, pointer, toActor);
					}
				});
				setColor(Color.LIGHT_GRAY);
			}

		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			if (Gajm.maxClearedLevel < 0) {
				return;
			}
			batch.enableBlending();
			batch.setColor(getColor());
			final Texture tex = Assets.skipLevelButton;
			batch.draw(tex, getX(), getY(), getOriginX(), getOriginY(),
					getWidth(), getHeight(), getScaleX(), getScaleY(),
					getRotation(), 0, 0, tex.getWidth(), tex.getHeight(),
					false, false);
			super.draw(batch, parentAlpha);

		}
	}

	private class HelpButton extends Label {

		public HelpButton() {
			super("How to play", new LabelStyle(manage(new BitmapFont()),
					Color.WHITE));
			setAlignment(Align.center);

			addListener(new InputListener() {

				public boolean touchDown(InputEvent event, float x, float y,
						int pointer, int button) {
					switchScreen(new Runnable() {
						@Override
						public void run() {
							gajm.setScreen(new HowToPlayScreen(gajm));
						}
					});
					return true;
				};

				@Override
				public void enter(InputEvent event, float x, float y,
						int pointer, Actor fromActor) {
					setColor(Color.WHITE);
					super.enter(event, x, y, pointer, fromActor);
				}

				@Override
				public void exit(InputEvent event, float x, float y,
						int pointer, Actor toActor) {
					setColor(Color.LIGHT_GRAY);
					super.exit(event, x, y, pointer, toActor);
				}
			});
			setColor(Color.LIGHT_GRAY);

		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.enableBlending();
			batch.setColor(getColor());
			final Texture tex = Assets.skipLevelButton;
			batch.draw(tex, getX(), getY(), getOriginX(), getOriginY(),
					getWidth(), getHeight(), getScaleX(), getScaleY(),
					getRotation(), 0, 0, tex.getWidth(), tex.getHeight(),
					false, false);
			super.draw(batch, parentAlpha);

		}
	}
}

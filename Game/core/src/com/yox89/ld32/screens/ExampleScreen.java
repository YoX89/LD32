package com.yox89.ld32.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.yox89.ld32.actors.PhysicsActor;
import com.yox89.ld32.actors.ShadyActor;
import com.yox89.ld32.actors.Wall;
import com.yox89.ld32.util.Collision;
import com.yox89.ld32.util.PhysicsUtil;
import com.yox89.ld32.util.PhysicsUtil.BodyParams;

public class ExampleScreen extends BaseScreen {
	private Stage game;

	@Override
	protected void init(Stage game, Stage ui, World physicsWorld) {
		this.game = game;
		final Texture img = manage(new Texture("badlogic.jpg"));
		img.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		final ShadyActor shady = manage(new ShadyActor());
		shady.setBounds(0, 0, 20, 20);
		game.addActor(shady);

		final ExampleActor actor = new ExampleActor(img, physicsWorld,
				Math.min(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT) / 5f);
		game.addActor(actor);

		for (int x = 0; x < GAME_WORLD_WIDTH; x++) {
			final Wall wall = new Wall(physicsWorld);
			wall.setX(x);
			game.addActor(wall);
		}

		actor.setPosition(GAME_WORLD_WIDTH / 2 - actor.getWidth() / 2,
				GAME_WORLD_HEIGHT / 2);

		final float mvx = GAME_WORLD_WIDTH / 4;
		actor.addAction(Actions.forever(Actions.sequence(
				Actions.moveBy(mvx, 0f, 2f, Interpolation.bounce),
				Actions.moveBy(-mvx, 0f, 2f, Interpolation.bounce))));

		final Label label = new Label("Hello, I am ui", new LabelStyle(
				new BitmapFont(), Color.CYAN)) {

			@Override
			public void act(float delta) {
				super.act(delta);

				setText(String
						.format("%.0f | %.0f", actor.getX(), actor.getY()));
			}
		};
		label.setPosition(50, 45);

		ui.addActor(label);

		setUpUsAdamTheCrime(img);
	}

	private void setUpUsAdamTheCrime(Texture img) {
		final AdamActor actor = new AdamActor();
		game.addActor(actor);

		final float mvx = GAME_WORLD_WIDTH / 4;
		actor.addAction(Actions.forever(Actions.sequence(
				Actions.moveBy(mvx * 2, 0f, 4f, Interpolation.sine),
				Actions.moveBy(0f, mvx, 4f, Interpolation.sineOut),
				Actions.moveBy(-mvx * 2, 0f, 4f, Interpolation.circleIn),
				Actions.moveBy(0f, -mvx, 4f, Interpolation.pow2)

		)));

		final float size = Math.min(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT) / 5f;
		actor.setSize(size, size);
		actor.setPosition(GAME_WORLD_WIDTH / 4, GAME_WORLD_HEIGHT / 4);

	}

	private class ExampleActor extends PhysicsActor {

		private final Texture mTexture;

		public ExampleActor(Texture tex, World world, final float size) {
			mTexture = tex;

			initPhysicsBody(PhysicsUtil.createBody(new BodyParams(world) {

				@Override
				public BodyType getBodyType() {
					return BodyType.DynamicBody;
				}

				@Override
				public short getCollisionType() {
					return Collision.PLAYER;
				}

				@Override
				public short getCollisionMask() {
					return Collision.WORLD;
				}

				@Override
				public void setShape(PolygonShape ps) {
					ps.setAsBox(size / 2, size / 2, new Vector2(size / 2,
							size / 2), 0f);
				}
			}));
			setSize(size, size);
		}

		@Override
		public void act(float delta) {
			super.act(delta);

			if (Gdx.input.isKeyPressed(Keys.UP)) {
				moveBy(0f, delta * 10f);
			} else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
				moveBy(0f, delta * -10f);
			}

			if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
				mPhysicsBody.applyForceToCenter(new Vector2(0f, 1000f), true);
			}
		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.draw(mTexture, getX(), getY(), getOriginX(), getOriginY(),
					getWidth(), getHeight(), getScaleX(), getScaleY(),
					getRotation(), 0, 0, mTexture.getWidth(),
					mTexture.getHeight(), false, true);
		}
	}

	private class AdamActor extends Actor {

		private final Texture mTexture;
		private float magicMultiplier;

		public AdamActor() {
			final Texture img = manage(new Texture("epl11737fig2.jpg"));
			img.setFilter(TextureFilter.Linear, TextureFilter.Linear);
			mTexture = img;
			magicMultiplier = 1f;
		}

		public void increaseSpeed(float f) {
			magicMultiplier += f;
		}

		@Override
		public void act(float delta) {
			if (Gdx.input.isKeyJustPressed(Keys.A)) {
				addAction(Actions.rotateBy(-11f, .25f, Interpolation.circleOut));
				increaseSpeed(0.3f);
			}
			super.act(delta * magicMultiplier);

		}

		@Override
		public void draw(Batch batch, float parentAlpha) {
			batch.draw(mTexture, getX(), getY(), getOriginX()
					+ (getWidth() / 2), getOriginY() + (getHeight() / 2),
					getWidth(), getHeight(), getScaleX(), getScaleY(),
					getRotation(), 0, 0, mTexture.getWidth(),
					mTexture.getHeight(), false, false);
		}
	}
}

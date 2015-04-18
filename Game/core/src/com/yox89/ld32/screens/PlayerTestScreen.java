package com.yox89.ld32.screens;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.yox89.ld32.Physics;
import com.yox89.ld32.actors.PlayerActor;

public class PlayerTestScreen extends BaseScreen {
	private Stage game;


	@Override
	protected void init(Stage game, Stage ui, Physics physicsWorld) {
		this.game = game;
		final Texture img = manage(new Texture("epl11737fig2.jpg"));
		img.setFilter(TextureFilter.Linear, TextureFilter.Linear);

		final PlayerActor player = new PlayerActor(img,physicsWorld);
		game.addActor(player);


		player.setPosition(GAME_WORLD_WIDTH / 2 - player.getWidth() / 2,
				GAME_WORLD_HEIGHT / 2);

		
	}

}

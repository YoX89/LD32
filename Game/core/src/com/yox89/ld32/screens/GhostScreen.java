package com.yox89.ld32.screens;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.yox89.ld32.Physics;
import com.yox89.ld32.actors.GhostActor;
import com.yox89.ld32.util.Assets;

public class GhostScreen extends BaseScreen {
	
	private Stage game;
	
	@Override
	protected void init(Stage game, Stage ui, Physics physicsWorld) {
		this.game = game;

		final TextureRegion texture = Assets.get("tilePink_04");
		
		ArrayList<Vector2>positions = new ArrayList<Vector2>();
		
		positions.add(new Vector2(5, 5));
		positions.add(new Vector2(60, 5));
		
		final GhostActor ghostActor = new GhostActor(texture, physicsWorld, 2, positions);
		
		game.addActor(ghostActor);
	}
}

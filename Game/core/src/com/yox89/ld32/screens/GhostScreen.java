package com.yox89.ld32.screens;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.yox89.ld32.Physics;
import com.yox89.ld32.actors.GhostActor;

public class GhostScreen extends BaseScreen {
	
	private Stage game;
	
	@Override
	protected void init(Stage game, Stage ui, Physics physicsWorld) {
		this.game = game;
		
		ArrayList<Vector2>positions = new ArrayList<Vector2>();
		
		positions.add(new Vector2(5, 5));
		positions.add(new Vector2(60, 5));
		
		final GhostActor ghostActor = new GhostActor(physicsWorld, positions);
		
		game.addActor(ghostActor);
	}
}

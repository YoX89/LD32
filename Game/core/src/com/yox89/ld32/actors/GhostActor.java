package com.yox89.ld32.actors;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.yox89.ld32.util.Collision;
import com.yox89.ld32.util.PhysicsUtil;
import com.yox89.ld32.util.PhysicsUtil.BodyParams;

public class GhostActor extends PhysicsActor {
	private final TextureRegion mTextureRegion;
	
	public GhostActor(TextureRegion textureRegion, World world, final float size, ArrayList<Vector2>positions) {
		this.mTextureRegion = textureRegion;
		
		this.initPhysicsBody(PhysicsUtil.createBody(new BodyParams(world) {

			@Override
			public BodyType getBodyType() {
				return BodyType.DynamicBody;
			}

			@Override
			public short getCollisionType() {
				return Collision.GHOST;
			}

			@Override
			public short getCollisionMask() {
				return Collision.NONE;
			}

			@Override
			public void setShape(PolygonShape ps) {
				ps.setAsBox(size / 2, size / 2, new Vector2(size / 2,
						size / 2), 0f);
			}
			
			@Override
			public boolean isSensor() {
				return true;
			}
			
		}));
		this.setSize(size, size);
		
		assert (positions.size() > 0) : "The ghost must have at least one position";
		Vector2 initialPosition = positions.get(0);
		
		this.setPosition(initialPosition.x, initialPosition.y);
		
		this.setupActions(positions);
	}
	
	private void setupActions(ArrayList<Vector2> positions) {
		
		if (positions.size() > 1) {
			Vector2 currentPosition = positions.get(0);
			
			Action[] actions = new Action[positions.size()];
			
			for (int i = 1 ; i < positions.size(); i++) {
				Vector2 newPosition = positions.get(i);
				actions[i-1] = Actions.moveTo(newPosition.x, newPosition.y, 2f);
				System.out.println("Adding action to x:" + newPosition.x + " y:" + newPosition.y + " to index " + (i-1));
			}
			
			actions[positions.size() - 1] = Actions.moveTo(currentPosition.x, currentPosition.y, 2f);
			System.out.println("Adding action to x: " + currentPosition.x + " y:" + currentPosition.y + " to index " + (positions.size() - 1));
			
			SequenceAction moveActions = Actions.sequence(actions);

			this.addAction(Actions.forever(moveActions));			
			
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(mTextureRegion, this.getX(), this.getY(), this.getOriginX(), this.getOriginY(),
				this.getWidth(), this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation());
	}
}

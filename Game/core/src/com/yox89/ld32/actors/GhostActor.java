package com.yox89.ld32.actors;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.yox89.ld32.Physics;
import com.yox89.ld32.raytracing.RayDispatcher.Dispatcher;
import com.yox89.ld32.raytracing.RayDispatcher.Ray;
import com.yox89.ld32.raytracing.RayDispatcher.RayRequest;
import com.yox89.ld32.raytracing.RayDispatcher.RayTarget;
import com.yox89.ld32.util.Collision;
import com.yox89.ld32.util.PhysicsUtil;
import com.yox89.ld32.util.PhysicsUtil.BodyParams;

public class GhostActor extends PhysicsActor implements Disposable, RayTarget {

	private static final float SPEED = 3f;
	private final Texture mTexture;

	public GhostActor(Physics physicsWorld, ArrayList<Vector2> positions) {

		final Texture img = new Texture("ghost_pixelart.png");
		this.mTexture = img;

		final Vector2[] polygonShape = this.getShapeVertices();
		
		this.initPhysicsBody(PhysicsUtil.createBody(new BodyParams(
				physicsWorld.world) {

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
				return Collision.PLAYER;
			}

			@Override
			public void setShape(PolygonShape ps) {
				ps.set(polygonShape);
			}

			@Override
			public boolean isSensor() {
				return true;
			}

		}));
		this.setSize(1f, 1f);

		assert (positions.size() > 0) : "The ghost must have at least one position";
		Vector2 initialPosition = positions.get(0);

		this.setPosition(initialPosition.x, initialPosition.y);

		this.setupActions(positions);
	}
	
	private Vector2[] getShapeVertices() {
		Vector2[] vertices = new Vector2[3];
		
		vertices[0] = new Vector2(0f, 0f);
		vertices[1] = new Vector2(4f, -2f);
		vertices[2] = new Vector2(4f, 2f);
		
		return vertices;
	}

	private void setupActions(ArrayList<Vector2> positions) {

		if (positions.size() > 1) {
			Vector2 startPosition = positions.get(0);
			Vector2 currentPosition = startPosition;

			Action[] actions = new Action[positions.size() * 2];

			for (int i = 1; i < positions.size(); i++) {
				Vector2 newPosition = positions.get(i);
				
				 Vector2 diff = new Vector2(newPosition.x - currentPosition.x, newPosition.y - currentPosition.y);
				 float length = diff.len();

				float duration = length / SPEED;
				
				actions[i * 2 - 2] = Actions.rotateTo(diff.angle());
				actions[i * 2 - 1] = Actions.moveTo(newPosition.x, newPosition.y,
						duration);
				diff.angle();
				
				currentPosition = newPosition;
			}
			
			Vector2 diff = new Vector2(startPosition.x - currentPosition.x, startPosition.y - currentPosition.y);
			float length = diff.len();
			float duration = length / SPEED;
			
			actions[actions.length - 2] = Actions.rotateTo(diff.angle());
			actions[actions.length - 1] = Actions.moveTo(startPosition.x,
					startPosition.y, duration);

			SequenceAction moveActions = Actions.sequence(actions);

			this.addAction(Actions.forever(moveActions));

		}
	}

	@Override
	public void onHitWithRay(Ray ray, Dispatcher dispatcher) {
		addAction(Actions.removeActor());
		
		Array<RayRequest> reqs = new Array<RayRequest>();
		reqs.add(new RayRequest(ray.color, ray.dst, ray.direction));
		dispatcher.dispatch(reqs);
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(mTexture, getX() - getWidth() / 2, getY() - getHeight() / 2, getOriginX() + getWidth() / 2, getOriginY() + getHeight() / 2,
				getWidth(), getHeight(), getScaleX(), getScaleY(),
				getRotation(), 0, 0, mTexture.getWidth(), mTexture.getHeight(),
				false, false);
	}

	@Override
	public void dispose() {
		mTexture.dispose();
	}
}

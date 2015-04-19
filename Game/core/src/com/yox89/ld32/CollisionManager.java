package com.yox89.ld32;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.yox89.ld32.actors.GhostActor;
import com.yox89.ld32.util.Collision;

public class CollisionManager implements ContactListener {
	
	public CollisionManagerListener mCollisionManagerListener;

	public CollisionManager(World world) {
		world.setContactListener(this);
	}
	
	@Override
	public void beginContact(Contact contact) {
		short ca = contact.getFixtureA().getFilterData().categoryBits;
		short cb = contact.getFixtureB().getFilterData().categoryBits;
		
		if (ca == Collision.PLAYER && cb == Collision.GHOST_VISION || ca == Collision.GHOST_VISION && cb == Collision.PLAYER) {
			if (mCollisionManagerListener != null) {
				GhostActor ghost = (GhostActor) (ca == Collision.GHOST_VISION ? contact.getFixtureA().getUserData() : contact.getFixtureB().getUserData()); 
				mCollisionManagerListener.playerDiscoveredByGhost(ghost);
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub

	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub

	}
	
	public interface CollisionManagerListener {
		public void playerDiscoveredByGhost(GhostActor ghost);
	}

}

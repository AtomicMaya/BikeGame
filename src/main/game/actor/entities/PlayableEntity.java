package main.game.actor.entities;

import main.game.actor.Actor;

/** Represent the {@linkplain GameEntity} playable by the player */
public interface PlayableEntity extends Actor {
	
	void triggerDeath(boolean wasGravity);

	void triggerVictory();

	void triggerCheckpoint();

	boolean getDeathStatus();

	boolean getVictoryStatus();

	boolean getIfWasKilledByGravity();
	
	public boolean isLookingRight();
	
}

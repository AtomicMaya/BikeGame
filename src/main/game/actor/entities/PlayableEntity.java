package main.game.actor.entities;

import main.game.actor.Actor;

/** Represent the {@linkplain GameEntity} playable by the player */
public interface PlayableEntity extends Actor {

	/**
	 * @param wasGravity whether this {@linkplain PlayableEntity} was killed by
	 * gravity
	 */
	void triggerDeath(boolean wasGravity);

	void triggerVictory();

	void triggerCheckpoint();

	boolean getDeathStatus();

	boolean getVictoryStatus();

	boolean getIfWasKilledByGravity();

	/** @return whether this {@linkplain PlayableEntity} is looking right */
	public boolean isLookingRight();

}

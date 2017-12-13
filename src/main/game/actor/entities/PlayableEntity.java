package main.game.actor.entities;

import main.game.actor.Actor;

/** Represent the {@linkplain GameEntity} playable by the player. */
public interface PlayableEntity extends Actor {

	/** @param wasGravity whether this {@linkplain PlayableEntity} was killed by gravity. */
	void triggerDeath(boolean wasGravity);

	/** Tell to this {@linkplain PlayableEntity} that it has won. */
	void triggerVictory();

	/** @return whether this {@linkplain PlayableEntity} is dead. */
	boolean getDeathStatus();

	/** @return whether this {@linkplain PlayableEntity} has won. */
	boolean getVictoryStatus();

	/** @return whether this {@linkplain PlayableEntity} was killed by gravity. */
	boolean getIfWasKilledByGravity();

	/** @return whether this {@linkplain PlayableEntity} is looking right. */
    boolean isLookingRight();
    
    /** Add the given number of ammunitions to the weapon using the good type of ammos */
    public void addAmmo(int quantity, boolean isRocket);

}

package main.game.actor.sensors;

import main.game.actor.Actor;

/** A global {@linkplain Sensor}. */
public interface Sensor extends Actor {
    /** @return whether the {@linkplain Sensor} detects anything. */
	boolean getSensorDetectionStatus();

	/** @return whether the {@linkplain Sensor} is running any actions. */
	boolean isOccupied();

    /**
     * Runs a specified action and sets the {@linkplain Sensor} to be busy for a certain time.
     * @param action The action to run.
     * @param time When the {@linkplain Sensor} should not be busy anymore.
     */
	void runAction(Runnable action, float time);
}

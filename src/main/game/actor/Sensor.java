package main.game.actor;

public interface Sensor extends Actor {
	boolean getSensorDetectionStatus();
	boolean isOccupied();

	void runAction(Runnable action, float time);

	/**
	 * Simulates a single time step.
	 * @param deltaTime elapsed time since last update, in seconds, non-negative
	 */
	default void update(float deltaTime) {
		// By default , actors have nothing to update
	}

	/**
	 * Default destroy, don't do anything
	 * */
	default void destroy() {
		// By default , actors have nothing to destroy
	}
}

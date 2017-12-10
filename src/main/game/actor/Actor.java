package main.game.actor;

import main.game.graphics.Graphics;
import main.math.Positionable;
import main.window.Canvas;

/** Represent an actor in it's logical state */
public interface Actor extends Positionable, Graphics {
	/**
	 * Simulates a single time step.
	 * @param deltaTime elapsed time since last update, in seconds, non-negative
	 */
	default void update(float deltaTime) {
		// By default , actors have nothing to update
	}

	/**
	 * Default destroy, don't do anything
	 */
	default void destroy() {
		// By default , actors have nothing to destroy
	}

	@Override
	default void draw(Canvas canvas) {
		// By default , actors have nothing to draw
	}

}
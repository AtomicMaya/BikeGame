package main.game.actor;

import java.io.Serializable;

import main.game.ActorGame;
import main.math.Positionable;
import main.window.Canvas;

public interface Actor extends Positionable, Graphics, Serializable {
	/**
	 * Simulates a single time step.
	 * 
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

	/**
	 * Method used to reCreate an Actor when loaded from a file, ActorGame can't be
	 * save and would anyway not be the same in a new game so this method reCreate
	 * the Actor using its parameters. Basically it do the same as the constructor
	 */
	public void reCreate(ActorGame game);
}
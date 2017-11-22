/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor;

import main.math.Positionable;

public interface Actor extends Positionable, Graphics {
	/**
	 * Simulates a single time step.
	 * 
	 * @param deltaTime
	 *            elapsed time since last update , in seconds , non-negative
	 */
	public default void update(float deltaTime) {
		// By default , actors have nothing to update
	}

	public abstract void destroy();// pk pas en abstract? si lactor a rien a destroy, on mettra rien dans sa methode
									// a lui, pas rien dans elle la
	// By default , actors have nothing to destroy

}

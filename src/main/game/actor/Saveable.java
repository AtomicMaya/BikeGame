/**
 *	Author: Clément Jeannet
 *	Date: 	4 déc. 2017
 */
package main.game.actor;

import main.game.ActorGame;

import java.io.Serializable;

/** Represent an {@linkplain Actor} which can be saved */
public interface Saveable extends Serializable, Actor {

	/**
	 * Method used recreate an {@linkplain Actor} when loaded from a file,
	 * {@linkplain ActorGame} can't be save and would anyway not be the same in
	 * a new game so this method create the {@linkplain Actor} using its
	 * parameters and an {@linkplain ActorGame} given in parameters.
	 * @param game {@linkplain ActorGame} where the {@linkplain Actor} evolve
	 */
	void reCreate(ActorGame game);
}

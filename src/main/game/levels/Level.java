/**
 *	Author: Clément Jeannet
 *	Date: 	23 nov. 2017
 */
package main.game.levels;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.math.Node;

import java.util.ArrayList;

public abstract class Level extends Node implements Actor {

	/**
	 * Because its asked
	 */
	private static final long serialVersionUID = 8954161709405265832L;

	// list of Actors in this level
	private ArrayList<Actor> actors = new ArrayList<>();

	// associated Actor Game
	protected transient ActorGame game;

	// Actor to follow with the camera in the game
	private Actor viewCandidate;

	/**
	 * Create a new level
	 * 
	 * @param game ActorGame in which the actors will evolve
	 */
	public Level(ActorGame game) {
		this.game = game;
	}

	/**
	 * Creation of all the actors in the level
	 */
    public abstract void createAllActors();

	/**
	 * @param actor to ads in the level
	 */
	public void addActor(Actor actor) {
		actors.add(actor);
	}

	/**
	 * @param player actor to follow with the camera in the level
	 */
	public void setViewCandidate(Actor player) {
		this.viewCandidate = player;
	}

	/**
	 * @return the list of all the actors in this level
	 */
	public ArrayList<Actor> getActors() {
		return actors;
	}

	/**
	 * @return the Actor to follow with he camera in the level
	 */
	public Actor getViewCandidate() {
		return viewCandidate;
	}

	/**
	 * @return : Whether this level is finished
	 */
	public abstract boolean isFinished();
}

/**
 *	Author: Clément Jeannet
 *	Date: 	23 nov. 2017
 */
package main.game.levels;

import java.util.ArrayList;

import main.game.actor.Actor;
import main.game.actor.ActorGame;
import main.game.actor.myEntities.FinishActor;
import main.math.Node;

public abstract class Level extends Node {

	// list of Actors in this level
	private ArrayList<Actor> actors = new ArrayList<Actor>();

	// associated Actor Game
	protected ActorGame game;

	// Actor to follow with the camera in the game
	private Actor viewCAndidate;

	// Actor use to end the level
	private FinishActor finishActor;

	/**
	 * Create a new level
	 * 
	 * @param game
	 *            ActorGame in wich the actors will evolve
	 */
	public Level(ActorGame game) {
		this.game = game;
	}

	/**
	 * Creation of all the actors in the level
	 */
	public abstract void createAllActors();

	/**
	 * @param actor
	 *            to ads in the level
	 */
	public void addActor(Actor actor) {
		actors.add(actor);
	}

	/**
	 * @param player
	 *            actor to follow with the camera in the level
	 */
	public void setViewCandidate(Actor player) {
		this.viewCAndidate = player;
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
		return viewCAndidate;
	}

	/**
	 * @param a
	 *            FinishActor use to end this level
	 */
	public void setFinishActor(FinishActor a) {
		this.finishActor = a;
	}

	/**
	 * @return the FinishActor use to end this level
	 */
	public FinishActor getFinishActor() {
		return finishActor;
	}
}

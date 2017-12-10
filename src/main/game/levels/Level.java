package main.game.levels;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.entities.PlayableEntity;
import main.game.graphicalStuff.EndGameGraphics;
import main.math.Node;
import main.math.Positionable;

import java.util.ArrayList;

/**
 * Represent a {@linkplain Level} which can create some {@linkplain Actor}s to
 * add to the game
 */
public abstract class Level extends Node implements Actor {

	// list of Actors in this level
	private ArrayList<Actor> actors = new ArrayList<>();

	// associated Actor Game
	protected transient ActorGame game;

	// Actor to follow with the camera in the game
	private Positionable viewCandidate;

	// player's actor
	private PlayableEntity payload;

	/**
	 * Create a new {@linkplain Level}.
	 * @param game {@linkplain ActorGame} in which the {@linkplain Actor}s will evolve
	 */
	public Level(ActorGame game) {
		this.game = game;
		this.actors.add(new EndGameGraphics(game));
	}

	/**
	 * Creation of all the {@linkplain Actor}s in the {@linkplain Level}
	 */
	public abstract void createAllActors();

	/**
	 * @param actor  {@linkplain Actor} to ads in the {@linkplain Level}
	 */
	public void addActor(Actor actor) {
		this.actors.add(actor);
	}

	/**
	 * @param player {@linkplain Actor} to follow with the camera in the {@linkplain Level}
	 */
	public void setViewCandidate(Actor player) {
		this.viewCandidate = player;
	}

	/** Main {@linkplain Actor} of the game */
	public void setPayload(PlayableEntity payload) {
		this.payload = payload;
	}

	/**
	 * @return the list of all the {@linkplain Actor}s in this {@linkplain Level}
	 */
	public ArrayList<Actor> getActors() {
        ArrayList<Actor> temporary = new ArrayList<>(this.actors);
		this.actors.clear();
		return temporary;
	}

	/**
	 * @return the {@linkplain Actor} to follow with he camera in the {@linkplain Level}
	 */
	public Positionable getViewCandidate() {
		return viewCandidate;
	}

	/**
	 * @return the playable {@linkplain Actor}.
	 */
	public PlayableEntity getPayload() {
		return payload;
	}

	/**
	 * @return whether this {@linkplain Level} is finished
	 */
	public abstract boolean isFinished();
}

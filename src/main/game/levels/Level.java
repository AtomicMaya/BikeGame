package main.game.levels;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.entities.PlayableEntity;
import main.game.actor.sensors.Checkpoint;
import main.game.actor.sensors.StartCheckpoint;
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
	protected ActorGame game;

	/**
	 * Create a new {@linkplain Level}.
	 * @param game {@linkplain ActorGame} in which the {@linkplain Actor}s will
	 * evolve
	 */
	public Level(ActorGame game) {
		this.game = game;
	}

	/**
	 * Creation of all the {@linkplain Actor}s in the {@linkplain Level}
	 */
	public abstract void createAllActors();

	/**
	 * @param actor {@linkplain Actor} to ads in the {@linkplain Level}
	 */
	public void addActor(Actor actor) {
		if (!actors.contains(actor))
			this.actors.add(actor);
	}

	/**
	 * @return the list of all the {@linkplain Actor}s in this
	 * {@linkplain Level}
	 */
	public ArrayList<Actor> getActors() {
		ArrayList<Actor> temporary = new ArrayList<>(this.actors);
		temporary.add(new EndGameGraphics(game));
		if (getPayload() != null && !temporary.contains(getPayload()))
			temporary.add(getPayload());
		if (getSpawnCheckpoint() != null && !temporary.contains(getSpawnCheckpoint()))
			temporary.add(getSpawnCheckpoint());
		this.actors.clear();
		return temporary;
	}

	/**
	 * @return the {@linkplain Actor} to follow with he camera in the
	 * {@linkplain Level}, if null the viewCandidate will be set to the bike
	 * spawned by the {@linkplain StartCheckpoint}
	 * @see #getSpawnCheckpoint()
	 */
	public Positionable getViewCandidate() {
		return null;
	}

	/**
	 * @return the playable {@linkplain Actor}, if null the payload will be set
	 * to the bike spawned by the {@linkplain StartCheckpoint}
	 * @see #getSpawnCheckpoint()
	 */
	public PlayableEntity getPayload() {
		return null;
	}

	/**
	 * @return whether this {@linkplain Level} is finished
	 */
	public abstract boolean isFinished();

	/**
	 * @return the spawn {@linkplain Checkpoint}, will spawn the bike if created
	 * with attribute player equals null, has to be defined if getPayload is not
	 * defined
	 * @see #getPayload
	 * @see StartCheckpoint#StartCheckpoint
	 */
	public abstract StartCheckpoint getSpawnCheckpoint();
}

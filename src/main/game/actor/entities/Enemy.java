package main.game.actor.entities;

import main.game.ActorGame;
import main.math.Vector;

public abstract class Enemy extends GameEntity {
	
	/**
	 * Create a new {@linkplain Enemy}
	 * @param game The master {@linkplain ActorGame}
	 * @param position Initial position of this {@linkplain Enemy}
	 * */
	public Enemy(ActorGame game, Vector position) {
		super(game, false, position);
	}

	/** Used for save purposes */
	private static final long serialVersionUID = 5478906395592240743L;

	abstract void kill();
}

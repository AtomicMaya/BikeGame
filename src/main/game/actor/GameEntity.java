/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor;

import main.math.Entity;
import main.math.Vector;

public abstract class GameEntity {
	private Entity entity;

	private ActorGame actorGame;

	public GameEntity(ActorGame game, boolean fixed, Vector position) {
		if (game == null)
			throw new NullPointerException("Game is null");
		if (position == null)
			throw new NullPointerException("Vector is null");
		this.actorGame = game;

		entity = game.newEntity(position, fixed);
	}

	public GameEntity(ActorGame game, boolean fixed) {
		if (game == null)
			throw new NullPointerException("Game is null");
		this.actorGame = game;

		entity = game.newEntity(fixed);
	}

	protected Entity getEntity() {
		return entity;
	}

	protected ActorGame getOwner() {
		return actorGame;
	}

	protected void destroy() {
		entity.destroy();
	}
}

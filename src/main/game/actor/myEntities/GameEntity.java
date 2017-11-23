/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.myEntities;

import main.game.actor.Actor;
import main.game.actor.ActorGame;
import main.math.Entity;
import main.math.Transform;
import main.math.Vector;

public abstract class GameEntity implements Actor {
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

	public void destroy() {
		entity.destroy();
	}

	@Override
	public Transform getTransform() {
		return entity.getTransform();
	}

	@Override
	public Vector getVelocity() {
		return entity.getVelocity();
	}

}

/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor;

import main.math.Entity;
import main.math.Node;
import main.math.Vector;

public abstract class GameEntity extends Node implements Actor { // Extend Node non???? Je veux pas redéfinir les
																	// methodes de Attachable alors que c'est deja fait
																	// dans Node surtout que je vois pas lutilite du
																	// extend positionable dans actor pour ca...
	private Entity entity;

	private ActorGame actorGame;// utilité de ca? ca fait une boucle jai l'impression

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
	public void update(float deltaTime) {
		//entity.setPosition(this.getPosition());
	}
}

/**
 *	Author: Clément Jeannet
 *	Date: 	27 nov. 2017
 */
package main.game.actor.entities;

import main.game.ActorGame;
import main.math.BasicContactListener;
import main.math.Entity;
import main.math.Shape;
import main.math.Vector;

public class FinishActor extends GameEntity {

	private Entity player;
	private BasicContactListener contactListener;

	private boolean finish = false;

	public FinishActor(ActorGame game, Vector position, GameEntity player, Shape shape) {
		super(game, true, position);

		build(shape, -1, -1, true);
		
		this.player = player.getEntity();
		contactListener = new BasicContactListener();
		this.addContactListener(contactListener);

	}

	@Override
	public void update(float deltaTime) {
		finish = contactListener.getEntities().contains(player);
	}

	public boolean isFinished() {
		return finish;
	}
	
	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}

}

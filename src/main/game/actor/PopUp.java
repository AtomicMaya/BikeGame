package main.game.actor;

import main.game.ActorGame;
import main.game.actor.entities.GameEntity;
import main.math.Vector;

public abstract class PopUp extends GameEntity{

	public PopUp(ActorGame game, Vector position) {
		super(game, true, position);

	}
	
}

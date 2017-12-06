package main.game.actor.actorBuilder;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.math.Transform;
import main.math.Vector;

public abstract class ActorBuilder implements Actor {

	public ActorBuilder(ActorGame game) {

	}

	public void update(float deltaTime) {
		
	}
	
	@Override
	public Transform getTransform() {
		return null;
	}

	@Override
	public Vector getVelocity() {
		return null;
	}
	
	public abstract Actor getActor();

}

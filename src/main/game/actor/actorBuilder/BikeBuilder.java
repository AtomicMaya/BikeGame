/**
 *	Author: Clément Jeannet
 *	Date: 	6 déc. 2017
 */
package main.game.actor.actorBuilder;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.entities.Bike;
import main.math.Vector;
import main.window.Canvas;

public class BikeBuilder extends ActorBuilder {

	private Bike bike;
	private Vector position;
	private ActorGame game;

	private boolean isDone = false;

	public BikeBuilder(ActorGame game) {
		super(game);
		this.game = game;
		bike = new Bike(game, game.getMouse().getPosition());

	}

	@Override
	public void update(float deltaTime) {
		if (isLeftPressed()) {
			isDone = true;
		}
		if (!isDone) {
			position = getMousePosition();
			bike.destroy();
			bike = new Bike(game, position);
		} else
			bike.update(deltaTime);

	}

	@Override
	public void draw(Canvas canvas) {
		bike.draw(canvas);
	}

	@Override
	public Actor getActor() {
		return new Bike(game, position);
	}

	@Override
	public boolean isDone() {

		return isDone;
	}

}

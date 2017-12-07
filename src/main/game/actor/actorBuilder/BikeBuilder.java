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
		bike = new Bike(game, getFlooredMousePosition());
	}

	@Override
	public void update(float deltaTime) {
		if (isLeftPressed()) {
			isDone = true;
		}
		if (!isDone) {
			position = getFlooredMousePosition();
			bike.destroy();
			bike = new Bike(game, position);
		} 

	}

	@Override
	public void draw(Canvas canvas) {
		bike.draw(canvas);
	}

	@Override
	public Actor getActor() {
		return bike;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		bike.destroy();
		bike = new Bike(game, position);
	}

}

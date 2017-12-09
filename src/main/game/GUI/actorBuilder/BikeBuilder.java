/**
 *	Author: Clément Jeannet
 *	Date: 	6 déc. 2017
 */
package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.entities.Bike;
import main.game.actor.entities.PlayableEntity;
import main.math.ExtendedMath;
import main.math.Vector;
import main.window.Canvas;

public class BikeBuilder extends ActorBuilder {

	private Bike bike;
	private Vector position;
	private ActorGame game;

	private boolean isDone = false;
	private boolean hover = false;

	public BikeBuilder(ActorGame game) {
		super(game);
		this.game = game;
		bike = new Bike(game, getFlooredMousePosition());
	}

	@Override
	public void update(float deltaTime, float zoom) {
		if (isLeftPressed() || isRightPressed()) {
			isDone = true;
		}
		if (!isDone) {
			position = game.getMouse().getPosition();
			bike.destroy();
			bike = new Bike(game, position);
		} else
			hover = ExtendedMath.isInRectangle(position.sub(2, .5f), position.add(2, 2), game.getMouse().getPosition());

		if (hover && isRightPressed())
			isDone = false;
	}

	@Override
	public void draw(Canvas canvas) {
		bike.draw(canvas);
	}

	@Override
	public Actor getActor() {
		return bike;
	}

	public PlayableEntity getBike() {
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

	@Override
	public boolean isHovered() {
		return hover;
	}

	@Override
	public void destroy() {
		this.bike.destroy();
	}

}

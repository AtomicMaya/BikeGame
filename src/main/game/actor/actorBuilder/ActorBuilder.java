package main.game.actor.actorBuilder;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.math.Vector;
import main.window.Canvas;

public abstract class ActorBuilder {

	private ActorGame game;

	public ActorBuilder(ActorGame game) {
		this.game = game;
	}

	public abstract void update(float deltaTime);

	public abstract void draw(Canvas canvas);

	public Vector getMousePosition() {
		return game.getMouse().getPosition();
	}

	public boolean isLeftPressed() {
		return game.getMouse().getLeftButton().isPressed();
	}

	public boolean isRightPressed() {
		return game.getMouse().getRightButton().isPressed();
	}

	public abstract Actor getActor();

	public abstract boolean isDone();


}

package main.game.actor.actorBuilder;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.Graphics;
import main.game.actor.QuickMafs;
import main.game.actor.menu.Menu;
import main.math.Vector;
import main.window.Button;
import main.window.Mouse;

/** Used to create an {@linkplain Actor} in the game */
public abstract class ActorBuilder implements Graphics{

	private ActorGame game;

	/**
	 * Create an {@linkplain ActorBuilder} to add an {@linkplain Actor} to the game
	 */
	public ActorBuilder(ActorGame game) {
		this.game = game;
	}

	/**
	 * Simulates a single time step.
	 * 
	 * @param deltaTime :elapsed time since last update, in seconds, non-negative
	 */
	public abstract void update(float deltaTime);

	/** @return the {@linkplain Mouse} position, floored to the closest {@linkplain Integer} */
	public Vector getFlooredMousePosition() {
		return QuickMafs.floor(game.getMouse().getPosition());
	}

	/** @return the {@linkplain Actor} created by the {@linkplain ActorBuilder} */
	public abstract Actor getActor();

	/** @return whether this {@linkplain ActorBuilder} finished building its {@linkplain Actor}*/
	public abstract boolean isDone();

	/** Method called to recreate what's needed after we test the game */
	public abstract void reCreate();

	/** @return whether this {@linkplain ActorBuilder} is hovered by the {@linkplain Mouse}*/
	public abstract boolean isHovered();

	/** Destroy this {@linkplain Menu} and all its associated stuff */
	public abstract void destroy();

	/** @return the {@linkplain Mouse} position */
	public Vector getMousePosition() {
		return game.getMouse().getPosition();
	}

	/** @return whether the left {@linkplain Button} of the {@linkplain Mouse} is pressed */
	public boolean isLeftPressed() {
		return game.getMouse().getLeftButton().isPressed();
	}

	/** @return whether the right {@linkplain Button} of the {@linkplain Mouse} is pressed */
	public boolean isRightPressed() {
		return game.getMouse().getRightButton().isPressed();
	}

	/** @return the {@linkplain ActorGame} */
	public ActorGame getOwner() {
		return game;
	}

}

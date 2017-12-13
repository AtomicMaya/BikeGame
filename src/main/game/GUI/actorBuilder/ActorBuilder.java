package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.GUIComponent;
import main.game.actor.Actor;
import main.math.Vector;

/** Used to create an {@linkplain Actor} in the game. */
public abstract class ActorBuilder extends GUIComponent {


	/**
	 * Create an {@linkplain ActorBuilder} to add an {@linkplain Actor} to the game.
	 * @param game The master {@linkplain ActorGame}.
	 */
	public ActorBuilder(ActorGame game) {
		super(game, Vector.ZERO);
	}

	@Override
	public void update(float deltaTime, float zoom) {
		if (isHovered() && isRightPressed())
			this.edit();
	}

	/**
	 * @return the {@linkplain Actor} created by the {@linkplain ActorBuilder}.
	 */
	public abstract Actor getActor();

	/**
	 * @return whether this {@linkplain ActorBuilder} finished building its
	 * {@linkplain Actor}
	 */
	public abstract boolean isDone();
	

	/**
	 * Method called to recreate what's needed after we test the game, or more
	 * generally to recreate the {@linkplain Actor}
	 */
	public abstract void reCreate();

	/** Start editing this {@linkplain ActorBuilder}. */
	public abstract void edit();

	/**
	 * @return whether this {@linkplain ActorBuilder} is hovered, mostly used to
	 * edit it
	 * @see #edit()
	 */
	@Override
	public abstract boolean isHovered();

}

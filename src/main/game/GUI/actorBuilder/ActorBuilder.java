package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.GUIComponent;
import main.game.actor.Actor;
import main.math.Vector;

/** Used to create an {@linkplain Actor} in the game */
public abstract class ActorBuilder extends GUIComponent {

	/** Create an {@linkplain ActorBuilder} to add an {@linkplain Actor} to the game */
	public ActorBuilder(ActorGame game) {
		super(game, Vector.ZERO);
	}

	/** @return the {@linkplain Actor} created by the {@linkplain ActorBuilder} */
	public abstract Actor getActor();

	/** @return whether this {@linkplain ActorBuilder} finished building its {@linkplain Actor}*/
	public abstract boolean isDone();

	/** Method called to recreate what's needed after we test the game */
	public abstract void reCreate();

	/** @return the {@linkplain ActorGame} where this {@linkplain ActorBuilder} belong */
	@Override
	public ActorGame getOwner() {
		return super.getOwner();
	}

}

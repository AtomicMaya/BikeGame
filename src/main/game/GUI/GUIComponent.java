package main.game.GUI;

import main.game.ActorGame;
import main.math.Attachable;
import main.math.Node;
import main.math.Transform;
import main.math.Vector;

public abstract class GUIComponent extends Node implements GUI, Attachable {

	private ActorGame game;
	public GUIComponent(ActorGame game, Vector position) {
		this.game = game;
		setRelativeTransform(Transform.I.translated(position));
	}
	/**
	 * @return the zero {@linkplain Vector}, because a positionable has no sense
	 * to move
	 */
	public Vector getVelocity() {
		return Vector.ZERO;
	}
	
	/** @return the {@linkplain ActorGame} */
	public ActorGame getOwner() {
		return game;
	}

}

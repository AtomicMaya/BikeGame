package main.game.GUI;

import main.game.ActorGame;
import main.math.Attachable;
import main.math.Node;
import main.math.Transform;
import main.math.Vector;

public abstract class GUIComponent extends Node implements GUI, Attachable {

	private ActorGame game;
	private Vector anchor = Vector.ZERO;
	public GUIComponent(ActorGame game, Vector anchor) {
		this.game = game;
		this.anchor = anchor;
		setRelativeTransform(Transform.I.translated(anchor));
	}

	@Override
	public void update(float deltaTime, float zoom) {
		setRelativeTransform(Transform.I.translated(anchor).scaled(zoom));
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

	/**
	 * Set the relative position to the parent
	 * @param anchor relative position
	 */
	public void setAnchor(Vector anchor) {
		this.anchor = anchor;
	}

}

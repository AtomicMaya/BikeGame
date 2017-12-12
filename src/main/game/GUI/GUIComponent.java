package main.game.GUI;

import main.game.ActorGame;
import main.math.Node;
import main.math.Transform;
import main.math.Vector;

/** Concrete implementation of GUI */
public abstract class GUIComponent extends Node implements GUI {

	/** The master {@linkplain ActorGame} */
	private ActorGame game;

	/** Default anchor of this {@linkplain GUIComponent}. */
	private Vector anchor = Vector.ZERO;

	/** Current zoom */
	private float zoom = 1;

	/**
	 * Create a new CUIComponent,
	 * @param game Master {@linkplain ActorGame}
	 * @param anchor relative anchor of this {@linkplain GUIComponent}, default
	 * if null
	 * @see NumberField
	 * @see GraphicalButton
	 * @see Comment
	 */
	public GUIComponent(ActorGame game, Vector anchor) {
		this.game = game;
		if (anchor != null)
			this.anchor = anchor;
		else this.anchor = Vector.ZERO;
		setRelativeTransform(Transform.I.translated(this.anchor));
	}

	@Override
	public void update(float deltaTime, float zoom) {
		this.zoom = zoom;
		if (getParent() == null)
			setRelativeTransform(
					Transform.I.translated(anchor).scaled(zoom).translated(getOwner().getCanvas().getPosition()));
		else {
			setRelativeTransform(Transform.I.translated(anchor));
		}
	}

	/**
	 * @return the zero {@linkplain Vector}, because a {@linkplain GUIComponent}
	 * has no sense to move
	 */
	public final Vector getVelocity() {
		return Vector.ZERO;
	}

	/** @return the {@linkplain ActorGame} */
	public final ActorGame getOwner() {
		return game;
	}

	/**
	 * Set the relative position to the parent
	 * @param anchor relative position, before rescaling with zoom
	 */
	public void setAnchor(Vector anchor) {
		this.anchor = anchor;
	}

	/** @return the relative position */
	public Vector getAnchor() {
		return this.anchor;
	}

	/** @return the current zoom */
	public final float getZoom() {
		return zoom;
	}
}

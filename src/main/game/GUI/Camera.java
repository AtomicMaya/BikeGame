/**
 *	Author: Clément Jeannet
 *	Date: 	8 déc. 2017
 */
package main.game.GUI;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.entities.GameEntity;
import main.math.Node;
import main.math.Positionable;
import main.math.Transform;
import main.math.Vector;
import main.window.Window;

public class Camera extends Node implements Actor {

	// Viewport properties
	private Vector viewCenter;
	private Vector viewTarget;
	private Positionable viewCandidate;
	private static final float VIEW_TARGET_VELOCITY_COMPENSATION = 0.2f;

	private static final float VIEW_INTERPOLATION_RATIO_PER_SECOND = 0.1f;
	private static final float VIEW_SCALE = 20.0f;
	private static float VIEW_SCALE_MOD = 0.0f;
	private static float VIEW_SCALE_CURRENT = VIEW_SCALE;
	private static float VIEW_SCALE_PREVIOUS = VIEW_SCALE;
	private static final float TRANSLATION_TIME = 3f;

	private ActorGame game;
	private Window window;

	public Camera(ActorGame game, Window window) {
		this.game = game;
		this.window = window;
		this.setParent(window);
		this.viewCenter = Vector.ZERO;
		this.viewTarget = Vector.ZERO;
	}

	@Override
	public void update(float deltaTime) {
		if (VIEW_SCALE_CURRENT > VIEW_SCALE + VIEW_SCALE_MOD
				&& !(VIEW_SCALE + VIEW_SCALE_MOD - 0.1f < VIEW_SCALE_CURRENT
						&& VIEW_SCALE_CURRENT < VIEW_SCALE + VIEW_SCALE_MOD + 0.1f))
			VIEW_SCALE_CURRENT -= VIEW_SCALE_MOD * deltaTime / TRANSLATION_TIME;
		else if (VIEW_SCALE_CURRENT < VIEW_SCALE + VIEW_SCALE_MOD
				&& !(VIEW_SCALE + VIEW_SCALE_MOD - 0.1f < VIEW_SCALE_CURRENT
						&& VIEW_SCALE_CURRENT < VIEW_SCALE + VIEW_SCALE_MOD + 0.1f))
			VIEW_SCALE_CURRENT += VIEW_SCALE_MOD * deltaTime / TRANSLATION_TIME;

		// Update expected viewport center
		if (this.viewCandidate != null) {
			this.viewTarget = this.viewCandidate.getPosition()
					.add(this.viewCandidate.getVelocity().mul(VIEW_TARGET_VELOCITY_COMPENSATION));
		}
		// Interpolate with previous location
		float ratio = (float) Math.pow(VIEW_INTERPOLATION_RATIO_PER_SECOND, deltaTime);
		this.viewCenter = this.viewCenter.mixed(this.viewTarget, 1f - ratio);
		// Compute new viewport
		Transform viewTransform = Transform.I.scaled(VIEW_SCALE_CURRENT).translated(this.viewCenter);
		this.window.setRelativeTransform(viewTransform);

		VIEW_SCALE_PREVIOUS = VIEW_SCALE_CURRENT;
	}

	/**
	 * Sets a modifier to the view scale for smooth transition.
	 * @param newModifier : The modifier.
	 */
	public void setViewScaleModifier(float newModifier) {
		VIEW_SCALE_MOD = newModifier;
	}

	/**
	 * @return the current view scale.
	 */
	public float getViewScale() {
		return VIEW_SCALE_CURRENT;
	}

	/**
	 * Directly set a new view scale, with no transition.
	 * @param newViewScale : the new value.
	 */
	public void setViewScale(float newViewScale) {
		VIEW_SCALE_CURRENT = newViewScale;
		VIEW_SCALE_MOD = (VIEW_SCALE_CURRENT > VIEW_SCALE + VIEW_SCALE_MOD ? VIEW_SCALE_CURRENT - VIEW_SCALE
				: VIEW_SCALE_CURRENT - VIEW_SCALE);
	}

	/** @return the camera position */
	public Vector getCameraPosition() {
		return this.viewCenter;
	}

	public void setCameraPosition(Vector position) {
		this.viewCenter = position;
	}

	/**
	 * Sets the game's view candidate, to be followed by the camera.
	 * @param positionable : the {@linkplain GameEntity} / {@linkplain Actor} to
	 * be followed.
	 */
	public void setViewCandidate(Positionable positionable) {
		this.viewCandidate = positionable;
	}

	public Positionable getViewCandidate() {

		return viewCandidate;
	}
}

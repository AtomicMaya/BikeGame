/**
 *	Author: Clément Jeannet
 *	Date: 	8 déc. 2017
 */
package main.game.GUI;

import main.game.ActorGame;
import main.game.graphics.Graphics;
import main.math.Attachable;
import main.math.ExtendedMath;
import main.math.Vector;
import main.window.Button;
import main.window.Mouse;

/** Regroup all methods used to interact with the user */
public interface GUI extends Graphics, Attachable {

	/** @return whether {@link this} is hovered by the {@linkplain Mouse} */
	public boolean isHovered();

	/** Destroy this {@linkplain GUI} and all its associated stuff */
	public void destroy();

	/** @return the {@linkplain Mouse} position */
	public default Vector getMousePosition() {
		return getOwner().getMouse().getPosition();
	}
	
	/**
	 * @return the {@linkplain Mouse} position, (x and y) floored to the closest .5 {@linkplain Float}
	 */
	public default Vector getHalfFlooredMousePosition() {
		return ExtendedMath.halfFloor(getMousePosition());
	}

	/**
	 * @return the {@linkplain Mouse} position, (x and y) floored to the closest
	 * {@linkplain Integer}
	 */
	public default Vector getFlooredMousePosition() {
		return ExtendedMath.floor(getMousePosition());
	}
	
	/**
	 * @return whether the left {@linkplain Button} of the {@linkplain Mouse} is
	 * pressed
	 */
	public default boolean isLeftPressed() {
		return getOwner().getMouse().getLeftButton().isPressed();
	}

	/**
	 * @return whether the right {@linkplain Button} of the {@linkplain Mouse}
	 * is pressed
	 */
	public default boolean isRightPressed() {
		return getOwner().getMouse().getRightButton().isPressed();
	}

	/** @return the {@linkplain ActorGame} where this {@linkplain GUI} belong */
	public ActorGame getOwner();

	/**
	 * Simulates a single time step.
	 *
	 * @param deltaTime :elapsed time since last update, in seconds,
	 * non-negative
	 * @param zoom current zoom of the game, put 1 for default
	 */
	public void update(float deltaTime, float zoom);
}

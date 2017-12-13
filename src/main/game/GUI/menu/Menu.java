/**
 *	Author: Clément Jeannet
 *	Date: 	8 déc. 2017
 */
package main.game.GUI.menu;

import main.game.ActorGame;
import main.game.GUI.GUIComponent;
import main.math.Vector;
import main.window.Mouse;

/** Represent a {@linkplain {@linkplain main.game.GUI.GUI}} of type {@linkplain Menu} */
public abstract class Menu extends GUIComponent {

	/** Whether this {@linkplain Menu} is open */
	private boolean open = false;

	/** Create a new {@linkplain Menu}
	 * @param game The master {@linkplain ActorGame}
	 * @param open Whether this {@linkplain Menu} is open when created
	 * @param anchor Absolute anchor of this {@linkplain Menu on screen}
	 * */
	public Menu(ActorGame game, Vector anchor, boolean open) {
		super(game, anchor);
		this.open = open;
	}

	@Override
	public void destroy() {
		// default destroy of a menu, does not do anything
	}

	/** @return whether this {@linkplain FullScreenMenu} is open */
	public boolean isOpen() {
		return open;
	}

	/** Invert the status of this {@linkplain FullScreenMenu} */
	public void changeStatus() {
		open = !open;
	}

	/** Set the status of this {@linkplain FullScreenMenu}.
     * @param isOpen The new status.
     */
	public void setStatus(boolean isOpen) {
		open = isOpen;
	}

	/**
	 * @return whether this {@linkplain FullScreenMenu} is hovered by the
	 * {@linkplain Mouse}, default return false
	 */
	@Override
	public boolean isHovered() {
		return false;
	}
}

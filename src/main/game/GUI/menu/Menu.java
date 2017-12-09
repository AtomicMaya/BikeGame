/**
 *	Author: Clément Jeannet
 *	Date: 	8 déc. 2017
 */
package main.game.GUI.menu;

import main.game.ActorGame;
import main.game.GUI.GUIComponent;
import main.math.Vector;
import main.window.Mouse;

public abstract class Menu extends GUIComponent {

	private boolean open = false;

	public Menu(ActorGame game, Vector anchor, boolean open) {
		super(game, anchor);
		this.open = open;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	/** @return whether this {@linkplain FullScreenMenu} is open */
	public boolean isOpen() {
		return open;
	}

	/** Invert the status of this {@linkplain FullScreenMenu} */
	public void changeStatus() {
		open = !open;
	}

	/** Set the status of this {@linkplain FullScreenMenu} */
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

package main.game.actor;

import main.window.Canvas;

/**
 * Represents a drawable element.
 */
public interface Graphics { // j'ai rajouté le extend pour la methode setParent, de toute facon tout
												// les Objets implementant Graphics implementent aussi Attachable

	/**
	 * Renders itself on specified canvas.
	 * 
	 * @param canvas
	 *            target, not null
	 */
	public void draw(Canvas canvas);

}

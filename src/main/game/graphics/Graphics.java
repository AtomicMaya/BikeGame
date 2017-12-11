package main.game.graphics;

import main.window.Canvas;

/**
 * Represents a drawable element.
 */
public interface Graphics {
	/**
	 * Renders itself on specified canvas.
	 * @param canvas The target, not null.
	 */
	void draw(Canvas canvas);

}

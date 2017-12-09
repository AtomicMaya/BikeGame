/**
 *	Author: Clément Jeannet
 *	Date: 	3 déc. 2017
 */
package main.game.GUI.menu;

import main.game.ActorGame;
import main.game.graphics.ShapeGraphics;
import main.math.Polygon;
import main.math.Shape;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;
import main.window.Window;

import java.awt.*;

public abstract class FullScreenMenu extends Menu {

	private boolean open;

	private ShapeGraphics background;

	private float scale = 30;
	private Window window;

	private Shape shape;
	private Color backgroundColor;

	/**
	 * Create a new Menu
	 * 
	 * @param window window where to draw this menu
	 * @param isOpen weather this menu is open or close when created
	 * @param backgroundColor Background color of this menu
	 * @param fullScreen whether this menu occupy all the screen
	 */
	public FullScreenMenu(ActorGame game, Window window, boolean isOpen, Color backgroundColor) {
		super(game, Vector.ZERO, isOpen);
		this.open = isOpen;
		this.window = window;
		this.backgroundColor = backgroundColor;

		shape = new Polygon(-scale * 2, -scale * 2, -scale * 2, scale * 2, scale * 2, scale * 2, scale * 2, -scale * 2);
		background = new ShapeGraphics(shape, backgroundColor, null, 0, 1, -10);

		game.setGameFreezeStatus(isOpen());
	}

	@Override
	public void update(float deltaTime, float zoom) {
		super.update(deltaTime, zoom);
		if (open) {

			window.setRelativeTransform(Transform.I.scaled(scale * zoom));
			shape = new Polygon(-scale * 2 * zoom, -scale * 2 * zoom, -scale * 2 * zoom, scale * 2 * zoom,
					scale * 2 * zoom, scale * 2 * zoom, scale * 2 * zoom, -scale * 2 * zoom);
			background = new ShapeGraphics(shape, backgroundColor, null, 0, 1, -10);

		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (open)
			if (background != null)
				background.draw(canvas);
	}
}

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

	private ShapeGraphics background;

	private float scale = 30;
	private Window window;

	private Shape shape;
	private Color backgroundColor;

	/**
	 * Create a new Menu
	 * @param game : The game.
	 * @param window : The window where to draw this menu.
	 * @param isOpen : Whether this menu is open or close when created.
	 * @param backgroundColor : The background color of this menu.
	 */
	public FullScreenMenu(ActorGame game, Window window, boolean isOpen, Color backgroundColor) {
		super(game, Vector.ZERO, isOpen);
		this.window = window;
		this.backgroundColor = backgroundColor;

        this.shape = new Polygon(-this.scale * 2, -this.scale * 2, -this.scale * 2, this.scale * 2,
                this.scale * 2, this.scale * 2, this.scale * 2, -this.scale * 2);
        this.background = new ShapeGraphics(this.shape, backgroundColor, null, 0, 1, -10);

		game.setGameFreezeStatus(isOpen());
	}

	@Override
	public void update(float deltaTime, float zoom) {
		super.update(deltaTime, zoom);
		
		if (isOpen()) {
            this.window.setRelativeTransform(Transform.I.scaled(this.scale * zoom));
            this.shape = new Polygon(-this.scale * 2 * zoom, -this.scale * 2 * zoom, -this.scale * 2 * zoom, this.scale * 2 * zoom,
                    this.scale * 2 * zoom, this.scale * 2 * zoom, this.scale * 2 * zoom, -this.scale * 2 * zoom);
            this.background = new ShapeGraphics(this.shape, this.backgroundColor, null, 0, 1, -10);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (isOpen())
			if (this.background != null)
                this.background.draw(canvas);
	}
}

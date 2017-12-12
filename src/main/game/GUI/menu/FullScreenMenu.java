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

/** Represent a {@linkplain Menu} which occupy the entire of the screen */
public abstract class FullScreenMenu extends Menu {

	/** Used to have a uniform backgroud behinnd this {@linkplain Menu} */
	private ShapeGraphics background;

	/** Default view scale of a {@linkplain FullScreenMenu} */
	private float scale = 30;
	
	/** Context frame in which this {@linkplain Menu} is draw. */
	private Window window;

	/** {@linkplain Shape} of this {@linkplain Menu}, for drawing purposes */
	private Shape shape;
	
	/** {@linkplain Color} of the {@linkplain ShapeGraphics} {@link #background} */
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

/**
 *	Author: Clément Jeannet
 *	Date: 	3 déc. 2017
 */
package main.game.actor.menu;

import java.awt.Color;

import main.game.ActorGame;
import main.game.actor.Graphics;
import main.game.actor.ShapeGraphics;
import main.math.Polygon;
import main.math.Shape;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;
import main.window.Mouse;
import main.window.Window;
import main.window.Button;

public abstract class Menu implements Graphics {

	private boolean open;

	private ShapeGraphics background;
	private Window window;

	private float zoom = 30;
	private Mouse mouse;
	private ActorGame game;

	private Shape shape;

	/**
	 * Create a new Menu
	 * 
	 * @param window window where to draw this menu
	 * @param isOpen weather this menu is open or close when created
	 * @param backgroundColor Background color of this menu
	 * @param fullScreen whether this menu occupy all the screen
	 */
	public Menu(ActorGame game, Window window, boolean isOpen, Color backgroundColor, boolean fullScreen) {
		this.open = isOpen;
		this.game = game;
		this.window = window;
		this.mouse = game.getMouse();
		if (fullScreen) {
			shape = new Polygon(-zoom * 2, -zoom * 2, -zoom * 2, zoom * 2, zoom * 2, zoom * 2, zoom * 2, -zoom * 2);
			background = new ShapeGraphics(shape, backgroundColor, null, 0, 1, -10);
		}
		game.setGameFreezeStatus(isOpen());
	}

	public void update(float detlaTime) {
		if (open) {
			window.setRelativeTransform(Transform.I.scaled(zoom));

		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (open)
			if (background != null)
				background.draw(canvas);
	}

	/** @return whether this {@linkplain Menu} is open */
	public boolean isOpen() {
		return open;
	}

	/** Invert the status of this {@linkplain Menu} */
	public void changeStatus() {
		open = !open;
	}

	/** Set the status of this {@linkplain Menu} */
	public void setStatus(boolean isOpen) {
		open = isOpen;
	}

	/** Destroy this {@linkplain Menu} and all its associated stuff */
	public abstract void destroy();

	/** @return the {@linkplain Mouse} position */
	public Vector getMousePosition() {
		return mouse.getPosition();
	}

	/** @return whether the left {@linkplain Button} of the {@linkplain Mouse} is pressed */
	public boolean isLeftPressed() {
		return mouse.getLeftButton().isPressed();
	}

	/** @return whether the right {@linkplain Button} of the {@linkplain Mouse} is pressed */
	public boolean isRightPressed() {
		return mouse.getRightButton().isPressed();
	}

	/** @return the {@linkplain ActorGame} */
	public ActorGame getOwner() {
		return game;
	}
}

/**
 *	Author: Clément Jeannet
 *	Date: 	3 déc. 2017
 */
package main.game.GUI.menu;

import java.awt.Color;

import main.game.ActorGame;
import main.game.GUI.GUI;
import main.game.graphics.ShapeGraphics;
import main.math.Polygon;
import main.math.Shape;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;
import main.window.Mouse;
import main.window.Window;

public abstract class Menu implements GUI {

	private boolean open;

	private ShapeGraphics background;

	private float scale = 30;
	private ActorGame game;
	private Window window;

	private Shape shape;
	private Color backgroundColor;

	private boolean fullScreen;

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
		this.backgroundColor = backgroundColor;
		this.fullScreen = fullScreen;
		if (fullScreen) {
			shape = new Polygon(-scale * 2, -scale * 2, -scale * 2, scale * 2, scale * 2, scale * 2, scale * 2,
					-scale * 2);
			background = new ShapeGraphics(shape, backgroundColor, null, 0, 1, -10);
		}
		game.setGameFreezeStatus(isOpen());
	}

	public void update(float detlaTime, float zoom) {
		if (open) {
			window.setRelativeTransform(Transform.I.scaled(scale * zoom));
			if (fullScreen) {
				shape = new Polygon(-scale * 2 * zoom, -scale * 2 * zoom, -scale * 2 * zoom, scale * 2 * zoom,
						scale * 2 * zoom, scale * 2 * zoom, scale * 2 * zoom, -scale * 2 * zoom);
				background = new ShapeGraphics(shape, backgroundColor, null, 0, 1, -10);
			}
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

	/**
	 * @return whether this {@linkplain Menu} is hovered by the
	 * {@linkplain Mouse}, default return false
	 */
	@Override
	public boolean isHovered() {
		return false;
	}

	/** @return the {@linkplain ActorGame} */
	public ActorGame getOwner() {
		return game;
	}

	@Override
	public Transform getTransform() {
		// TODO Auto-generated method stub
		return Transform.I;
	}

	@Override
	public Vector getVelocity() {
		// TODO Auto-generated method stub
		return Vector.ZERO;
	}

}

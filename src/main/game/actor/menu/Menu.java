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
import main.math.Transform;
import main.window.Canvas;
import main.window.Window;

public abstract class Menu implements Graphics {

	private boolean open;

	private ShapeGraphics background;
	private Window window;

	private ActorGame game;

	/**
	 * Create a new Menu
	 * 
	 * @param window window where to draw this menu
	 * @param isOpen weather this menu is open or close when created
	 */
	public Menu(ActorGame game, Window window, boolean isOpen, Color backgroundColor) {
		this.open = isOpen;
		this.window = window;
		this.game = game;
		background = new ShapeGraphics(new Polygon(-10, -10, -10, 10, 10, 10, 10, -10), backgroundColor, null, 0, 1,
				-10);
		game.setGameFreezeStatus(isOpen());
	}

	public void update(float detlaTime) {
		if (open)
			window.setRelativeTransform(Transform.I.scaled(10));
	}

	@Override
	public void draw(Canvas canvas) {
		if (open)
			background.draw(canvas);
	}

	public boolean isOpen() {
		return open;
	}

	public void changeStatut() {
		open = !open;
		game.setGameFreezeStatus(isOpen());
	}
	
	public void setStatut(boolean isOpen) {
		open = isOpen;
		game.setGameFreezeStatus(isOpen());
	}

}

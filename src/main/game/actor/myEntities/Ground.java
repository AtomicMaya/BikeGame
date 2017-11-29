/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.myEntities;

import main.game.actor.ActorGame;
import main.game.actor.ShapeGraphics;
import main.math.Polyline;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Ground extends GameEntity {
	// keep reference to the graphics
	private ShapeGraphics g;

	/**
	 * Create a Ground
	 * @param game : ActorGame where the ground exists
	 * @param position : the position of the ground
	 * @param p : a polyline shape of the ground
	 */
	public Ground(ActorGame game, Vector position, Polyline p) {
		super(game, true, (position == null) ? Vector.ZERO : position);
		build(getEntity(), p, .6f, -1, false);
		g = addGraphics(getEntity(), p, null, Color.black, .1f, 1, 0);
	}

	@Override
	public void draw(Canvas window) {
		g.draw(window);
		if (super.getOwner().getKeyboard().get(KeyEvent.VK_G).isPressed()) {
			destroy();
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}

}

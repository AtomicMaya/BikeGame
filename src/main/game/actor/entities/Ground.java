package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ShapeGraphics;
import main.math.Polyline;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Ground extends GameEntity {
	// keep reference to the graphics
	private ShapeGraphics graphics;

	/**
	 * Create a Ground
	 * @param game : ActorGame where the ground exists
	 * @param position : the position of the ground
	 * @param shape : a polyline shape of the ground
	 */
	public Ground(ActorGame game, Vector position, Polyline shape) {
		super(game, true, (position == null) ? Vector.ZERO : position);
		this.build(shape, 2f, -1, false);
		graphics = this.addGraphics(shape, null, Color.black, .1f, 1, 0);
	}

	@Override
	public void draw(Canvas window) {
		graphics.draw(window);
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

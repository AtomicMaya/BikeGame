package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ShapeGraphics;
import main.math.Polyline;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class Ground extends GameEntity {
	// keep reference to the graphics
	private transient ShapeGraphics graphics;

	private List<Vector> points;

	/**
	 * Create a Ground
	 * 
	 * @param game : ActorGame where the ground exists
	 * @param position : the position of the ground
	 * @param shape : a polyline shape of the ground
	 */
	public Ground(ActorGame game, Vector position, Polyline shape) {
		super(game, true, (position == null) ? Vector.ZERO : position);
		this.points = shape.getPoints();
		construct();
	}
	
	private void construct() {
		Polyline p = new Polyline(points);
		this.build(p, 2f, -1, false);
		graphics = this.addGraphics(p, Color.decode("#6D5D49"), Color.decode("#548540"), .2f, 1, 0);
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
	
	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
		construct();
	}
}

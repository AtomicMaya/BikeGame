package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ShapeGraphics;
import main.math.Polyline;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.util.List;

public class Ground extends GameEntity {
	private static final long serialVersionUID = 7454750386019416210L;

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
		this.create();
	}

	/**
	 * Actual creation of the parameters of the GameEntity, not in the constructor
	 * to avoid duplication with the method reCreate
	 */
	private void create() {
		Polyline p = new Polyline(points);
		this.build(p, 2f, -1, false, ObjectGroup.TERRAIN.group);
		graphics = this.addGraphics(p, Color.decode("#6D5D49"), Color.decode("#548540"), .2f, 1, 10);
	}

	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
		this.create();
	}

	@Override
	public void draw(Canvas window) {
		graphics.draw(window);
	}

	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}

}

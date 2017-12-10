package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.graphics.ShapeGraphics;
import main.math.Polyline;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.util.List;

public class Terrain extends GameEntity {
	private static final long serialVersionUID = 7454750386019416210L;

	// keep reference to the graphics
	private transient ShapeGraphics graphics;
	private int type;

	private List<Vector> points;

	/**
	 * Create a Terrain
	 * 
	 * @param game : ActorGame where the ground exists
	 * @param position : the position of the ground
	 * @param shape : a polyline shape of the ground
     * @param type : whether it's normal ground (0), ice (1), mud (2)
	 */
	public Terrain(ActorGame game, Vector position, Polyline shape, int type) {
		super(game, true, (position == null) ? Vector.ZERO : position);
		this.points = shape.getPoints();
		this.type = type;
		this.create();
	}

	/**
	 * Actual creation of the parameters of the GameEntity, not in the constructor
	 * to avoid duplication with the method reCreate
	 */
	private void create() {
		Polyline points = new Polyline(this.points);
		float friction;
		if (this.type == 0) friction = 2;
		else if (this.type == 1) friction = 0;
		else if (this.type == 2) friction = 0.7f;
		else friction = 2;

		this.build(points, friction, -1, false, ObjectGroup.TERRAIN.group);
		String color1, color2;
        if (this.type == 0) { color1 =  "#6D5D49"; color2 = "#548542"; }
        else if (this.type == 1) { color1 = "#a2d2df"; color2 = "#baf2ef"; }
        else if (this.type == 2) { color1 = "#92817c"; color2 = "#a99790"; }
        else { color1 =  "#6D5D49"; color2 = "#548542"; }
        this.graphics = this.addGraphics(points, Color.decode(color1), Color.decode(color2), .2f, 1, 10);
	}

	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
		this.create();
	}

	@Override
	public void draw(Canvas window) {
		this.graphics.draw(window);
	}

	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}

}

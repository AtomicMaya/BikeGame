package main.game.actor.entities;

import java.awt.Color;
import java.util.List;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.math.Polygon;
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;

/** Create a new {@linkplain Obstacle} */
public class Obstacle extends GameEntity {

	/** Used for save purposes */
	private static final long serialVersionUID = 7493706529152384948L;

	/** List of all the {@linkplain Vector} points of this {@linkplain Obstacle}. */
	private List<Vector> points;

	/**
	 * Create a new fixed {@linkplain Obstacle}
	 * @param position Position of this {@linkplain Obstacle}
	 * @param shape {@linkplain Shape} of this {@linkplain Obstacle}
	 */
	public Obstacle(ActorGame game, Vector position, Polygon shape) {
		super(game, true, position);
		points = shape.getPoints();
		create();
	}

    /**
     * Actual creation of the parameters of the {@linkplain GameEntity}, not in the
     * constructor to avoid duplication with the method {@linkplain #reCreate(ActorGame)}
     */
	private void create() {
		build(new Polygon(points), -1, -1, false, ObjectGroup.OBSTACLE.group);
	}
	
	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
		create();
	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.drawShape(new Polygon(points), getTransform(), new Color(196,196,188), Color.BLACK, .05f, 1, 2);
	}

}

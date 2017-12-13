package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.graphics.ShapeGraphics;
import main.math.Polygon;
import main.math.Polyline;
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.util.List;


public class Terrain extends GameEntity {
    /** Used for save purposes. */
	private static final long serialVersionUID = 7454750386019416210L;

    /** Creates a new {@linkplain ShapeGraphics} associated to this {@linkplain Terrain}. */
	private transient ShapeGraphics graphics;

    /** This {@linkplain TerrainType} of this {@linkplain Terrain}. */
	private TerrainType type;

	/** Whether this {@linkplain Terrain} can be represented by a {@linkplain Polyline}*/
	private boolean isPolyline;

	/** A {@linkplain List} of {@linkplain Vector}'s used to draw the {@linkplain Shape}. */
	private List<Vector> points;

	/**
	 * Create a {@linkplain Terrain}.
	 * @param game The {@linkplain ActorGame} where the {@linkplain Terrain} exists.
	 * @param position The position {@linkplain Vector} of the {@linkplain Terrain}.
	 * @param shape : The {@linkplain Shape} of the {@linkplain Terrain}.
     * @param type : The {@linkplain TerrainType} of {@linkplain Terrain}.
	 */
	public Terrain(ActorGame game, Vector position, Polyline shape, TerrainType type) {
		super(game, true, (position == null) ? Vector.ZERO : position);
		this.points = shape.getPoints();
        this.type = type;
        this.isPolyline = true;
		this.create();
	}

    /**
     * Create a {@linkplain Terrain}.
     * @param game The {@linkplain ActorGame} where the {@linkplain Terrain} exists.
     * @param position The position {@linkplain Vector} of the {@linkplain Terrain}.
     * @param shape : The {@linkplain Shape} of the {@linkplain Terrain}.
     * @param type : The {@linkplain TerrainType} of {@linkplain Terrain}.
     */
	public Terrain(ActorGame game, Vector position, Polygon shape, TerrainType type) {
        super(game, true, (position == null) ? Vector.ZERO : position);
        this.points = shape.getPoints();
        this.isPolyline = false;
        this.type = type;
        this.create();
    }

	/**
	 * Actual creation of the parameters of the {@linkplain GameEntity}, not in the constructor to avoid duplication
     * with the method {@linkplain #reCreate(ActorGame)}.
	 */
	private void create() {
        Shape shape = this.isPolyline ? new Polyline(this.points) : new Polygon(this.points);
        float friction = this.type.friction;
		this.build(shape, friction, -1, false, ObjectGroup.TERRAIN.group);
        this.graphics = this.addGraphics(shape, Color.decode(this.type.fillColor), Color.decode(this.type.outlineColor),
                .2f, 1, 1);
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

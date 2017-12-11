package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.graphics.ImageGraphics;
import main.game.graphics.ShapeGraphics;
import main.io.Saveable;
import main.math.*;
import main.math.Shape;

import java.awt.*;

/** Represent an {@linkplain Actor} in its physical sense */
public abstract class GameEntity implements Actor, Saveable {
	private static final long serialVersionUID = 8519429675636563656L;

	private transient Entity entity;
	private transient ActorGame actorGame;

	private Vector position;
	private boolean fixed;

	/**
	 * Create a new {@linkplain GameEntity}, and its associated
	 * {@linkplain Entity}.
	 * @param game : The {@linkplain ActorGame} where this {@linkplain Entity}
	 * inhabits.
	 * @param fixed : Whether the {@linkplain Entity} is fixed.
	 * @param position : The position of the {@linkplain Entity}.
	 */
	public GameEntity(ActorGame game, boolean fixed, Vector position) {
		if (game == null)
			throw new NullPointerException("Game is null");
		if (position == null)
			throw new NullPointerException("Vector is null");

		this.position = position;
		this.fixed = fixed;

		this.actorGame = game;
		create();
	}

    /** @see #GameEntity(ActorGame, boolean, Vector) */
    public GameEntity(ActorGame game, boolean fixed) {
        this(game, fixed, Vector.ZERO);
    }

	/**
	 * Actual creation of the parameters of the {@linkplain GameEntity}, not in
	 * the constructor to avoid duplication with the method reCreate
	 */
	private void create() {
		if (this.entity == null)
			this.entity = this.actorGame.newEntity(this.position, this.fixed);
	}

	@Override
	public void reCreate(ActorGame game) {
        this.actorGame = game;
		create();
	}

	@Override
	public void update(float deltaTime) {
		if (!this.entity.isAlive())
			this.destroy();
	}

	/**
	 * Get the {@linkplain Entity} associated with this {@linkplain GameEntity}
	 * @return the {@linkplain Entity}.
	 */
	protected Entity getEntity() {
		return this.entity;
	}

	/**
	 * Get the ActorGame associated with this GameEntity.
	 * @return the {@linkplain ActorGame}.
	 */
	protected ActorGame getOwner() {
		return this.actorGame;
	}

	/**
	 * Destroy the {@linkplain Entity} associated with this
	 * {@linkplain GameEntity}.
	 */
	@Override
	public void destroy() {
		this.entity.destroy();
	}

	@Override
	public Transform getTransform() {
		return this.entity.getTransform();
	}

	@Override
	public Vector getVelocity() {
		return this.entity.getVelocity();
	}


	/**
     * Create and add a {@linkplain ImageGraphics} to this {@linkplain Entity}
	 * @param imagePath : the path to the image to add
	 * @param width : the width of the image
	 * @param height : the height of the image
	 * @param anchor : a {@linkplain Vector} which give an anchor to the
	 * {@linkplain ImageGraphics}, relative to its parent
	 * @param alpha : the transparency, between 0 (invisible) and 1 (opaque)
	 * @param depth : the render priority, lower-values drawn first
	 * @return a new {@linkplain ImageGraphics} associated to this
	 * {@linkplain GameEntity}
	 */
	public ImageGraphics addGraphics(String imagePath, float width, float height, Vector anchor, float alpha,
			float depth) {
		ImageGraphics graphics = new ImageGraphics(imagePath, width, height, anchor, alpha, depth);
		graphics.setParent(this.entity);
		return graphics;
	}

    /** @see #addGraphics(String, float, float, Vector, float, float) */
    public ImageGraphics addGraphics(String imagePath, float width, float height, Vector anchor) {
        return this.addGraphics(imagePath, width, height, anchor, 1, 0);
    }

    /** @see #addGraphics(String, float, float, Vector, float, float) */
    public ImageGraphics addGraphics(String imagePath, float width, float height) {
        return this.addGraphics(imagePath, width, height, Vector.ZERO, 1, 0);
    }

	/**
	 * Create and add a {@linkplain ShapeGraphics} to this {@linkplain Entity}
	 * @param shape : a {@linkplain Shape}, may be null
	 * @param fillColor : a fill {@linkplain Color}, may be null
	 * @param outlineColor : an outline {@linkplain Color}, may be null
	 * @param thickness : the outline thickness
	 * @param alpha : the transparency, between 0 (invisible) and 1 (opaque)
	 * @param depth : the render priority, lower-values drawn first
	 * @return a new {@linkplain ShapeGraphics} associated to this
	 * {@linkplain GameEntity}
	 */
	public ShapeGraphics addGraphics(Shape shape, Color fillColor, Color outlineColor, float thickness, float alpha,
			float depth) {
		ShapeGraphics graphics = new ShapeGraphics(shape, fillColor, outlineColor, thickness, alpha, depth);
		graphics.setParent(this.entity);
		return graphics;
	}

	/** @see #addGraphics(Shape, Color, Color, float, float, float) */
	public ShapeGraphics addGraphics(Shape shape, Color color) {
		return this.addGraphics(shape, color, color, 0.f, 0.f, 0.f);
	}

	/**
     * Builds the {@linkplain Entity}, which gives it a physical representation in the engine.
     * @param shape : The {@linkplain Shape} to be given to the {@linkplain Entity}.
     * @param friction : The friction to be given to the {@linkplain Entity}, defaults if negative.
     * @param density : The density of the {@linkplain Entity}, defaults if negative.
     * @param ghost : Whether this part is hidden and should act only as a sensor.
     * @param collisionGroup : This {@linkplain Entity}'s collision group.
     */
    public void build(Shape shape, float friction, float density, boolean ghost, int collisionGroup) {
        PartBuilder partBuilder = this.entity.createPartBuilder();
        partBuilder.setShape(shape);
        if (friction >= 0)
            partBuilder.setFriction(friction);
        if (density >= 0)
            partBuilder.setDensity(density);
        partBuilder.setGhost(ghost);
        partBuilder.setCollisionGroup(collisionGroup);
        partBuilder.build();
    }

    /** @see #build(Shape, float, float, boolean, int) */
	public void build(Shape shape) {
		build(shape, -1, -1, false, 0);
	}

	/** @see #build(Shape, float, float, boolean, int) */
	public void build(Shape shape, float friction, float density, boolean ghost) {
        this.build(shape, friction, density, ghost, 0);
	}

	/**
	 * @param listener : the listener to add this {@linkplain Entity}.
	 */
	public void addContactListener(ContactListener listener) {
		this.entity.addContactListener(listener);
	}

	/**
	 * Gives a new position to this {@linkplain Entity}.
	 * @param newPosition : A position {@linkplain Vector} (x-axis value, y-axis
	 * value).
	 */
	public void setPosition(Vector newPosition) {
		entity.setPosition(newPosition);
		this.position = newPosition;
	}

	/**
	 * @return whether this {@linkplain GameEntity} is still in the
	 * {@linkplain World}
	 */
	public boolean isAlive() {
		return entity.isAlive();
	}

}
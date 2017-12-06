package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.ImageGraphics;
import main.game.actor.Savable;
import main.game.actor.ShapeGraphics;
import main.math.*;
import main.math.Shape;

import java.awt.*;


public abstract class GameEntity implements Actor, Savable {
	private static final long serialVersionUID = 8519429675636563656L;
	
	private transient Entity entity;
	private transient ActorGame actorGame;

	private Vector position;
	private boolean fixed;

	/**
	 * Create a new GameEntity, and its associated Entity
	 * 
	 * @param game : The Game where this entity inhabits
	 * @param fixed : Whether the entity is fixed
	 * @param position : The position of the entity
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

	/**
	 * Actual creation of the parameters of the GameEntity, not in the constructor to
	 * avoid duplication with the method reCreate
	 */
	private void create() {
		if (entity == null)
            this.entity = this.actorGame.newEntity(this.position, this.fixed);
        }

	@Override
	public void reCreate(ActorGame game) {
		actorGame = game;
		create();
	}

	/**
	 * Create a new GameEntity, and its associated Entity
	 * 
	 * @param game : The ActorGame where the GameEntity evolves
	 * @param fixed : Whether the Entity is fixed
	 */
	public GameEntity(ActorGame game, boolean fixed) {
		if (game == null)
			throw new NullPointerException("Game is null");
		this.actorGame = game;

		this.fixed = fixed;
		
		this.entity = game.newEntity(fixed);
	}

	/**
	 * Get the entity associated with this GameEntity
	 * 
	 * @return the entity
	 */
	protected Entity getEntity() {
		return this.entity;
	}

	/**
	 * Get the ActorGame associated with this GameEntity
	 * 
	 * @return The actorGame
	 */
	protected ActorGame getOwner() {
		return this.actorGame;
	}

	/**
	 * Destroy the entity associated with this GameEntity
	 */
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
	 * Create and add an ImageGraphics to this entity
	 * 
	 * @param imagePath : the path to the image to add
	 * @param width : the width of the image
	 * @param height : the height of the image
	 * @return a new ImageGraphics associated to this
	 */
	public ImageGraphics addGraphics(String imagePath, float width, float height) {
		ImageGraphics graphics = new ImageGraphics(imagePath, width, height);
		graphics.setParent(this.entity);
		return graphics;
	}

    /**
     *
     * @param imagePath
     * @param width
     * @param height
     * @param anchor
     * @param alpha
     * @param depth
     * @return
     */
	public ImageGraphics addGraphics(String imagePath, float width, float height, Vector anchor, float alpha, float depth) {
		ImageGraphics graphics = new ImageGraphics(imagePath, width, height, anchor, alpha, depth);
		graphics.setParent(this.entity);
		return graphics;
	}

	/**
	 * Create and add a ShapeGraphics to this entity
	 * 
	 * @param shape : a shape, may be null
	 * @param fillColor : a fill color, may be null
	 * @param outlineColor : an outline color, may be null
	 * @param thickness : the outline thickness
	 * @param alpha : the transparency, between 0 (invisible) and 1 (opaque)
	 * @param depth : the render priority, lower-values drawn first
	 * @return The ShapeGraphics created and associated to this entity
	 */
	public ShapeGraphics addGraphics(Shape shape, Color fillColor, Color outlineColor, float thickness, float alpha,
			float depth) {
		ShapeGraphics graphics = new ShapeGraphics(shape, fillColor, outlineColor, thickness, alpha, depth);
		graphics.setParent(this.entity);
		return graphics;
	}

	/**
	 * Create and add a ShapeGraphics to an entity
	 *
	 * @param shape : a shape, may be null
	 * @param color : a fill color, may be null
	 * @return The ShapeGraphics created and associated to this entity
	 */
	public ShapeGraphics addGraphics(Shape shape, Color color) {
		return addGraphics(shape, color, color, 0.f, 0.f, 0.f);
	}

	/**
	 * Build the entity, which gives it a physical representation in the engine
	 * 
	 * @param shape : the shape to be given to the entity
	 */
	public void build(Shape shape) {
		build(shape, -1, -1, false);
	}

	/**
	 * Builds the entity, which gives it a physical representation in the engine.
	 * @param shape : The shape to be given to the entity.
	 * @param friction : The friction to be given to the entity, defaults if negative.
	 * @param density : The density of the entity, defaults if negative.
	 * @param ghost : Whether this part is hidden and should act only as a sensor.
	 */
	public void build(Shape shape, float friction, float density, boolean ghost) {
		PartBuilder partBuilder = this.entity.createPartBuilder();
		partBuilder.setShape(shape);
		if (friction >= 0)
			partBuilder.setFriction(friction);
		if (density >= 0)
			partBuilder.setDensity(density);
		partBuilder.setGhost(ghost);
		partBuilder.build();
	}

    /**
     * Builds the entity, which gives it a physical representation in the engine.
     * @param shape : The shape to be given to the entity.
     * @param friction : The friction to be given to the entity, defaults if negative.
     * @param density : The density of the entity, defaults if negative.
     * @param ghost : Whether this part is hidden and should act only as a sensor.
     * @param collisionGroup : This entity's collision group.
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

	/**
	 * @param listener : the listener to add this entity
	 */
	public void addContactListener(ContactListener listener) {
		this.entity.addContactListener(listener);
	}

    /**
     * Gives a new position to this entity.
     * @param newPosition : A position Vector (x-axis value, y-axis value).
     */
	public void setPosition(Vector newPosition) {
		entity.setPosition(newPosition);
	}
}
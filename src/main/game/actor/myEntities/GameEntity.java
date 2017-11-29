/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.myEntities;

import main.game.actor.Actor;
import main.game.actor.ActorGame;
import main.game.actor.ImageGraphics;
import main.game.actor.ShapeGraphics;
import main.math.*;
import main.math.Shape;

import java.awt.*;

public abstract class GameEntity implements Actor {
	private Entity entity;

	private ActorGame actorGame;

	/**
	 * Create a new GameEntity, and its associated Entity
	 * 
	 * @param game
	 *            The ActorGame where the GameEntity evolve
	 * @param fixed
	 *            Weather the Entity is fixed
	 * @param position
	 *            The position of the Entity
	 */
	public GameEntity(ActorGame game, boolean fixed, Vector position) {
		if (game == null)
			throw new NullPointerException("Game is null");
		if (position == null)
			throw new NullPointerException("Vector is null");
		this.actorGame = game;

		entity = game.newEntity(position, fixed);
	}

	/**
	 * Create a new GameEntity, and its associated Entity
	 * 
	 * @param game
	 *            The ActorGame where the GameEntity evolve
	 * @param fixed
	 *            Wether the Entity is fixed
	 */
	public GameEntity(ActorGame game, boolean fixed) {
		if (game == null)
			throw new NullPointerException("Game is null");
		this.actorGame = game;

		entity = game.newEntity(fixed);
	}

	/**
	 * Get the Entity associated with this GameEntity
	 * 
	 * @return The Entity
	 */
	protected Entity getEntity() {
		return entity;
	}

	/**
	 * Get the ActorGame associated with this GameEntity
	 * 
	 * @return The ActorGame
	 */
	protected ActorGame getOwner() {
		return actorGame;
	}

	/**
	 * Destroy the entity associated with this GameEntity
	 */
	public void destroy() {
		entity.destroy();
	}

	@Override
	public Transform getTransform() {
		return entity.getTransform();
	}

	@Override
	public Vector getVelocity() {
		return entity.getVelocity();
	}

	/**
	 * Create and add an ImageGraphics to an entity
	 *
	 * @param entity
	 *            The entity
	 * @param imagePath
	 *            The path to the image to add
	 * @param width
	 *            The width of the image
	 * @param height
	 *            The height of the image
	 * @return A new ImageGraphics associated to the entity
	 */
	public static ImageGraphics addGraphics(Entity entity, String imagePath, float width, float height) {
		ImageGraphics graphics = new ImageGraphics(imagePath, width, height);
		graphics.setParent(entity);
		return graphics;
	}

	/**
	 * Create and add a ShapeGraphics to an entity
	 *
	 * @param entity
	 *            The entity
	 * @param shape
	 *            shape, may be null
	 * @param fillColor
	 *            fill color, may be null
	 * @param outlineColor
	 *            outline color, may be null
	 * @param thickness
	 *            outline thickness
	 * @param alpha
	 *            transparency, between 0 (invisible) and 1 (opaque)
	 * @param depth
	 *            render priority, lower-values drawn first
	 * @return The ShapeGraphics created and associated to the image
	 */
	public static ShapeGraphics addGraphics(Entity entity, Shape shape, Color fillColor, Color outlineColor,
			float thickness, float alpha, float depth) {
		ShapeGraphics graphics = new ShapeGraphics(shape, fillColor, outlineColor, thickness, alpha, depth);
		graphics.setParent(entity);
		return graphics;
	}

	public static ShapeGraphics addGraphics(Entity entity, Shape shape, Color color) {
		return addGraphics(entity, shape, color, color, 0.f, 0.f, 0.f);
	}

	/**
	 * Build the entity, which give a physical representation for the engine
	 *
	 * @param entity
	 *            The entity
	 * @param shape
	 *            The shape to give to the entity
	 */
	public static void build(Entity entity, Shape shape) {
		build(entity, shape, -1, -1, false);
	}

	/**
	 * Build the entity, which give a physical representation for the engine
	 *
	 * @param entity
	 *            The entity
	 * @param shape
	 *            The shape to give to the entity
	 * @param friction
	 *            friction to give to the entity, if negative -> default value
	 * @param density
	 *            density of the entity, if negative -> default value
	 * @param ghost
	 *            whether this part is hidden and act only as a sensor
	 */
	public static void build(Entity entity, Shape shape, float friction, float density, boolean ghost) {
		PartBuilder partBuilder = entity.createPartBuilder();
		partBuilder.setShape(shape);
		if (friction >= 0)
			partBuilder.setFriction(friction);
		if (density >= 0)
			partBuilder.setDensity(density);
		partBuilder.setGhost(ghost);
		partBuilder.build();
	}

}

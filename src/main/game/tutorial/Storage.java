/**
 *	Author: Clément Jeannet
 *	Date: 	18 nov. 2017
 */
package main.game.tutorial;

import java.awt.Color;
import java.util.ArrayList;

import main.game.actor.Graphics;
import main.game.actor.ImageGraphics;
import main.game.actor.ShapeGraphics;
import main.math.Circle;
import main.math.Entity;
import main.math.EntityBuilder;
import main.math.PartBuilder;
import main.math.Polygon;
import main.math.Shape;
import main.math.Vector;
import main.math.World;

public class Storage extends Stock {

	/**
	 * Because a serialVersionUID is needed
	 */
	private static final long serialVersionUID = 42L;

	/** The physical engine */
	private static World world;

	/**
	 * Create a new instance of a Storage
	 * 
	 * @param w
	 *            the physical engine
	 */
	public Storage(World w) {
		super();
		world = w;
	}

	/**
	 * Create a new entity with a shape of a square
	 * 
	 * @param position
	 *            the Vector position of the entity
	 * @param imagePath
	 *            The path to the image associated to the entity, if null --> no
	 *            image
	 * @param size
	 *            The length of the square, has to be positive
	 * @param fixed
	 *            Whether or not the entity is fixed
	 * @param id
	 *            the id, to find later the entity
	 */
	public void newSquare(Vector position, String imagePath, float size, boolean fixed, int id) {
		newRectangle(position, imagePath, size, size, fixed, id);
	}

	/**
	 * Create a new entity with a shape of a square
	 * 
	 * @param position
	 *            the Vector position of the entity
	 * @param imagePath
	 *            The path to the image associated to the entity, if null --> no
	 *            image
	 * @param width
	 *            Width of the rectangle, has to be positive
	 * @param height
	 *            Height of the rectangle, has to be positive
	 * @param fixed
	 *            Whether or not the entity is fixed
	 * @param id
	 *            the id, to find later the entity
	 */
	public void newRectangle(Vector position, String imagePath, float width, float height, boolean fixed, int id) {

		// Create a shape
		Polygon polygon = new Polygon(new Vector(0.0f, 0.0f), new Vector(width, 0.0f), new Vector(width, height),
				new Vector(0.0f, height));

		// Create an image
		ImageGraphics image = null;
		if (imagePath != null && imagePath != "") {
			image = new ImageGraphics(imagePath, Math.abs(width), Math.abs(height));
		}
		newThing(position, fixed, polygon, image, id);
	}

	/**
	 * Create a new entity with a shape of a Sphere
	 * 
	 * @param position
	 *            the Vector position of the entity
	 * @param imagePath
	 *            The path to the image associated to the entity, if null --> no
	 *            image
	 * @param radius
	 *            The radius of the sphere
	 * @param fixed
	 *            Whether or not the entity is fixed
	 * @param id
	 *            the id, to find later the entity
	 */
	public void newSphere(Vector position, float radius, String imagePath, boolean fixed, int id) {

		// Create a shape
		Circle circle = new Circle(radius);

		// give the entity an image
		ImageGraphics image = null;
		if (imagePath != null && imagePath != "") {
			image = new ImageGraphics(imagePath, radius * 2f, radius * 2f, new Vector(0.5f, 0.5f));
		}
		// Create the entity
		newThing(position, fixed, circle, image, id);
	}

	/**
	 * Create a new entity with a shape of a Sphere
	 * 
	 * @param position
	 *            the Vector position of the entity
	 * @param radius
	 *            The radius of the sphere, has to be positive
	 * @param fixed
	 *            Whether or not the entity is fixed
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
	 * @param id
	 *            the id, to find later the entity
	 */
	public void newSphere(Vector position, float radius, boolean fixed, Color fillColor, Color outlineColor,
			float thickness, float alpha, float depth, int id) {

		// create shape
		Circle circle = new Circle(radius);

		// Create graphics
		ShapeGraphics ballGraphics = new ShapeGraphics(circle, fillColor, outlineColor, thickness, alpha, depth);

		// Create the entity
		newThing(position, fixed, circle, ballGraphics, id);
	}

	/**
	 * Private general method to build an entity
	 */
	private void newThing(Vector position, boolean fixed, Shape s, Graphics g, int id) {

		// Storage
		ArrayList<Object> alO = new ArrayList<Object>();

		// create entity
		EntityBuilder entityBuilder = world.createEntityBuilder();
		entityBuilder.setFixed(fixed);
		entityBuilder.setPosition(position);
		Entity entity = entityBuilder.build();

		// store entity
		alO.add(entity);

		// give graphics to the entity
		addGraphics(entity, g);
		ArrayList<Graphics> g33 = new ArrayList<Graphics>();
		g33.add(g);
		alO.add(g33);

		PartBuilder partBuilder = null;
		// give the entity a shape
		if (s != null) {
			partBuilder = entity.createPartBuilder();
			partBuilder.setShape(s);
			partBuilder.setFriction(.4f);
			partBuilder.build();
		}
		// store PartBuilder
		alO.add(partBuilder);

		// Store the id
		alO.add(id);

		// add everything to this
		this.add(alO);
	}
}

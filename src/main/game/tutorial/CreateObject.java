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

public class CreateObject {

	private static World world;
	private static ArrayList<Entity> alE;
	private static ArrayList<Graphics> alIG;

	/**
	 * Initialize the references to the world, entities and ImagesGraphics so we
	 * don't need to always give it in parameters
	 * 
	 * @param w
	 *            The world
	 * @param allEntities
	 *            The ArrayList containing all the entities of the game
	 * @param allImagesGraphics
	 *            The ArrayList containing all the associated images
	 */
	public static void init(World w, ArrayList<Entity> allEntities, ArrayList<Graphics> allImagesGraphics) {
		alE = allEntities;
		alIG = allImagesGraphics;
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
	 */
	public static void newSquare(Vector position, String imagePath, float size, boolean fixed) {
		newRectangle(position, imagePath, size, size, fixed);
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
	 */
	public static void newRectangle(Vector position, String imagePath, float width, float height, boolean fixed) {

		// Create a shape
		Polygon polygon = new Polygon(new Vector(0.0f, 0.0f), new Vector(width, 0.0f), new Vector(width, height),
				new Vector(0.0f, height));

		// Create an image
		ImageGraphics image = null;
		if (imagePath != null && imagePath != "") {
			image = new ImageGraphics(imagePath, Math.abs(width), Math.abs(height));
		}
		newThing(position, fixed, polygon, image);
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
	 */
	public static void newSphere(Vector position, float radius, String imagePath, boolean fixed) {

		// Create a shape
		Circle circle = new Circle(radius);

		// give the entity an image
		ImageGraphics image = null;
		if (imagePath != null && imagePath != "") {
			image = new ImageGraphics(imagePath, radius * 2f, radius * 2f, new Vector(0.5f, 0.5f));
		}
		// Create the entity
		newThing(position, fixed, circle, image);
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
	 */
	public static void newSphere(Vector position, float radius, boolean fixed, Color innerColor, Color borderColor,
			float thickness, float alpha, float depth) {

		// create shape
		Circle circle = new Circle(radius);

		// Create graphics
		ShapeGraphics ballGraphics = new ShapeGraphics(circle, innerColor, borderColor, thickness, alpha, depth);

		// Create the entity
		newThing(position, fixed, circle, ballGraphics);
	}

	private static void newThing(Vector position, boolean fixed, Shape s, Graphics g) {

		// create entity
		EntityBuilder entityBuilder = world.createEntityBuilder();
		entityBuilder.setFixed(fixed);
		entityBuilder.setPosition(position);
		Entity entity = entityBuilder.build();

		// give the entity a shape
		if (s != null) {
			PartBuilder partBuilder = entity.createPartBuilder();
			partBuilder.setShape(s);
			partBuilder.setFriction(.4f);
			partBuilder.build();
		}

		// give graphics to the entity
		if (g != null) {
			if (g instanceof ShapeGraphics) {
				ShapeGraphics g2 = (ShapeGraphics) g;
				g2.setParent(entity);
				alIG.add(g2);
			} else if (g instanceof ImageGraphics) {
				ImageGraphics g2 = (ImageGraphics) g;
				g2.setParent(entity);
				alIG.add(g2);
			}
		}

		// add the entity to the ArrayList of entity
		alE.add(entity);
	}
}

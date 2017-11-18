/**
 *	Author: Clément Jeannet
 *	Date: 	18 nov. 2017
 */
package main.game.tutorial;

import java.util.ArrayList;

import main.game.actor.ImageGraphics;
import main.math.Entity;
import main.math.EntityBuilder;
import main.math.PartBuilder;
import main.math.Polygon;
import main.math.Vector;
import main.math.World;

public class CrateObject {

	static World world;
	static ArrayList<Entity> alE;
	static ArrayList<ImageGraphics> alIG;

	public static void init(World w, ArrayList<Entity> allEntities, ArrayList<ImageGraphics> allImagesGraphics) {
		alE = allEntities;
		alIG = allImagesGraphics;
		world = w;
	}

	public static void newCarre(Vector position, String imagePath, float size, boolean fixed) {

		EntityBuilder entityBuilder = world.createEntityBuilder();
		entityBuilder.setFixed(fixed);
		entityBuilder.setPosition(position);
		Entity entity = entityBuilder.build();

		PartBuilder partBuilder = entity.createPartBuilder();
		Polygon polygon = new Polygon(new Vector(0.0f, 0.0f), new Vector(size, 0.0f), new Vector(size, size),
				new Vector(0.0f, size));
		partBuilder.setShape(polygon);
		partBuilder.build();

		ImageGraphics image = new ImageGraphics(imagePath, size, size);
		image.setParent(entity);

		alE.add(entity);
		alIG.add(image);

	}

}

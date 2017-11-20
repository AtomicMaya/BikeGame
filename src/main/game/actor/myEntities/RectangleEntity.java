/**
 *	Author: Clément Jeannet
 *	Date: 	20 nov. 2017
 */
package main.game.actor.myEntities;

import main.game.actor.ImageGraphics;
import main.math.Polygon;
import main.math.Vector;
import main.math.World;

public class RectangleEntity extends EntityExtended {

	public RectangleEntity(World world, Vector position, float width, float height, boolean fixed, int id) {
		super(world, position, fixed, id);
		// Create a shape
		Polygon polygon = new Polygon(new Vector(0.0f, 0.0f), new Vector(width, 0.0f), new Vector(width, height),
				new Vector(0.0f, height));
		super.setShape(polygon);
	}

	public RectangleEntity(World world, Vector position, String imagePath, float width, float height, boolean fixed,
			int id) {
		this(world, position, width, height, fixed, id);

		// Create an image
		ImageGraphics image = null;
		if (imagePath != null && imagePath != "") {
			image = new ImageGraphics(imagePath, Math.abs(width), Math.abs(height));
		}
		super.setGraphics(image);

	}

}

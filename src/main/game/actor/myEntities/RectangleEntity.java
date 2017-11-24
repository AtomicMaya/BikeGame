/**
 *	Author: Clément Jeannet
 *	Date: 	20 nov. 2017
 */
package main.game.actor.myEntities;

import main.game.actor.ImageGraphics;
import main.game.actor.ActorGame;
import main.math.Entity;
import main.math.Polygon;
import main.math.Vector;

public class RectangleEntity extends SimpleEntity {

	public RectangleEntity(Entity entity, float width, float height) {
		super(entity);
		// Create a shape
		Polygon polygon = new Polygon(new Vector(0.0f, 0.0f), new Vector(width, 0.0f), new Vector(width, height),
				new Vector(0.0f, height));
		super.setShape(polygon);
	}

	public RectangleEntity(Entity entity, String imagePath, float width, float height) {
		this(entity, width, height);

		// Create an image
		ImageGraphics image = null;
		if (imagePath != null && imagePath != "") {
			image = new ImageGraphics(imagePath, Math.abs(width), Math.abs(height));
		}
		super.setGraphics(image);

	}

}

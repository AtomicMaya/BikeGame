/**
 *	Author: Clément Jeannet
 *	Date: 	20 nov. 2017
 */
package main.game.actor.myEntities;

import main.game.actor.ImageGraphics;
import main.game.actor.ActorGame;
import main.math.Polygon;
import main.math.Vector;

public class RectangleEntity extends SimpleEntity {

	public RectangleEntity(ActorGame game, Vector position, float width, float height, boolean fixed) {
		super(game, position, fixed);
		// Create a shape
		Polygon polygon = new Polygon(new Vector(0.0f, 0.0f), new Vector(width, 0.0f), new Vector(width, height),
				new Vector(0.0f, height));
		super.setShape(polygon);
	}

	public RectangleEntity(ActorGame game, Vector position, String imagePath, float width, float height, boolean fixed) {
		this(game, position, width, height, fixed);

		// Create an image
		ImageGraphics image = null;
		if (imagePath != null && imagePath != "") {
			image = new ImageGraphics(imagePath, Math.abs(width), Math.abs(height));
		}
		super.setGraphics(image);

	}

}

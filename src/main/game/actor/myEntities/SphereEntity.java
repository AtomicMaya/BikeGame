/**
 *	Author: Clément Jeannet
 *	Date: 	20 nov. 2017
 */
package main.game.actor.myEntities;

import java.awt.Color;

import main.game.actor.ImageGraphics;
import main.game.actor.ActorGame;
import main.game.actor.ShapeGraphics;
import main.math.Circle;
import main.math.Vector;

public class SphereEntity extends SimpleEntity {

//	public SphereEntity(ActorGame game, Vector position, boolean fixed) {
//		super(game, position, fixed);
//	}

	public SphereEntity(ActorGame game, Vector position, float radius, String imagePath, boolean fixed) {
		super(game, position, fixed);

		// Create a shape
		Circle circle = new Circle(radius);

		// give the entity an image
		ImageGraphics image = null;
		if (imagePath != null && imagePath != "") {
			image = new ImageGraphics(imagePath, radius * 2f, radius * 2f, new Vector(0.5f, 0.5f));
		}
		super.setShape(circle);
		super.setGraphics(image);
	}

	public SphereEntity(ActorGame game, Vector position, float radius, boolean fixed, Color fillColor, Color outlineColor,
			float thickness, float alpha, float depth) {
		super(game, position, fixed);

		// create shape
		Circle circle = new Circle(radius);

		// Create graphics
		ShapeGraphics ballGraphics = new ShapeGraphics(circle, fillColor, outlineColor, thickness, alpha, depth);
		super.setShape(circle);
		super.setGraphics(ballGraphics);
	}
}

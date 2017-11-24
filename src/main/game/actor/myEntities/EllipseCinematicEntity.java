/**
 *	Author: Clément Jeannet
 *	Date: 	24 nov. 2017
 */
package main.game.actor.myEntities;

import main.game.actor.ActorGame;
import main.game.actor.ImageGraphics;
import main.game.actor.ShapeGraphics;
import main.math.Ellipse;
import main.math.Entity;
import main.math.Vector;

import java.awt.*;

public class EllipseCinematicEntity extends SimpleEntity {

	public EllipseCinematicEntity(Entity entity, Vector position, float longRadius, float shortRadius, String imagePath, boolean fixed) {
		super(entity);
		Ellipse e = new Ellipse(shortRadius, longRadius);

		// give the entity an image
		ImageGraphics image = null;
		if (imagePath != null && imagePath != "") {
			image = new ImageGraphics(imagePath, longRadius, shortRadius , new Vector(longRadius, shortRadius));
		}
		super.setShape(e);
		super.setGraphics(image);

	}
	public EllipseCinematicEntity(Entity entity, float longRadius, float shortRadius, Color innerColor, Color outerColor, float thickness, float alpha, float depth) {
		super(entity);
		Ellipse e = new Ellipse(shortRadius, longRadius);

		ShapeGraphics s = new ShapeGraphics(e, innerColor, outerColor,thickness,alpha,depth);
		super.setShape(e);
		super.setGraphics(s);
	}

}

/**
 *	Author: Clément Jeannet
 *	Date: 	24 nov. 2017
 */
package main.game.actor.myEntities;

import main.game.actor.ActorGame;
import main.game.actor.ImageGraphics;
import main.game.actor.ShapeGraphics;
import main.math.Ellipse;
import main.math.Vector;

import java.awt.*;

public class EllipseEntity extends SimpleEntity {

	public EllipseEntity(ActorGame game, Vector position, float longRadius, float shortRadius, String imagePath, boolean fixed) {
		super(game, position, fixed);
		Ellipse e = new Ellipse(shortRadius, longRadius);

		// give the entity an image
		ImageGraphics image = null;
		if (imagePath != null && imagePath != "") {
			image = new ImageGraphics(imagePath, longRadius * 2f, shortRadius * 2f, new Vector(longRadius, shortRadius));
		}
		super.setShape(e);
		super.setGraphics(image);

	}
	public EllipseEntity(ActorGame game, Vector position, float longRadius, float shortRadius, boolean fixed) {
		super(game, position, fixed);
		Ellipse e = new Ellipse(shortRadius, longRadius);

		// give the entity an image

		ShapeGraphics s = new ShapeGraphics(e, Color.BLUE,Color.BLUE,1,1,0);
		super.setShape(e);
		super.setGraphics(s);

	}

}

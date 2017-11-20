/**
 *	Author: Clément Jeannet
 *	Date: 	20 nov. 2017
 */
package main.game.actor;

import java.awt.Color;
import java.util.ArrayList;

import main.game.actor.myEntities.EntityExtended;
import main.math.Polyline;
import main.math.Vector;
import main.math.World;

public class Ground extends EntityExtended {

	public Ground(World world, Vector position, ArrayList<Vector> alV, Color color, float thickness, int id) {
		super(world, position, true, id);

		Polyline p = new Polyline(alV);

		ShapeGraphics shapeGraphic = new ShapeGraphics(p, null, color, .1f);
		// so the thing on it are not in the line
		shapeGraphic.setRelativeTransform(shapeGraphic.getTransform().translated(new Vector(0, -.05f)));

		super.setShape(p);
		super.setGraphics(shapeGraphic);
	}
	public Ground(World world, Vector position, Polyline p, Color color, float thickness, int id) {
		super(world, position, true, id);

		ShapeGraphics shapeGraphic = new ShapeGraphics(p, null, color, .1f);
		// so the thing on it are not in the line
		shapeGraphic.setRelativeTransform(shapeGraphic.getTransform().translated(new Vector(0, -.05f)));

		super.setShape(p);
		super.setGraphics(shapeGraphic);
	}
	
}

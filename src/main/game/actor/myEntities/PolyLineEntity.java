/**
 *	Author: Clément Jeannet
 *	Date: 	20 nov. 2017
 */
package main.game.actor.myEntities;

import java.awt.Color;
import java.util.ArrayList;

import main.game.actor.ActorGame;
import main.game.actor.ShapeGraphics;
import main.math.Entity;
import main.math.Polyline;
import main.math.Vector;

public class PolyLineEntity extends SimpleEntity {

	public PolyLineEntity(Entity entity, ArrayList<Vector> alV, Color color, float thickness) {
		super(entity);


		Polyline p = new Polyline(alV);

		ShapeGraphics shapeGraphic = new ShapeGraphics(p, null, color, .1f);
		// so the thing on it are not in the line
		shapeGraphic.setRelativeTransform(shapeGraphic.getTransform().translated(new Vector(0, -.05f)));

		super.setShape(p);
		super.setGraphics(shapeGraphic);
	}
	public PolyLineEntity(Entity entity, Polyline p, Color color, float thickness) {
		super(entity);

		ShapeGraphics shapeGraphic = new ShapeGraphics(p, null, color, .1f);
		// so the thing on it are not in the line
		//shapeGraphic.setRelativeTransform(shapeGraphic.getTransform().translated(new Vector(0, -.05f)));

		super.setShape(p);
		super.setGraphics(shapeGraphic);
	}
	
}

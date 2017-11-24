/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.myEntities;

import main.game.actor.ActorGame;
import main.math.Entity;
import main.math.Polygon;
import main.math.Vector;


public class PolygonEntity extends SimpleEntity {

	public PolygonEntity(Entity entity, Vector... vectors) {
		super(entity);
		Polygon p = new Polygon(vectors);
		super.setShape(p);
	}

	public PolygonEntity(Entity entity, Polygon polygon) {
		super(entity);
		super.setShape(polygon);
	}

}

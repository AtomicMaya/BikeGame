/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.myEntities;

import main.game.actor.ActorGame;
import main.math.Polygon;
import main.math.Vector;

public class PolygonEntity extends SimpleEntity {

	public PolygonEntity(ActorGame game, Vector position, boolean fixed, Vector... vectors) {
		super(game, position, fixed);
		Polygon p = new Polygon(vectors);
		super.setShape(p);
	}

	public PolygonEntity(ActorGame game, Vector position, boolean fixed, Polygon polygon) {
		super(game, position, fixed);
		super.setShape(polygon);
	}

}

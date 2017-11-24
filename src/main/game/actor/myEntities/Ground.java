/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.myEntities;

import java.awt.Color;

import main.game.actor.Actor;
import main.game.actor.ActorGame;
import main.math.Entity;
import main.math.Polyline;
import main.math.Vector;

public class Ground {


	public Ground(ActorGame game, Vector position, Polyline p) {
		Entity e = game.newEntity((position == null) ? Vector.ZERO : position,true);
		Actor a = new PolyLineEntity(e, p, Color.black, .2f);
		game.addActor(a);
	}




}

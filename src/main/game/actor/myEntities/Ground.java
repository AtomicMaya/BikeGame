/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.myEntities;

import main.game.actor.Actor;
import main.game.actor.ActorGame;
import main.math.Entity;
import main.math.Polyline;
import main.math.Vector;

import java.awt.*;

public class Ground extends GameEntity {
	public Ground(ActorGame game, Vector position, Polyline p) {
		Entity e = game.newEntity((position == null) ? Vector.ZERO : position,true);
		Actor a = new PolyLineEntity(e, p, Color.BLACK, .2f);
		game.addActor(a);
	}

	@Override
	public void draw(Canvas canvas) {}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub

	}


}

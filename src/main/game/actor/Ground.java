/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor;

import java.awt.Color;

import main.game.actor.myEntities.ComplexObject;
import main.math.Polyline;
import main.math.Vector;

public class Ground extends ComplexObject {

	private PolyLineEntity ground;
	public Ground(MyGame game, Vector position, Polyline p) {
		addEntity(ground = new PolyLineEntity(game, (position == null) ? Vector.ZERO : position, p, Color.black, .2f));
	}

	@Override
	public void update(float deltaTime) {
	}

	@Override
	public Vector getPosition() {
		
		return ground.getEntity().getPosition();
	}
	
	@Override
	public Vector getVelocity() {
		return Vector.ZERO;
	}

}

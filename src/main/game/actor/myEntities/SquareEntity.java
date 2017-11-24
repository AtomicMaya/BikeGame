/**
 *	Author: Clément Jeannet
 *	Date: 	20 nov. 2017
 */
package main.game.actor.myEntities;

import main.game.actor.ActorGame;
import main.math.Entity;
import main.math.Vector;

public class SquareEntity extends RectangleEntity {

	public SquareEntity(Entity entity, float size) {
		super(entity, size, size);
	}

	public SquareEntity(Entity entity, String imagePath, float size) {
		super(entity, imagePath, size, size);
	}

}

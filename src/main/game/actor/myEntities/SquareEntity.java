/**
 *	Author: Clément Jeannet
 *	Date: 	20 nov. 2017
 */
package main.game.actor.myEntities;

import main.math.Vector;
import main.math.World;

public class SquareEntity extends RectangleEntity {

	public SquareEntity(World world, Vector position, float size, boolean fixed, int id) {
		super(world, position, size, size, fixed, id);
	}

	public SquareEntity(World world, Vector position, String imagePath, float size, boolean fixed, int id) {
		super(world, position, imagePath, size, size, fixed, id);
	}

}

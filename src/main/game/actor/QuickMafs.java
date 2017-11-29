package main.game.actor;

import main.math.Vector;

/**
 * Created on 11/29/2017 at 11:09 AM.
 */
public class QuickMafs {
	public static float getDistance(Vector one, Vector two) {
		return (float) Math.sqrt(Math.pow(one.x - two.x, 2) + Math.pow(one.y - two.y, 2));
	}

}

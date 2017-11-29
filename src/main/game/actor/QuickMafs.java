package main.game.actor;

import main.math.Vector;

public class QuickMafs {
	public static float getDistance(Vector one, Vector two) {
		return (float) Math.sqrt(Math.pow(one.x - two.x, 2) + Math.pow(one.y - two.y, 2));
	}

}

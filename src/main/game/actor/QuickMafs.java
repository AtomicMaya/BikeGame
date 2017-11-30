package main.game.actor;

import main.math.Vector;

import java.util.ArrayList;
import java.util.List;

public class QuickMafs {
	/**
	 * Gets distance between two points
	 * @param first : first point
	 * @param second : second point
	 * @return the distance between both points
	 */
	public static float getDistance(Vector first, Vector second) {
		return (float) Math.sqrt(Math.pow(first.x - second.x, 2) + Math.pow(first.y - second.y, 2));
	}

	/**
	 * Converts from degrees to radians
	 * @param angle : an angle in degrees
	 * @return : an angle in radians
	 */
	public static float toRadians(float angle) {
		return (float) (angle * Math.PI / 180.f);
	}


	/**
	 * Inverts a list of vectors on an axis.
	 * @param vectors : the given list of vectors
	 * @param axis : the given axis, should be ([-1.f | 1.f], [-1.f | 1.f]) for clean inversion.
	 * @return a list with the inverted vectors
	 */
	public static List<Vector> invertXCoordinates(List<Vector> vectors, Vector axis) {
		List<Vector> newVectors = new ArrayList<>();
		for (Vector vector : vectors) {
			newVectors.add(vector.mul(axis));
		}
		return newVectors;
	}
}

package main.game.actor;

import main.math.Vector;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

// ASCII
// 050 032 112 108 117 115 032 050 032 105 115 032 052 032 109 105 110 117 115 032 049 032 105 115 032 051 044 032 113 117 105 099 107 032 109 097 102 115 032 033

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

	public static float getAngle(Vector center, Vector from, Vector to) {
		return (float) Math.atan2((from.y - center.y) - (to.y - center.y), (from.x - center.x) - (to.x - center.x));
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

	public static Vector xyNormal = new Vector(1.f, 1.f);
	public static Vector xInverted = new Vector(-1.f, 1.f);
	public static Vector yInverted = new Vector(1.f, -1.f);
	public static Vector xyInverted = new Vector(-1.f, -1.f);

	public static SwingWorker<Void, Void> generateWorker(Runnable action) {
		return new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {
				action.run();
				return null;
			}
		};
	}
}

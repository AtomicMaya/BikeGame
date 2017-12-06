package main.game.actor;

import main.math.Vector;

import java.util.ArrayList;
import java.util.List;

// ASCII
// 050 032 112 108 117 115 032 050 032 105 115 032 052 032 109 105 110 117 115 032 049 032 105 115 032 051 044 032 113 117 105 099 107 032 109 097 102 115 032 033

public class QuickMafs {
	/**
	 * Gets distance between two points.
	 * @param first : The first point.
	 * @param second : The second point.
	 * @return the distance between both points.
	 */
	public static float getDistance(Vector first, Vector second) {
		return (float) Math.sqrt(Math.pow(first.x - second.x, 2) + Math.pow(first.y - second.y, 2));
	}

	public static float getAngle(Vector center, Vector from, Vector to) {
		return (float) Math.atan2((from.y - center.y) - (to.y - center.y), (from.x - center.x) - (to.x - center.x));
	}

	/**
	 * Converts from degrees to radians
	 * @param angle : An angle in degrees
	 * @return : an angle in radians
	 */
	public static float toRadians(float angle) {
		return (float) (angle * Math.PI / 180.f);
	}

	/**
	 * Inverts a list of vectors on an axis.
	 * @param vectors : The given list of vectors
	 * @param axis : The given axis, should be ([-1.f | 1.f], [-1.f | 1.f]) for
	 *            clean inversion.
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

	public static int getRed(int rgb) {
		return (rgb >> 16) & 0xFF;
	}

	/**
	 * Returns green component from given packed color.
	 * @param rgb : A 32-bits ARGB color
	 * @return an integer between 0 and 255
	 */
	public static int getGreen(int rgb) {
		return (rgb >> 8) & 0xFF;
	}

	/**
	 * Returns blue component from given packed color.
	 * @param rgb : A 32-bits ARGB color.
	 * @return an integer between 0 and 255.
	 */
	public static int getBlue(int rgb) {
		return rgb & 0xFF;
	}

    /**
     * Returns alpha component from given packed color.
     * @param argb : A 32-bits ARGB color.
     * @return an int between 0 and 255.
     */
	public static int getAlpha(int argb) {
		return (argb >> 24) & 0xFF;
	}


	public static int validate8bit(int someInt) {
		if (someInt < 0)
			someInt = 0;
		else if (255 < someInt)
			someInt = 255;
		return someInt;
	}

	/**
	 * Floor a Vector
	 * 
	 * @param v a vector to floor
	 * @return a new Vector with floored x and y component
	 */
	public static Vector floor(Vector v) {
		return new Vector((float) Math.floor(v.x), (float) Math.floor(v.y));
	}

	public static boolean isInRectangle(Vector min, Vector max, Vector toTest) {

		float minX = Math.min(min.x, max.x);
		float maxX = Math.max(min.x, max.x);
		float minY = Math.min(min.y, max.y);
		float maxY = Math.max(min.y, max.y);

		if (minX > toTest.x || maxX < toTest.x)
			return false;
        return !(minY > toTest.y) && !(maxY < toTest.y);
    }

	public static ArrayList<Vector> sortVectorByX(ArrayList<Vector> points) {

		if (points.size() < 2)
			return points;
		else {
			ArrayList<Vector> smaller = new ArrayList<>();
			ArrayList<Vector> equals = new ArrayList<>();
			ArrayList<Vector> bigger = new ArrayList<>();

			Vector p = points.get(0);

			for (Vector v : points) {
				if (p.x > v.x) {
					smaller.add(v);
				} else if (p.x < v.x) {
					bigger.add(v);
				} else
					equals.add(v);
			}
			ArrayList<Vector> sorted = new ArrayList<>();
			sorted.addAll(sortVectorByX(smaller));
			sorted.addAll(deleteUselessInY(equals));
			sorted.addAll(sortVectorByX(bigger));
			return sorted;
		}
	}

	public static ArrayList<Vector> deleteUselessInY(ArrayList<Vector> points) {
		if (points.size() <= 1)
			return points;
		Vector s = points.get(0);
		Vector b = points.get(0);
		for (Vector v : points) {
			if (s.y > v.y)
				s = v;
			if (b.y < v.y)
				b = v;
		}
		ArrayList<Vector> p = new ArrayList<>();
		p.add(s);
		p.add(b);

		return p;
	}
}

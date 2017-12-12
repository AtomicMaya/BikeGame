package main.math;

import java.util.ArrayList;
import java.util.List;

// ASCII
// 050 032 112 108 117 115 032 050 032 105 115 032 052 032 109 105 110 117 115 032 049 032 105 115 032 051 044 032 113 117 105 099 107 032 109 097 102 115 032 033

public class ExtendedMath {
	/**
	 * Gets the distance between two {@linkplain Vector}s.
	 * @param first The first {@linkplain Vector}.
	 * @param second The second {@linkplain Vector}.
	 * @return the distance between both {@linkplain Vector}s.
	 */
	public static float getDistance(Vector first, Vector second) {
		return (float) Math.sqrt(Math.pow(first.x - second.x, 2) + Math.pow(first.y - second.y, 2));
	}

	/**
	 * Gets the angle between the two {@linkplain Vector}s and the x-axis.
	 * @param center The center {@linkplain Vector}.
	 * @param from The first {@linkplain Vector}.
	 * @param to The second {@linkplain Vector}.
	 * @return an angle in radians.
	 */
	public static float getAngle(Vector center, Vector from, Vector to) {
		return (float) Math.atan2((from.y - center.y) - (to.y - center.y), (from.x - center.x) - (to.x - center.x));
	}

	/**
	 * Converts from degrees to radians
	 * @param angle An angle in degrees
	 * @return an angle in radians
	 */
	public static float toRadians(float angle) {
		return (float) (angle * Math.PI / 180.f);
	}

	/**
	 * Inverts a list of {@linkplain Vector}s on an axis.
	 * @param vectors The given {@linkplain List} of {@linkplain Vector}s.
	 * @param axis The given axis, should be ([-1.f | 1.f], [-1.f | 1.f]) for
	 * clean inversion.
	 * @return a {@linkplain List} with the inverted {@linkplain Vector}.
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

	/**
	 * Returns red component from given packed color.
	 * @param rgb A 32-bits ARGB color
	 * @return an integer between 0 and 255
	 */
	public static int getRed(int rgb) {
		return (rgb >> 16) & 0xFF;
	}

	/**
	 * Returns green component from given packed color.
	 * @param rgb A 32-bits ARGB color
	 * @return an integer between 0 and 255
	 */
	public static int getGreen(int rgb) {
		return (rgb >> 8) & 0xFF;
	}

	/**
	 * Returns blue component from given packed color.
	 * @param rgb A 32-bits ARGB color.
	 * @return an integer between 0 and 255.
	 */
	public static int getBlue(int rgb) {
		return rgb & 0xFF;
	}

	/**
	 * Returns alpha component from given packed color.
	 * @param argb A 32-bits ARGB color.
	 * @return an int between 0 and 255.
	 */
	public static int getAlpha(int argb) {
		return (argb >> 24) & 0xFF;
	}

	/**
	 * Checks whether a value is a 8-bit value, and corrects the value otherwise.
	 * @param someInt : The ?-bit value.
	 * @return a possible refactored value, if necessary.
	 */
	public static int validate8bit(int someInt) {
		if (someInt < 0)
			someInt = 0;
		else if (255 < someInt)
			someInt = 255;
		return someInt;
	}

	/**
	 * Floor a {@linkplain Vector}.
	 * @param vector A {@linkplain Vector} to floor.
	 * @return a new {@linkplain Vector} with floored x and y components.
	 */
	public static Vector floor(Vector vector) {
		return new Vector((float) Math.floor(vector.x), (float) Math.floor(vector.y));
	}
	
	/**
	 * Floor a {@linkplain Vector}, to the lower .5f
	 * @param vector A {@linkplain Vector} to floor.
	 * @return a new {@linkplain Vector} with x and y components floored to .5f.
	 */
	public static Vector halfFloor(Vector vector) {
		return new Vector((float) Math.floor(vector.x*2)/2f, (float) Math.floor(vector.y*2f)/2f);
	}

	/**
	 * Test whether a {@linkplain Vector} is between two other {@linkplain Vector}s.     *
	 * @param first The first {@linkplain Vector}.
	 * @param second The second {@linkplain Vector}.
	 * @param toTest The {@linkplain Vector} to test.
	 * @return whether the given {@linkplain Vector} is in the bounds.
	 */
	public static boolean isInRectangle(Vector first, Vector second, Vector toTest) {
		float minX = Math.min(first.x, second.x);
		float maxX = Math.max(first.x, second.x);
		float minY = Math.min(first.y, second.y);
		float maxY = Math.max(first.y, second.y);

		return (minX < toTest.x) && (maxX > toTest.x) && (minY < toTest.y) && (maxY > toTest.y);
	}

	/**
	 * Sort {@linkplain Vector}s by their x coordinate.
	 * @param points The {@linkplain ArrayList} of {@linkplain Vector}s.
	 * @return the sorted {@linkplain ArrayList}.
	 */
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

	/**
	 * Delete useless {@linkplain Vector} which have the same x coordinate.
	 * @param points The {@linkplain Vector}s to test.
	 * @return a {@linkplain ArrayList} with two {@linkplain Vector}s, the one with the smallest
	 * and the one with the biggest y coordinate.
	 */
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

	/**
	 * @param first The first {@linkplain Vector}.
	 * @param second The second {@linkplain Vector}.
     * @return the {@linkplain Rectangle} between them.
	 */
	public static Polygon createRectangle(Vector first, Vector second) {

		float width = Math.abs(first.x - second.x);
		float height = Math.abs(first.y - second.y);

		return new Polygon(0, 0, 0, height, width, height, width, 0);
	}

	/**
	 * @param width width of the {@linkplain Rectangle}.
	 * @param height height of the {@linkplain Rectangle}.
     * @return the {@linkplain Rectangle} between them.
	 */
	public static Polygon createRectangle(float width, float height) {
		return new Polygon(0, 0, 0, height, width, height, width, 0);
	}

    /**
     * Compute the unit direction {@linkplain Vector}.
     * @param first The first {@linkplain Vector}.
     * @param second The second {@linkplain Vector}.
     * @return the new unit direction {@linkplain Vector}.
     */
	public static Vector direction(Vector first, Vector second) {
		return new Vector(first.x - second.x, first.y - second.y).normalized();
	}
}

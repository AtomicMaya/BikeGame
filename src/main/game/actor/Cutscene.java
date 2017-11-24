package main.game.actor;

import org.jbox2d.common.Vec2;

/**
 * Created on 11/23/2017 at 7:32 PM.
 */
public class Cutscene {
	// Cutscene format :
	// #                                                line is ignored
	// polygon x1 y1 x1 y2 . . ... color strokeWidth    is a polygon
	// polygon x1 y1 x2 y2 . . ... filename             is a polygon filled with sprite
	// ellipse w h color strokeWidth                     is a ellipsoid of width w and height h
	// ellipse w h filename                              is an ellipsoid of width w and height h filled with sprite
	// break n                                          will pause the cutscene for n seconds
	// break key                                        will pause the cutscene until key pressed

	public Cutscene(String fileName) {

	}
}

class Test {
	public static void main(String[] args) {
		float longRadius = 5.f, shortRadius = 2.5f;
		int vertexCount = 32; // Magic value for quick modification
		Vec2[] vertices = new Vec2[vertexCount + 1];
		for (int i = 0; i < vertexCount; i++) {
			float angle = (float) (((Math.PI * 2) / vertexCount) * i);
			float radiusAtAngle = (float) ((longRadius * shortRadius) /
					Math.sqrt(Math.pow(longRadius * Math.sin(angle), 2) + Math.pow(shortRadius * Math.cos(angle), 2)));
			float xPos = (float) (radiusAtAngle * Math.cos(angle));
			float yPos = (float) (radiusAtAngle * Math.sin(angle));
			vertices[i] = new Vec2(xPos, yPos);
		}

		vertices[vertexCount] = vertices[0];
		int i = 0;
		for(Vec2 vec : vertices) {
			System.out.println((char) (65 + i) + " = (" + vec.x + ", " + vec.y + ")");
			i++;
		}
	}
}
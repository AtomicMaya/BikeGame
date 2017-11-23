package main.game.actor;

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
	}
}
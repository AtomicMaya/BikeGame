package main.game.actor;

import main.math.Vector;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created on 11/23/2017 at 7:32 PM.
 */
public class Cutscene {
	// Cutscene format :
	// #                                                line is ignored
	// polygon x1 y1 x1 y2 . . ... color strokeWidth alpha etc...    is a polygon
	// polygon x1 y1 x2 y2 . . ... filename             is a polygon filled with sprite
	// line x1 y1 x2 y3 . . ... color strokewidth       is a line with color
	// ellipse w h color strokeWidth                    is a ellipsoid of width w and height h
	// ellipse w h filename                             is an ellipsoid of width w and height h filled with sprite
	// break n                                          will pause the cutscene for n seconds
	// break key                                        will pause the cutscene until key pressed
	// button                                           next line is a button polygon
	// text x y size "some text"                             text, x y at upper right of first letter

	private ArrayList<ArrayList<String>> cutsceneContents = new ArrayList<>();
	private ArrayList<Actor> actors = new ArrayList<>();

	public Cutscene(String fileName) throws IOException {   // Handle externally for optimum smootheness
		FileReader fileReader = new FileReader(fileName);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			ArrayList<String> splittedLine = new ArrayList<>();
			Collections.addAll(splittedLine, line.split(" "));
			cutsceneContents.add(splittedLine);
		}
		bufferedReader.close();

		decode();

	}

	private ArrayList<Vector> convertToVectors(ArrayList<Float> coordinates) {
		if (coordinates.size() % 2 != 0) return new ArrayList<>(); // Verify that there is an even number if coordinates
		ArrayList<Vector> vectors = new ArrayList<>();
		for(int i = 0; i < coordinates.size() - 1; i += 2) vectors.add(new Vector(coordinates.get(i), coordinates.get(i + 1)));
		return vectors;
	}

	private void decode() {
		for (ArrayList<String> actor : cutsceneContents) {
			ArrayList<Float> coordinates = new ArrayList<>();
			switch (actor.get(0)){
				case "line":
					String color = "";
					float strokeWidth = 0;
					for(int i = 1; i < actor.size(); i++) {
						if (i == actor.size() - 2) color = actor.get(i);
						else if (i == actor.size() - 1) strokeWidth = Float.parseFloat(actor.get(i));
						else coordinates.add(Float.parseFloat(actor.get(i)));
					}
					System.out.println(convertToVectors(coordinates) + " " + color + " " + strokeWidth);
					System.out.println("Was a line !");
					break;
				case "polygon":
					System.out.println("Was a Polygon !");
					break;
				case "text":
					System.out.println("Was a Text !");
					break;
				case "ellipse":
					break;
				default:
					break;
			}
		}
	}
}

class Test {
	public static void main(String[] args) {
		try {
			Cutscene a = new Cutscene("./res/cutscene/level1-1.cts");
		} catch (IOException e) {
			System.out.println("Next Scene Jump");
		}
	}
}
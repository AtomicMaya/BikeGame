package main.game.actor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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


	public Cutscene(String fileName) throws IOException {   // Handle externally for optimum smootheness
		FileReader fileReader = new FileReader(fileName);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		List<ArrayList<String>> lines = new ArrayList<>();
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			ArrayList<String> splittedLine = new ArrayList<>();
			for(String s : line.split(" ")) {
				splittedLine.add(s);
			}
			lines.add(splittedLine);
		}
		bufferedReader.close();

		System.out.println(lines);

	}
}

class Test {
	public static void main(String[] args) {
		try {
			Cutscene a = new Cutscene("");
		} catch (IOException e) {
			System.out.println("Next Scene Jump");
		}
	}
}
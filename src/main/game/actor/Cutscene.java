package main.game.actor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import main.game.ActorGame;


public class Cutscene extends ActorGame {
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
		String line = null;
		while ((line = bufferedReader.readLine()) != null) {
			ArrayList<String> splittedLine = new ArrayList<>();
			for(String s : line.split(" ")) {
				splittedLine.add(s);
			}
			cutsceneContents.add(splittedLine);
		}
		bufferedReader.close();

		decode();

	}

	private void decode() {
		for (ArrayList<String> actor : cutsceneContents) {
			switch (actor.get(0)){
				case "line":
					ArrayList<Integer> coordinates = new ArrayList<>();
					String color = "";
					int strokeWidth = 0;

					for(int i = 1; i < actor.size(); i++) {
						if (i == actor.size() - 2) color = actor.get(i);
						else if (i == actor.size() - 1) strokeWidth = Integer.parseInt(actor.get(i));
						else coordinates.add(Integer.parseInt(actor.get(i)));
					}
					System.out.println(coordinates);
					System.out.println(color + " " + strokeWidth);
					System.out.println("Was a line !");
					break;
				case "polygon":
					System.out.println("Was a Polygon !");
					break;
				case "text":
					System.out.println("Was a Text !");
					break;
				default:
					break;
			}
		}
	}
}

class Test {
	static int counter = 0;
	static Timer timer;

	public static void main(String[] args) {

		//create timer task to increment counter
		TimerTask timerTask = new TimerTask() {

			@Override
			public void run() {
				// System.out.println("TimerTask executing counter is: " + counter);
				counter++;
			}
		};

		//create thread to print counter value
		Thread t = new Thread(() -> {
			while (true) {
				try {
					System.out.println("Thread reading counter is: " + counter);
					if (counter == 3) {
						System.out.println("Counter has reached 3 now will terminate");
						timer.cancel();//end the timer
						break;//end this loop
					}
					Thread.sleep(1000);
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}
			}
		});

		timer = new Timer("MyTimer");//create a new timer
		timer.scheduleAtFixedRate(timerTask, 30, 3000);//start timer in 30ms to increment  counter

		t.start();//start thread to display counter
	}
}
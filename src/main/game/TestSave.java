/**
 *	Author: Clément Jeannet
 *	Date: 	1 déc. 2017
 */
package main.game;

import main.game.actor.Actor;
import main.io.FileSystem;
import main.window.Window;

public class TestSave extends ActorGame {
	Actor crate;
	FileSystem fileSystem;

	boolean saved = false;
	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);
		this.fileSystem = fileSystem;
//		crate = new Crate(this, new Vector(0, 5), "res/images/box.4.png", false, 1);
//
//		Polyline p = new Polyline(-1000.0f, -1000.0f, -1000.0f, 0.0f, 0.0f, 0.0f, 3.0f, 1.0f, 8.0f, 1.0f, 15.0f, 3.0f,
//				16.0f, 3.0f, 25.0f, 0.0f, 35.0f, -5.0f, 50.0f, -5.0f, 55.0f, -4.0f, 65.0f, 0.0f, 6500.0f, -1000.0f);
//		Ground ground = new Ground(this, new Vector(0, -3), p);
//		
//		
//		Bike b = new Bike(this, new Vector(4,5));
//		addActor(crate);
//		
//
//		addActor(b);
//		addActor(ground);
		//super.save("res/saves/");
		super.load("save1");
		//setViewCandidate(b);
		return true;
	}
	
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (!saved) {
			super.save("save1");
			saved = true;
		}
		
	}
}

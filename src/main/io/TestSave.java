/**
 *	Author: Clément Jeannet
 *	Date: 	1 déc. 2017
 */
package main.io;

import java.io.File;
import java.io.IOException;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.Save;
import main.game.actor.crate.Crate;
import main.game.actor.entities.Bike;
import main.game.actor.entities.Ground;
import main.math.Polyline;
import main.math.Vector;
import main.window.Window;

public class TestSave extends ActorGame {
	Actor crate;
	FileSystem fileSystem;

	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);
		this.fileSystem = fileSystem;
		crate = new Crate(this, new Vector(0, 5), "res/images/box.4.png", false, 1);

		Polyline p = new Polyline(-1000.0f, -1000.0f, -1000.0f, 0.0f, 0.0f, 0.0f, 3.0f, 1.0f, 8.0f, 1.0f, 15.0f, 3.0f,
				16.0f, 3.0f, 25.0f, 0.0f, 35.0f, -5.0f, 50.0f, -5.0f, 55.0f, -4.0f, 65.0f, 0.0f, 6500.0f, -1000.0f);

		Ground ground = new Ground(this, new Vector(0, -3), p);
		
		
		Bike b = new Bike(this, new Vector(4,5));
		addActor(crate);
		addActor(ground);
		try {
			Save.newSave(ground, new File("res/saves/crate.object"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Actor o = Save.readSavedActor(this, new File("res/saves/crate.object"));
		destroyActor(ground);
		addActor(o);


		return true;
	}
	
	public Actor rebuild(Object o) {
		Actor a = (Actor)o;
		a.reCreate(this);
		return a;
	}
}

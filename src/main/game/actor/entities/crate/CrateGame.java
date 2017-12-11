/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.entities.crate;

import main.game.ActorGame;
import main.io.FileSystem;
import main.math.Vector;
import main.window.Window;

/** Creates a new {@linkplain CrateGame} as asked in Part 4.5. */
public class CrateGame extends ActorGame {
	@Override
	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);
		this.addActor(new Crate(this, new Vector(0, 5), "res/images/box.4.png", false, 1));
		this.addActor(new Crate(this, new Vector(0.2f, 7), "res/images/box.4.png", false, 1));
		this.addActor(new Crate(this, new Vector(2, 6), "res/images/box.4.png", false, 1));
		return true;
	}
}

/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.crate;

import main.game.actor.ActorGame;
import main.io.FileSystem;
import main.math.Vector;
import main.window.Window;

/**
 * Part 4.5, Test de l'architecture: CrateGame
 */
public class CrateGame extends ActorGame {

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);
		addActor(new Crate(this, new Vector(0, 5), "res/box.4.png", false, 1));
		addActor(new Crate(this, new Vector(0.2f, 7), "res/box.4.png", false, 1));
		addActor(new Crate(this, new Vector(2, 6), "res/box.4.png", false, 1));
		return true;

	}
}

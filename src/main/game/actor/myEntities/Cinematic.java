/**
 *	Author: Clément Jeannet
 *	Date: 	27 nov. 2017
 */
package main.game.actor.myEntities;

import main.game.actor.ActorGame;
import main.io.FileSystem;
import main.window.Window;

public class Cinematic extends ActorGame {
	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);

		// TODO creation objects du program
		
		return true;
	}
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}
}

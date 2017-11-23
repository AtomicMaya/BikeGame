/**
 *	Author: Clément Jeannet
 *	Date: 	23 nov. 2017
 */
package main.game.actor;

import main.io.FileSystem;
import main.window.Window;

public class Level extends ActorGame {

	boolean ended = false;

	private Window window;
	private FileSystem fileSystem;
	private String file;
	private int levelNumber = 0;

	public boolean begin(Window window, FileSystem fileSystem, String file) {
		super.begin(window, fileSystem);
		this.window = window;
		this.fileSystem = fileSystem;
		this.file = file;
		levelNumber++;

		create(file);
		return true;
	}

	public void create(String filePath) {

		// Reade the file

		// create the level using the data in the file

	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		if (ended)
			begin(window, fileSystem, file);
	}

}

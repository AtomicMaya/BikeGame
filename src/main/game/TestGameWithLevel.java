/**
 *	Author: Clément Jeannet
 *	Date: 	27 nov. 2017
 */
package main.game;

import main.game.levels.Level;
import main.game.levels.Level1;
import main.io.FileSystem;
import main.window.Window;

import java.util.ArrayList;
import java.util.List;

public class TestGameWithLevel extends GameWithLevels {

	public boolean begin(Window window, FileSystem fileSystem) {
		return super.begin(window, fileSystem);
	}

	@Override
	protected List<Level> createLevelList() {

		List<Level> levels = new ArrayList<>();
        //levels.add(new CinematicLevel1(this));
		levels.add(new Level1(this));
		levels.add(new Level1(this));

		return levels;
	}

}

/**
 *	Author: Clément Jeannet
 *	Date: 	27 nov. 2017
 */
package main.game;

import main.game.levels.Level;
import main.game.levels.Level0;
import main.io.FileSystem;
import main.window.Window;

import java.util.ArrayList;
import java.util.List;

public class TestGameWithLevel extends GameWithLevels {

	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);
		this.beginLevel(0);
		return true;
	}

	@Override
	protected List<Level> createLevelList() {

		List<Level> levels = new ArrayList<>();
		// levels.add(new CinematicLevel1(this));
		levels.add(new Level0(this));
		levels.add(new Level0(this));

		return levels;
	}

}

/**
 *	Author: Clément Jeannet
 *	Date: 	27 nov. 2017
 */
package main;

import java.util.ArrayList;
import java.util.List;

import main.game.actor.GameWithLevels;
import main.game.levels.Level;
import main.game.levels.Level1;
import main.game.levels.Level2;
import main.game.levels.LevelVide;
import main.io.FileSystem;
import main.window.Window;

public class TestGameWithLevel extends GameWithLevels {

	public boolean begin(Window window, FileSystem fileSystem) {
		return super.begin(window, fileSystem);
	}

	@Override
	protected List<Level> createLevelList() {

		List<Level> levels = new ArrayList<Level>();
		levels.add(new Level1(this));
		levels.add(new Level2(this));
		levels.add(new Level1(this));
		levels.add(new LevelVide(this));

		return levels;
	}

}

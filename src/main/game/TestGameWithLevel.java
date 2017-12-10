/**
 *	Author: Clément Jeannet
 *	Date: 	27 nov. 2017
 */
package main.game;

import main.game.levels.PlayableLevel;
import main.game.levels.PlayableLevel1;
import main.io.FileSystem;
import main.window.Window;

import java.util.ArrayList;
import java.util.List;

public class TestGameWithLevel extends GameWithLevels {

	public boolean begin(Window window, FileSystem fileSystem) {
		return super.begin(window, fileSystem);
	}

	@Override
	protected List<PlayableLevel> createLevelList() {

		List<PlayableLevel> playableLevels = new ArrayList<>();
        //playableLevels.add(new CinematicLevel1(this));
		playableLevels.add(new PlayableLevel1(this));
		playableLevels.add(new PlayableLevel1(this));

		return playableLevels;
	}

}

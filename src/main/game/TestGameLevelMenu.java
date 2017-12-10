/**
 *	Author: Clément Jeannet
 *	Date: 	3 déc. 2017
 */
package main.game;

import main.game.levels.PlayableLevel;
import main.game.levels.PlayableLevel1;
import main.io.FileSystem;
import main.window.Window;
import main.window.swing.FontList;
import main.window.swing.MyFont;

import java.util.Arrays;
import java.util.List;

public class TestGameLevelMenu extends GameWithLevelAndMenu {

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);

		MyFont.setFont(FontList.BELL_MT);
		return true;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}

	@Override
	protected List<PlayableLevel> createLevelList() {
		return Arrays.asList(new PlayableLevel1(this), new PlayableLevel1(this));
	}

}

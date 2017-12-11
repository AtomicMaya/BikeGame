/**
 *	Author: Clément Jeannet
 *	Date: 	3 déc. 2017
 */
package main.game;

import java.util.Arrays;
import java.util.List;

import main.game.levels.Level;
import main.game.levels.Level0;
import main.game.levels.Level2;
import main.game.levels.LevelTest;
import main.io.FileSystem;
import main.window.Window;
import main.window.swing.FontList;
import main.window.swing.MyFont;

public class TestGameLevelMenu extends ComplexBikeGame {

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
	protected List<Level> createLevelList() {

		return Arrays.asList(new Level0(this), new LevelTest(this), new Level2(this));
	}

}

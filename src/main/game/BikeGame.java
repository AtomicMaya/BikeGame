/**
 *	Author: Clément Jeannet
 *	Date: 	3 déc. 2017
 */
package main.game;

import main.game.levels.*;
import main.io.FileSystem;
import main.window.Window;

import java.util.Arrays;
import java.util.List;

/** Create an instance of {@linkplain ComplexBikeGame}. */
public class BikeGame extends ComplexBikeGame {

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);
		return true;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}

	@Override
	protected List<Level> createLevelList() {

		return Arrays.asList(new Level0(this), new Level1(this), new Level2(this), new Level3(this));
	}

}

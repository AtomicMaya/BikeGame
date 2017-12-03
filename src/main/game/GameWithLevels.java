/**
 *	Author: Clément Jeannet
 *	Date: 	27 nov. 2017
 */
package main.game;

import java.util.List;

import main.game.levels.Level;
import main.io.FileSystem;
import main.window.Window;

public abstract class GameWithLevels extends ActorGame {

	private List<Level> levels;
	private int currentLevel = 0;

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);

		levels = createLevelList();
		return true;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (levels != null && !levels.isEmpty() && levels.get(currentLevel).isFinished()) {
			nextLevel();
		}
	}

	protected void nextLevel() {
		clearCurrentLevel();
		beginLevel(currentLevel + 1);
	}

	protected void clearCurrentLevel() {
		super.destroyAllActors();
	}

	protected void beginLevel(int i) {
		currentLevel = i;
		if (currentLevel > levels.size())
			currentLevel = 0;
		super.addActor(levels.get(i));
		levels.get(i).createAllActors();
		super.addActor(levels.get(i).getActors());
		super.setViewCandidate(levels.get(i).getViewCandidate());
	}

	protected abstract List<Level> createLevelList();
}

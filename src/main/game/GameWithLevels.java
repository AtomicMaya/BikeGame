/**
 *	Author: Clément Jeannet
 *	Date: 	27 nov. 2017
 */
package main.game;

import java.awt.event.KeyEvent;
import java.util.List;

import main.game.levels.Level;
import main.io.FileSystem;
import main.window.Window;

/** Represent an {@linkplain ActorGame} with {@linkplain Level}s*/
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
		if (getKeyboard().get(KeyEvent.VK_R).isPressed()) {
			resetLevel();
		}
		if (levels != null && !levels.isEmpty() && levels.get(currentLevel).isFinished()) {
			nextLevel();
		}
	}

	/** Go to the next {@linkplain Level} */
	protected void nextLevel() {
		beginLevel(currentLevel + 1);
	}

	/** Reset the current {@linkplain Level} */
	protected void resetLevel() {
		clearCurrentLevel();
		beginLevel(currentLevel);
	}

	/** Clear all {@linkplain Actor} in the current {@linkplain Level} */
	protected void clearCurrentLevel() {
		super.destroyAllActors();
	}

	/**
	 * Begin a {@linkplain Level}
	 * @param i : Number in the {@linkplain List} of the {@linkplain Level} to
	 * start
	 */
	public void beginLevel(int i) {
		clearCurrentLevel();
		currentLevel = i;
		if (currentLevel > levels.size() - 1)
			currentLevel = 0;
		super.addActor(levels.get(currentLevel));
		this.levels.get(currentLevel).createAllActors();
		super.addActor(levels.get(currentLevel).getActors());
		super.setViewCandidate(levels.get(currentLevel).getViewCandidate());
		super.setPayload(levels.get(currentLevel).getPayload());
	}

	/**
	 * Create all the {@linkplain Level} for this {@linkplain GameWithLevels}
	 */
	protected abstract List<Level> createLevelList();

	/**
	 * @return the number of {@linkplain Level} in this
	 * {@linkplain GameWithLevels}
	 */
	public int numberOfLevel() {
		return levels.size();
	}
}

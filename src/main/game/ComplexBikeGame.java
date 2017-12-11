package main.game;

import main.game.GUI.menu.InGameMenu;
import main.game.GUI.menu.MainMenu;
import main.game.actor.Actor;
import main.game.actor.sensors.SpawnCheckpoint;
import main.game.levels.Level;
import main.io.FileSystem;
import main.window.Window;

import java.util.List;

/**
 * Represent a game with different level, and some menus
 * 
 * @see ActorGame
 */
public abstract class ComplexBikeGame extends ActorGame {

	private List<Level> levels;
	private int currentLevel = 0;
	private boolean wasPlayed = false;

	private InGameMenu ingameMenu;
	private MainMenu mainMenu;

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);

		this.levels = createLevelList();
		this.ingameMenu = new InGameMenu(this, window);

		this.mainMenu = new MainMenu(this, window);

		return true;
	}

	@Override
	public void update(float deltaTime) {
		// System.out.println(this.levels.get(currentLevel));
		super.update(deltaTime);
		mainMenu.update(deltaTime, 1);
		if (mainMenu.isOpen()) {
			mainMenu.draw(getCanvas());
			return;
		}
		ingameMenu.update(deltaTime, 1);
		if (ingameMenu.isOpen())
			ingameMenu.draw(getCanvas());

	}

	/** Go to the MainMenu */
	public void goToMainMenu() {
		this.destroyAllActors();
		mainMenu.setStatus(true);
		ingameMenu.setStatus(false);
	}

	/** Go to the next {@linkplain Level} */
	public void nextLevel() {
		wasPlayed = false;
		beginLevel(currentLevel + 1);
	}

	/**
	 * Reset the current {@linkplain Level}
	 * @param wasPlayed whether its a respawn
	 */
	public void resetLevel() {
		this.wasPlayed = true;
		this.beginLevel(currentLevel);
	}

	/** Clear all {@linkplain Actor} in the current {@linkplain Level} */
	private void clearCurrentLevel() {
		if (levels.get(currentLevel).loaded)
			this.levels.get(currentLevel).dispose();
		super.destroyAllActors();

	}

	/**
	 * Begin a {@linkplain Level}
	 * @param i : Number in the {@linkplain List} of the {@linkplain Level} to
	 * start
	 */
	public void beginLevel(int i) {
		this.getGameManager().setGameState(this);
		this.clearCurrentLevel();
		this.currentLevel = i;
		if (this.currentLevel > this.levels.size() - 1)
			this.currentLevel = 0;

		System.out.println("loading level");
		this.levels.get(currentLevel).createAllActors();
		super.addActor(levels.get(currentLevel).getActors());
		super.addActor(levels.get(currentLevel));

		SpawnCheckpoint sc = levels.get(currentLevel).getSpawnCheckpoint();
		if (!wasPlayed) {
			// if first time level is loaded,
			if (sc == null && (levels.get(currentLevel).getViewCandidate() == null)
					&& (levels.get(currentLevel).getPayload() == null))
				throw new NullPointerException("No PlayableEntity detected");
		}
		super.addActor(sc);

		// in case they are @Override
		if (levels.get(currentLevel).getViewCandidate() != null)
			super.setViewCandidate(levels.get(currentLevel).getViewCandidate());
		if (levels.get(currentLevel).getPayload() != null)
			super.setPayload(levels.get(currentLevel).getPayload());

	}

	/**
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

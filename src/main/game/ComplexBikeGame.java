package main.game;

import main.game.GUI.menu.MainMenu;
import main.game.GUI.menu.PauseMenu;
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

	private PauseMenu ingameMenu;
	private MainMenu mainMenu;

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);

		this.levels = createLevelList();
		this.ingameMenu = new PauseMenu(this, window);

		this.mainMenu = new MainMenu(this, window);
        //new Audio("./res/audio/audio_compilation.wav", -1, 5);
		return true;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		mainMenu.update(deltaTime, 1);
		if (mainMenu.isOpen()) {
			mainMenu.draw(getCanvas());
			return;
		}
		if (getPayload() != null && (getPayload().getDeathStatus() || getPayload().getVictoryStatus())) {
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
	 * Reset the current {@linkplain Level}.
	 */
	public void resetLevel() {
		this.wasPlayed = true;
		this.beginLevel(currentLevel);
	}

	/** Clear all {@linkplain Actor} in the current {@linkplain Level} */
	private void clearCurrentLevel() {
        try { 
        	this.levels.get(currentLevel).dispose();
        } catch ( NullPointerException ignored) { ignored.printStackTrace(); }
		if (levels.get(currentLevel).loaded) {
        }
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

		this.levels.get(currentLevel).createAllActors();
		super.addActor(levels.get(currentLevel).getActors());

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
     * @return the new {@linkplain List} of {@linkplain Level}s in the game.
	 */
	protected abstract List<Level> createLevelList();

	/**
	 * @return the number of {@linkplain Level}s in this
	 * {@linkplain ComplexBikeGame}.
	 */
	public int numberOfLevel() {
		return levels.size();
	}
}

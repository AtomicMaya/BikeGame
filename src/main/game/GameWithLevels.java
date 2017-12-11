/**
 *	Author: Clément Jeannet
 *	Date: 	27 nov. 2017
 */
package main.game;

import main.game.graphics.TextGraphics;
import main.game.levels.Level;
import main.io.FileSystem;
import main.math.Transform;
import main.math.Vector;
import main.window.Window;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

/** Represent an {@linkplain ActorGame} with {@linkplain Level}s */
public abstract class GameWithLevels extends ActorGame {

	private List<Level> levels;
	private int currentLevel = 0;

	private float timerMessage = 0;

	private final float timeBeforeMessage1 = 2f;
	private final String messageNextLevelText = "Press N to go to the next level";
	private final String messageRestartLevelText = "Press R to restart the level";
	private TextGraphics message1;
	private float fontSize = 1.5f;

	private boolean display = false;

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);

		this.message1 = new TextGraphics(this.messageNextLevelText, this.fontSize, new Color(66, 241, 244), Color.RED.darker(), .02f,
				false, false, Vector.ZERO, 1, 1337);
        this.message1.setAnchor(new Vector(.5f, -2.5f));
        this.levels = createLevelList();
		return true;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		if (this.isGameFrozen()) {
            this.message1.setText("");
            this.display = false;
            this.timerMessage = 0;
		}
		if (this.levels != null && !this.levels.isEmpty() && this.levels.get(this.currentLevel).isFinished()) {
			if (getKeyboard().get(KeyEvent.VK_R).isPressed()) {
				resetLevel();
			}

            this.timerMessage += deltaTime;
			if (this.timerMessage > this.timeBeforeMessage1 & !this.display) {
                this.display = true;

				if (getPayload().getVictoryStatus()) {
                    this.message1.setText(this.messageNextLevelText);
                    this.message1.setFillColor(Color.BLUE);
                    this.message1.setOutlineColor(null);
				} else if (getPayload().getDeathStatus()) {
                    this.message1.setFillColor(new Color(66, 241, 244));
                    this.message1.setOutlineColor(new Color(155, 18, 48));
                    this.message1.setText(this.messageRestartLevelText);
				}

			}
			if (this.display) {
				Vector position = getCanvas().getPosition();
                this.message1.setRelativeTransform(Transform.I.translated(position));
                this.message1.draw(getCanvas());
			}
			if (getPayload().getVictoryStatus()) {
				if (getKeyboard().get(KeyEvent.VK_N).isPressed())
					nextLevel();
			} else if (getPayload().getDeathStatus()) {
				if (getKeyboard().get(KeyEvent.VK_N).isPressed())
					resetLevel();
			}

		}
	}

	/** Go to the next {@linkplain Level} */
	protected void nextLevel() {
		beginLevel(this.currentLevel + 1);
	}

	/** Reset the current {@linkplain Level} */
	protected void resetLevel() {
		clearCurrentLevel();
		beginLevel(this.currentLevel);
	}

	/** Clear all {@linkplain main.game.actor.Actor} in the current {@linkplain Level} */
	protected void clearCurrentLevel() {
        if(this.levels.size() > 0) this.levels.get(this.currentLevel).dispose();
		super.destroyAllActors();
        this.message1.setText("");
        this.display = false;
	}

	/**
	 * Begin a {@linkplain Level}
	 * @param levelIndex : Index in the {@linkplain List} of the {@linkplain Level} to start.
	 */
	public void beginLevel(int levelIndex) {
        this.clearCurrentLevel();
        this.currentLevel = levelIndex;
		if (this.currentLevel > this.levels.size() - 1)
            this.currentLevel = 0;
		super.addActor(this.levels.get(this.currentLevel));
		this.levels.get(this.currentLevel).createAllActors();
		super.addActor(this.levels.get(this.currentLevel).getActors());
		super.setViewCandidate(this.levels.get(this.currentLevel).getViewCandidate());
		super.setPayload(this.levels.get(this.currentLevel).getPayload());
	}

	/**
	 * Create all the {@linkplain Level} for this {@linkplain GameWithLevels}
	 */
	protected abstract List<Level> createLevelList();

	/**
	 * @return the number of {@linkplain Level} in this {@linkplain GameWithLevels}
	 */
	public int numberOfLevel() {
		return levels.size();
	}
}

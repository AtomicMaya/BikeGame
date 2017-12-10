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

		message1 = new TextGraphics(messageNextLevelText, fontSize, new Color(66, 241, 244), Color.RED.darker(), .02f,
				false, false, Vector.ZERO, 1, 1337);
		message1.setAnchor(new Vector(.5f, -2.5f));
		levels = createLevelList();
		return true;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		if (this.isGameFrozen()) {
			message1.setText("");
			display = false;
			timerMessage = 0;
		}
		if (levels != null && !levels.isEmpty() && levels.get(currentLevel).isFinished()) {
			if (getKeyboard().get(KeyEvent.VK_R).isPressed()) {
				resetLevel();
			}

			timerMessage += deltaTime;
			if (timerMessage > timeBeforeMessage1 & !display) {
				display = true;

				if (getPayload().getVictoryStatus()) {
					message1.setText(messageNextLevelText);
					message1.setFillColor(Color.BLUE);
					message1.setOutlineColor(null);
				} else if (getPayload().getDeathStatus()) {
					message1.setFillColor(new Color(66, 241, 244));
					message1.setOutlineColor(new Color(155, 18, 48));
					message1.setText(messageRestartLevelText);
				}

			}
			if (display) {
				Vector position = getCanvas().getPosition();
				message1.setRelativeTransform(Transform.I.translated(position));
				message1.draw(getCanvas());
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
		message1.setText("");
		display = false;
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

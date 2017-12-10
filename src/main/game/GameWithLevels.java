/**
 *	Author: Clément Jeannet
 *	Date: 	27 nov. 2017
 */
package main.game;

import main.game.graphics.TextGraphics;
import main.game.levels.PlayableLevel;
import main.io.FileSystem;
import main.math.Transform;
import main.math.Vector;
import main.window.Window;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

/** Represent an {@linkplain ActorGame} with {@linkplain PlayableLevel}s */
public abstract class GameWithLevels extends ActorGame {

	private List<PlayableLevel> playableLevels;
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
		playableLevels = createLevelList();
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
		if (playableLevels != null && !playableLevels.isEmpty() && playableLevels.get(currentLevel).isFinished()) {
			if (getKeyboard().get(KeyEvent.VK_R).isPressed()) {
				resetLevel();
			}

			timerMessage += deltaTime;
			if (timerMessage > timeBeforeMessage1 & !display) {
				display = true;
				Vector position = getCanvas().getPosition();// .add(new
															// Vector(.5f, 0));
				message1.setRelativeTransform(Transform.I.translated(position));

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
			if (display)
				message1.draw(getCanvas());
			if (getPayload().getVictoryStatus()) {
				if (getKeyboard().get(KeyEvent.VK_N).isPressed())
					nextLevel();
			} else if (getPayload().getDeathStatus()) {
				if (getKeyboard().get(KeyEvent.VK_N).isPressed())
					resetLevel();
			}

		}
	}

	/** Go to the next {@linkplain PlayableLevel} */
	protected void nextLevel() {
		beginLevel(currentLevel + 1);
	}

	/** Reset the current {@linkplain PlayableLevel} */
	protected void resetLevel() {
		clearCurrentLevel();
		beginLevel(currentLevel);
	}

	/** Clear all {@linkplain Actor} in the current {@linkplain PlayableLevel} */
	protected void clearCurrentLevel() {
		super.destroyAllActors();
		message1.setText("");
		display = false;
	}

	/**
	 * Begin a {@linkplain PlayableLevel}
	 * @param i : Number in the {@linkplain List} of the {@linkplain PlayableLevel} to
	 * start
	 */
	public void beginLevel(int i) {
		clearCurrentLevel();
		currentLevel = i;
		if (currentLevel > playableLevels.size() - 1)
			currentLevel = 0;
		super.addActor(playableLevels.get(currentLevel));
		this.playableLevels.get(currentLevel).createAllActors();
		super.addActor(playableLevels.get(currentLevel).getActors());
		super.setViewCandidate(playableLevels.get(currentLevel).getViewCandidate());
		super.setPayload(playableLevels.get(currentLevel).getPayload());
	}

	/**
	 * Create all the {@linkplain PlayableLevel} for this {@linkplain GameWithLevels}
	 */
	protected abstract List<PlayableLevel> createLevelList();

	/**
	 * @return the number of {@linkplain PlayableLevel} in this
	 * {@linkplain GameWithLevels}
	 */
	public int numberOfLevel() {
		return playableLevels.size();
	}
}

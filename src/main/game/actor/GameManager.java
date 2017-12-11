/**
 *	Author: Clément Jeannet
 *	Date: 	10 déc. 2017
 */
package main.game.actor;

import main.game.ActorGame;
import main.game.ComplexBikeGame;
import main.game.GUI.menu.LevelEditor;
import main.game.actor.entities.Bike;
import main.game.actor.sensors.Checkpoint;
import main.game.actor.sensors.StartCheckpoint;
import main.game.graphics.BetterTextGraphics;
import main.math.ExtendedMath;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameManager {

	private static final int levelState = 0;
	private static final int saveState = 1;
	private static final int levelEditorState = 2;

	private ActorGame game;
	private ComplexBikeGame gameLevel;
	private LevelEditor levelEditor;

	// score management
	private int score = 0;
	private BetterTextGraphics scoreDisapplay;
	private final String scoreText = "Your score is :";
	private Vector scorePos = new Vector(-19, -9.5f);

	// level management
	private final String messageNextLevelText = "Press N to go to the next level";
	private final String respawnText = "Press R to respawn";
	private final String backToMenu = "Press ESCAPE to go back to the menu";
	private String messageDisplayed = "";
	private float fontSize = 1.5f;

	// respawn mechanics
	private Vector position;
	private Checkpoint lastCheckpoint;
	private StartCheckpoint startCheckpoint;
	private final float timeToRespawn = 3f;
	private float respawnTimer = 0;

	// temp save management
	private String saveLoaded;

	private int gameState = -1;

	public GameManager(ActorGame game) {
		this.game = game;
		scoreDisapplay = new BetterTextGraphics(game, scoreText + score, 1f, scorePos);
		scoreDisapplay.setParent(game.getCanvas());
		scoreDisapplay.setDepth(42042);
	}

	public void update(float deltaTime) {
		if (game.getPayload() != null && game.getKeyboard().get(KeyEvent.VK_9).isPressed())
			System.out.println(game.getPayload().getDeathStatus() + " " + game.getPayload().getVictoryStatus());
		// spawn a bike if payload is not defind, which happend at the start of
		// a game or after a reset
		if (game.getPayload() == null && (startCheckpoint != null)) {
			spawnBike();
			// if bike is spawned, ckeck for bike death/win status in order to
			// respawn one if needed
		} else if (game.getPayload() != null)
			// victory stronger that death

			if (game.getPayload().getVictoryStatus()) {

				this.respawnTimer += deltaTime;

				if (this.respawnTimer > this.timeToRespawn) {
					messageDisplayed = messageNextLevelText;
					switch (gameState) {
						case levelState: {
							if (game.getKeyboard().get(KeyEvent.VK_N).isPressed()) {
								restart();
								gameLevel.nextLevel();
								respawnTimer = 0;
								messageDisplayed = "";
							}
							break;
						}
						case saveState: {

							if (game.getKeyboard().get(KeyEvent.VK_ESCAPE).isPressed()) {
								// TODO
								restart();
								gameLevel.goToMainMenu();
								respawnTimer = 0;
								messageDisplayed = "";
							}
							messageDisplayed = backToMenu;
							break;
						}
						case levelEditorState: {
							game.setGameFreezeStatus(false);
							messageDisplayed = "";
							break;
						}
					}
				}
			} else if (game.getPayload().getDeathStatus()) {
				this.respawnTimer += deltaTime;
				messageDisplayed = respawnText;
				if (this.respawnTimer > this.timeToRespawn && game.getKeyboard().get(KeyEvent.VK_R).isPressed()) {

					reset();
					switch (gameState) {
						case levelState:
							gameLevel.resetLevel();
						break;
						case saveState:
							game.load(saveLoaded);

						break;
						case levelEditorState:
						break;
					}
					messageDisplayed = "";
					respawnTimer = 0;
				}
			}

	}

	public void draw(Canvas canvas) {
		if (this.respawnTimer > this.timeToRespawn)
			canvas.drawText(messageDisplayed, fontSize * game.getViewScale() / 20f,
					Transform.I.translated(canvas.getPosition()), new Color(66, 241, 244), new Color(155, 18, 48), .02f,
					false, false, new Vector(.5f, -2.5f * game.getViewScale() / 20f), 1, 42000);
		if (!game.isGameFrozen()) {
			scoreDisapplay.setText(scoreText + score);
			scoreDisapplay.draw(canvas);
			canvas.drawShape(ExtendedMath.createRectangle(scoreDisapplay.getTotalWidth(), scoreDisapplay.getCharSize()),
					Transform.I.translated(scorePos).translated(game.getCanvas().getPosition()),
					new Color(220, 220, 220), new Color(200, 200, 200), .2f, .8f, 42000);
		}
	}

	// respawn management
	/**
	 * Give to this {@linkplain GameManager} the last triggered
	 * {@linkplain Checkpoint} by the payload
	 */
	public void setLastCheckpoint(Checkpoint checkpoint) {
		if (lastCheckpoint != null)
			lastCheckpoint.setTriggerStatus(false);
		lastCheckpoint = checkpoint;
	}

	public void setStartCheckpoint(StartCheckpoint checkpoint) {
		startCheckpoint = checkpoint;
	}

	/** Called at the start of a new level */
	private void restart() {
		startCheckpoint = null;
		lastCheckpoint = null;
		reset();
	}

	/** Called at the reset of a level */
	private void reset() {
		game.destroyAllActors();
		game.getPayload().destroy();
		game.setPayload(null);
		game.setViewCandidate(null);
		messageDisplayed = "";
		respawnTimer = 0;
	}

	private void spawnBike() {
		Bike nextPlayer = new Bike(game,
				(lastCheckpoint == null ? startCheckpoint : lastCheckpoint).getPosition().add(0, 2));
		game.addActor(nextPlayer);
		game.setPayload(nextPlayer);
		game.setViewCandidate(nextPlayer);

	}

	// score management
	public void resetScore() {
		score = 0;
	}

	public void addToScore(int score) {
		this.score += score;
	}

	public int getScore() {
		return score;
	}

	public void setGameState(ComplexBikeGame gameLevel) {
		this.gameState = levelState;
		this.gameLevel = gameLevel;
	}

	public void setGameState(ComplexBikeGame gameLevel, String saveName) {
		this.gameState = saveState;
		this.saveLoaded = saveName;
		this.gameLevel = gameLevel;
	}

	public void setGameState(LevelEditor levelEditor) {
		this.gameState = levelEditorState;
		this.levelEditor = levelEditor;
	}
}

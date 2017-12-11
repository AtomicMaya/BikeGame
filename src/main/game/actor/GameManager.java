/**
 *	Author: Clément Jeannet
 *	Date: 	10 déc. 2017
 */
package main.game.actor;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import main.game.ActorGame;
import main.game.GameWithLevelAndMenu;
import main.game.actor.entities.Bike;
import main.game.actor.sensors.Checkpoint;
import main.game.actor.sensors.StartCheckpoint;
import main.game.graphics.TextGraphics;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

public class GameManager {

	private ActorGame game;
	private GameWithLevelAndMenu gameLevel;
	private int score = 0;

	// level management
	private final float timeBeforeMessage1 = 2f;
	private final String messageNextLevelText = "Press N to go to the next level";
	private final String messageRestartLevelText = "Press R to restart the level";
	private final String respawnText = "Press R to respawn";
	private final String backToMenu = "Press ESCAPE to go back to the menu";
	private String messageDisplayed = "";
	private TextGraphics message1;
	private float fontSize = 1.5f;

	// respawn mechanics
	private Vector position;
	private Checkpoint lastCheckpoint;
	private final float timeToRespawn = 3f;
	private float respawnTimer = 0;

	// temp save management
	private String saveLoaded;

	private boolean isInLevel = true;

	public GameManager(ActorGame game, ArrayList<Actor> actors) {
		this.game = game;

	}

	public void update(float deltaTime) {
		if (game.getPayload() != null)
			System.out.println(game.getPayload().getDeathStatus() + " " + game.getPayload().getVictoryStatus());
		if (game.getPayload() == null) {
			spawnBike();
		} else if (game.getPayload().getDeathStatus()) {
			this.respawnTimer += deltaTime;
			messageDisplayed = respawnText;
			if (this.respawnTimer > this.timeToRespawn && game.getKeyboard().get(KeyEvent.VK_R).isPressed()) {
				if (isInLevel) {
					gameLevel.resetLevel();
					spawnBike();
				} else {
					game.load(saveLoaded);
					game.getPayload().destroy();
					spawnBike();
				}
				messageDisplayed = "";
				respawnTimer = 0;
			}
		} else if (game.getPayload().getVictoryStatus()) {
			this.respawnTimer += deltaTime;
			if (this.respawnTimer > this.timeToRespawn) {
				if (isInLevel) {
					if (game.getKeyboard().get(KeyEvent.VK_N).isPressed()) {
						gameLevel.nextLevel();
						respawnTimer = 0;
						messageDisplayed = "";
					}
					messageDisplayed = messageNextLevelText;
				} else {
					if (game.getKeyboard().get(KeyEvent.VK_ESCAPE).isPressed()) {
						// TODO
						gameLevel.goToMainMenu();
						respawnTimer = 0;
						messageDisplayed = "";
					}
					messageDisplayed = backToMenu;
				}

			}
		}

	}

	public void draw(Canvas canvas) {
		if (this.respawnTimer > this.timeToRespawn)
			canvas.drawText(messageDisplayed, 1.5f, Transform.I.translated(canvas.getPosition()),
					new Color(66, 241, 244), new Color(155, 18, 48), .02f, false, false,
					new Vector(.5f, -2.5f * 20f / game.getViewScale()), 1, 42000);
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

	public void setLastCheckpoint(StartCheckpoint checkpoint) {
		if (lastCheckpoint == null)
			lastCheckpoint = checkpoint;
	}

	public void resetCheckpoint() {
		lastCheckpoint = null;
		messageDisplayed = "";
		respawnTimer = 0;
	}

	private void spawnBike() {
		if (lastCheckpoint != null) {
			Bike nextPlayer = new Bike(game, lastCheckpoint.getPosition());
			game.addActor(nextPlayer);
			game.setPayload(nextPlayer);
			game.setViewCandidate(nextPlayer);
		}
	}

	// score management
	public void resetScore() {
		score = 0;
	}

	public void addScore(int score) {
		this.score += score;
	}

	public int getScore() {
		return score;
	}

	public void inLevel(GameWithLevelAndMenu gameLevel) {
		this.isInLevel = true;
		this.gameLevel = gameLevel;
	}

	public void inLevel(String saveName) {
		this.isInLevel = false;
		this.saveLoaded = saveName;
	}

}

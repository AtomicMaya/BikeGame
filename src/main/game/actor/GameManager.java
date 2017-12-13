package main.game.actor;

import main.game.ActorGame;
import main.game.ComplexBikeGame;
import main.game.GUI.menu.LevelEditor;
import main.game.actor.entities.Bike;
import main.game.actor.entities.PlayableEntity;
import main.game.actor.sensors.Checkpoint;
import main.game.actor.sensors.SpawnCheckpoint;
import main.game.graphics.BetterTextGraphics;
import main.game.graphics.Graphics;
import main.math.ExtendedMath;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameManager implements Graphics {

	/** Tell {@link this} that the last thing loaded is a {@linkplain main.game.levels.Level} */
	private static final int levelState = 0;

	/** Tell {@link this} that the last thing loaded is a saved game */
	private static final int saveState = 1;

	/**
	 * Tell {@linkplain GameManager} that the last thing loaded is a {@linkplain LevelEditor}.
	 */
	private static final int levelEditorState = 2;

	/** The master {@linkplain ActorGame}. */
	private ActorGame game;

	/** The {@linkplain ComplexBikeGame}, needed in some cases. */
	private ComplexBikeGame gameLevel;

	/** {@linkplain LevelEditor} from which the {@linkplain #levelEditorState} is set.*/
	private LevelEditor levelEditor;

	// score management
	/** Keep track of the current score */
	private int score = 0;

	/** Score at the last {@linkplain Checkpoint} */
	private int savedScore = 0;

	/** {@linkplain BetterTextGraphics} to display the score */
	private BetterTextGraphics scoreDisapplay;

	/** Message to display with {@link #scoreDisapplay} */
	private final String scoreText = "Your score is : ";

	/** {@linkplain Vector} position of the {@link #scoreDisapplay} */
	private Vector scorePos = new Vector(-19, -9.5f);

	// level management
	/** Message to display when we won a {@linkplain main.game.levels.Level} */
	private final String messageNextLevelText = "Press N to go to the next level";

	/** Message to display when we have lost */
	private final String respawnText = "Press R to respawn";

	/** Message to display when we won a saved game */
	private final String backToMenu = "Press ESCAPE to go back to the menu";

	/**
	 * Actual message display
	 * @see #messageNextLevelText
	 * @see #respawnText
	 * @see #backToMenu
	 */
	private String messageDisplayed = "";

	/** Font size of the {@link #scoreDisapplay} */
	private float fontSize = 1.5f;

	// respawn mechanics
	/** Last {@linkplain Checkpoint} triggered */
	private Checkpoint lastCheckpoint;

	/** {@linkplain SpawnCheckpoint} of the main {@linkplain PlayableEntity} */
	private SpawnCheckpoint startCheckpoint;

	/** Time (in second) before being able to respawn */
	private final float timeToRespawn = 2f;

	/**
	 * Timer to count how much time passed, when it reach
	 * {@link #timeToRespawn}, {@link #messageDisplayed} is displayed
	 */
	private float respawnTimer = 0;

	// temp save management
	/**
	 * If {@link #gameState} == {@value #saveState}, this is the name of the
	 * save which is going to be loaded
	 */
	private String saveLoaded;

	/**
	 * Actual state, can be {@link #levelState}, {@link #saveState} or
	 * {@link #levelEditorState}
	 */
	private int gameState = -1;

	/** Create a new {@linkplain GameManager}.
     * @param game The master {@linkplain ActorGame}.
     */
	public GameManager(ActorGame game) {
		this.game = game;
		this.scoreDisapplay = new BetterTextGraphics(game, this.scoreText + this.score, 1f, this.scorePos);
		this.scoreDisapplay.setParent(game.getCanvas());
		this.scoreDisapplay.setDepth(42042);
	}

	/**
	 * Simulates a single time step.
	 * @param deltaTime elapsed time since last update, in seconds, non-negative
	 */
	public void update(float deltaTime) {
		// TODO supress thoses
		if (game.getPayload() != null && game.getKeyboard().get(KeyEvent.VK_9).isPressed())
			System.out.println("player start : death: " + game.getPayload().getDeathStatus() + " win : "
					+ game.getPayload().getVictoryStatus());

		// spawn a bike if payload is not defind, which happend at the start of
		// a game or after a reset
		if (game.getKeyboard().get(KeyEvent.VK_8).isPressed()) {
			Bike b = new Bike(game, game.getMouse().getPosition());
			game.setPayload(b);
			game.setViewCandidate(b);
			game.addActor(b);
		}

		if (game.getPayload() == null && (startCheckpoint != null) && !game.isGameFrozen()) {
			spawnBike();
			// if bike is spawned, ckeck for bike death/win status in order to
			// respawn one if needed
		} else if (game.getPayload() != null)
			// victory stronger that death

			if (game.getPayload().getVictoryStatus()) {
				this.respawnTimer += deltaTime;

				if (this.respawnTimer > this.timeToRespawn) {
					this.messageDisplayed = this.messageNextLevelText;
					switch (this.gameState) {
						case levelState: {
							if (game.getKeyboard().get(KeyEvent.VK_N).isPressed()) {
								this.restart();
								this.gameLevel.nextLevel();
								this.respawnTimer = 0;
								this.messageDisplayed = "";
							}
							break;
						}
						case saveState: {
							this.messageDisplayed = this.backToMenu;
							if (this.game.getKeyboard().get(KeyEvent.VK_ESCAPE).isPressed()) {
								// TODO
								restart();
								this.gameLevel.goToMainMenu();
								this.respawnTimer = 0;
								this.messageDisplayed = "";
								this.game.setGameFreezeStatus(true);
							}
							break;
						}
						case levelEditorState: {
							this.game.setGameFreezeStatus(false);
							this.messageDisplayed = "You won! You can continue editing.";
							break;
						}
					}
				}
			} else if (this.game.getPayload().getDeathStatus()) {
				this.respawnTimer += deltaTime;
				this.messageDisplayed = this.respawnText;
				if (this.respawnTimer > this.timeToRespawn && this.game.getKeyboard().get(KeyEvent.VK_R).isPressed()) {
					score = savedScore;
					reset();
					switch (this.gameState) {
						case levelState:
							this.gameLevel.resetLevel();
							break;
						case saveState:
							this.game.load(this.saveLoaded);
							break;
						case levelEditorState:
							game.addActor(levelEditor.getActors());
							break;
					}
					messageDisplayed = "";
					respawnTimer = 0;
				}
			}
	}

	@Override
	public void draw(Canvas canvas) {
		if (this.respawnTimer > this.timeToRespawn) {
			canvas.drawText(this.messageDisplayed, fontSize * game.getViewScale() / 20f,
					Transform.I.translated(canvas.getPosition()), new Color(219, 207, 39), new Color(155, 18, 48), .02f,
					true, false, new Vector(.5f, -2.5f * game.getViewScale() / 20f), 1, 42000);
		}
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
	 * Give to this {@linkplain GameManager} the last triggered {@linkplain Checkpoint} by the payload.
     * @param checkpoint The last triggered {@linkplain Checkpoint} by the payload.
	 */
	public void setLastCheckpoint(Checkpoint checkpoint) {
		if (lastCheckpoint != null)
			lastCheckpoint.setTriggerStatus(false);
		lastCheckpoint = checkpoint;
		savedScore = score;
	}

	/** Method called by the {@linkplain SpawnCheckpoint}
     * @param checkpoint The {@linkplain SpawnCheckpoint} that called the method.
     */
	public void setStartCheckpoint(SpawnCheckpoint checkpoint) {
		startCheckpoint = checkpoint;
	}

	/** Called at the start of a new level */
	private void restart() {
		startCheckpoint = null;
		lastCheckpoint = null;
		savedScore = 0;
		reset();
	}

	/** Called at the reset of a level */
	private void reset() {
		game.destroyAllActors();
		game.setViewCandidate(null);
		messageDisplayed = "";
		respawnTimer = 0;
		saveLoaded = null;
		resetScore();
	}

	/** Spawn a new {@linkplain Bike} at the right place */
	private void spawnBike() {
		Bike nextPlayer = new Bike(game,
				(lastCheckpoint == null ? startCheckpoint : lastCheckpoint).getPosition().add(0, 2));
		game.addActor(nextPlayer);
		game.setPayload(nextPlayer);
		game.setViewCandidate(nextPlayer);

	}

	// score management
	/** Reset the score */
	private void resetScore() {
		score = 0;
	}

	public void addToScore(int score) {
		this.score += score;
	}

	/** @return the {@linkplain} #score} */
	public int getScore() {
		return score;
	}

	/**
	 * Set the current {@link #gameState} to the {@link #levelState}
	 * @param gameLevel the {@linkplain ComplexBikeGame} from which it is called
	 */
	public void setGameState(ComplexBikeGame gameLevel) {
		this.gameState = levelState;
		this.gameLevel = gameLevel;
	}

	/**
	 * Set the current {@link #gameState} to the {@link #saveState}
	 * @param gameLevel the {@linkplain ComplexBikeGame} from which it is called
	 * @param saveName Name of the save loaded
	 */
	public void setGameState(ComplexBikeGame gameLevel, String saveName) {
		this.gameState = saveState;
		this.saveLoaded = saveName;
		this.gameLevel = gameLevel;
	}

	/**
	 * Set the current {@link #gameState} to the {@link #levelEditorState}
	 * @param levelEditor the {@linkplain LevelEditor} from which it is called
	 */
	public void setGameState(LevelEditor levelEditor) {
		this.gameState = levelEditorState;
		this.levelEditor = levelEditor;
	}

	/** @return the last {@linkplain Checkpoint} triggered */
	public Checkpoint getLastCheckpoint() {
		if (this.lastCheckpoint != null)
			return this.lastCheckpoint;
		else
			return this.startCheckpoint;
	}

	public String getSaveName() {
		return saveLoaded;
	}
}

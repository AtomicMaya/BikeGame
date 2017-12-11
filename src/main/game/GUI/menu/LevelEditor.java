/**
 * Author: Clément Jeannet Date: 4 déc. 2017
 */
package main.game.GUI.menu;

import main.game.ActorGame;
import main.game.ComplexBikeGame;
import main.game.GUI.Comment;
import main.game.GUI.GraphicalButton;
import main.game.GUI.actorBuilder.ActorBuilder;
import main.game.GUI.actorBuilder.FinishBuilder;
import main.game.GUI.actorBuilder.GroundBuilder;
import main.game.GUI.actorBuilder.SpawnBuilder;
import main.game.actor.Actor;
import main.game.actor.entities.Terrain;
import main.game.graphics.BetterTextGraphics;
import main.game.graphics.Graphics;
import main.game.graphics.ShapeGraphics;
import main.io.Save;
import main.math.*;
import main.math.Polygon;
import main.math.Shape;
import main.window.Canvas;
import main.window.Window;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;

/**
 * {@linkplain LevelEditor} used to create, edit and add {@linkplain Actor}s to
 * the game.
 */
public class LevelEditor implements Graphics {

	// actorBuilder stuff
	/** {@linkplain ArrayList} of all the {@linkplain ActorBuilder} created by the user. */
	private ArrayList<ActorBuilder> actorBuilders = new ArrayList<>();
	
	/** Unique {@linkplain GroundBuilder}, has to be created to save/test the game. */
	private GroundBuilder gb; // is unique
	
	/** Unique {@linkplain SpawnBuilder}, has to be created to save/test the game. */
	private SpawnBuilder spawn; // is unique
	
	/** Unique {@linkplain FinishBuilder}, has to be created to save/test the game. */
	private FinishBuilder finish; // is unique

	
	/** The master {@linkplain ActorGame}. */
	private ActorGame game;
	
	/** {@linkplain ActorMenu}. */
	private ActorMenu actorMenu;
	
	/** @see Window. */
	private Window window;
	
	/** Whether this {@linkplain LevelEditor} is open. */
	private boolean open = false;
	
	
	/** Initial position of the camera, represent the center of the screen. */
	private Vector cameraPosition = Vector.ZERO;
	
	/** Default zoom of the {@linkplain Window} : {@value}. */
	private static final float windowZoom = 30f;
	
	/** Default speed of the camera in (x or y) direction. */
	private static final float cameraSpeed = 20f;
	
	/** Current zoom, between {@value #minZoom} and {@value #maxZoom}. */
	private float zoom = 1f;
	
	/** Maximum zoom value : {@value #maxZoom}. */
	private static final float maxZoom = 2f;
	
	/** Maximum zoom value : {@value #minZoom}. */
	private static final float minZoom = 0.4f;
	
	/** 
	 * Maximum camera position. 
	 * @see #cameraPosition 
	 * */
	private float maxPosX = 120;
	
	/** 
	 * Minimun camera position. 	
	 * @see #cameraPosition 
	 * */
	private float maxPosY = 30;
	
	/** Camera acceleration, when CTRL is pressed. */
	private float cameraAcceleration = .4f;
	
	/** Current camera acceleration, default value : 1. */
	private float currentCameraAcceleration = 1;
	
	/** Maximum camera acceleration. */
	private final float maxCameraXPP = 3;
 
	
	// Grid parameters
	/** {@linkplain ArrayList} containing the {@linkplain Graphics} of a grid. */
	private ArrayList<ShapeGraphics> gridLine = new ArrayList<>();
	
	/** Axe of the grid, in x = 0 and y = 0. */
	private ShapeGraphics axeX, axeY;
	
	/** Number of lines on the screen. */
	private final int lineNumberX = 120, lineNumberY = 66;
	
	/** Thickness of the lines of the grid. */
	private float lineThickness = .01f;

	// button font size
	/** Font size of the different {@linkplain GraphicalButton}. */
	private float fontSize = .63f;
	
	/** Depth of the {@linkplain GraphicalButton}. */
	private float butonDepth = 51;

	// position showing
	/** Initial position of the {@link #redSquareGraphics}. */
	private Vector redSquarePosition = Vector.ZERO;
	
	/** Red square displayed if the {@link #showRedSquare} {@linkplain GraphicalButton} is clicked. */
	private ShapeGraphics redSquareGraphics;
	
	/** Whether the {@link #redSquareGraphics} is displayed. */
	private boolean showRedSquare = false; 
	
	/** Whether a place has been clicked on screen. */
	private boolean hasClicked = false; 
									
	/** Display the coordinates of the {@link #redSquareGraphics}. */
	private BetterTextGraphics redSquarePosText;

	// activate/desactivate position pointer
	/** {@linkplain GraphicalButton} which show/unshow the {@link #redSquareGraphics}. */
	private GraphicalButton getPositionButton;
	
	/** Absolute position on screen of the {@linkplain GraphicalButton} {@link #getPositionButton}. */
	private Vector getPositionButtonPosition = new Vector(-29, 14);
	
	/** Text of the {@linkplain GraphicalButton} {@link #getPositionButton}. */
	private final String getPosButtonText = "Positionneur";

	// reset camera button + position (absolue sur l'ecran)
	/** {@linkplain GraphicalButton} which reset the camera position to (0, 0). */
	private GraphicalButton cameraResetPosition;
	
	/** Absolute position on screen of the {@linkplain GraphicalButton} {@link #cameraResetPosition}. */
	private Vector cameraResetButtonPosition = new Vector(-21, 14);
	
	/** Text of the {@linkplain GraphicalButton} {@link #cameraResetPosition}. */
	private final String resetCameraButtonText = "Reset camera";

	// test play button
	/** {@linkplain GraphicalButton} to test the game, or continue editing the game */
	private GraphicalButton playButton;
	
	/** Absolute position on screen of the {@linkplain GraphicalButton} {@link #playButton}. */
	private Vector playButtonPosition = new Vector(0, 14);
	
	/** Text of the {@linkplain GraphicalButton} {@link #playButton}. */
	private final String playButtonText = "Play";
	
	/** Text of the {@linkplain GraphicalButton} {@link #playButton}. */
	private final String playButtonEditText = "Edit";

	// save button
	/** {@linkplain GraphicalButton} used to save the current {@linkplain Actor}s placed. */
	private GraphicalButton saveButon;
	
	/** Text of the {@linkplain GraphicalButton} {@link #saveButon}. */
	private final String saveButonText = "Save";
	
	/** Absolute position on screen of the {@linkplain GraphicalButton} {@link #saveButon}. */
	private Vector saveButonPos = new Vector(4, 14);
	
	/** Name of the first free save name. */
	private final String currentSaveName;
	
	/** {@linkplain Comment} to display some error messages. */
	private Comment error;
	
	/** Actual text displayed by {@link #error}. */
	private String errorText;
	
	/** Timer to stop displaying the {@link #error} {@linkplain Comment}. */
	private float errorTimer = 0;
	
	/** Time the {@link #error} {@linkplain Comment} stay displayed. */
	private final float maxErrorTimer = 2f;
	
	/** Whether the {@link #error} {@linkplain Comment} is displayed. */
	private boolean displayErrorText = false;

	// back to main menu button
	/** {@linkplain GraphicalButton} to go back to the {@linkplain MainMenu}. */
	private GraphicalButton backToMainMenu;
	
	/** Text of the {@linkplain GraphicalButton} {@link #backToMainMenu}. */
	private String backText = "Back to menu";
	
	/** Absolute position on screen of the {@linkplain GraphicalButton} {@link #backToMainMenu}. */
	private Vector backPos = new Vector(-8, 14);

	/**
	 * Create a new {@linkplain LevelEditor}
	 * @param game {@linkplain ActorGame} where this {@linkplain LevelEditor}
	 * belong
	 * @param window {@linkplain Window} graphical context
	 * @param mainMenu {@linkplain MainMenu} where this {@linkplain LevelEditor}
	 * is created
	 */
	public LevelEditor(ComplexBikeGame game, Window window, MainMenu mainMenu) {
		this.game = game;
		this.window = window;
		this.actorMenu = new ActorMenu(game, this, Color.LIGHT_GRAY);

		// red square
		Polygon p = new Polygon(0, 0, 0, 1, 1, 1, 1, 0);
		redSquareGraphics = new ShapeGraphics(p, Color.RED, null, 0, .5f, 100);
		redSquarePosText = new BetterTextGraphics(game, "0.0", 1f, new Vector(1, 1));
		redSquarePosText.setDepth(101);
		redSquarePosText.setParent(redSquareGraphics);

		game.setGameFreezeStatus(true);
		gridLine = grid();

		// create axis
		float times = 3f;
		float t2 = 12f;
		Shape line1 = new Polygon(-maxPosX * t2, -lineThickness * times, maxPosX * t2, -lineThickness * times,
				maxPosX * t2, lineThickness * times, -maxPosX * t2, lineThickness * times);
		axeX = new ShapeGraphics(line1, Color.BLACK, null, 0, 1, 50);

		Shape line2 = new Polygon(-lineThickness * times, -maxPosY * t2, -lineThickness * times, maxPosY * t2,
				lineThickness * times, maxPosY * t2, lineThickness * times, -maxPosY * t2);
		axeY = new ShapeGraphics(line2, Color.BLACK, null, 0, 1, 50);

		// get or not the position on screen when click
		getPositionButton = new GraphicalButton(game, getPositionButtonPosition, getPosButtonText, fontSize);
		getPositionButton.setDepth(butonDepth);
		getPositionButton.addOnClickAction(() -> {
			hasClicked = false;
			showRedSquare = !showRedSquare;
		});

		// reset the camera when clicked
		cameraResetPosition = new GraphicalButton(game, cameraResetButtonPosition, resetCameraButtonText, fontSize);
		cameraResetPosition.setDepth(butonDepth);
		cameraResetPosition.addOnClickAction(() -> {
			cameraPosition = Vector.ZERO;
			zoom = 1;
		});

		// playButton, centered
		playButton = new GraphicalButton(game, playButtonPosition, playButtonText, fontSize);
		playButton.setDepth(butonDepth);
		playButtonPosition = new Vector(-playButton.getWidth() / 2, playButtonPosition.y);
		playButton.setAnchor(playButtonPosition);
		playButton.addOnClickAction(() -> {

			error.setParent(playButton);
			if (isBusy() && game.isGameFrozen()) {
				errorText = "Please finish editing the actor";
				displayErrorText = true;
				errorTimer = 0;
				return;
			}
			game.setGameFreezeStatus(!game.isGameFrozen());
			if (!game.isGameFrozen()) {// play
				this.playButton.setText(playButtonEditText, fontSize);
				game.addActor(getActors());

				game.getGameManager().setStartCheckpoint(this.spawn.getSpawn());
				game.getGameManager().setGameState(this);
				game.setPayload(null);
			} else { // edit
				playButton.setText(playButtonText, fontSize);
				game.destroyAllActors();
				for (ActorBuilder ab : actorBuilders) {
					ab.reCreate();
				}
				game.setViewCandidate(null);
			}
		});

		backToMainMenu = new GraphicalButton(game, backPos, backText, fontSize);
		backToMainMenu.addOnClickAction(() -> {
			game.destroyAllActors();
			this.close();
		});

		// get an available name for the save
		File[] saves = Save.availableSaves(game);
		ArrayList<String> savesNames = new ArrayList<>();
		for (File f : saves) {
			savesNames.add(f.getName());
		}
		String temp = "";
		for (int i = 1; i < saves.length + 2; i++) {
			if (!savesNames.contains("save" + ((i < 10) ? "0" : "") + i)) {
				temp = "save" + ((i < 10) ? "0" : "") + i;
				break;
			}
		}
		this.currentSaveName = (temp);
		System.out.println("current save name: " + currentSaveName);

		// create save button
		saveButon = new GraphicalButton(game, saveButonPos, saveButonText, fontSize);
		saveButon.setDepth(butonDepth);
		saveButon.addOnClickAction(() -> {
			errorText = null;
			displayErrorText = false;
			error.setParent(saveButon);
			if (isBusy()) {
				errorText = "Please finish editing the actor";
				displayErrorText = true;
				errorTimer = 0;
				return;
			}
			if (this.gb == null)
				this.errorText = "Please create a ground";
			if (this.spawn == null || this.finish == null)
				this.errorText = "Please create a Spawn and a finish point";
			if (this.gb == null && (this.spawn == null || this.finish == null))
				this.errorText = "Please create a ground, Spawn and a finish point";
			if (this.errorText == null) {
				// TODO
				System.out.println("    - start saving");
				game.save(getActors(), currentSaveName);
				this.errorText = "Actors saved sucessfully";
			}
			this.displayErrorText = true;

		});
		this.error = new Comment(game, "");
		this.error.setParent(this.saveButon);
		this.error.setAnchor(new Vector(this.saveButon.getWidth() / 2, -4));
	}

	/**
	 * Simulates a single time step.
	 * @param deltaTime elapsed time since last update, in seconds, non-negative
	 */
	public void update(float deltaTime) {

		// case we are testing the game
		if (!this.game.isGameFrozen()) {
			float z = this.game.getViewScale() / windowZoom;
			this.playButton.update(deltaTime, z);
			return;
		}
		
		// camera acceleration
		if (this.game.getKeyboard().get(KeyEvent.VK_CONTROL).isDown()) {
			this.currentCameraAcceleration += this.cameraAcceleration * deltaTime;
			this.currentCameraAcceleration = (this.currentCameraAcceleration >= this.maxCameraXPP) ? this.maxCameraXPP
					: this.currentCameraAcceleration;
		}
		if (this.game.getKeyboard().get(KeyEvent.VK_CONTROL).isReleased())
			this.currentCameraAcceleration = 1;

		// camera controls
		float posX = this.cameraPosition.x;
		float posY = this.cameraPosition.y;
		if (this.game.getKeyboard().get(KeyEvent.VK_W).isDown()) {
			posY += deltaTime * cameraSpeed * this.currentCameraAcceleration;
		}
		if (this.game.getKeyboard().get(KeyEvent.VK_S).isDown()) {
			posY -= deltaTime * cameraSpeed * this.currentCameraAcceleration;
		}
		if (this.game.getKeyboard().get(KeyEvent.VK_A).isDown()) {
			posX -= deltaTime * cameraSpeed * this.currentCameraAcceleration;
		}
		if (this.game.getKeyboard().get(KeyEvent.VK_D).isDown()) {
			posX += deltaTime * cameraSpeed * this.currentCameraAcceleration;
		}

		posX = (posX >= this.maxPosX) ? this.maxPosX : posX;
		posY = (posY >= this.maxPosY) ? this.maxPosY : posY;
		posX = (posX <= -this.maxPosX) ? -this.maxPosX : posX;
		posY = (posY <= -this.maxPosY) ? -this.maxPosY : posY;
		cameraPosition = new Vector(posX, posY);

		// zoom control
		if (game.getMouse().getMouseScrolledUp()) {
			this.zoom -= .1f;
			this.zoom = (this.zoom < minZoom) ? minZoom : this.zoom;
		} else if (game.getMouse().getMouseScrolledDown()) {
			this.zoom += .1f;
			this.zoom = (this.zoom > maxZoom) ? maxZoom : this.zoom;
		}

		// finalement placement de la camera
		this.window.setRelativeTransform(Transform.I.scaled(windowZoom * this.zoom).translated(this.cameraPosition));

		// ligne de placement
		this.gridLine = grid();

		// right click menu
		if (!isBusy())
			this.actorMenu.update(deltaTime, this.zoom);
		else
			this.actorMenu.setStatus(false);

		// positionneur stuff
		if (this.showRedSquare && this.game.getMouse().getLeftButton().isPressed()) {
			this.hasClicked = true;
			this.redSquarePosition = this.game.getMouse().getPosition();
			this.redSquareGraphics
					.setRelativeTransform(Transform.I.translated(ExtendedMath.floor(this.redSquarePosition)));
			this.redSquarePosText.setText(
					(int) Math.floor(this.redSquarePosition.x) + ", " + (int) Math.floor(this.redSquarePosition.y));
		}

		// buttons update
		this.cameraResetPosition.update(deltaTime, this.zoom);
		this.getPositionButton.update(deltaTime, this.zoom);
		this.playButton.update(deltaTime, this.zoom);
		this.backToMainMenu.update(deltaTime, this.zoom);

		// current actors update
		ActorBuilder current = null;
		for (ActorBuilder actor : actorBuilders) {
			actor.update(deltaTime, zoom);
			if (!actor.isDone() && !actor.equals(gb))
				current = actor;
			if (actor.isHovered() && game.getMouse().getRightButton().isPressed())
				actor.edit();
		}

		// destroy selected actor if delete is pressed
		if (current != null && game.getKeyboard().get(KeyEvent.VK_DELETE).isPressed()) {
			current.destroy();
			actorBuilders.remove(current);
		}

		// save button and stuff update
		saveButon.update(deltaTime, zoom);
		error.update(deltaTime, zoom);

		if (displayErrorText && errorTimer > maxErrorTimer) {
			displayErrorText = false;
			errorTimer = 0;
		} else if (displayErrorText) {
			error.setText(errorText);
			errorTimer += deltaTime;
		}
	}

	@Override
	public void draw(Canvas canvas) {

		// draw button play
		playButton.draw(canvas);

		// if we are playing, return
		if (!game.isGameFrozen()) {
			return;
		}

		// draw right click menu
		actorMenu.draw(canvas);

		// draw current actors
		for (ActorBuilder actor : actorBuilders) {
			actor.draw(canvas);
		}

		// draw grid

		for (ShapeGraphics sg : gridLine) {
			sg.draw(canvas);
		}
		// draw axis
		axeX.draw(canvas);
		axeY.draw(canvas);

		if (showRedSquare && hasClicked) {
			redSquareGraphics.draw(canvas);
			redSquarePosText.draw(canvas);
		}
		getPositionButton.draw(canvas);
		cameraResetPosition.draw(canvas);
		backToMainMenu.draw(canvas);

		// draw button save
		saveButon.draw(canvas);
		if (displayErrorText)
			error.draw(canvas);

	}

	/** @return the list of all {@linkplain Actor}s created. */
	public ArrayList<Actor> getActors() {
		ArrayList<Actor> a = new ArrayList<>();
		for (ActorBuilder ab : actorBuilders) {
			a.add(ab.getActor());
		}
		return a;
	}

	/** @param actor {@linkplain ActorBuilder} to add */
	public void addActorBuilder(ActorBuilder actor) {
		actorBuilders.add(actor);
	}

	/**
	 * Make sure we have a unique {@linkplain Terrain}
	 * @param {@linkplain Terrain} to add to the game
	 */
	public void addGround() {
		if (this.gb != null) {
			gb.continueBuilding();
		} else {
			gb = new GroundBuilder(game);
			actorBuilders.add(gb);
		}
	}

	/**
	 * Make sure we have a unique {@linkplain SpawnBuilder}
	 * @param spawn {@linkplain SpawnBuilder}
	 */
	public void addSpawn(SpawnBuilder spawn) {
		if (this.spawn != null) {
			this.spawn.getActor().destroy();
			actorBuilders.remove(this.spawn);
		}
		this.spawn = spawn;
		this.actorBuilders.add(spawn);
	}

	/**
	 * Make sure we have a unique {@linkplain FinishBuilder} in the game
	 * @param finish the {@linkplain FinishBuilder}
	 */
	public void addFinish(FinishBuilder finish) {
		if (this.finish != null) {
			this.finish.getActor().destroy();
			actorBuilders.remove(this.finish);
		}
		this.finish = finish;
		this.actorBuilders.add(finish);
	}

	/** @return an ArrayList containing an updated grid */
	private ArrayList<ShapeGraphics> grid() {
		ArrayList<ShapeGraphics> lines = new ArrayList<>();

		// lignes verticales
		Shape t = new Polygon(-lineThickness, -lineNumberY / 2, -lineThickness, lineNumberY / 2, lineThickness,
				lineNumberY / 2, lineThickness, -lineNumberY / 2);

		Vector c2 = ExtendedMath.floor(cameraPosition);
		for (int i = 0; i < lineNumberX; i++) {
			lines.add(new ShapeGraphics(t, Color.BLACK, null, 0, .8f, -3));
			lines.get(i).setRelativeTransform(
					Transform.I.translated(new Vector(1f * i - lineNumberX / 2, 0)).translated(c2));
		}

		// lignes horizontales
		Shape t2 = new Polygon(-lineNumberX / 2, -lineThickness, lineNumberX / 2, -lineThickness, lineNumberX / 2,
				lineThickness, -lineNumberX / 2, lineThickness);
		for (int i = 0; i < lineNumberX; i++) {
			lines.add(new ShapeGraphics(t2, Color.BLACK, null, 0, .8f, -3));
			lines.get(i + lineNumberX).setRelativeTransform(
					Transform.I.translated(new Vector(0, 1f * i - lineNumberX / 2)).translated(c2));
		}
		return lines;
	}

	/**
	 * @return the current {@link #zoom}, between {@value #minZoom} and
	 * {@value #maxZoom}
	 */
	public float getWindowScale() {
		return windowZoom;
	}

	/**
	 * @return the current camera position
	 */
	public Vector getCameraPosition() {
		return this.cameraPosition;
	}

	/**
	 * Destroy this
	 */
	public void destroy() {
		this.actorMenu.destroy();
		for (ActorBuilder a : actorBuilders)
			a.destroy();
		this.cameraResetPosition.destroy();
		this.playButton.destroy();
		this.getPositionButton.destroy();
		this.cameraResetPosition.destroy();
		this.backToMainMenu.destroy();
	}

	/** @return whether an {@linkplain ActorBuilder} is being created/edited */
	public boolean isBusy() {
		for (ActorBuilder actor : actorBuilders) {
			// make sure no ActorBuilder is being created
			if (!actor.isDone() || actor.isHovered())
				return true;
		}
		return false;
	}

	/** @return whether we are in this {@linkplain LevelEditor} */
	public boolean isOpen() {
		return open;
	}

	/** Set the status of this {@linkplain LevelEditor} to true */
	public void open() {
		this.open = true;
	}

	/** Set the status of this {@linkplain LevelEditor} to false */
	public void close() {
		this.open = false;
		this.destroy();
	}
}

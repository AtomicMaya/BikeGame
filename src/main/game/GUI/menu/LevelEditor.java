/**
 *	Author: Clément Jeannet
 *	Date: 	4 déc. 2017
 */
package main.game.GUI.menu;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.GraphicalButton;
import main.game.GUI.actorBuilder.ActorBuilder;
import main.game.GUI.actorBuilder.BikeBuilder;
import main.game.GUI.actorBuilder.GroundBuilder;
import main.game.actor.Actor;
import main.game.actor.entities.Bike;
import main.game.actor.entities.Ground;
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
 * the game
 */
public class LevelEditor implements Graphics {

	// actorBuilder stuff
	private ArrayList<ActorBuilder> actors = new ArrayList<>();
	private GroundBuilder gb;
	private BikeBuilder bb;

	// stuffs
	private ActorGame game;
	private ActorMenu actorMenu;
	private Window window;

	// Camera stuff
	private Vector cameraPosition = Vector.ZERO;
	private static final float windowZoom = 30f;
	private static final float cameraSpeed = 20f;
	/** Curent zoom, between {@value #minZoom} and {@value #maxZoom} */
	private float zoom = 1f;
	/** Maximum zoom value : {@value #maxZoom} */
	private static final float maxZoom = 2f;
	/** Maximum zoom value : {@value #minZoom} */
	private static final float minZoom = 0.4f;
	private float maxPosX = 120;
	private float maxPosY = 30;
	private float cameraAcceleration = .4f;
	private float xPP = 1;// camera acceleration
	private final float maxCameraXPP = 3;

	// Grid parameters
	private ArrayList<ShapeGraphics> gridLine = new ArrayList<>();
	private ShapeGraphics axeX, axeY;
	private int lineNumberX = 120, lineNumberY = 66;
	private float lineThickness = .01f;

	// button font size
	private float fontSize = .63f;
	private float butonDepth = 51;

	// position showing
	private Vector redSquarePosition = Vector.ZERO;
	private ShapeGraphics redSquareGraphics;
	private boolean showRedSquare = false; // true if the red square is show
	private boolean hasClicked = false; // true if we clicked once to get
										// position on screen
	private BetterTextGraphics redSquarePosText;

	// activate/desactivate position pointer
	private GraphicalButton getPositionButton;
	private Vector getPositionButtonPosition = new Vector(-29, 14);
	private final String getPosButtonText = "Positionneur";

	// reset camera button + position (absolue sur l'ecran)
	private GraphicalButton cameraResetPosition;
	private Vector cameraResetButtonPosition = new Vector(-21, 14);
	private final String resetCameraButtonText = "Reset camera";

	// test play button
	private GraphicalButton playButton;
	private Vector playButtonPosition = new Vector(0, 14);
	private final String playButtonText = "Play";
	private final String playButtonEditText = "Edit";

	// save button
	private GraphicalButton saveButon;
	private final String saveButonText = "Save";
	private Vector saveButonPos = new Vector(4, 14);
	private final String currentSaveName;
	private Comment error;
	private String errorText;
	private float errorTimer = 0;
	private final float maxErrorTimer = 2f;
	private boolean displayErrorText = false;

	/**
	 * Create a new {@linkplain LevelEditor}
	 * @param game {@linkplain ActorGame} where this {@linkplain LevelEditor}
	 * belong
	 * @param window {@linkplain Window} graphical context
	 */
	public LevelEditor(ActorGame game, Window window) {
		this.game = game;
		this.window = window;
		this.actorMenu = new ActorMenu(game, this, window, Color.LIGHT_GRAY);

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
		playButtonPosition = new Vector(-playButton.getWidth() / 2 , playButtonPosition.y);
		playButton.setAnchor(playButtonPosition);
		playButton.addOnClickAction(() -> {

			game.setGameFreezeStatus(!game.isGameFrozen());
			if (!game.isGameFrozen()) {
				playButton.setText(playButtonEditText, fontSize);
				game.addActor(getActors());
				game.setViewCandidate(this.bb.getActor());
			} else { // unplay
				playButton.setText(playButtonText, fontSize);
				game.destroyAllActors();
				game.setViewCandidate(null);
				for (ActorBuilder ab : actors) {
					ab.reCreate();
				}
			}
		});

		// get available name for the save
		File[] saves = Save.availableSaves(game);
		ArrayList<String> savesNames = new ArrayList<>();

		for (File f : saves) {
			savesNames.add(f.getName().replaceAll("save", ""));
		}

		ArrayList<Integer> number = new ArrayList<>();
		for (int i = 1; i < savesNames.size(); i++) {
			if (ExtendedMath.isNumeric(savesNames.get(i)))
				number.add(Integer.parseInt(savesNames.get(i)));
		}
//		for (int s:number)System.out.println(s);
		int z = 1;
		Object w = z;
System.out.println(number.contains(2));
		String temp = "";
		for (int i = 1; i < number.size() + 1; i++) {
			if (number.get(i) != i) {
				System.out.println(i);
				temp = "save" + ((i < 10) ? "0" : "") + i;
				break;
			}
		}

		currentSaveName = (temp);
		System.out.println(currentSaveName);

		// create save button
		saveButon = new GraphicalButton(game, saveButonPos, saveButonText, fontSize);
		saveButon.setDepth(butonDepth);
		saveButon.addOnClickAction(() -> {
			errorText = null;
			displayErrorText = false;
			if (isBusy()) {
				errorText = "Please finish editing the actor";
				displayErrorText = true;
				return;
			}
			if (this.gb == null)
				errorText = "Please create a ground";
			if (this.bb == null)
				errorText = "Please create a bike";
			if (this.gb == null && this.bb == null)
				errorText = "Please create a bike and a ground";
			if (errorText == null) {
				// TODO save
			} else
				displayErrorText = true;

		});
		error = new Comment(game, "");
		error.setParent(saveButon);
		error.setAnchor(new Vector(saveButon.getWidth() / 2, -4));
	}

	/**
	 * Simulates a single time step.
	 * @param deltaTime elapsed time since last update, in seconds, non-negative
	 */
	public void update(float deltaTime) {

		if (!game.isGameFrozen()) {

			float z = game.getViewScale() / windowZoom;
			playButton.update(deltaTime, z);

			return;
		}
		// camera accelaration
		if (game.getKeyboard().get(KeyEvent.VK_CONTROL).isDown()) {
			xPP += cameraAcceleration * deltaTime;
			xPP = (xPP >= maxCameraXPP) ? maxCameraXPP : xPP;
		}
		if (game.getKeyboard().get(KeyEvent.VK_CONTROL).isReleased())
			xPP = 1;

		// camera controls
		float posX = cameraPosition.x;
		float posY = cameraPosition.y;
		if (game.getKeyboard().get(KeyEvent.VK_W).isDown()) {
			posY += deltaTime * cameraSpeed * xPP;
		}
		if (game.getKeyboard().get(KeyEvent.VK_S).isDown()) {
			posY += -deltaTime * cameraSpeed * xPP;
		}
		if (game.getKeyboard().get(KeyEvent.VK_A).isDown()) {
			posX += -deltaTime * cameraSpeed * xPP;
		}
		if (game.getKeyboard().get(KeyEvent.VK_D).isDown()) {
			posX += deltaTime * cameraSpeed * xPP;
		}

		posX = (posX >= maxPosX) ? maxPosX : posX;
		posY = (posY >= maxPosY) ? maxPosY : posY;
		posX = (posX <= -maxPosX) ? -maxPosX : posX;
		posY = (posY <= -maxPosY) ? -maxPosY : posY;
		cameraPosition = new Vector(posX, posY);

		// zoom control
		if (game.getMouse().getMouseScrolledUp()) {
			zoom -= .1f;
			zoom = (zoom < minZoom) ? minZoom : zoom;
		} else if (game.getMouse().getMouseScrolledDown()) {
			zoom += .1f;
			zoom = (zoom > maxZoom) ? maxZoom : zoom;
		}

		// finalement placement de la camera
		window.setRelativeTransform(Transform.I.scaled(windowZoom * zoom).translated(cameraPosition));
		// game.setCameraPosition(cameraPosition);

		// ligne de placement
		gridLine = grid();

		// right click menu
		boolean temp = true;
		for (ActorBuilder actor : actors) {
			// make sure no ActorBuilder is being created
			temp = actor.isDone() & !actor.isHovered() & temp;
		}
		if (temp)
			actorMenu.update(deltaTime, zoom);
		else if (game.getMouse().getLeftButton().isPressed() || game.getMouse().getRightButton().isPressed())
			actorMenu.setStatus(false);

		// positionneur stuff
		if (showRedSquare && game.getMouse().getLeftButton().isPressed()) {
			hasClicked = true;
			redSquarePosition = game.getMouse().getPosition();
			redSquareGraphics.setRelativeTransform(Transform.I.translated(ExtendedMath.floor(redSquarePosition)));
			redSquarePosText
					.setText((int) Math.floor(redSquarePosition.x) + ", " + (int) Math.floor(redSquarePosition.y));
		}

		// buttons update
		cameraResetPosition.update(deltaTime, zoom);
		getPositionButton.update(deltaTime, zoom);
		playButton.update(deltaTime, zoom);

		// current actors update
		ActorBuilder current = null;
		for (ActorBuilder actor : actors) {
			actor.update(deltaTime, zoom);
			if (!actor.isDone() && !actor.equals(gb))
				current = actor;
		}

		// destroy selected actor if delete is pressed
		if (current != null && game.getKeyboard().get(KeyEvent.VK_DELETE).isPressed()) {
			current.destroy();
			actors.remove(current);
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

		//draw button play
		playButton.draw(canvas);

		// if we are playing, return
		if (!game.isGameFrozen()) {
			return;
		}

		// draw right click menu
		actorMenu.draw(canvas);

		// draw current actors
		for (ActorBuilder actor : actors) {
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

		// draw button save
		saveButon.draw(canvas);
		if (displayErrorText)
			error.draw(canvas);

	}

	/** @return the list of all {@linkplain Actor}s created */
	public ArrayList<Actor> getActors() {
		ArrayList<Actor> a = new ArrayList<>();
		for (ActorBuilder ab : actors) {
			a.add(ab.getActor());
		}
		return a;
	}

	/** @param actor {@linkplain ActorBuilder} to add */
	public void addActor(ActorBuilder actor) {
		actors.add(actor);
	}

	/**
	 * Make sure we have a unique {@linkplain Ground}
	 * @param {@linkplain Ground} to add to the game
	 */
	public void addGround() {
		if (this.gb != null) {
			gb.continueBuilding();
		} else {
			gb = new GroundBuilder(game, this);
			actors.add(gb);
		}
	}

	/**
	 * Make sure we have a unique {@linkplain Bike}
	 * @param bike {@linkplain Bike} to add to the game
	 */
	public void addBike(BikeBuilder bike) {
		if (this.bb != null) {
			this.bb.getActor().destroy();
			actors.remove(this.bb);
		}

		this.bb = bike;
		actors.add(bike);
	}

	/** @return an ArrayList containing an updated grid */
	private ArrayList<ShapeGraphics> grid() {
		ArrayList<ShapeGraphics> lines = new ArrayList<>();

		// lignes en |
		Shape t = new Polygon(-lineThickness, -lineNumberY / 2, -lineThickness, lineNumberY / 2, lineThickness,
				lineNumberY / 2, lineThickness, -lineNumberY / 2);

		Vector c2 = ExtendedMath.floor(cameraPosition);
		for (int i = 0; i < lineNumberX; i++) {
			lines.add(new ShapeGraphics(t, Color.BLACK, null, 0, .8f, -3));
			lines.get(i).setRelativeTransform(
					Transform.I.translated(new Vector(1f * i - lineNumberX / 2, 0)).translated(c2));
		}

		// lignes en --
		Shape t2 = new Polygon(-lineNumberX / 2, -lineThickness, lineNumberX / 2, -lineThickness, lineNumberX / 2,
				lineThickness, -lineNumberX / 2, lineThickness);
		for (int i = 0; i < lineNumberX; i++) {
			lines.add(new ShapeGraphics(t2, Color.BLACK, null, 0, .8f, -3));
			lines.get(i + lineNumberX).setRelativeTransform(
					Transform.I.translated(new Vector(0, 1f * i - lineNumberX / 2)).translated(c2));
		}
		return lines;

	}

	public float getZoom() {
		return this.zoom;
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
		for (ActorBuilder a : actors)
			a.destroy();
		this.cameraResetPosition.destroy();
		this.playButton.destroy();
		this.getPositionButton.destroy();
		this.cameraResetPosition.destroy();
	}

	/** @return whether an {@linkplain ActorBuilder} is being created/edited */
	public boolean isBusy() {
		boolean temp = true;
		for (ActorBuilder actor : actors) {
			// make sure no ActorBuilder is being created
			if (!actor.isDone())
				return true;
			temp = actor.isDone() & !actor.isHovered() & temp;
		}
		return false;
	}

}

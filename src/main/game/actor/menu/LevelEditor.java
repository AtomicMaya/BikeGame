/**
 *	Author: Clément Jeannet
 *	Date: 	4 déc. 2017
 */
package main.game.actor.menu;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.Graphics;
import main.game.actor.ShapeGraphics;
import main.game.actor.actorBuilder.ActorBuilder;
import main.game.actor.actorBuilder.BikeBuilder;
import main.game.actor.actorBuilder.GroundBuilder;
import main.game.actor.entities.BetterTextGraphics;
import main.game.actor.entities.GraphicalButton;
import main.io.Save;
import main.math.Polygon;
import main.math.Shape;
import main.math.Transform;
import main.math.Vector;
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
		redSquarePosText = new BetterTextGraphics(game, "0.0", 1f);
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

		// playButton
		playButton = new GraphicalButton(game, playButtonPosition, playButtonText, fontSize);
		playButton.setDepth(butonDepth);
		playButtonPosition = new Vector(-playButton.getWidth() / 2, playButtonPosition.y);
		playButton.addOnClickAction(() -> {

			game.setGameFreezeStatus(!game.isGameFrozen());
			if (!game.isGameFrozen()) {
				game.addActor(getActors());
				game.setViewCandidate(this.bb.getActor());
			} else {
				game.destroyAllActors();
				game.setViewCandidate(null);
				for (ActorBuilder ab : actors) {
					ab.reCreate();
				}
			}

		});

		// create save button
		saveButon = new GraphicalButton(game, saveButonPos, saveButonText, fontSize);
		saveButon.setDepth(butonDepth);
		saveButon.addOnClickAction(() -> {});

		// get available name for the save
		File[] saves = Save.availableSaves(game);
		ArrayList<String> savesNames = new ArrayList<>();

		for (File f : saves) {
			savesNames.add(f.getName().replaceAll("save", ""));
		}
		ArrayList<Integer> number = new ArrayList<>();
		for (int i = 1; i < savesNames.size(); i++) {
			if (QuickMafs.isNumeric(savesNames.get(i)))
				number.add(Integer.parseInt(savesNames.get(i)));
		}
		String temp = "";
		for (int i = 1; i < number.size() + 1; i++) {
			if (!number.contains(i)) {
				temp = "save" + ((i < 10) ? "0" : "") + i;
				break;
			}
		}

		currentSaveName = (temp);
	}

	/**
	 * Update all the buttons of this {@linkplain LevelEditor} * @param
	 * deltaTime elapsed time since last update, in seconds, non-negative
	 */
	private void updateButtons(float deltaTime) {
		// camera reset button update
		cameraResetPosition.setText(resetCameraButtonText, fontSize * zoom);
		cameraResetPosition.setPosition((cameraResetButtonPosition).mul(zoom).add(cameraPosition));
		cameraResetPosition.update(deltaTime);

		// position button update
		getPositionButton.setText(getPosButtonText, fontSize * zoom);
		getPositionButton.setPosition((getPositionButtonPosition).mul(zoom).add(cameraPosition));
		getPositionButton.update(deltaTime);

		// play button update
		// playButton.forceShape(playButonSizeX*zoom, -1);
		playButton.setText(playButtonText, fontSize * zoom);
		playButton.setPosition((playButtonPosition).mul(zoom).add(cameraPosition));
		playButton.update(deltaTime);
	}

	/**
	 * Simulates a single time step.
	 * @param deltaTime elapsed time since last update, in seconds, non-negative
	 */
	public void update(float deltaTime) {

		if (!game.isGameFrozen()) {

			float z = game.getViewScale() / windowZoom;
			playButton.setText(playButtonEditText, fontSize * z);
			playButton.setPosition((playButtonPosition).mul(z).add(game.getCameraPosition()));
			playButton.update(deltaTime);

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

		// ligne de placement
		gridLine = grid();

		// right click menu
		boolean temp = true;
		for (ActorBuilder actor : actors) {
			temp = actor.isDone() & !actor.isHovered() & temp;
		}
		if (temp)
			actorMenu.update(deltaTime);
		else if (game.getMouse().getLeftButton().isPressed() || game.getMouse().getRightButton().isPressed())
			actorMenu.setStatus(false);

		// positionneur stuff
		if (showRedSquare && game.getMouse().getLeftButton().isPressed()) {
			hasClicked = true;
			redSquarePosition = game.getMouse().getPosition();
			redSquareGraphics.setRelativeTransform(Transform.I.translated(ExtendedMath.floor(redSquarePosition)));
			redSquarePosText
					.setText((int) Math.floor(redSquarePosition.x) + ", " + (int) Math.floor(redSquarePosition.y));
			redSquarePosText.setRelativeTransform(Transform.I.translated(new Vector(1, 1)));
		}

		// static buttons update
		updateButtons(deltaTime);

		// current actors update
		ActorBuilder current = null;
		for (ActorBuilder actor : actors) {
			actor.update(deltaTime);
			if (!actor.isDone() && !actor.equals(gb))
				current = actor;
		}

		// destroy selected actor id delete is pressed
		if (current != null && game.getKeyboard().get(KeyEvent.VK_DELETE).isPressed()) {
			current.destroy();
			actors.remove(current);
		}

	}

	@Override
	public void draw(Canvas canvas) {

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

}

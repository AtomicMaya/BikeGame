/**
 *	Author: Clément Jeannet
 *	Date: 	4 déc. 2017
 */
package main.game.actor.menu;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.Graphics;
import main.game.actor.QuickMafs;
import main.game.actor.ShapeGraphics;
import main.game.actor.actorBuilder.ActorBuilder;
import main.game.actor.entities.BetterTextGraphics;
import main.game.actor.entities.GraphicalButton;
import main.math.Polygon;
import main.math.Shape;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;
import main.window.Window;

public class LevelEditor implements Graphics {

	private ArrayList<ActorBuilder> actors = new ArrayList<>();
	private ActorGame game;
	private ActorMenu actorMenu;

	private Window window;

	// Camera stuff
	private Vector cameraPosition = Vector.ZERO;
	private final float cameraSpeed = 10;
	private float zoom = 1f;

	// Grid parameters
	private ArrayList<ShapeGraphics> gridLine = new ArrayList<>();
	private int lineNumber = 60;
	private float lineThickness = .01f;

	// position showing
	private Vector redSquarePosition = Vector.ZERO;
	private ShapeGraphics regSquareGraphics;
	private boolean showRedSquare = false; // true if the red square is show
	private boolean hasClicked = false; // true if we clicked once to get position on screen
	private BetterTextGraphics redSquarePosText;

	// activate/desactivate position pointer
	private GraphicalButton getPositionButton;
	private Vector getPositionButtonPosition;

	// reset camera button + position (absolue sur l'ecran)
	private GraphicalButton carmeraResetButton;
	private Vector carmeraResetButtonPosition;

	public LevelEditor(ActorGame game, Window window) {
		this.game = game;
		this.window = window;
		this.actorMenu = new ActorMenu(game, this, window, Color.LIGHT_GRAY);

		Polygon p = new Polygon(0, 0, 0, 1, 1, 1, 1, 0);
		regSquareGraphics = new ShapeGraphics(p, Color.RED, null, 0, .5f, 100);
		redSquarePosText = new BetterTextGraphics("0.0", .7f);
		redSquarePosText.setAlpha(101);
		redSquarePosText.setParent(regSquareGraphics);
		game.setGameFreezeStatus(true);
		gridLine = grid();

		// get or not the position on screen when click
		getPositionButtonPosition = new Vector(-30, 14);
		getPositionButton = new GraphicalButton(game, getPositionButtonPosition, "Positioneur", .62f);
		getPositionButton.addOnClickAction(() -> {
			hasClicked = false;
			showRedSquare = !showRedSquare;
		});

		// reset the camera when clicked
		carmeraResetButtonPosition = new Vector(-21, 14);
		carmeraResetButton = new GraphicalButton(game, carmeraResetButtonPosition, "Reset camera", .62f);
		carmeraResetButton.addOnClickAction(() -> cameraPosition = Vector.ZERO);
	}

	public void update(float deltaTime) {

		// camera controls
		if (game.getKeyboard().get(KeyEvent.VK_W).isDown()) {
			cameraPosition = cameraPosition.add(new Vector(0, deltaTime * cameraSpeed));
		}
		if (game.getKeyboard().get(KeyEvent.VK_S).isDown()) {
			cameraPosition = cameraPosition.add(new Vector(0, -deltaTime * cameraSpeed));
		}
		if (game.getKeyboard().get(KeyEvent.VK_A).isDown()) {
			cameraPosition = cameraPosition.add(new Vector(-deltaTime * cameraSpeed, 0));
		}
		if (game.getKeyboard().get(KeyEvent.VK_D).isDown()) {
			cameraPosition = cameraPosition.add(new Vector(deltaTime * cameraSpeed, 0));
		}
		if (game.getMouse().getMouseScrolledUp()) {
			System.out.println("up");
			zoom += .2f;
			zoom = (zoom + .1f > 2) ? 2f : zoom;
		} else if (game.getMouse().getMouseScrolledDown()) {
			System.out.println("down");
			zoom -= .2f;
			zoom = (zoom - .1f < 0.4f) ? .4f : zoom;
		}
		
		
		gridLine = grid();
		window.setRelativeTransform(Transform.I.scaled(30 * zoom).translated(cameraPosition));

		// right click menu
		actorMenu.update(deltaTime);

		if (showRedSquare && game.getMouse().getLeftButton().isPressed()) {
			hasClicked = true;
			redSquarePosition = game.getMouse().getPosition();
			regSquareGraphics.setRelativeTransform(Transform.I.translated(QuickMafs.floor(redSquarePosition)));
			redSquarePosText
					.setText((int) Math.floor(redSquarePosition.x) + " " + (int) Math.floor(redSquarePosition.y));
		}

		// camera reset button update
		carmeraResetButton.update(deltaTime);
		carmeraResetButton.setPosition(cameraPosition.add(carmeraResetButtonPosition));

		// position when clicked button update
		getPositionButton.update(deltaTime);
		getPositionButton.setPosition(cameraPosition.add(getPositionButtonPosition));

		// current actors update
		for (Actor actor : actors) {
			actor.update(deltaTime);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		actorMenu.draw(canvas);
		for (Actor actor : actors) {
			actor.draw(canvas);
		}

		for (ShapeGraphics sg : gridLine) {
			sg.draw(canvas);
		}
		if (showRedSquare && hasClicked) {
			regSquareGraphics.draw(canvas);
			redSquarePosText.draw(canvas);
		}
		getPositionButton.draw(canvas);
		carmeraResetButton.draw(canvas);
	}

	public ArrayList<Actor> getActors() {
		return null;
	}

	public void addActor(ActorBuilder actor) {
		actors.add(actor);
	}

	private ArrayList<ShapeGraphics> grid() {
		ArrayList<ShapeGraphics> lines = new ArrayList<>();
		Shape t = new Polygon(-lineThickness, -lineNumber / 2, -lineThickness, lineNumber / 2, lineThickness,
				lineNumber / 2, lineThickness, -lineNumber / 2);

		Vector c2 = QuickMafs.floor(cameraPosition);
		for (int i = 0; i < lineNumber; i++) {
			lines.add(new ShapeGraphics(t, Color.BLACK, null, 0, 1, -3));
			lines.get(i).setRelativeTransform(
					Transform.I.translated(new Vector(1f * i - lineNumber / 2, 0)).translated(c2));
		}
		Shape t2 = new Polygon(-lineNumber / 2, -lineThickness, lineNumber / 2, -lineThickness, lineNumber / 2,
				lineThickness, -lineNumber / 2, lineThickness);
		for (int i = 0; i < lineNumber; i++) {
			lines.add(new ShapeGraphics(t2, Color.BLACK, null, 0, 1, -3));
			lines.get(i + lineNumber).setRelativeTransform(
					Transform.I.translated(new Vector(0, 1f * i - lineNumber / 2)).translated(c2));
		}
		return lines;
	}

}

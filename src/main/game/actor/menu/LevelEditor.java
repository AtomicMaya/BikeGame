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

	// list actors
	private ArrayList<ActorBuilder> actors = new ArrayList<>();

	private ActorGame game;
	private ActorMenu actorMenu;
	private Window window;

	// Camera stuff
	private Vector cameraPosition = Vector.ZERO;
	private final float cameraSpeed = 20;
	private float zoom = 1f;
	private float maxPosX = 120;
	private float maxPosY = 30;

	// Grid parameters
	private ArrayList<ShapeGraphics> gridLine = new ArrayList<>();
	private ShapeGraphics axeX, axeY;
	private int lineNumberX = 120, lineNumberY = 66;
	private float lineThickness = .01f;

	// position showing
	private Vector redSquarePosition = Vector.ZERO;
	private ShapeGraphics redSquareGraphics;
	private boolean showRedSquare = false; // true if the red square is show
	private boolean hasClicked = false; // true if we clicked once to get position on screen
	private BetterTextGraphics redSquarePosText;

	// activate/desactivate position pointer
	private GraphicalButton getPositionButton;
	private Vector getPositionButtonPosition;
	private final String getPosButtonText = "Positionneur";

	// reset camera button + position (absolue sur l'ecran)
	private GraphicalButton carmeraResetButton;
	private Vector carmeraResetButtonPosition;
	private final String resetCameraButtonText = "Reset camera";

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
		getPositionButtonPosition = new Vector(-29, 14);
		getPositionButton = new GraphicalButton(game, getPositionButtonPosition, getPosButtonText, 1f);
		getPositionButton.addOnClickAction(() -> {
			hasClicked = false;
			showRedSquare = !showRedSquare;
		});

		// reset the camera when clicked
		carmeraResetButtonPosition = new Vector(-21, 14);
		carmeraResetButton = new GraphicalButton(game, carmeraResetButtonPosition, resetCameraButtonText, 1f);
		carmeraResetButton.addOnClickAction(() -> {
			cameraPosition = Vector.ZERO;
			zoom = 1;
		});
	}

	private void updateButtons(float deltaTime) {
		// camera reset button update
		carmeraResetButton.setText(resetCameraButtonText, .83f * zoom);
		carmeraResetButton.setPosition((carmeraResetButtonPosition).mul(zoom).add(cameraPosition));
		carmeraResetButton.update(deltaTime);

		// position button update
		getPositionButton.setText(getPosButtonText, .83f * zoom);
		getPositionButton.setPosition((getPositionButtonPosition).mul(zoom).add(cameraPosition));
		getPositionButton.update(deltaTime);
	}

	public void update(float deltaTime) {

		// camera controls
		float posX = cameraPosition.x;
		float posY = cameraPosition.y;
		if (game.getKeyboard().get(KeyEvent.VK_W).isDown()) {
			posY += deltaTime * cameraSpeed;
		}
		if (game.getKeyboard().get(KeyEvent.VK_S).isDown()) {
			posY += -deltaTime * cameraSpeed;
		}
		if (game.getKeyboard().get(KeyEvent.VK_A).isDown()) {
			posX += -deltaTime * cameraSpeed;
		}
		if (game.getKeyboard().get(KeyEvent.VK_D).isDown()) {
			posX += deltaTime * cameraSpeed;
		}

		posX = (posX >= maxPosX) ? maxPosX : posX;
		posY = (posY >= maxPosY) ? maxPosY : posY;
		posX = (posX <= -maxPosX) ? -maxPosX : posX;
		posY = (posY <= -maxPosY) ? -maxPosY : posY;
		cameraPosition = new Vector(posX, posY);

		// zoom control
		if (game.getMouse().getMouseScrolledUp()) {
			zoom -= .1f;
			zoom = (zoom < .4f) ? .4f : zoom;
		} else if (game.getMouse().getMouseScrolledDown()) {
			zoom += .1f;
			zoom = (zoom > 2f) ? 2f : zoom;
		}

		// ligne de placement
		gridLine = grid();

		// right click menu
		actorMenu.update(deltaTime);

		// positionneur stuff
		if (showRedSquare && game.getMouse().getLeftButton().isPressed()) {
			hasClicked = true;
			redSquarePosition = game.getMouse().getPosition();
			redSquareGraphics.setRelativeTransform(Transform.I.translated(QuickMafs.floor(redSquarePosition)));
			redSquarePosText
					.setText((int) Math.floor(redSquarePosition.x) + ", " + (int) Math.floor(redSquarePosition.y));
			redSquarePosText.setRelativeTransform(Transform.I.translated(new Vector(1, 1)));
		}

		// static buttons update
		updateButtons(deltaTime);

		// current actors update
		for (Actor actor : actors) {
			actor.update(deltaTime);
		}

		// finalement placement de la camera
		window.setRelativeTransform(Transform.I.scaled(30 * zoom).translated(cameraPosition));

	}

	@Override
	public void draw(Canvas canvas) {
		// draw right click menu
		actorMenu.draw(canvas);
		
		// draw current actors
		for (Actor actor : actors) {
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

		// lignes en |
		Shape t = new Polygon(-lineThickness, -lineNumberY / 2, -lineThickness, lineNumberY / 2, lineThickness,
				lineNumberY / 2, lineThickness, -lineNumberY / 2);

		Vector c2 = QuickMafs.floor(cameraPosition);
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

}

/**
 *	Author: Clément Jeannet
 *	Date: 	3 déc. 2017
 */
package main.game.actor.menu;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

import main.game.ActorGame;
import main.game.GameWithLevelAndMenu;
import main.game.actor.ShapeGraphics;
import main.game.actor.TextGraphics;
import main.game.actor.entities.GraphicalButton;
import main.io.Save;
import main.math.Circle;
import main.math.Polygon;
import main.math.Shape;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;
import main.window.Window;
import main.window.swing.FontList;
import main.window.swing.MyFont;

public class MainMenu extends Menu {

	private Window window;
	private TextGraphics menuMainGraphics;
	private final String menuMainTest = "Menu";
	private ArrayList<ShapeGraphics> graphics = new ArrayList<>();

	private ArrayList<GraphicalButton> buttons = new ArrayList<>();

	// our level buttons
	private ArrayList<GraphicalButton> levelButtons = new ArrayList<>();
	private final float sizeX = 5f;
	private final float sizeY = 2f;
	private final float shiftLB = .3f;
	private final Vector topLeftLB = new Vector(6, -4);

	private GraphicalButton play;
	private ActorGame game;

	private int savePage = 0;

	private final int maxNumberButtonsSave = 9;

	private GraphicalButton left, right;

	private GraphicalButton levelEditorButton;

	private float waitBeforeClick = 0;

	private LevelEditor levelEditor;

	private boolean inLevelEditor = false;

	// if a save is clicked
	private boolean busy = false;

	public MainMenu(GameWithLevelAndMenu game, Window window) {
		super(game, window, true, Color.GRAY, true);
		this.window = window;
		this.game = game;

		float fontSize = 4f;
		Vector anchor = new Vector(.5f, 3 * 4f / fontSize);
		menuMainGraphics = new TextGraphics(menuMainTest, fontSize, Color.BLACK, Color.BLUE, .1f, false, false, anchor,
				1, 1);

		// cadrillage
		float w = .01f;
		int size = 40;
		Shape t = new Polygon(-w, -size / 2, -w, size / 2, w, size / 2, w, -size / 2);

		for (int i = 0; i < size; i++) {
			graphics.add(new ShapeGraphics(t, Color.BLACK, null, 0));
			graphics.get(i).setRelativeTransform(Transform.I.translated(new Vector(1f * i - size / 2, 0)));
		}
		Shape t2 = new Polygon(-size / 2, -w, size / 2, -w, size / 2, w, -size / 2, w);
		for (int i = 0; i < size; i++) {
			graphics.add(new ShapeGraphics(t2, Color.BLACK, null, 0));
			graphics.get(i + size).setRelativeTransform(Transform.I.translated(new Vector(0, 1f * i - size / 2)));
		}

		// get the saves
		File[] list = Save.availableSaves(game);
		for (int i = 0; i < list.length; i++) {
			Vector position = new Vector(-28, -(i % maxNumberButtonsSave) * 2f + 8f);
			buttons.add(new GraphicalButton(game, position, list[i].getName(), 1.4f));
			int p = i;
			buttons.get(i).addOnClickAction(() -> load(list[p]));
			buttons.get(i).forceShape(6, -1);
		}

		// Arrow buttons to navigate in the save menu
		Polygon arrowShape = new Polygon(0, 0, 1, 0, 1, 1, 1, 0);

		right = new GraphicalButton(game, new Vector(-24, -11), 2, 2);
		right.addOnClickAction(() -> PagePlusPlus());

		left = new GraphicalButton(game, new Vector(-28, -11), 2, 2);
		left.addOnClickAction(() -> PageMinusMinus());

		// set arrows graphics
		right.setNewGraphics("./res/images/arrows/right_arrow_dark_green.png",
				"./res/images/arrows/right_arrow_light_green.png");
		left.setNewGraphics("./res/images/arrows/left_arrow_dark_green.png",
				"./res/images/arrows/left_arrow_light_green.png");

		// level editor
		levelEditorButton = new GraphicalButton(game, new Vector(4, 3), "Level Editor", 1.4f);
		// levelEditorButton.forceInbetweenCharOffset(1.4f/2f);
		levelEditorButton.addOnClickAction(() -> {
			levelEditor = new LevelEditor(game, window);
			inLevelEditor = true;
		});

		// play button
		play = new GraphicalButton(game, new Vector(0, 0), "Play", 1f);
		play.setPosition(new Vector(-play.getWidth() / 2f, 0));
		play.addOnClickAction(() -> {
			game.beginLevel(0);
			game.setGameFreezeStatus(false);
			changeStatut();
		});

		// our level
		int n = game.numberOfLevel();
		for (int i = 0; i < n; i++) {
			Vector position = new Vector(topLeftLB.x + (i % 3) * (sizeX + shiftLB), topLeftLB.y - (i / 3) * sizeY);
			levelButtons.add(new GraphicalButton(game, position, "Level" + (i + 1), 1));
			levelButtons.get(i).forceShape(sizeX, -1);
			int temp = i;
			levelButtons.get(i).addOnClickAction(() -> {
				game.beginLevel(temp);
				game.setGameFreezeStatus(false);
				changeStatut();
			});
		}

	}

	private void load(File file) {
		if (!busy && waitBeforeClick > .5f) {

			game.destroyAllActors();
			changeStatut();
			busy = true;
			game.setGameFreezeStatus(false);
			game.load(file.getName());
		}

	}

	private void PagePlusPlus() {
		savePage++;
		if (savePage > buttons.size() % maxNumberButtonsSave)
			savePage--;
	}

	private void PageMinusMinus() {
		savePage--;
		if (savePage < 0)
			savePage++;
	}

	@Override
	public void update(float deltaTime) {

		if (inLevelEditor) {
			levelEditor.update(deltaTime);
			return;
		}
		super.update(deltaTime);

		waitBeforeClick += deltaTime;
		for (int i = 0; i < buttons.size(); i++) {
			// update only the buttons on the current page
			if (i >= savePage * maxNumberButtonsSave && i < (savePage + 1) * maxNumberButtonsSave)
				buttons.get(i).update(deltaTime);
		}
		// update the left/right buttons only of their is too much saves
		if (buttons.size() > maxNumberButtonsSave) {
			left.update(deltaTime);
			right.update(deltaTime);
		}
		for (GraphicalButton gb : levelButtons) {
			gb.update(deltaTime);
		}
		levelEditorButton.update(deltaTime);
		play.update(deltaTime);
	}

	@Override
	public void draw(Canvas canvas) {

		if (inLevelEditor) {
			levelEditor.draw(canvas);
			return;
		}

		super.draw(canvas);
		menuMainGraphics.draw(canvas);
		for (ShapeGraphics sg : graphics) {
			sg.draw(canvas);
		}

		for (int i = 0; i < buttons.size(); i++) {
			// draw only the buttons on the current page
			if (i >= savePage * maxNumberButtonsSave && i < (savePage + 1) * maxNumberButtonsSave)
				buttons.get(i).draw(canvas);
		}
		// draw the left/right buttons only of their is too much saves
		if (buttons.size() > maxNumberButtonsSave) {
			left.draw(canvas);
			right.draw(canvas);
		}
		for (GraphicalButton gb : levelButtons) {
			gb.draw(canvas);
		}
		levelEditorButton.draw(canvas);
		play.draw(canvas);
		canvas.drawShape(new Circle(.2f), Transform.I, Color.MAGENTA, Color.RED, .02f, 1, 10000);
	}

	@Override
	public void setStatut(boolean isOpen) {
		super.setStatut(isOpen);
		busy = !isOpen;
	}

}

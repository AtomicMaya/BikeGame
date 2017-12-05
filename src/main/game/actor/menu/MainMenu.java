/**
 *	Author: Clément Jeannet
 *	Date: 	3 déc. 2017
 */
package main.game.actor.menu;

import main.game.ActorGame;
import main.game.actor.ImageGraphics;
import main.game.actor.ShapeGraphics;
import main.game.actor.TextGraphics;
import main.game.actor.entities.GraphicalButton;
import main.io.Save;
import main.math.Polygon;
import main.math.Shape;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;
import main.window.Window;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class MainMenu extends Menu {

	private Window window;
	private TextGraphics tg;
	private ImageGraphics ig;
	private ArrayList<ShapeGraphics> graphics = new ArrayList<>();

	private ArrayList<GraphicalButton> buttons = new ArrayList<>();

	private ActorGame game;

	private int savePage = 0;

	private final int maxNumberButtonsSave = 9;

	private GraphicalButton left, right;

	private GraphicalButton levelEditorButton;

	private float waitBeforeClick = 0;

	private LevelEditor levelEditor;

	private boolean inLevelEditor = false;

	private boolean busy = false;

	public MainMenu(ActorGame game, Window window) {
		super(game, window, true, Color.GRAY, true);
		this.window = window;
		this.game = game;
		float fontSize = 4f;
		tg = new TextGraphics("Menu", fontSize, Color.BLACK, Color.BLUE, .1f, false, false, new Vector(.5f, fontSize),
				1, 1);

		ig = new ImageGraphics("res/images/box.4.png", 2, 1);
		ig.setAnchor(new Vector(.5f, .5f));

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
		File[] list = Save.availableSaves(new File(game.getSaveDirectory()));

		Polygon buttonShape = new Polygon(0, .1f, 6, .1f, 6, .9f, 0, .9f);
		for (int i = 0; i < list.length; i++) {
			Vector position = new Vector(-12, -(i % maxNumberButtonsSave) * 2f + 2.5f);
			buttons.add(new GraphicalButton(game, position, list[i].getName(), 1.4f));
			int p = i;
			buttons.get(i).addOnClickAction(() -> load(list[p]));
			buttons.get(i).forceShape(6, -1);
		}

		// Arrow buttons to navigate in the save menu
		Polygon arrowShape = new Polygon(0, 0, 1, 0, 1, 1, 1, 0);

		right = new GraphicalButton(game, new Vector(-6, -7), 1, 1);
		right.addOnClickAction(() -> PagePlusPlus());

		left = new GraphicalButton(game, new Vector(-8, -7), 1, 1);
		left.addOnClickAction(() -> PageMinusMinus());

		// set arrows graphics
		right.setNewGraphics("./res/images/arrows/right_arrow_dark_green.png",
				"./res/images/arrows/right_arrow_light_green.png");
		left.setNewGraphics("./res/images/arrows/left_arrow_dark_green.png",
				"./res/images/arrows/left_arrow_light_green.png");

		// level editor
		levelEditor = new LevelEditor(game, window);
		levelEditorButton = new GraphicalButton(game, new Vector(4, 3), "Level Editor", 1f);

		levelEditorButton.addOnClickAction(() -> inLevelEditor = true);
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
		levelEditorButton.update(deltaTime);

	}

	@Override
	public void draw(Canvas canvas) {

		if (inLevelEditor) {
			levelEditor.draw(canvas);
			return;
		}

		super.draw(canvas);
		tg.draw(canvas);
		ig.draw(canvas);
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
		levelEditorButton.draw(canvas);
	}

	@Override
	public void setStatut(boolean isOpen) {
		super.setStatut(isOpen);
		busy = !isOpen;
	}

}

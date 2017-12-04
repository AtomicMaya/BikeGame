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

	private GraphicalButton levelEditor;

	int r = 0;

	public MainMenu(ActorGame game, Window window) {
		super(game, window, true, Color.GRAY);
		this.window = window;
		this.game = game;
		float fontSize = 2f;
		tg = new TextGraphics("Me\nnu", fontSize, Color.BLACK, Color.BLUE, .1f, false, false, new Vector(.5f, fontSize),
				1, 1);

		ig = new ImageGraphics("res/images/box.4.png", 2, 1);
		ig.setAnchor(new Vector(.5f, .5f));

		// cadrillage
		float w = .01f;
		Shape t = new Polygon(-w, -10, -w, 10, w, 10, w, -10);
		for (int i = 0; i < 20; i++) {
			graphics.add(new ShapeGraphics(t, Color.BLACK, null, 0));
			graphics.get(i).setRelativeTransform(Transform.I.translated(new Vector(1f * i - 10, 0)));
		}
		Shape t2 = new Polygon(-10, -w, 10, -w, 10, w, -10, w);
		for (int i = 0; i < 20; i++) {
			graphics.add(new ShapeGraphics(t2, Color.BLACK, null, 0));
			graphics.get(i + 20).setRelativeTransform(Transform.I.translated(new Vector(0, 1f * i - 10)));
		}

		// get tha saves
		File[] list = Save.availableSaves(new File(game.getSaveDirectory()));

		Polygon buttonShape = new Polygon(0, .1f, 4, .1f, 4, .9f, 0, .9f);
		for (int i = 0; i < list.length; i++) {
			Vector position = new Vector(-8, -(i % maxNumberButtonsSave) + 2.5f);
			buttons.add(new GraphicalButton(game, position, buttonShape, list[i].getName(), 6));
			int p = i;
			buttons.get(i).addOnClickAction(() -> load(list[p]), 0f);
		}

		// Arrow buttons to navigate in the save menu
		Polygon arrowShape = new Polygon(0, 0, 1, 0, 1, 1, 1, 0);

		right = new GraphicalButton(game, new Vector(-6, -7), arrowShape, "", 6);
		right.addOnClickAction(() -> PagePlusPlus(), 0f);

		left = new GraphicalButton(game, new Vector(-8, -7), arrowShape, "", 6);
		left.addOnClickAction(() -> PageMinusMinus(), 0f);

		// set arrows graphics
		right.setNewGraphics("./res/images/arrows/right_arrow_dark_green.png",
				"./res/images/arrows/right_arrow_dark_green.png");
        left.setNewGraphics("./res/images/arrows/left_arrow_dark_green.png",
				"./res/images/arrows/left_arrow_light_green.png");

		// level editor
		levelEditor = new GraphicalButton(game, new Vector(4, 3), new Polygon(0, 0, 0, 1, 5, 1, 5, 0), "Level Editor",
				4);

		levelEditor.addOnClickAction(() -> r++, 0);
	}

	private void load(File file) {
		game.destroyAllActors();
		game.load(file.getName());
		changeStatut();
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
		super.update(deltaTime);
		if (this.isOpen()) {
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
			levelEditor.update(deltaTime);
		}
	}

	@Override
	public void draw(Canvas canvas) {
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
		levelEditor.draw(canvas);
	}
}

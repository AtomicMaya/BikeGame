/**
 *	Author: Clément Jeannet
 *	Date: 	3 déc. 2017
 */
package main.game.GUI.menu;

import main.game.GUI.GraphicalButton;
import main.game.GameWithLevelAndMenu;
import main.game.graphics.ShapeGraphics;
import main.game.graphics.TextGraphics;
import main.io.Save;
import main.math.*;
import main.math.Polygon;
import main.math.Shape;
import main.window.Canvas;
import main.window.Window;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

public class MainMenu extends FullScreenMenu {

	// text main menu
	private TextGraphics menuMainGraphics;
	private final String menuMainTest = "Menu";

	// TODO remove this, for placing purpose #grid lines
	private ArrayList<ShapeGraphics> graphics = new ArrayList<>();

	// buttons list
	private ArrayList<GraphicalButton> buttons = new ArrayList<>();

	// our level buttons
	private ArrayList<GraphicalButton> levelButtons = new ArrayList<>();
	private final float sizeX = 5f;
	private final float sizeY = 2f;
	private final float shiftLB = .3f;
	private final Vector topLeftLB = new Vector(6, -4);

	// if a save is clicked
	private boolean busy = false;

	private GraphicalButton play;

	// load saved created with level editor
	private int savePage = 0;
	private final int maxNumberButtonsSave = 9;
	private GraphicalButton left, right; // navigate between the saves
	private float waitBeforeClick = 0; // avoid spam click with this timer

	// level editor
	private GraphicalButton levelEditorButton;
	private LevelEditor levelEditor;
	private boolean inLevelEditor = false;

	/**
	 * Create a MainMenu for a {@linkplain GameWithLevelAndMenu }
	 * 
	 * @param game : {@linkplain GameWithLevelAndMenu} the game in which
	 * {@link this} MainMenu belong
	 * @param window : {@linkplain Window} Window context
	 */
	public MainMenu(GameWithLevelAndMenu game, Window window) {
		super(game, window, true, Color.GRAY);

		// main menu text
		float fontSize = 4f;
		Vector anchor = new Vector(.5f, 3 * 4f / fontSize);
		menuMainGraphics = new TextGraphics(menuMainTest, fontSize, Color.BLACK, Color.BLUE, .1f, false, false, anchor,
				1, 1);

		// TODO remove this grid
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

		// get the saves and create buttons to load them
		File[] list = Save.availableSaves(game);
		for (int i = 0; i < list.length; i++) {
			Vector position = new Vector(-28, -(i % maxNumberButtonsSave) * 2f + 8f);
			buttons.add(new GraphicalButton(game, position, list[i].getName(), 1.4f));
			int p = i;
			buttons.get(i).addOnClickAction(() -> {
				// load a saves
				if (!busy && waitBeforeClick > .5f) {
					game.destroyAllActors();
					
					busy = true;
					
					
					// problematic loading part
					System.out.println("    - Going to load");
					if (game.load(list[p].getName()))
						System.out.println("    - loaded successfully");
					else System.out.println("error");
					
					System.out.println("    - Finish loading");
					game.setGameFreezeStatus(false);
					this.setStatus(false);
				}
			});
			buttons.get(i).forceShape(6, -1);
		}

		// Arrow buttons to navigate in the save menu
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
		levelEditorButton.addOnClickAction(() -> {
			levelEditor = new LevelEditor(game, window);
			inLevelEditor = true;
		});

		// play button
		play = new GraphicalButton(game, new Vector(0, 0), "Play", 1f);
		play.setAnchor(new Vector(-play.getWidth() / 2f, 0));
		play.addOnClickAction(() -> {
			// start the first level
			game.beginLevel(0);
			game.setGameFreezeStatus(false);
			setStatus(false);
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
				this.setStatus(false);
			});
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
	public void update(float deltaTime, float zoom) {

		// if in level editor, don't update the rest
		if (inLevelEditor) {
			levelEditor.update(deltaTime);
			return;
		}
		super.update(deltaTime, zoom);

		waitBeforeClick += deltaTime;
		for (int i = 0; i < buttons.size(); i++) {
			// update only the buttons on the current page
			if (i >= savePage * maxNumberButtonsSave && i < (savePage + 1) * maxNumberButtonsSave)
				buttons.get(i).update(deltaTime, zoom);
		}
		// update the left/right buttons only of their is too much saves
		if (buttons.size() > maxNumberButtonsSave) {
			left.update(deltaTime, zoom);
			right.update(deltaTime, zoom);
		}
		for (GraphicalButton gb : levelButtons) {
			gb.update(deltaTime, zoom);
		}
		levelEditorButton.update(deltaTime, zoom);
		play.update(deltaTime, zoom);
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
	public void setStatus(boolean isOpen) {
		super.setStatus(isOpen);
		busy = !isOpen;
	}

	@Override
	public void destroy() {
		this.left.destroy();
		this.right.destroy();
		for (GraphicalButton gb : this.buttons)
			gb.destroy();
		for (GraphicalButton gb : this.levelButtons)
			gb.destroy();
		this.levelEditorButton.destroy();
		this.play.destroy();
	}

}

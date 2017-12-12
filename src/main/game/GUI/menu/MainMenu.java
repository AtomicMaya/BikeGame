package main.game.GUI.menu;

import main.game.ComplexBikeGame;
import main.game.GUI.GraphicalButton;
import main.game.graphics.TextGraphics;
import main.game.levels.Level;
import main.io.Save;
import main.math.Vector;
import main.window.Canvas;
import main.window.Window;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/** MainMenu of our game */
public class MainMenu extends FullScreenMenu {

	/** The master {@linkplain ComplexBikeGame}. */
	private ComplexBikeGame game;
	// text main menu
	/** {@linkplain TextGraphics} to display {@link #menuMainText}. */
	private TextGraphics menuMainGraphics;
	
	/** Text to display in the {@linkplain TextGraphics}  {@link #menuMainGraphics}. */
	private final String menuMainText = "Menu";
	
	/** {@linkplain Color} of the {@link #menuMainText}. */
	private Color menuColor = new Color(58, 160, 201);

//	// TODO remove this, for placing purpose #grid lines
//	private ArrayList<ShapeGraphics> graphics = new ArrayList<>();

	// buttons list
	/** {@linkplain ArrayList} containing all the loading save {@linkplain GraphicalButton} created by the user. */
	private ArrayList<GraphicalButton> loadingSaveButtons = new ArrayList<>();
	
	/** Absolute position on screen of the top left corner of the first save button */
	private Vector loadingSavePosition = new Vector(-24, 8);
	
	/** {@linkplain ArrayList} containing all the {@linkplain GraphicalButton} to delete the corresponding save. */
	private ArrayList<GraphicalButton> deleteButons = new ArrayList<>();

	// our level buttons
	/** {@linkplain ArrayList} containing all the {@linkplain GraphicalButton} to load our levels. */
	private ArrayList<GraphicalButton> levelButtons = new ArrayList<>();
	
	/** Size of the {@linkplain GraphicalButton} contained in {@link #levelButtons}. */
	private final float sizeX = 5f, sizeY = 2f;
	
	/** Space between the {@linkplain GraphicalButton}. */
	private final float shiftLB = .3f;
	
	/** Top left corner of the first {@linkplain GraphicalButton} of {@link #levelButtons}. */
	private final Vector topLeftLB = new Vector(9, -2);

	// if a save is clicked
	/** Whether this menu is busy, to avoid spam click on {@linkplain GraphicalButton}. */
	private boolean busy = false;

	/** {@linkplain GraphicalButton} to load the first {@linkplain Level}. */
	private GraphicalButton play;

	// load saved created with level editor
	/** Current page of the load save. */
	private int savePage = 0;
	
	/** Maximum number of saves on the {@linkplain Menu}. */
	private final int maxNumberButtonsSave = 9;
	
	/** {@linkplain GraphicalButton} used to move between the pages of the load save, if the number of saved
	 *  {@linkplain GraphicalButton} is bigger then {@value #maxNumberButtonsSave}.
	 *  */
	private GraphicalButton left, right; // navigate between the saves
	
	/** Timer used to avoid spam clicking on the {@linkplain GraphicalButton}. */
	private float waitBeforeClick = 0; // avoid spam click with this timer

	// level editor
	/** Go into the {@linkplain LevelEditor} interface */
	private GraphicalButton levelEditorButton;
	
	/** {@linkplain LevelEditor} where you can create your own levels! */
	private LevelEditor levelEditor;

	/**
	 * Create a MainMenu for a {@linkplain ComplexBikeGame }
	 * 
	 * @param game : {@linkplain ComplexBikeGame} the game in which this
	 * {@linkplain MainMenu} belong
	 * @param window : {@linkplain Window} Window context
	 */
	public MainMenu(ComplexBikeGame game, Window window) {
		super(game, window, true, Color.GRAY);

		this.game = game;

		// main menu text
		float fontSize = 8f;
		Vector anchor = new Vector(.5f, 3 * 4f / fontSize);
		menuMainGraphics = new TextGraphics(menuMainText, fontSize, menuColor, Color.BLACK.brighter(), .01f, true,
				false, anchor, 1, 1);

//		// TODO remove this grid
//		float w = .01f;
//		int size = 40;
//		Shape t = new Polygon(-w, -size / 2, -w, size / 2, w, size / 2, w, -size / 2);
//
//		for (int i = 0; i < size; i++) {
//			graphics.add(new ShapeGraphics(t, Color.BLACK, null, 0));
//			graphics.get(i).setRelativeTransform(Transform.I.translated(new Vector(1f * i - size / 2, 0)));
//		}
//		Shape t2 = new Polygon(-size / 2, -w, size / 2, -w, size / 2, w, -size / 2, w);
//		for (int i = 0; i < size; i++) {
//			graphics.add(new ShapeGraphics(t2, Color.BLACK, null, 0));
//			graphics.get(i + size).setRelativeTransform(Transform.I.translated(new Vector(0, 1f * i - size / 2)));
//		}

		// create the saves buttons
		createSaveButtons();

		// Arrow buttons to navigate in the save menu
		right = new GraphicalButton(game, new Vector(loadingSavePosition.x + 4, -14), 2, 2);
		right.addOnClickAction(() -> PagePlusPlus());

		left = new GraphicalButton(game, new Vector(loadingSavePosition.x, -14), 2, 2);
		left.addOnClickAction(() -> PageMinusMinus());

		// set arrows graphics
		right.setNewGraphics("./res/images/arrows/right_arrow_dark_green.png",
				"./res/images/arrows/right_arrow_light_green.png");
		left.setNewGraphics("./res/images/arrows/left_arrow_dark_green.png",
				"./res/images/arrows/left_arrow_light_green.png");

		// level editor
		levelEditorButton = new GraphicalButton(game, new Vector(topLeftLB.x, topLeftLB.y + 5), "Level Editor", 1.4f);
		levelEditorButton.addOnClickAction(() -> {
			levelEditor = new LevelEditor(game, window, this);
			levelEditor.open();
		});

		// play button
		play = new GraphicalButton(game, new Vector(0, 0), "Play", 3f);
		play.setAnchor(new Vector(-play.getWidth() / 2f, -play.getHeight()/2));
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
			levelButtons.add(new GraphicalButton(game, position, "Level" + (i), 1));
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
		if (savePage > loadingSaveButtons.size() % maxNumberButtonsSave)
			savePage--;
	}

	private void PageMinusMinus() {
		savePage--;
		if (savePage < 0)
			savePage++;
	}

	@Override
	public void update(float deltaTime, float zoom) {
		if (isOpen()) {
			// if in level editor, don't update the rest
			if (levelEditor != null && levelEditor.isOpen()) {
				levelEditor.update(deltaTime);
				return;
			}
			if (levelEditor != null) {
				levelEditor = null;
				createSaveButtons();
			}
			super.update(deltaTime, zoom);
			waitBeforeClick += deltaTime;
			if (savePage > loadingSaveButtons.size() % maxNumberButtonsSave)
				savePage--;
			for (int i = 0; i < loadingSaveButtons.size(); i++) {
				// update only the buttons on the current page
				if (i >= savePage * maxNumberButtonsSave && i < (savePage + 1) * maxNumberButtonsSave) {
					loadingSaveButtons.get(i).update(deltaTime, zoom);
					deleteButons.get(i).update(deltaTime, zoom);
				}
			}
			// update the left/right buttons only of their is too much saves
			if (loadingSaveButtons.size() > maxNumberButtonsSave) {
				left.update(deltaTime, zoom);
				right.update(deltaTime, zoom);
			}
			for (GraphicalButton gb : levelButtons) {
				gb.update(deltaTime, zoom);
			}
			levelEditorButton.update(deltaTime, zoom);
			play.update(deltaTime, zoom);
		}
	}

	@Override
	public void draw(Canvas canvas) {

		if (levelEditor != null && levelEditor.isOpen()) {
			levelEditor.draw(canvas);
			return;
		}

		super.draw(canvas);
		menuMainGraphics.draw(canvas);
//		for (ShapeGraphics sg : graphics) {
//			sg.draw(canvas);
//		}

		for (int i = 0; i < loadingSaveButtons.size(); i++) {
			// draw only the buttons on the current page
			if (i >= savePage * maxNumberButtonsSave && i < (savePage + 1) * maxNumberButtonsSave) {
				loadingSaveButtons.get(i).draw(canvas);
				deleteButons.get(i).draw(canvas);
			}
		}
		// draw the left/right buttons only of their is too much saves
		if (loadingSaveButtons.size() > maxNumberButtonsSave) {
			left.draw(canvas);
			right.draw(canvas);
		}
		for (GraphicalButton gb : levelButtons) {
			gb.draw(canvas);
		}
		levelEditorButton.draw(canvas);
		play.draw(canvas);
//		canvas.drawShape(new Circle(.2f), Transform.I, Color.MAGENTA, Color.RED, .02f, 1, 10000);
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
		for (GraphicalButton gb : this.loadingSaveButtons)
			gb.destroy();
		for (GraphicalButton gb : this.levelButtons)
			gb.destroy();
		for (GraphicalButton gb : this.deleteButons)
			gb.destroy();
		this.levelEditorButton.destroy();
		this.play.destroy();
	}

	/** Create the different save {@linkplain GraphicalButton}. */
	private void createSaveButtons() {
		loadingSaveButtons.clear();
		deleteButons.clear();
		ArrayList<GraphicalButton> tempSaveButons = new ArrayList<>();
		ArrayList<GraphicalButton> tempDeleteButons = new ArrayList<>();

		// get the saves and create buttons to load them
		File[] list = Save.availableSaves(getOwner());
		for (int i = 0; i < list.length; i++) {
			Vector position = new Vector(loadingSavePosition.x, -(i % maxNumberButtonsSave) * 2.4f + loadingSavePosition.y);
			tempSaveButons.add(new GraphicalButton(getOwner(), position, list[i].getName(), 1.4f));
			int p = i;
			tempSaveButons.get(i).addOnClickAction(() -> {
				// load a saves
				if (!busy && waitBeforeClick > .5f) {
					getOwner().destroyAllActors();

					busy = true;

					// problematic loading part
					System.out.println("    - Going to load");
					if (getOwner().load(list[p].getName())) {
						getOwner().getGameManager().setGameState(game, list[p].getName());
						System.out.println("    - loaded successfully");
					} else
						System.out.println("error");

					System.out.println("    - Finish loading");
					getOwner().setGameFreezeStatus(false);
					this.setStatus(false);
				}
			});
			tempSaveButons.get(i).forceShape(6, -1);
			tempDeleteButons.add(new GraphicalButton(getOwner(), position.add(-1.5f, .6f), 1, 1));
			tempDeleteButons.get(i).setNewGraphics("res/images/delete.png", "res/images/delete_hover.png");
			tempDeleteButons.get(i).addOnClickAction(() -> {
				Save.deleteDirectory(new File(list[p].getPath()));
				createSaveButtons();
			});
			tempDeleteButons.get(i).setDepth(1337);
		}
		loadingSaveButtons.addAll(tempSaveButons);
		deleteButons.addAll(tempDeleteButons);
	}

}

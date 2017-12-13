/**
 * Author: Clément Jeannet Date: 3 déc. 2017
 */
package main.game.GUI.menu;

import main.game.ComplexBikeGame;
import main.game.GUI.Comment;
import main.game.GUI.GraphicalButton;
import main.math.Vector;
import main.window.Canvas;
import main.window.Window;

import java.awt.*;
import java.awt.event.KeyEvent;

/** In game {@linkplain Menu} */
public class PauseMenu extends FullScreenMenu {

	/** {@linkplain GraphicalButton} used in this {@linkplain PauseMenu} */
	private GraphicalButton close, backToMainMenu, save;

	/** {@linkplain Comment} of {@link #save} */
	private Comment saveComment;

	/** Text display by the {@linkplain Comment} {@link #saveComment} */
	private String saveCommentText = "Saved sucessfully";

	/** Timer for the display of the {@linkplain Comment} */
	private float saveTimer = 0;

	/** Whether the {@linkplain Comment} is display */
	private boolean display = false;

	/**
	 * Create a new {@linkplain PauseMenu}
	 * @param game The {@linkplain ComplexBikeGame} where this
	 * {@linkplain PauseMenu} live
	 * @param window : The window where to draw this menu.
	 */
	public PauseMenu(ComplexBikeGame game, Window window) {
		super(game, window, false, Color.GRAY);

		close = new GraphicalButton(game, new Vector(8, -10), "Close", 2);
		close.setNewGraphics("./res/images/button.white.1.png", "./res/images/button.white.2.png");
		close.addOnClickAction(() -> changeStatus(), .1f);

		backToMainMenu = new GraphicalButton(game, new Vector(-15, -10), "Back to Menu", 2f);
		backToMainMenu.addOnClickAction(() -> {
			game.getGameManager().setLastCheckpoint(null);
			game.getGameManager().setStartCheckpoint(null);
			game.destroyAllActors();
			game.goToMainMenu();
		});

//		save = new GraphicalButton(game, new Vector(0, 0), "Save", 2);
//		save.addOnClickAction(() -> {
//			if (game.saveCurrentActors())
//				display = true;
//		});
//		saveComment = new Comment(game, saveCommentText);

	}

	@Override
	public void update(float deltaTime, float zoom) {
		super.update(deltaTime, zoom);

		if (getOwner().getKeyboard().get(KeyEvent.VK_M).isPressed()
				|| getOwner().getKeyboard().get(KeyEvent.VK_ESCAPE).isPressed()
				|| getOwner().getKeyboard().get(KeyEvent.VK_P).isPressed()) {
			changeStatus();
		}
		if (isOpen()) {
			close.update(deltaTime, zoom);
			backToMainMenu.update(deltaTime, zoom);
//			save.update(deltaTime, zoom);
//			saveComment.update(deltaTime, zoom);
//			if (display) {
//				saveTimer += deltaTime;
//				if (saveTimer > 2) {
//					saveTimer = 0;
//					display = false;
//				}
//			}

		}
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (isOpen()) {
			close.draw(canvas);
			backToMainMenu.draw(canvas);
//			save.draw(canvas);
//			if (display)
//				saveComment.draw(canvas);
		}
	}

	@Override
	public void changeStatus() {
		super.changeStatus();
		getOwner().setGameFreezeStatus(isOpen());
	}

	@Override
	public void destroy() {
		this.backToMainMenu.destroy();
		this.close.destroy();
	}

}

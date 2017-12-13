/**
 * Author: Clément Jeannet Date: 3 déc. 2017
 */
package main.game.GUI.menu;

import main.game.ComplexBikeGame;
import main.game.GUI.GraphicalButton;
import main.game.graphics.TextGraphics;
import main.math.Vector;
import main.window.Canvas;
import main.window.Window;

import java.awt.*;
import java.awt.event.KeyEvent;

/** In game {@linkplain Menu} */
public class PauseMenu extends FullScreenMenu {

	/** {@linkplain GraphicalButton} used in this {@linkplain PauseMenu} */
	private GraphicalButton close, backToMainMenu, save;

	/** {@linkplain TextGraphics} to display {@link #menuMainText}. */
	private TextGraphics menuPauseGraphics;
	
	/** Text to display in the {@linkplain TextGraphics}  {@link #menuMainGraphics}. */
	private final String menuPauseText = "Pause";
	
	/** {@linkplain Color} of the {@link #menuMainText}. */
	private Color menuColor = new Color(58, 160, 201);
	
	/**
	 * Create a new {@linkplain PauseMenu}
	 * @param game The {@linkplain ComplexBikeGame} where this
	 * {@linkplain PauseMenu} live
	 * @param window : The window where to draw this menu.
	 */
	public PauseMenu(ComplexBikeGame game, Window window) {
		super(game, window, false, Color.GRAY);

		float fontSize = 8f;
		Vector anchor = new Vector(.5f, 3 * 4f / fontSize);
		menuPauseGraphics = new TextGraphics(menuPauseText, fontSize, menuColor, Color.BLACK.brighter(), .01f, true,
				false, anchor, 1, 1);
		
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
		}
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (isOpen()) {
			close.draw(canvas);
			backToMainMenu.draw(canvas);
			menuPauseGraphics.draw(canvas);
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

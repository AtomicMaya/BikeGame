/**
 * Author: Clément Jeannet Date: 3 déc. 2017
 */
package main.game.GUI.menu;

import main.game.ComplexBikeGame;
import main.game.GUI.GraphicalButton;
import main.math.Vector;
import main.window.Canvas;
import main.window.Window;

import java.awt.*;
import java.awt.event.KeyEvent;

/** In game {@linkplain Menu} */
public class InGameMenu extends FullScreenMenu {

	/** {@linkplain GraphicalButton} used in this {@linkplain InGameMenu} */
	private GraphicalButton close, backToMainMenu;

	/** Create a new {@linkplain InGameMenu}
	 * @param game The {@linkplain ComplexBikeGame} where this {@linkplain InGameMenu} live 
	 * @param window : The window where to draw this menu.
	 * */
	public InGameMenu(ComplexBikeGame game, Window window) {
		super(game, window, false, Color.GRAY);

		close = new GraphicalButton(game, new Vector(8, -10), "Close", 2);

		close.setNewGraphics("./res/images/button.white.1.png", "./res/images/button.white.2.png");
		close.addOnClickAction(() -> changeStatus(), .1f);

		backToMainMenu = new GraphicalButton(game, new Vector(-15, -10), "Back to Menu", 2f);
		backToMainMenu.addOnClickAction(() -> {
			game.destroyAllActors();
			game.goToMainMenu();
		});
	}

	@Override
	public void update(float deltaTime, float zoom) {
		super.update(deltaTime, zoom);

		if (getOwner().getKeyboard().get(KeyEvent.VK_M).isPressed() || getOwner().getKeyboard().get(KeyEvent.VK_ESCAPE).isPressed()
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

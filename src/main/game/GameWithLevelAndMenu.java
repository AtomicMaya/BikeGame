/**
 *	Author: Clément Jeannet
 *	Date: 	3 déc. 2017
 */
package main.game;

import main.game.GUI.menu.InGameMenu;
import main.game.GUI.menu.MainMenu;
import main.io.FileSystem;
import main.window.Window;

/**
 * Represent a game with different level, and some menus
 * 
 * @see GameWithLevels
 */
public abstract class GameWithLevelAndMenu extends GameWithLevels {

	private InGameMenu ingameMenu;
	private MainMenu mainMenu;
	private Window window;

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);

		this.window = window;

		ingameMenu = new InGameMenu(this, window);

		mainMenu = new MainMenu(this, window);

		return true;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		mainMenu.update(deltaTime, 1);
		if (mainMenu.isOpen()) {
			mainMenu.draw(window);
			return;
		}
		ingameMenu.update(deltaTime, 1);
		if (ingameMenu.isOpen())
			ingameMenu.draw(window);

	}

	/** Go to the MainMenu */
	public void goToMainMenu() {
		this.destroyAllActors();
		mainMenu.setStatus(true);
		ingameMenu.setStatus(false);
	}
}

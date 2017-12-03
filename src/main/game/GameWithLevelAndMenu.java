/**
 *	Author: Clément Jeannet
 *	Date: 	3 déc. 2017
 */
package main.game;

import main.game.actor.menu.InGameMenu;
import main.game.actor.menu.MainMenu;
import main.io.FileSystem;
import main.window.Window;

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
		mainMenu.update(deltaTime);
		if (mainMenu.isOpen()) {
			mainMenu.draw(window);
			return;
		}
		ingameMenu.update(deltaTime);
		if (ingameMenu.isOpen())
			ingameMenu.draw(window);
		
	}
	public void goToMainMenu() {
		this.destroyAllActors();
		mainMenu.setStatut(true);
		ingameMenu.setStatut(false);
	}
}

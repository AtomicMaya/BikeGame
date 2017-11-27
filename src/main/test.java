/**
 *	Author: Clément Jeannet
 *	Date: 	23 nov. 2017
 */
package main;

import main.game.Game;
import main.game.actor.Actor;
import main.game.actor.ActorGame;
import main.game.actor.TestGame;
import main.game.actor.myEntities.Cinematic;
import main.io.DefaultFileSystem;
import main.io.FileSystem;
import main.io.FolderFileSystem;
import main.io.ResourceFileSystem;
import main.window.Window;
import main.window.swing.SwingWindow;

public class test {

	/** Maximal time step allowed for a single frame. */
	public static final float MAX_DELTA_TIME = 0.1f;

	public static boolean ended = false;

	static Cinematic cinematics;
static FileSystem fileSystem;

	static Window window;
	
	static ActorGame game;
	public static void main(String[] args) {
		// Define cascading file system
		fileSystem = new FolderFileSystem(new ResourceFileSystem(DefaultFileSystem.INSTANCE));

		// Use Swing display
		window = new SwingWindow("Play", fileSystem);
		 game = new TestGame();
		cinematics = new Cinematic();
		try {
			run(window, fileSystem, game, cinematics);

		} finally {
			// Release resources
			window.dispose();
		}
	}

	public static void run(Window window, FileSystem fileSystem, ActorGame game, ActorGame cinematics) {

		if (game.begin(window, fileSystem)) {

			// Use system clock to keep track of time progression
			long before;
			long now = System.nanoTime();

			// Run until the user try to close the window
			while (!window.isCloseRequested() && !ended) {

				// Compute time interval
				before = now;
				now = System.nanoTime();

				float deltaTime = (now - before);

				try {
					int timeDiff = Math.max(0, (int) (1E9 / 300 - deltaTime));
					Thread.sleep((int) (timeDiff / 1E6), (int) (timeDiff % 1E6));
				} catch (InterruptedException e) {
				}

				now = System.nanoTime();
				deltaTime = (now - before) / 1E9f;

				// Clip time interval
				if (deltaTime > MAX_DELTA_TIME) {
					System.out.println("Can't keep up! " + deltaTime);
					deltaTime = MAX_DELTA_TIME;

				}

				// Let the game do its stuff
				if (!game.isGameFrozen())
					game.update(deltaTime);
				else
					cinematics.update(deltaTime);
				// Render and update input
				window.update();
			}

		}
		game.end();

		ended = false;

	}

	public static void setEnded(boolean end) {
		ended = end;
	}
	
	public static void startCinematics() {
		cinematics.begin(window, fileSystem, game);
	}

}

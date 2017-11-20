package main;

import main.game.Game;
import main.game.tutorial.ContactGame;
import main.game.tutorial.HelloWorldGame;
import main.game.tutorial.PolyLineGame;
import main.game.tutorial.RopeGame;
import main.game.tutorial.ScaleGame;
import main.game.tutorial.SimpleCrateGame;
import main.io.DefaultFileSystem;
import main.io.FileSystem;
import main.io.FolderFileSystem;
import main.io.ResourceFileSystem;
import main.window.Window;
import main.window.swing.SwingWindow;

/**
 * Main entry point.
 */
@SuppressWarnings("unused")
public class Program {

	/** Maximal time step allowed for a single frame. */
	public static final float MAX_DELTA_TIME = 0.1f;

	/**
	 * Main entry point.
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {

		// Define cascading file system
		FileSystem fileSystem = new FolderFileSystem(new ResourceFileSystem(DefaultFileSystem.INSTANCE));

		// Use Swing display
		Window window = new SwingWindow("Play", fileSystem);
		try {

			// Create a demo game
			// Game game = new HelloWorldGame();
			// Game game = new SimpleCrateGame();
			// Game game = new RopeGame();
			// Game game = new ScaleGame();
			// Game game = new ContactGame();
			Game game = new PolyLineGame();
			if (game.begin(window, fileSystem)) {

				// Use system clock to keep track of time progression
				long before;
				long now = System.nanoTime();

				// Run until the user try to close the window
				while (!window.isCloseRequested()) {

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
						deltaTime = MAX_DELTA_TIME;
						System.out.println("Can't keep up!");
					}

					// Let the game do its stuff
					game.update(deltaTime);

					// Render and update input
					window.update();
				}

			}
			game.end();

		} finally {

			// Release resources
			window.dispose();

		}
	}

}

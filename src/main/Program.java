package main;

import main.game.BikeGame;
import main.game.Game;
import main.io.DefaultFileSystem;
import main.io.FileSystem;
import main.io.FolderFileSystem;
import main.io.ResourceFileSystem;
import main.window.Window;
import main.window.swing.SwingWindow;

/**
 * Main entry point.
 */
public class Program {

	/** Maximal time step allowed for a single frame. */
	public static final float MAX_DELTA_TIME = 0.1f;

	/**
	 * Main entry point.
	 * @param args Ignored
	 */
	public static void main(String[] args) {

		// Define cascading file system
		FileSystem fileSystem = new FolderFileSystem(new ResourceFileSystem(DefaultFileSystem.INSTANCE));

		// Use Swing display
		Window window = new SwingWindow("See you next semester to the MAN !", fileSystem);

		try {

			// Create a demo game
			// Game game = new HelloWorldGame();
			// Game game = new SimpleCrateGame();
			// Game game = new RopeGame();
			// Game game = new ScaleGame();
			// Game game = new ContactGame();
			// Game game = new PolyLineGame();
			// Game game = new CrateGame();

			// Game game = new TestGame();
			 Game game = new BikeGame();
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
					} catch (InterruptedException ignored) {}

					now = System.nanoTime();
					deltaTime = (now - before) / 1E9f;

					// Clip time interval
					if (deltaTime > MAX_DELTA_TIME) {
						System.out.println("Can't keep up! " + deltaTime);
						deltaTime = MAX_DELTA_TIME;

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

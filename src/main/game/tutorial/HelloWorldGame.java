package main.game.tutorial;

import main.game.Game;
import main.game.graphics.ImageGraphics;
import main.io.FileSystem;
import main.math.*;
import main.window.Window;

/**
 * Simple game, to show basic the basic architecture
 */
public class HelloWorldGame implements Game {

	// Store context
	private Window window;

	// We need our physics engine
	private World world;

	// And we need to keep references on our game objects
	private Entity body;

	private ImageGraphics bodyGraphics, bowGraphics;

	// This event is raised when game has just started
	@Override
	public boolean begin(Window window, FileSystem fileSystem) {

		// Store context
		this.window = window;

		world = new World();
		world.setGravity(new Vector(0.0f, -9.81f));

		EntityBuilder entityBuilder = world.createEntityBuilder();
		entityBuilder.setFixed(true);
		entityBuilder.setPosition(new Vector(2f, -1.5f));
		body = entityBuilder.build();

		bodyGraphics = new ImageGraphics("res/images/stone.broken.4.png", 1, 1);
		bodyGraphics.setParent(body);
		bodyGraphics.setAlpha(1.0f);
		bodyGraphics.setDepth(0.0f);
		bodyGraphics.setParent(body);

		bowGraphics = new ImageGraphics("res/images/bow.png", 1, 1);
		bowGraphics.setAlpha(1.0f);
		bowGraphics.setDepth(1.0f);

		bowGraphics.setParent(body);

		// Successfully initiated
		return true;
	}

	// This event is called at each frame
	@Override
	public void update(float deltaTime) {
		window.setRelativeTransform(Transform.I.scaled(7.0f));

		world.update(deltaTime);
		
		bodyGraphics.draw(window);
		bowGraphics.draw(window);
		// The actual rendering will be done now, by the program loop
	}

	// This event is raised after game ends, to release additional resources
	@Override
	public void end() {
		// Empty on purpose, no cleanup required yet
	}

}

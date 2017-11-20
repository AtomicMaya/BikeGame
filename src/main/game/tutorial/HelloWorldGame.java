package main.game.tutorial;

import main.game.Game;
import main.game.actor.ImageGraphics;
import main.io.FileSystem;
import main.math.Entity;
import main.math.EntityBuilder;
import main.math.Transform;
import main.math.Vector;
import main.math.World;
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

	private ImageGraphics g, g2;

	private float alpha = 0f;
	private boolean increasing = true;

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

		g = new ImageGraphics("res/stone.broken.4.png", 1, 1);
		g.setParent(body);
		g.setAlpha(0.5f);
		g.setDepth(0.0f);
		g.setParent(body);

		g2 = new ImageGraphics("res/bow.png", 1, 1);
		g2.setAlpha(0.5f);
		g2.setDepth(1.0f);

		g2.setParent(body);

		// Successfully initiated
		return true;
	}

	// This event is called at each frame
	@Override
	public void update(float deltaTime) {
		window.setRelativeTransform(Transform.I.scaled(7.0f));

		if (increasing) {
			if (alpha < 2)
				alpha += 0.01f;
			else
				increasing = false;
		} else {
			if (alpha > -1f)
				alpha -= 0.01f;
			else
				increasing = true;
		}

		world.update(deltaTime);

		g2.setAlpha(alpha);
		g.setAlpha(1f - alpha);
		
		g.draw(window);
		g2.draw(window);
		// The actual rendering will be done now, by the program loop
	}

	// This event is raised after game ends, to release additional resources
	@Override
	public void end() {
		// Empty on purpose, no cleanup required yet
	}

}

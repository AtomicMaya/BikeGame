/**
 *	Author: Clément Jeannet
 *	Date: 	19 nov. 2017
 */
package main.game.tutorial;

import main.game.Game;
import main.game.actor.ImageGraphics;
import main.game.actor.ShapeGraphics;
import main.io.FileSystem;
import main.math.*;
import main.math.Polygon;
import main.window.Window;

import java.awt.*;

/**
 * Simple game, to show basic the basic architecture
 */
public class ContactGame implements Game {

	// Store context
	private Window window;

	// We need our physics engine
	private World world;

	// And we need to keep references on our game objects
	private Entity block, ball;

	private ImageGraphics blockGraphics;
	private ShapeGraphics ballGraphics;

	private BasicContactListener contactListener;
	// This event is raised when game has just started

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {

		// Store context
		this.window = window;

		world = new World();
		world.setGravity(new Vector(0.0f, -9.81f));

		// Create the block
		EntityBuilder entityBuilder = world.createEntityBuilder();
		entityBuilder.setFixed(true);
		entityBuilder.setPosition(new Vector(-5.0f, -1.0f));
		block = entityBuilder.build();

		PartBuilder partBuilder = block.createPartBuilder();
		Polygon polygon = new Polygon(new Vector(0.0f, 0.0f), new Vector(10.0f, 0.0f), new Vector(10.0f, 1.0f),
				new Vector(0.0f, 1.0f));
		partBuilder.setShape(polygon);
		partBuilder.build();

		blockGraphics = new ImageGraphics("res/stone.broken.4.png", 10, 1);
		blockGraphics.setParent(block);

		// ball
		float radius = .5f;
		entityBuilder = world.createEntityBuilder();
		entityBuilder.setFixed(false);
		entityBuilder.setPosition(new Vector(0.5f, 4.0f));
		ball = entityBuilder.build();

		Circle circle = new Circle(radius);

		partBuilder = ball.createPartBuilder();
		partBuilder.setShape(circle);
		partBuilder.setFriction(.4f);
		partBuilder.build();

		ballGraphics = new ShapeGraphics(circle, Color.BLUE, Color.BLUE, .1f, 1f, 0);
		ballGraphics.setParent(ball);

		// add a contact listener
		contactListener = new BasicContactListener();
		ball.addContactListener(contactListener);
		return true;
	}

	// This event is called at each frame
	@Override
	public void update(float deltaTime) {
		// Set camera to origin with zoom 10
		window.setRelativeTransform(Transform.I.scaled(10.0f));
		world.update(deltaTime);

		int numberOfCollisions = contactListener.getEntities().size();
		if (numberOfCollisions > 0) {
			ballGraphics.setFillColor(Color.RED);
		}

		// The actual rendering will be done now, by the program loop
		ballGraphics.draw(window);
		blockGraphics.draw(window);
	}

	// This event is raised after game ends, to release additional resources
	@Override
	public void end() {
		// Empty on purpose, no cleanup required yet
	}

}

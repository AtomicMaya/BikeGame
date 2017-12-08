/**
 *	Author: Clément Jeannet
 *	Date: 	19 nov. 2017
 */
package main.game.tutorial;

import main.game.Game;
import main.game.graphics.ImageGraphics;
import main.game.graphics.ShapeGraphics;
import main.io.FileSystem;
import main.math.*;
import main.math.Polygon;
import main.window.Window;

import java.awt.*;

/**
 * Simple game, to show basic the basic architecture
 */
public class RopeGame implements Game {

	// Store context
	private Window window;

	// We need our physics engine
	private World world;

	// And we need to keep references on our game objects
	private Entity block, ball;

	private ImageGraphics blockGraphics;
	private ShapeGraphics ballGraphics;

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
		entityBuilder.setPosition(new Vector(1.0f, 0.5f));
		block = entityBuilder.build();

		PartBuilder partBuilder = block.createPartBuilder();
		Polygon polygon = new Polygon(new Vector(0.0f, 0.0f), new Vector(1.0f, 0.0f), new Vector(1.0f, 1.0f),
				new Vector(0.0f, 1.0f));
		partBuilder.setShape(polygon);
		partBuilder.build();

		blockGraphics = new ImageGraphics("res/images/stone.broken.4.png", 1, 1);
		blockGraphics.setParent(block);

		// Create the ball
		entityBuilder = world.createEntityBuilder();
		entityBuilder.setFixed(false);
		entityBuilder.setPosition(new Vector (0.6f, 4.0f) );
		ball = entityBuilder.build();

		partBuilder = ball.createPartBuilder();
		Circle circle = new Circle(.6f);
		partBuilder.setShape(circle);
		partBuilder.build();

		ballGraphics = new ShapeGraphics(circle, Color.BLUE, Color.RED, .1f, 1.f, 0);
		ballGraphics.setParent(ball);

		// Link them
		RopeConstraintBuilder ropeConstraintBuilder = world.createRopeConstraintBuilder();
		ropeConstraintBuilder.setFirstEntity(block);
		ropeConstraintBuilder.setFirstAnchor(new Vector(.5f, .5f));
		ropeConstraintBuilder.setSecondEntity(ball);
		ropeConstraintBuilder.setSecondAnchor(Vector.ZERO);
		ropeConstraintBuilder.setMaxLength(6.0f);
		ropeConstraintBuilder.setInternalCollision(true);
		ropeConstraintBuilder.build();

		// Successfully initiated
		return true;
	}

	// This event is called at each frame
	@Override
	public void update(float deltaTime) {
		// Set camera to origin with zoom 10
		window.setRelativeTransform(Transform.I.scaled(10.0f));
		world.update(deltaTime);

		// The actual rendering will be done now, by the program loop
		blockGraphics.draw(window);
		ballGraphics.draw(window);
	}

	// This event is raised after game ends, to release additional resources
	@Override
	public void end() {
		// Empty on purpose, no cleanup required yet
	}

}

/**
 *	Author: Clément Jeannet
 *	Date: 	19 nov. 2017
 */
package main.game.tutorial;

import java.awt.Color;

import main.game.Game;
import main.game.actor.ImageGraphics;
import main.game.actor.ShapeGraphics;
import main.io.FileSystem;
import main.math.Circle;
import main.math.Entity;
import main.math.EntityBuilder;
import main.math.PartBuilder;
import main.math.Polygon;
import main.math.RopeConstraintBuilder;
import main.math.Transform;
import main.math.Vector;
import main.math.World;
import main.window.Window;

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

	private ImageGraphics blockAsset;
	private ShapeGraphics ballGraphics;

	// This event is raised when game has just started
	@Override
	public boolean begin(Window window, FileSystem fileSystem) {

		// Store context
		this.window = window;

		world = new World();
		world.setGravity(new Vector(0.0f, -9.81f));

		// Create the block
		EntityBuilder ebBlock = world.createEntityBuilder();
		ebBlock.setFixed(true);
		ebBlock.setPosition(new Vector(1.0f, 0.5f));
		block = ebBlock.build();

		PartBuilder pbBlock = block.createPartBuilder();
		Polygon polygon = new Polygon(new Vector(0.0f, 0.0f), new Vector(1.0f, 0.0f), new Vector(1.0f, 1.0f),
				new Vector(0.0f, 1.0f));
		pbBlock.setShape(polygon);
		pbBlock.build();

		blockAsset = new ImageGraphics("res/stone.broken.4.png", 1, 1);
		blockAsset.setParent(block);

		// Create the ball
		EntityBuilder ebBall = world.createEntityBuilder();
		ebBall.setFixed(false);
		ebBall.setPosition(new Vector (0.6f, 4.0f) );
		ball = ebBall.build();

		PartBuilder pbBall = ball.createPartBuilder();
		Circle circle = new Circle(.6f);
		pbBall.setShape(circle);
		pbBall.build();

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
		blockAsset.draw(window);
		ballGraphics.draw(window);
	}

	// This event is raised after game ends, to release additional resources
	@Override
	public void end() {
		// Empty on purpose, no cleanup required yet
	}

}

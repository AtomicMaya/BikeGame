/**
 *	Author: Clément Jeannet
 *	Date: 	19 nov. 2017
 */
package main.game.tutorial;

import java.awt.event.KeyEvent;

import main.game.Game;
import main.game.actor.ImageGraphics;
import main.io.FileSystem;
import main.math.Circle;
import main.math.Entity;
import main.math.EntityBuilder;
import main.math.PartBuilder;
import main.math.Polygon;
import main.math.RevoluteConstraintBuilder;
import main.math.Transform;
import main.math.Vector;
import main.math.World;
import main.window.Window;

/**
 * Simple game, to show basic the basic architecture
 */
public class ScaleGame implements Game {

	// Store context
	private Window window;

	// We need our physics engine
	private World world;

	// And we need to keep references on our game objects
	private Entity block, plank, ball;
	// private ImageGraphics image;

	private ImageGraphics blockGraphics, plankGRaphics, ballGraphics;

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
		ebBlock.setPosition(new Vector(-5.0f, -1.0f));
		block = ebBlock.build();

		PartBuilder pbBlock = block.createPartBuilder();
		Polygon polygon = new Polygon(new Vector(0.0f, 0.0f), new Vector(10.0f, 0.0f), new Vector(10.0f, 1.0f),
				new Vector(0.0f, 1.0f));
		pbBlock.setShape(polygon);
		pbBlock.build();

		blockGraphics = new ImageGraphics("res/stone.broken.4.png", 10, 1);
		blockGraphics.setParent(block);

		// Create the plank
		EntityBuilder ebPlank = world.createEntityBuilder();
		ebPlank.setFixed(false);
		plank = ebPlank.build();

		PartBuilder pbPlank = plank.createPartBuilder();
		Polygon p2 = new Polygon(new Vector(0.0f, 0.0f), new Vector(5.0f, 0f), new Vector(5.0f, .2f),
				new Vector(0.0f, .2f));
		pbPlank.setShape(p2);
		pbPlank.build();

		plankGRaphics = new ImageGraphics("res/wood.4.png", 5, .2f);
		plankGRaphics.setParent(plank);

		// ball
		float radius = .5f;
		EntityBuilder entityBuilder = world.createEntityBuilder();
		entityBuilder.setFixed(false);
		entityBuilder.setPosition(new Vector(0.5f, 4.0f));
		ball = entityBuilder.build();

		Circle circle = new Circle(radius);

		PartBuilder partBuilder = ball.createPartBuilder();
		partBuilder.setShape(circle);
		partBuilder.setFriction(.4f);
		partBuilder.build();

		ballGraphics = new ImageGraphics("res/explosive.11.png", radius * 2f, radius * 2f, new Vector(.5f, .5f));
		ballGraphics.setParent(ball);

		// link them
		RevoluteConstraintBuilder revoluteConstraintBuilder = world.createRevoluteConstraintBuilder();
		revoluteConstraintBuilder.setFirstEntity(block);
		revoluteConstraintBuilder.setFirstAnchor(new Vector(10f / 2, (1f * 7) / 4));
		revoluteConstraintBuilder.setSecondEntity(plank);
		revoluteConstraintBuilder.setSecondAnchor(new Vector(5f / 2, .2f / 2));
		revoluteConstraintBuilder.setInternalCollision(true);
		revoluteConstraintBuilder.build();
		return true;
	}

	// This event is called at each frame
	@Override
	public void update(float deltaTime) {
		// Set camera to origin with zoom 10
		window.setRelativeTransform(Transform.I.scaled(10.0f));
		world.update(deltaTime);

		// Keyboard Control
		if (window.getKeyboard().get(KeyEvent.VK_LEFT).isDown()) {
			ball.applyAngularForce(10.0f);
		} else if (window.getKeyboard().get(KeyEvent.VK_RIGHT).isDown()) {
			ball.applyAngularForce(-10.0f);
		}

		// The actual rendering will be done now, by the program loop
		ballGraphics.draw(window);
		blockGraphics.draw(window);
		plankGRaphics.draw(window);
	}

	// This event is raised after game ends, to release additional resources
	@Override
	public void end() {
		// Empty on purpose, no cleanup required yet
	}

}

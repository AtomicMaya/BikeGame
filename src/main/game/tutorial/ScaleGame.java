/**
 *	Author: Clément Jeannet
 *	Date: 	19 nov. 2017
 */
package main.game.tutorial;

import java.awt.event.KeyEvent;

import main.game.Game;
import main.io.FileSystem;
import main.math.Entity;
import main.math.PartBuilder;
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
	private Storage storage;

	// This event is raised when game has just started
	@Override
	public boolean begin(Window window, FileSystem fileSystem) {

		// Store context
		this.window = window;

		world = new World();
		world.setGravity(new Vector(0.0f, -9.81f));
		storage = new Storage(world);

		storage.newRectangle(new Vector(-5.0f, -1.0f), "res/stone.broken.4.png", 10f, 1f, true, 0);
		storage.newRectangle(new Vector(-5f, 0.8f), "res/wood.3.png", 5f, 0.2f, false, 1);
		storage.newSphere(new Vector(0.5f, 4.0f), .6f, "res/explosive.11.png", false, 2);

		System.out.println(storage.size());
		// float radius = .5f;
		// EntityBuilder entityBuilder = world.createEntityBuilder();
		// entityBuilder.setFixed(false);
		// entityBuilder.setPosition(new Vector(0.5f, 4.0f));
		// Entity entity = entityBuilder.build();
		//
		//// Circle circle = new Circle(radius);
		////
		//// PartBuilder partBuilder = entity.createPartBuilder();
		//// partBuilder.setShape(circle);
		//// partBuilder.setFriction(.4f);
		//// partBuilder.build();
		//
		// ImageGraphics image = new ImageGraphics("res/explosive.11.png", radius * 2f,
		// radius * 2f, new Vector(.5f, .5f));
		// image.setParent(entity);
		//
		// storage.add(entity, image, null, 3);

		// allEntity.add(entity);
		// alImageGraphics.add(image);

		RevoluteConstraintBuilder revoluteConstraintBuilder = world.createRevoluteConstraintBuilder();
		revoluteConstraintBuilder.setFirstEntity((Entity) storage.getEntity(0));
		revoluteConstraintBuilder.setFirstAnchor(new Vector(10f / 2, (1f * 7) / 4));
		revoluteConstraintBuilder.setSecondEntity((Entity) storage.getEntity(1));
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

		if (window.getKeyboard().get(KeyEvent.VK_1).isDown()) {
			PartBuilder pb = storage.getPartBuilder(0);
			pb.setFriction(100);
			pb.build();
		}
		// Keyboard Control
		if (window.getKeyboard().get(KeyEvent.VK_LEFT).isDown()) {
			storage.getEntity(2).applyAngularForce(10.0f);
		} else if (window.getKeyboard().get(KeyEvent.VK_RIGHT).isDown()) {
			storage.getEntity(2).applyAngularForce(-10.0f);
		}

		// The actual rendering will be done now, by the program loop
		storage.drawAll(window);
	}

	// This event is raised after game ends, to release additional resources
	@Override
	public void end() {
		// Empty on purpose, no cleanup required yet
	}

}

/**
 *	Author: Clément Jeannet
 *	Date: 	19 nov. 2017
 */
package main.game.tutorial;

import java.awt.Color;

import main.game.Game;
import main.game.actor.ShapeGraphics;
import main.game.actor.Storage;
import main.io.FileSystem;
import main.math.BasicContactListener;
import main.math.Transform;
import main.math.Vector;
import main.math.World;
import main.window.Window;

/**
 * Simple game, to show basic the basic architecture
 */
public class ContactGame implements Game {

	// Store context
	private Window window;

	// We need our physics engine
	private World world;

	// And we need to keep references on our game objects
	private Storage storage;
	
	// Our contactListener
	private BasicContactListener contactListener;

	// This event is raised when game has just started
	@Override
	public boolean begin(Window window, FileSystem fileSystem) {

		// Store context
		this.window = window;

		world = new World();
		world.setGravity(new Vector(0.0f, -9.81f));
		storage = new Storage(world);

		// Create the stone block
		storage.newRectangle(new Vector(-5.0f, -1.0f), "res/stone.broken.4.png", 10f, 1f, true, 0);
		// Create the falling spheres
		storage.newSphere(new Vector(0.5f, 4.0f), .6f, false, Color.BLUE, Color.BLUE, .1f, 1, 0, 1);

		// add a contact listener
		contactListener = new BasicContactListener();
		storage.getEntity(1).addContactListener(contactListener);

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
			((ShapeGraphics) storage.getGraphics(1).get(0)).setFillColor(Color.RED);
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

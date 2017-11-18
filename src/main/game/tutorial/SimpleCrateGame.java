/**
 *	Author: Clément Jeannet
 *	Date: 	18 nov. 2017
 */
package main.game.tutorial;

import java.util.ArrayList;

import main.game.Game;
import main.game.actor.ImageGraphics;
import main.io.FileSystem;
import main.math.Entity;
import main.math.EntityBuilder;
import main.math.PartBuilder;
import main.math.Polygon;
import main.math.Transform;
import main.math.Vector;
import main.math.World;
import main.window.Window;

/**
 * Simple game, to show basic the basic architecture
 */
public class SimpleCrateGame implements Game {

	// Store context
	private Window window;

	// We need our physics engine
	private World world;

	// And we need to keep references on our game objects
	private ArrayList<Entity> alEntity = new ArrayList<Entity>();

	private ArrayList<ImageGraphics> alImageGraphics = new ArrayList<ImageGraphics>();

	// This event is raised when game has just started
	@Override
	public boolean begin(Window window, FileSystem fileSystem) {

		// Store context
		this.window = window;

		world = new World();
		world.setGravity(new Vector(0.0f, -9.81f));
		CrateObject.init(world, alEntity, alImageGraphics);
		
		CrateObject.newCarre(new Vector(1.0f, 0.5f), "res/stone.broken.4.png", 1, true);
		CrateObject.newCarre(new Vector(0.2f, 4.0f), "res/box.4.png", 1, false);

		// Successfully initiated
		return true;
	}

	// This event is called at each frame
	@Override
	public void update(float deltaTime) {
		window.setRelativeTransform(Transform.I.scaled(7.0f));

		world.update(deltaTime);
		for (ImageGraphics ig : alImageGraphics) {
			ig.draw(window);
		}
		// The actual rendering will be done now, by the program loop
	}

	// This event is raised after game ends, to release additional resources
	@Override
	public void end() {
		// Empty on purpose, no cleanup required yet
	}

}

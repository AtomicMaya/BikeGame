/**
 *	Author: Clément Jeannet
 *	Date: 	20 nov. 2017
 */
package main.game.tutorial;

import java.awt.event.KeyEvent;

import main.game.Game;
import main.io.FileSystem;
import main.math.Polyline;
import main.math.Transform;
import main.math.Vector;
import main.math.World;
import main.window.Window;

/**
 * Simple game, to show basic the basic architecture
 */
public class PolyLineGame implements Game {

	// Store context
	private Window window;

	// We need our physics engine
	private World world;

	// And we need to keep references on our game objects


	// This event is raised when game has just started
	@Override
	public boolean begin(Window window, FileSystem fileSystem) {

		// Store context
		this.window = window;

		world = new World();
		world.setGravity(new Vector(0.0f, -9.81f));


		// storage.newRectangle(new Vector(-5.0f, -1.0f), "res/stone.broken.4.png", 10f,
		// 1f, true, 0);



//		Polyline p = new Polyline(new Vector(-10, 3), new Vector(-10, -4), new Vector(-2, -4), new Vector(10, 0));

		// EntityBuilder entityBuilder = world.createEntityBuilder();
		// entityBuilder.setFixed(true);
		// entityBuilder.setPosition(new Vector(0f, 0f));
		// polyline = entityBuilder.build();
		//
		// PartBuilder partBuilder = polyline.createPartBuilder();
		// partBuilder.setShape(p);
		// partBuilder.setFriction(.5f);
		// partBuilder.build();
		//
		// s = new ShapeGraphics(p, null, Color.RED, .1f);
		// s.setRelativeTransform(s.getTransform().translated(new Vector(0,-.05f)));;
		// s.setParent(polyline);
		return true;
	}

	// This event is called at each frame
	@Override
	public void update(float deltaTime) {
		// Set camera to origin with zoom 10
		window.setRelativeTransform(Transform.I.scaled(10.0f));
		world.update(deltaTime);

		if (window.getKeyboard().get(KeyEvent.VK_1).isPressed()) {
			

		}
		// Keyboard Control


		// The actual rendering will be done now, by the program loop

	}

	// This event is raised after game ends, to release additional resources
	@Override
	public void end() {
		// Empty on purpose, no cleanup required yet
	}

}

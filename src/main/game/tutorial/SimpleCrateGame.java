/**
 *	Author: Clément Jeannet
 *	Date: 	18 nov. 2017
 */
package main.game.tutorial;

import main.game.Game;
import main.game.actor.ImageGraphics;
import main.io.FileSystem;
import main.math.*;
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
	private static Entity block, crate;
	private static ImageGraphics blockAsset, crateAsset;

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

		blockAsset = new ImageGraphics("res/images/stone.broken.4.png", 1, 1);
		blockAsset.setParent(block);
		
		// Create the crate
		EntityBuilder ebCrate = world.createEntityBuilder();
		ebCrate.setFixed(false);
		ebCrate.setPosition(new Vector(0.2f, 4.0f));
		crate = ebCrate.build();

		PartBuilder pbCrate = crate.createPartBuilder();
		pbCrate.setShape(polygon);
		pbCrate.build();
		
		crateAsset = new ImageGraphics("res/images/box.4.png", 1, 1);
		crateAsset.setParent(crate);

		// Successfully initiated
		return true;
	}

	// This event is called at each frame
	@Override
	public void update(float deltaTime) {
		window.setRelativeTransform(Transform.I.scaled(10.0f));

		world.update(deltaTime);

		// The actual rendering will be done now, by the program loop
		crateAsset.draw(window);
		blockAsset.draw(window);
	}

	// This event is raised after game ends, to release additional resources
	@Override
	public void end() {
		// Empty on purpose, no cleanup required yet
	}

}

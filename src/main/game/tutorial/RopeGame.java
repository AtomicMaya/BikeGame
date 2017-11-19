/**
 *	Author: Clément Jeannet
 *	Date: 	19 nov. 2017
 */
package main.game.tutorial;

import java.awt.Color;
import java.util.ArrayList;

import main.game.Game;
import main.game.actor.Graphics;
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
	private ArrayList<Entity> alEntity = new ArrayList<Entity>();

	private ArrayList<Graphics> alImageGraphics = new ArrayList<Graphics>();

	// This event is raised when game has just started
	@Override
	public boolean begin(Window window, FileSystem fileSystem) {

		// Store context
		this.window = window;

		world = new World();
		world.setGravity(new Vector(0.0f, -9.81f));
		CreateObject.init(world, alEntity, alImageGraphics);

		// Create stone block
		CreateObject.newSquare(new Vector(.8f, 0.5f), "res/stone.broken.4.png", 1, true);
		
		// Create shpere
		CreateObject.newSphere(new Vector(0.3f, 4.0f), .6f, false, Color.BLUE, Color.RED, .1f, 1.f, 0);
		
		// Link them
		RopeConstraintBuilder ropeConstraintBuilder = world.createRopeConstraintBuilder();
		ropeConstraintBuilder.setFirstEntity(alEntity.get(0));
		ropeConstraintBuilder.setFirstAnchor(new Vector(.5f, .5f));
		ropeConstraintBuilder.setSecondEntity(alEntity.get(1));
		ropeConstraintBuilder.setSecondAnchor(Vector.ZERO);
		ropeConstraintBuilder.setMaxLength(2.0f);
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

		for (Graphics ig : alImageGraphics) {
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

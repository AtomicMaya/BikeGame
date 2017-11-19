/**
 *	Author: Clément Jeannet
 *	Date: 	19 nov. 2017
 */
package main.game.tutorial;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

import main.game.Game;
import main.game.actor.Graphics;
import main.io.FileSystem;
import main.math.Entity;
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

	private ArrayList<Graphics> alImageGraphics = new ArrayList<Graphics>();
	private ArrayList<Entity> allEntity = new ArrayList<Entity>();

	// This event is raised when game has just started
	@Override
	public boolean begin(Window window, FileSystem fileSystem) {

		// Store context
		this.window = window;

		world = new World();
		world.setGravity(new Vector(0.0f, -9.81f));
		CreateObject.init(world, allEntity, alImageGraphics);

		CreateObject.newRectangle(new Vector(-5.0f, -1.0f), "res/stone.broken.4.png", 10f, 1f, true);
		CreateObject.newRectangle(new Vector(-5f, 0.8f), "res/wood.3.png", 5f, 0.2f, false);
		CreateObject.newSphere(new Vector(0.5f, 4.0f), .6f, "res/explosive.11.png", false);

		RevoluteConstraintBuilder revoluteConstraintBuilder = world.createRevoluteConstraintBuilder();
		revoluteConstraintBuilder.setFirstEntity(allEntity.get(0));
		revoluteConstraintBuilder.setFirstAnchor(new Vector(10f / 2, (1f * 7) / 4));
		revoluteConstraintBuilder.setSecondEntity(allEntity.get(1));
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
			allEntity.get(2).applyAngularForce(10.0f);
		} else if (window.getKeyboard().get(KeyEvent.VK_RIGHT).isDown()) {
			allEntity.get(2).applyAngularForce(-10.0f);
		}

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

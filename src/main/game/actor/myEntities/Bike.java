/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.myEntities;

import main.game.actor.ActorGame;
import main.game.actor.ShapeGraphics;
import main.math.Polygon;
import main.math.Polyline;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;


public class Bike extends GameEntity {
	// game where the Bike evolves
	private ActorGame game;

	private float MAX_WHEEL_SPEED = 20f;

	// weather
	private boolean lookRight = true;

	// physical shape of the Bike
	private Polygon hitbox;
	// shapes of the Bike
	private Polyline bikeFrame;

	// Graphics to represent the Bike
	private ShapeGraphics bikeFrameGraphic;

	// Entities associeted to the bike
	private Wheel leftWheel, rightWheel;
	private CharacterBike character;

	/**
	 * Create a Bike, controllable by the player
	 * @param game : the actorGame in which this object exists
	 * @param position : the initial position of the bike
	 */
	public Bike(ActorGame game, Vector position) {
		super(game, false, position);
		this.game = game;

		hitbox = new Polygon(0.0f, 0.5f, 0.5f, 1.0f, 0.0f, 2.0f, -0.5f, 1.0f);
		build(getEntity(), hitbox);

		bikeFrame = new Polyline(-1.3f, .8f, -1.f, .9f,
				-1.f, .9f, -.7f, .8f, -.3f, .8f, // rear mud guard
				-.4f, 1.1f, -.3f, .7f, // ass holder
				-1.f, 0.1f, -.25f, .2f,
				-1.f, 0.1f, -.3f, .7f,
				-.25f, .2f, .8f, .85f,
				-.3f, .7f, .8f, .85f, // Frame
				1.f, .85f, 1.f, 1.25f,
				0.9f, 1.3f, 1.f, 1.25f,
				1.f, .8f, 1.f, 0.1f,
				1.f, .8f, 1.3f, .75f, 1.4f, .7f);
		bikeFrameGraphic = addGraphics(this.getEntity(), bikeFrame, null, Color.blue.brighter().brighter().brighter(), .1f, 1, 0);

		leftWheel = new Wheel(game, new Vector(-1, 0).add(position), .5f);
		rightWheel = new Wheel(game, position.add(new Vector(1, 0)), .5f);
		character = new CharacterBike(game, position, 3);

		leftWheel.attach(this.getEntity(), new Vector(-1.0f, 0.0f), new Vector(-0.5f, -1.0f));
		rightWheel.attach(this.getEntity(), new Vector(1.0f, 0.0f), new Vector(0.5f, -1.0f));
		character.attach(this.getEntity(), new Vector(0.f, 0.5f));

		game.addActor(rightWheel);
		game.addActor(leftWheel);
		game.addActor(character);
	}

	@Override
	public void update(float deltaTime) {
		/*
		Graphics
		 if (game.getKeyboard().get(KeyEvent.VK_D).isDown()) {
		 if (lookRight && rearWheel.getSpeed() > MAX_WHEEL_SPEED) {
		 rearWheel.power(-10f);
		 cycliste.getEntity().applyAngularForce(10f);
		 } else if (frontWheel.getSpeed() < MAX_WHEEL_SPEED) {
		 frontWheel.power(-10f);
		 cycliste.getEntity().applyAngularForce(-10f);
		 }
		 } else if (game.getKeyboard().get(KeyEvent.VK_A).isDown()) {
		 if (lookRight && rearWheel.getSpeed() < MAX_WHEEL_SPEED) {
		 rearWheel.power(10f);
		 cycliste.getEntity().applyAngularForce(-10f);
		 } else if (frontWheel.getSpeed() > -MAX_WHEEL_SPEED) {
		 frontWheel.power(10f);
		 cycliste.getEntity().applyAngularForce(10f);
		 }
		 }
		*/
		leftWheel.relax();
		rightWheel.relax();

		if (game.getKeyboard().get(KeyEvent.VK_SPACE).isPressed()) {
			lookRight = !lookRight;
			character.invertX();
			bikeFrame = new Polyline(invertXCoordinates(bikeFrame.getPoints()));
			bikeFrameGraphic = addGraphics(this.getEntity(), bikeFrame, null, Color.BLUE.brighter().brighter().brighter(), .1f, 1f, 0f);
			character.setlFootPos(lookRight ? new Vector(.1f, -.3f) : new Vector(-.1f, -.3f));
			character.setrFootPos(lookRight ? new Vector(-.1f, -.3f): new Vector(.1f, -.3f));
		}

		if (game.getKeyboard().get(KeyEvent.VK_S).isDown()) {
			leftWheel.power(0);
			rightWheel.power(0);
		} else if (game.getKeyboard().get(KeyEvent.VK_W).isDown()) {
			character.nextPedal(lookRight ? -1 : 1);
			if (character.getIsYaying()) character.nextYay(lookRight ? -1 : 1, deltaTime);
			if (lookRight && leftWheel.getSpeed() > -MAX_WHEEL_SPEED) {
				leftWheel.power(-MAX_WHEEL_SPEED);
			} else if (rightWheel.getSpeed() < MAX_WHEEL_SPEED) {
				rightWheel.power(MAX_WHEEL_SPEED);
			}
			if (game.getKeyboard().get(KeyEvent.VK_A).isDown()) {
				getEntity().applyAngularForce(10.0f);
			} else if (game.getKeyboard().get(KeyEvent.VK_D).isDown()) {
				getEntity().applyAngularForce(-10f);
			}
		}

	}

	private List<Vector> invertXCoordinates(List<Vector> vectors) {
		List<Vector> newVectors = new ArrayList<>();
		for (int i = 0; i < vectors.size(); i++) {
			newVectors.add(vectors.get(i).mul(new Vector(-1.f, 1.f)));
		}
		return newVectors;
	}

	@Override
	public void draw(Canvas canvas) {
		bikeFrameGraphic.draw(canvas);
		character.draw(canvas);
	}

	@Override
	public void destroy() {
		this.leftWheel.destroy();
		this.rightWheel.destroy();
		this.character.destroy();
		super.destroy();
		super.getOwner().destroyActor(this);
	}

}

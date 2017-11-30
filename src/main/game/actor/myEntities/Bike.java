package main.game.actor.myEntities;

import main.game.actor.ActorGame;
import main.game.actor.ShapeGraphics;
import main.math.Polygon;
import main.math.Polyline;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

import static main.game.actor.QuickMafs.invertXCoordinates;


public class Bike extends GameEntity {
	// game where the Bike evolves
	private final ActorGame game;

	private final float MAX_WHEEL_SPEED = 20f;

	// weather
	private boolean lookRight = true;

	// physical shape of the Bike
	private final Polygon hitbox;
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
		character = new CharacterBike(game, position);

		leftWheel.attach(this.getEntity(), new Vector(-1.0f, 0.0f), new Vector(-0.5f, -1.0f));
		rightWheel.attach(this.getEntity(), new Vector(1.0f, 0.0f), new Vector(0.5f, -1.0f));
		character.attach(this.getEntity(), new Vector(0.f, 0.5f));
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
			bikeFrame = new Polyline(invertXCoordinates(bikeFrame.getPoints(), new Vector(-1.f, 1.f)));
			bikeFrameGraphic = addGraphics(this.getEntity(), bikeFrame, null, Color.BLUE.brighter().brighter().brighter(), .1f, 1.f, .0f);
			character.setlFootPos(lookRight ? new Vector(.1f, -.3f) : new Vector(-.1f, -.3f));
			character.setrFootPos(lookRight ? new Vector(-.1f, -.3f): new Vector(.1f, -.3f));
		}
		if (character.getIsYaying()) character.nextYay(deltaTime);

		if (game.getKeyboard().get(KeyEvent.VK_S).isDown()) {
			leftWheel.power(0);
			rightWheel.power(0);
		} else if (game.getKeyboard().get(KeyEvent.VK_W).isDown()) {
			character.nextPedal();
			if (lookRight && leftWheel.getSpeed() > -MAX_WHEEL_SPEED) {
				leftWheel.power(-MAX_WHEEL_SPEED);
			} else if (rightWheel.getSpeed() < MAX_WHEEL_SPEED) {
				rightWheel.power(MAX_WHEEL_SPEED);
			}
			if (game.getKeyboard().get(KeyEvent.VK_A).isDown()) {
				getEntity().applyAngularForce(10.0f);
			} else if (game.getKeyboard().get(KeyEvent.VK_D).isDown()) {
				getEntity().applyAngularForce(-10.f);
			}
		}

		leftWheel.update(deltaTime);
		rightWheel.update(deltaTime);
		character.update(deltaTime);
	}


	@Override
	public void draw(Canvas canvas) {
		bikeFrameGraphic.draw(canvas);
		leftWheel.draw(canvas);
		rightWheel.draw(canvas);
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


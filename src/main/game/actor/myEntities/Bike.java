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
import java.util.Arrays;
import java.util.List;

import static main.game.actor.myEntities.EntityBuilder.addGraphics;
import static main.game.actor.myEntities.EntityBuilder.build;

public class Bike extends GameEntity {

	private ActorGame game;

	private float MAX_WHEEL_SPEED = 20f;

	private boolean lookRight = true;

	private Polygon hitbox;
	private Polyline bikeFrame;

	private ShapeGraphics bikeFrameGraphic;

	private Wheel leftWheel, rightWheel;

	private Character character;

	public Bike(ActorGame game, Vector position) {
		super(game, false, position);
		this.game = game;

		hitbox = new Polygon(0.0f, 0.5f, 0.5f, 1.0f, 0.0f, 2.0f, -0.5f, 1.0f);
		bikeFrame = new Polyline(-1.3f, .6f, -1.f, .7f,-1.f, .7f, -.7f, .6f, -.3f, .6f,//rear mud guard
				-.4f, 1.f, -.3f, .6f, // ass holder
				-1.f, 0.f, -.25f, .1f, -1.f, 0.f, -.3f, .6f, -.25f, .1f, .8f, .75f, -.3f, .6f, .8f, .75f, // Frame
				1.f, .75f, 1.f, .85f, 1.f, .7f, 1.f, 0.f, 1.f, .7f, 1.3f, .65f, 1.4f, .6f);

		build(getEntity(), hitbox);

		bikeFrameGraphic = addGraphics(this.getEntity(), bikeFrame, null, Color.BLUE, .1f, 1, 0);

		Vector[] indices1 = new Vector[]{ new Vector(.4f, 1.75f),
				new Vector(.2f, 1.5f),
				new Vector(0.f, .4f),
				new Vector(.5f, 1.f),
				new Vector(1.f, 1.f),
				new Vector(.5f, 1.f),
				new Vector(1.f, 1.f),
				new Vector(.6f, .2f),
				new Vector(.5f, -.2f),
				new Vector(.6f, .2f),
				new Vector(.2f, -.2f)};
		ArrayList<Vector> indices = new ArrayList<>(Arrays.asList(indices1));
		character = new Character(game, position, indices, 3);

		Polygon hitBox = new Polygon(0.0f, 0.5f, 0.5f, 1.0f, 0.0f, 2.0f, -0.5f, 1.0f);
		build(getEntity(), hitBox);

		leftWheel = new Wheel(game, new Vector(-1, 0).add(position), .5f);
		rightWheel = new Wheel(game, position.add(new Vector(1, 0)), .5f);

		leftWheel.attach(this.getEntity(), new Vector(-1.0f, 0.0f), new Vector(-0.5f, -1.0f));
		rightWheel.attach(this.getEntity(), new Vector(1.0f, 0.0f), new Vector(0.5f, -1.0f));

		game.addActor(rightWheel);
		game.addActor(leftWheel);
		game.addActor(character);
	}

	@Override
	public void update(float deltaTime) {
		//Graphics

		// if (game.getKeyboard().get(KeyEvent.VK_D).isDown()) {
		// if (lookRight && rearWheel.getSpeed() > MAX_WHEEL_SPEED) {
		// rearWheel.power(-10f);
		// cycliste.getEntity().applyAngularForce(10f);
		// } else if (frontWheel.getSpeed() < MAX_WHEEL_SPEED) {
		// frontWheel.power(-10f);
		// cycliste.getEntity().applyAngularForce(-10f);
		// }
		// } else if (game.getKeyboard().get(KeyEvent.VK_A).isDown()) {
		// if (lookRight && rearWheel.getSpeed() < MAX_WHEEL_SPEED) {
		// rearWheel.power(10f);
		// cycliste.getEntity().applyAngularForce(-10f);
		// } else if (frontWheel.getSpeed() > -MAX_WHEEL_SPEED) {
		// frontWheel.power(10f);
		// cycliste.getEntity().applyAngularForce(10f);
		// }
		// }

		leftWheel.relax();
		rightWheel.relax();

		if (game.getKeyboard().get(KeyEvent.VK_SPACE).isPressed()) {
			System.out.println("Inversion");
			// rearWheel.relax();
			// frontWheel.relax();
			lookRight = !lookRight;
			character.invertX();
			bikeFrame = new Polyline(invertXCoordinates(bikeFrame.getPoints()));
			bikeFrameGraphic = addGraphics(this.getEntity(), bikeFrame, null, Color.BLUE, .1f, 1f, 0f);
			//System.out.println((Math.abs(getVelocity().x) > .2f) ? getVelocity().x : 0.0f);
		}

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
				getEntity().applyAngularForce(-10f);
			}
		}

		// if (game.getKeyboard().get(KeyEvent.VK_D).isDown()) {
		// frontWheel.power(-20);
		// rearWheel.power(-20);
		// } else if (game.getKeyboard().get(KeyEvent.VK_A).isDown()) {
		// frontWheel.power(20);
		// rearWheel.power(20);
		// }

	}

	private List<Vector> invertXCoordinates(List<Vector> vectors) {
		List<Vector> newVectors = new ArrayList<>();
		for(int i = 0; i < vectors.size(); i++) {
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
		super.destroy();
		super.getOwner().destroyActor(this);
	}

}

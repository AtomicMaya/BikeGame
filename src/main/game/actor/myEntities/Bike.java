/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.myEntities;

import main.game.actor.ActorGame;
import main.game.actor.Graphics;
import main.game.actor.ShapeGraphics;
import main.math.Circle;
import main.math.Polygon;
import main.math.Polyline;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import static main.game.actor.myEntities.EntityBuilder.addGraphics;
import static main.game.actor.myEntities.EntityBuilder.build;

public class Bike extends GameEntity {

	private ActorGame game;

	private float MAX_WHEEL_SPEED = 20f;

	private boolean lookRight = true;

	private Polygon hitbox;
	private Polyline bikeFrame, characterBody;
	private Circle head;
	private Polyline lKnee, rKnee, lFoot, rFoot;

	private Vector headPosition;

	private ShapeGraphics bikeFrameGraphic, charBodyGraphic, charLKneeGraphic, charRKneeGraphic;
	private Vector lKneePosition, rKneePosition, lFootPosition, rFootPosition;

	private Wheel leftWheel, rightWheel;

	private Graphics headGraphic;

	Joint back, quadL, quadR, tibiaL, tibiaR;
	public Bike(ActorGame game, Vector position) {
		super(game, false, position);
		this.game = game;

		headPosition = new Vector(0.4f, 1.75f);
		hitbox = new Polygon(0.0f, 0.5f, 0.5f, 1.0f, 0.0f, 2.0f, -0.5f, 1.0f);
		bikeFrame = new Polyline(-1.3f, .6f, -1.f, .7f,-1.f, .7f, -.7f, .6f, -.3f, .6f,//rear mud guard
				-.4f, 1.f, -.3f, .6f, // ass holder
				-1.f, 0.f, -.25f, .1f, -1.f, 0.f, -.3f, .6f, -.25f, .1f, .8f, .75f, -.3f, .6f, .8f, .75f, // Frame
				1.f, .75f, 1.f, .85f, 1.f, .7f, 1.f, 0.f, 1.f, .7f, 1.3f, .65f, 1.4f, .6f);
		characterBody = new Polyline(.4f, 1.75f, .2f, 1.5f, 0.5f, 1.f, 1.f, 1.f, 0.5f, 1.f, .2f, 1.5f, 0.f, 0.85f);

		build(getEntity(), hitbox);

		bikeFrameGraphic = addGraphics(this.getEntity(), bikeFrame, null, Color.BLUE, .1f, 1, 0);
		charBodyGraphic = addGraphics(this.getEntity(), characterBody, null, Color.BLACK, .1f, 1.f, 0);



		Polygon hitBox = new Polygon(0.0f, 0.5f, 0.5f, 1.0f, 0.0f, 2.0f, -0.5f, 1.0f);
		build(getEntity(), hitBox);

		head = new Circle(0.2f, getHeadPosition());

		headGraphic = addGraphics(getEntity(), head, Color.PINK, Color.BLACK, .05f, 1, 0.1f);

		leftWheel = new Wheel(game, new Vector(-1, 0).add(position), .5f);
		rightWheel = new Wheel(game, position.add(new Vector(1, 0)), .5f);

		leftWheel.attach(this.getEntity(), new Vector(-1.0f, 0.0f), new Vector(-0.5f, -1.0f));
		rightWheel.attach(this.getEntity(), new Vector(1.0f, 0.0f), new Vector(0.5f, -1.0f));

		game.addActor(rightWheel);
		game.addActor(leftWheel);
	}

	private Vector getHeadPosition() {
		return headPosition; //new Vector(0.0f, 1.75f);
	}

	private void setHeadPosition(Vector newPosition) {
		headPosition = newPosition;
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
			// rearWheel.relax();
			// frontWheel.relax();
			lookRight = !lookRight;
		//	System.out.println("look changed");
			setHeadPosition(getHeadPosition().mul(new Vector(-1.f, 1.f)));
			head = new Circle(head.getRadius(), getHeadPosition());
			headGraphic = addGraphics(this.getEntity(), head, Color.PINK, Color.BLACK, .05f, 1, .1f);
			bikeFrame = new Polyline(invertXCoordinates(bikeFrame.getPoints()));
			bikeFrameGraphic = addGraphics(this.getEntity(), bikeFrame, null, Color.LIGHT_GRAY, .1f, 1f, 0f);
			characterBody = new Polyline(invertXCoordinates(characterBody.getPoints()));
			charBodyGraphic = addGraphics(this.getEntity(), characterBody, null, Color.BLACK, .1f, 1.f, 0.f);

			//System.out.println((Math.abs(getVelocity().x) > .2f) ? getVelocity().x : 0.0f);
		}

		if (game.getKeyboard().get(KeyEvent.VK_S).isDown()) {
			leftWheel.power(0);
			rightWheel.power(0);
		} else if (game.getKeyboard().get(KeyEvent.VK_W).isDown()) {
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
		headGraphic.draw(canvas);
		bikeFrameGraphic.draw(canvas);
		charBodyGraphic.draw(canvas);
//		charLKneeGraphic.draw(canvas);
	}

	@Override
	public void destroy() {
		this.leftWheel.destroy();
		this.rightWheel.destroy();
		this.back.destroy();
		this.quadL.destroy();
		super.destroy();
		super.getOwner().destroyActor(this);
	}

}

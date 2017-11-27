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

import static main.game.actor.myEntities.EntityBuilder.addGraphics;
import static main.game.actor.myEntities.EntityBuilder.build;

public class Bike extends GameEntity {

	private ActorGame game;

	private float MAX_WHEEL_SPEED = 20f;

	private boolean lookRight = true;

	private Wheel rearWheel, frontWheel;
	private Polyline lKnee, rKnee, lFoot, rFoot;

	private Graphics headGraphic;
	private ShapeGraphics bikeFrameGraphic, charBodyGraphic, charLKneeGraphic, charRKneeGraphic;
	private Vector lKneePosition, rKneePosition, lFootPosition, rFootPosition;

	public Bike(ActorGame game, Vector position) {
		super(game, false, position);
		this.game = game;

		Polygon hitBox = new Polygon(0.0f, 0.5f, 0.5f, 1.0f, 0.0f, 2.0f, -0.5f, 1.0f);
		Polyline bikeFrame = new Polyline(-1.3f, .6f, -1.f, .7f,-1.f, .7f, -.7f, .6f, -.3f, .6f,//rear mud guard
				-.4f, 1.f, -.3f, .6f, // ass holder
				-1.f, 0.f, -.25f, .1f, -1.f, 0.f, -.3f, .6f, -.25f, .1f, .8f, .75f, -.3f, .6f, .8f, .75f, // Frame
				1.f, .75f, 1.f, .85f, 1.f, .7f, 1.f, 0.f, 1.f, .7f, 1.3f, .65f, 1.4f, .6f);
		Polyline characterBody = new Polyline(.4f, 1.75f, .2f, 1.5f, 0.5f, 1.f, 1.f, 1.f, 0.5f, 1.f, .2f, 1.5f, -.2f, 0.85f);

		build(getEntity(), hitBox);

		Circle head = new Circle(0.2f, getHeadLocation());

		headGraphic = addGraphics(getEntity(), head, Color.PINK, Color.BLACK, .1f, 1, 0);
		bikeFrameGraphic = addGraphics(this.getEntity(), bikeFrame, null, Color.LIGHT_GRAY, .1f, 1, 0);

		Joint back = new Joint(game, new Vector(-.2f, .85f), 1.f);
		Joint leg = new Joint(game, new Vector(.1f, .5f), 1.f);
		leg.attach(back, back.getAnchor(), new Vector(.0f, 1.f));


		rearWheel = new Wheel(game, new Vector(-1, 0).add(position), .5f);
		frontWheel = new Wheel(game, position.add(new Vector(1, 0)), .5f);

		rearWheel.attach(getEntity(), new Vector(-1.0f, 0.0f), new Vector(-0.5f, -1.0f));
		frontWheel.attach(getEntity(), new Vector(1.0f, 0.0f), new Vector(0.5f, -1.0f));

		game.addActor(frontWheel);
		game.addActor(rearWheel);
	}

	private Vector getHeadLocation() {
		return new Vector(0.5f, 2.f);
	}

	@Override
	public void update(float deltaTime) {
		//Graphics


		rearWheel.relax();
		frontWheel.relax();
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

		if (game.getKeyboard().get(KeyEvent.VK_D).isDown()) {
			frontWheel.power(-20);
			rearWheel.power(-20);
		} else if (game.getKeyboard().get(KeyEvent.VK_A).isDown()) {
			frontWheel.power(20);
			rearWheel.power(20);
		}
		if (game.getKeyboard().get(KeyEvent.VK_SPACE).isDown()) {
			// rearWheel.relax();
			// frontWheel.relax();

			System.out.println((Math.abs(getVelocity().x) > .2f) ? getVelocity().x : 0.0f);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		headGraphic.draw(canvas);
		bikeFrameGraphic.draw(canvas);
		charBodyGraphic.draw(canvas);
		charLKneeGraphic.draw(canvas);
	}


}

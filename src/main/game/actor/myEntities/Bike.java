/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.myEntities;

import java.awt.Color;
import java.awt.event.KeyEvent;

import main.game.actor.MyGame;
import main.game.actor.ShapeGraphics;
import main.math.Circle;
import main.math.Polygon;
import main.math.Vector;
import main.math.WheelConstraint;

public class Bike extends ComplexObject {

	private MyGame game;

	private float MAX_WHEEL_SPEED = 20f;

	private boolean lookRight = true;

	private Wheel rearWheel, frontWheel;
	private PolygonEntity cycliste;

	public Bike(MyGame game, Vector position) {
		this.game = game;

		Polygon polygon = new Polygon(0.0f, 0.5f, 0.5f, 1.0f, 0.0f, 2.0f, -0.5f, 1.0f);
		cycliste = new PolygonEntity(game, position, false, polygon);

		Circle head = new Circle(0.2f, getHeadLocation());

		cycliste.setGraphics(new ShapeGraphics(head, Color.PINK, Color.BLACK, .1f));
		// cycliste.setGraphics(new ShapeGraphics(polygon, Color.RED, Color.BLACK,
		// .1f));

		rearWheel = new Wheel(game, new Vector(-1, 0).add(position));
		frontWheel = new Wheel(game, position.add(new Vector(1, 0)));

		rearWheel.attach(cycliste, new Vector(-1.0f, 0.0f), new Vector(-0.5f, -1.0f));
		frontWheel.attach(cycliste, new Vector(1.0f, 0.0f), new Vector(0.5f, -1.0f));
		System.out.println(frontWheel.getPosition());
		System.out.println(rearWheel.getPosition());

		addEntity(cycliste);
		addEntity(rearWheel);
		addEntity(frontWheel);

	}

	private Vector getHeadLocation() {
		return new Vector(0.0f, 1.75f);
	}

	@Override
	public void update(float deltaTime) {
		rearWheel.relax();
		frontWheel.relax();
		// if (game.getKeyboard().get(KeyEvent.VK_D).isDown()) {
		// if (lookRight && rearWheel.getSpeed() > -MAX_WHEEL_SPEED) {
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
	public Vector getPosition() {

		return cycliste.getEntity().getPosition();
	}

	@Override
	public Vector getVelocity() {
		return cycliste.getEntity().getVelocity();
	}
}

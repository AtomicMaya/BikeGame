/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.myEntities;

import java.awt.Color;
import java.awt.event.KeyEvent;

import main.game.actor.ActorGame;
import main.game.actor.Graphics;
import main.math.Circle;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

import static main.game.actor.myEntities.EntityBuilder.*;

public class Bike extends GameEntity {

	private ActorGame game;

	private float MAX_WHEEL_SPEED = 20f;

	private boolean lookRight = true;

	private Wheel rearWheel, frontWheel;

	private Graphics headGraphic;

	public Bike(ActorGame game, Vector position) {
		super(game, false, position);
		this.game = game;

		Polygon hitBox = new Polygon(0.0f, 0.5f, 0.5f, 1.0f, 0.0f, 2.0f, -0.5f, 1.0f);
		build(getEntity(),hitBox);


		Circle head = new Circle(0.2f, getHeadLocation());

		headGraphic = addGraphics(getEntity(), head, Color.PINK, Color.BLACK, .1f, 1, 0);


		rearWheel = new Wheel(game, new Vector(-1, 0).add(position), .5f);
		frontWheel = new Wheel(game, position.add(new Vector(1, 0)), .5f);

		rearWheel.attach(getEntity(), new Vector(-1.0f, 0.0f), new Vector(-0.5f, -1.0f));
		frontWheel.attach(getEntity(), new Vector(1.0f, 0.0f), new Vector(0.5f, -1.0f));

		game.addActor(frontWheel);
		game.addActor(rearWheel);
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
	public void draw(Canvas canvas) {
		headGraphic.draw(canvas);
		
	}


}

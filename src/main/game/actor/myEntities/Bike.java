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
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

import static main.game.actor.myEntities.EntityBuilder.*;

public class Bike extends GameEntity {

	private ActorGame game;

	private float MAX_WHEEL_SPEED = 20f;

	private boolean lookRight = true;

	private Wheel leftWheel, rightWheel;

	private Graphics headGraphic;
	ShapeGraphics cone;

	Shape triangle;

	public Bike(ActorGame game, Vector position) {
		super(game, false, position);
		this.game = game;

		Polygon hitBox = new Polygon(0.0f, 0.5f, 0.5f, 1.0f, 0.0f, 2.0f, -0.5f, 1.0f);
		build(getEntity(), hitBox);

		Circle head = new Circle(0.2f, getHeadLocation());

		headGraphic = addGraphics(getEntity(), head, Color.PINK, Color.BLACK, .05f, 1, 0);

		triangle = new Polygon(.15f, 1.75f, .55f, 1.65f, .1f, 1.55f);
		cone = addGraphics(getEntity(), triangle, Color.red, Color.black, .05f, 1, -1);

		leftWheel = new Wheel(game, new Vector(-1, 0).add(position), .5f);
		rightWheel = new Wheel(game, position.add(new Vector(1, 0)), .5f);

		leftWheel.attach(getEntity(), new Vector(-1.0f, 0.0f), new Vector(-0.5f, -1.0f));
		rightWheel.attach(getEntity(), new Vector(1.0f, 0.0f), new Vector(0.5f, -1.0f));

		game.addActor(rightWheel);
		game.addActor(leftWheel);
	}

	private Vector getHeadLocation() {
		return new Vector(0.0f, 1.75f);
	}

	@Override
	public void update(float deltaTime) {
		leftWheel.relax();
		rightWheel.relax();

		if (game.getKeyboard().get(KeyEvent.VK_SPACE).isPressed()) {
			// rearWheel.relax();
			// frontWheel.relax();
			lookRight = !lookRight;
			if (!lookRight) {
				triangle = new Polygon(-.15f, 1.75f, -.55f, 1.65f, -.1f, 1.55f);
				System.out.println("look left");
			} else {
				triangle = new Polygon(.15f, 1.75f, .55f, 1.65f, .1f, 1.55f);
				System.out.println("look right");
			}

			cone = addGraphics(getEntity(), triangle, Color.red, Color.black, .05f, 1, -1);

			System.out.println((Math.abs(getVelocity().x) > .2f) ? getVelocity().x : 0.0f);
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

	@Override
	public void draw(Canvas canvas) {
		headGraphic.draw(canvas);
		cone.draw(canvas);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		this.leftWheel.destroy();
		this.rightWheel.destroy();
		
	}

}

/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.myEntities;

import java.awt.Color;

import main.game.actor.ActorGame;
import main.game.actor.ImageGraphics;
import main.game.actor.ShapeGraphics;
import main.math.Circle;
import main.math.Entity;
import main.math.Vector;
import main.math.WheelConstraint;
import main.math.WheelConstraintBuilder;
import main.window.Canvas;

public class Wheel extends GameEntity {

	// keep references
	private WheelConstraint constraint = null;

	private ShapeGraphics g;


	/**
	 * Create a wheel, can be associated to an other Entity
	 * 
	 * @param game
	 *            ActorGame where the wheel evolve
	 * @param position
	 *            initial position of the wheel
	 * @param radius
	 *            size of the radius of the wheel
	 */
	public Wheel(ActorGame game, Vector position, float radius) {
		super(game, false, position);
		Circle c = new Circle(radius - .05f);
		build(getEntity(), c, .6f, -1, false);
		g = addGraphics(getEntity(), c, Color.LIGHT_GRAY, Color.DARK_GRAY, .15f, 1.f, 0.f);

		//g.setAnchor(new Vector(.5f, .5f));

	}

	/**
	 * Attach this wheel to an Entity
	 * 
	 * @param vehicle
	 *            Entity where to attach this wheel
	 * @param anchor
	 *            where to attach the wheel to the vehicle, in local coordinate of
	 *            the vehicle
	 * @param axis
	 *            around witch the wheel can move
	 */
	public void attach(Entity vehicle, Vector anchor, Vector axis) {
		WheelConstraintBuilder constraintBuilder = super.getOwner().createWheelConstraintBuilder();

		constraintBuilder.setFirstEntity(vehicle);
		// point d'ancrage du véhicule :
		constraintBuilder.setFirstAnchor(anchor);
		// Entity associée à la roue :
		constraintBuilder.setSecondEntity(getEntity());
		// point d'ancrage de la roue (son centre) :
		constraintBuilder.setSecondAnchor(Vector.ZERO);

		// axe le long duquel la roue peut se déplacer :
		constraintBuilder.setAxis(axis);
		// fréquence du ressort associé
		constraintBuilder.setFrequency(3.0f);
		constraintBuilder.setDamping(0.5f);
		// force angulaire maximale pouvant être appliquée
		// à la roue pour la faire tourner :
		constraintBuilder.setMotorMaxTorque(10.0f);
		constraint = constraintBuilder.build();
	}

	/**
	 * @param velocity
	 *            angular velocity to give to the wheel, in radiant per second
	 */
	public void power(float velocity) {
		if (constraint != null) {
			constraint.setMotorEnabled(true);
			constraint.setMotorSpeed(velocity);
		}
	}

	/**
	 * Stop the wheel
	 */
	public void relax() {
		if (constraint != null) {
			constraint.setMotorEnabled(false);
			constraint.setMotorSpeed(0);
		}
	}

	/**
	 * DEtach the wheel from its vehicle
	 */
	public void detach() {
		if (constraint != null)
			constraint.destroy();
	}

	/**
	 * @return relative rotation speed , in radians per second
	 */
	public float getSpeed() {
		if (constraint != null) {
			System.out.println(constraint.getMotorSpeed());
			return constraint.getMotorSpeed();
		} else
			return -1;
	}

	@Override
	public void draw(Canvas canvas) {
		g.draw(canvas);
	}

	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}
}

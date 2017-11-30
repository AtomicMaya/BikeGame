package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ShapeGraphics;
import main.math.*;
import main.window.Canvas;

import java.awt.*;

public class Wheel extends GameEntity {
	// keep references
	private WheelConstraint constraint = null;
	private ShapeGraphics graphics;

	/**
	 * Create a wheel, can be associated to an other Entity
	 * @param game : ActorGame where the wheel evolve
	 * @param position : initial position of the wheel
	 * @param radius : size of the radius of the wheel, non-negative
	 */
	public Wheel(ActorGame game, Vector position, float radius) {
		super(game, false, position);
		Circle circle = new Circle(radius - .05f);
		this.build(circle, .6f, -1, false);
		graphics = this.addGraphics(circle, Color.LIGHT_GRAY, Color.DARK_GRAY, .15f, 1.f, 0.f);
	}

	/**
	 * Attach this wheel to an entity
	 * @param vehicle : the entity to which this object will be attached
	 * @param anchor : where to attach the wheel to the vehicle, in the vehicle's coordinate system
	 * @param axis : the axes around which the wheel can move
	 */
	public void attach(Entity vehicle, Vector anchor, Vector axis) {
		WheelConstraintBuilder constraintBuilder = super.getOwner().createWheelConstraintBuilder();

		constraintBuilder.setFirstEntity(vehicle);
		constraintBuilder.setFirstAnchor(anchor);               // Vehicle's anchor point
		constraintBuilder.setSecondEntity(this.getEntity());    // Entity associated to this object
		constraintBuilder.setSecondAnchor(Vector.ZERO);         // Anchor point of this object, its center

		constraintBuilder.setAxis(axis);                        // Movement axes
		constraintBuilder.setFrequency(3.0f);                   // Associated Spring Frequency
		constraintBuilder.setDamping(0.9f);
		constraintBuilder.setMotorMaxTorque(10.0f);             // Maximum force that can be applied to this object
		constraint = constraintBuilder.build();
	}

	/**
	 * Accelerate the wheel
	 * @param velocity : the angular velocity to be given to the wheel, in radians per second
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
	 * Detach the wheel from its vehicle
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
			return constraint.getMotorSpeed();
		} else
			return -1;
	}

	@Override
	public void draw(Canvas canvas) {
		graphics.draw(canvas);
	}

	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}
}
package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ImageGraphics;
import main.math.*;
import main.window.Canvas;

public class Wheel extends GameEntity {
	// keep references
	private WheelConstraint constraint = null;
	private ImageGraphics graphics;
	private BasicContactListener listener;

	private float radius;
	/**
	 * Creates a wheel, can be associated to an other Entity.
	 * @param game : The actorGame where the wheel evolves.
	 * @param position : The initial position of the wheel.
	 * @param radius : The radius of the wheel, non-negative.
	 */
	public Wheel(ActorGame game, Vector position, float radius) {
		super(game, false, position);
		this.radius = radius;
		create();
	}

	/**
	 * Actual creation of the parameters of the GameEntity, not in the constructor to
	 * avoid duplication with the method reCreate
	 */
	private void create() {
		Circle circle = new Circle(radius - .05f);
		this.build(circle, 20f, -1, false, CollisionGroups.WHEEL.group);
		this.graphics = this.addGraphics("/res/images/wheel.png", this.radius * 2, this.radius * 2, new Vector(this.radius, this.radius), 1, 0);
	}
	
//	@Override
//	public void reCreate(ActorGame game) {
//		super.reCreate(game);
//		create();
//	}

	/**
	 * Attach this wheel to an entity
	 * @param vehicle : The entity to which this object will be attached.
	 * @param anchor : Where to attach the wheel to the vehicle, in the vehicle's
	 *            coordinate system.
	 * @param axis : The axes around which the wheel can move.
	 */
	public void attach(Entity vehicle, Vector anchor, Vector axis) {
		WheelConstraintBuilder constraintBuilder = super.getOwner().createWheelConstraintBuilder();

		constraintBuilder.setFirstEntity(vehicle);
		constraintBuilder.setFirstAnchor(anchor); // Vehicle's anchor point
		constraintBuilder.setSecondEntity(this.getEntity()); // Entity associated to this object
		constraintBuilder.setSecondAnchor(Vector.ZERO); // Anchor point of this object, its center

		constraintBuilder.setAxis(axis); // Movement axes
		constraintBuilder.setFrequency(3.0f); // Associated Spring Frequency
		constraintBuilder.setDamping(0.9f);
		constraintBuilder.setMotorMaxTorque(10.0f); // Maximum force that can be applied to this object
        this.constraint = constraintBuilder.build();
	}

	/**
	 * Accelerate the wheel.
	 * @param velocity : the angular velocity to be given to the wheel, in radians
	 *            per second.
	 */
	public void power(float velocity) {
		if (this.constraint != null) {
            this.constraint.setMotorEnabled(true);
            this.constraint.setMotorSpeed(velocity);
		}
	}

	/**
	 * Stop the wheel's forced rotation,
	 */
	public void relax() {
		if (this.constraint != null) {
            this.constraint.setMotorEnabled(false);
            this.constraint.setMotorSpeed(0);
		}
	}

	/**
	 * Detach the wheel from it's vehicle.
	 */
	public void detach() {
		if (this.constraint != null)
            this.constraint.destroy();
	}

	/**
	 * @return The relative rotation speed, in radians per second
	 */
	public float getSpeed() {
		if (this.constraint != null) {
			return this.constraint.getMotorSpeed();
		} else
			return -1;
	}

	@Override
	public void draw(Canvas canvas) {
        this.graphics.draw(canvas);
	}

	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}
}
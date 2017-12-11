package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.graphics.ImageGraphics;
import main.math.*;
import main.window.Canvas;

/** Wheels to be attached to our bike. */
public class Wheel extends GameEntity {
    /** Used for save purpose. */
    private static final long serialVersionUID = 3427364356329461380L;

    /** The local {@linkplain WheelConstraint} that this {@linkplain Wheel} should use. */
	private WheelConstraint constraint = null;

	/** The associated {@linkplain ImageGraphics}. */
	private ImageGraphics graphics;

	/** A {@linkplain BasicContactListener} so the */
	private BasicContactListener listener;

	/** The radius of this {@linkplain Wheel}. */
	private float radius;

	/**
	 * Creates a {@linkplain Wheel}, can be associated to any other {@linkplain Entity}.
	 * @param game The master {@linkplain ActorGame}.
	 * @param position The initial position {@linkplain Vector}.
	 * @param radius The radius of this {@linkplain Wheel}, non-negative.
	 */
	public Wheel(ActorGame game, Vector position, float radius) {
		super(game, false, position);
		this.radius = radius;
		create();
	}

	/**
	 * Actual creation of the parameters of the {@linkplain GameEntity}, not in the constructor to
	 * avoid duplication with the method {@linkplain #reCreate(ActorGame)}.
	 */
	private void create() {
		Circle circle = new Circle(radius - .05f);
		this.build(circle, 20f, 1, false, ObjectGroup.WHEEL.group);
		this.graphics = this.addGraphics("/res/images/wheel.png", this.radius * 2, this.radius * 2, new Vector(this.radius, this.radius), 1, 0);
		this.listener = new BasicContactListener();
		this.addContactListener(this.listener);
	}

	/**
	 * Attach this wheel to an entity
	 * @param vehicle The {@linkplain Entity} to which this {@linkplain Wheel} will be attached.
	 * @param anchor Where to attach the {@linkplain Wheel} to the vehicle, in the vehicle's coordinate system.
	 * @param axis The axes around which the {@linkplain Wheel} can move.
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
	 * Accelerate the {@linkplain Wheel}.
	 * @param velocity the angular velocity to be given to the wheel, in radians / second.
	 */
	public void power(float velocity) {
		if (this.constraint != null) {
            this.constraint.setMotorEnabled(true);
            this.constraint.setMotorSpeed(velocity);
		}
	}

	/** Stop the {@linkplain Wheel}'s forced rotation. */
	public void relax() {
		if (this.constraint != null) {
            this.constraint.setMotorEnabled(false);
            this.constraint.setMotorSpeed(0);
		}
	}

	/** Detach this {@linkplain Wheel} from its vehicle. */
	public void detach() {
		if (this.constraint != null)
            this.constraint.destroy();
	}

	/** @return The relative rotation speed, in radians / second. */
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

	/** @return whether or not this {@linkplain Wheel} is colliding with the {@linkplain Terrain}. */
	public boolean isCollidingWithTerrain() {
	    if (this.listener.getEntities().size() > 0)
	        for(Entity entity : this.listener.getEntities())
	            return (entity.getCollisionGroup() == ObjectGroup.TERRAIN.group);

        return false;
    }
}
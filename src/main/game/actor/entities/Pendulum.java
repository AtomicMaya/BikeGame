package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Linker;
import main.game.actor.ObjectGroup;
import main.game.graphics.ImageGraphics;
import main.io.Saveable;
import main.math.*;
import main.window.Canvas;

import java.awt.*;

/** The mass at the end of the {@linkplain Pendulum}. */
class Weight extends GameEntity {
    /** The {@linkplain ImageGraphics} representation of this {@linkplain Weight}. */
	private ImageGraphics graphics;

	/** The associated {@linkplain RopeConstraint}. */
	private RopeConstraint constraint;

    /**
     * Creates a new {@linkplain Weight}.
     * @param game The master {@linkplain ActorGame}.
     * @param position The initial position {@linkplain Vector}.
     * @param radius The radius of the {@linkplain Weight}.
     */
	Weight(ActorGame game, Vector position, float radius) {
		super(game, false, position);
		radius = Math.abs(radius);
		this.build(new Circle(radius), 1, 1, false, ObjectGroup.OBSTACLE.group);
		this.graphics = this.addGraphics("./res/images/stone.broken.11.png", radius * 2, radius * 2,
				new Vector(.5f, .5f));
	}

    /**
     * Sets a new {@linkplain RopeConstraint} to this {@linkplain Weight}.
     * @param constraint The {@linkplain RopeConstraint} to associate.
     */
	public void setConstraint(RopeConstraint constraint) {
		this.constraint = constraint;
	}

	@Override
	public void draw(Canvas canvas) {
		this.graphics.draw(canvas);
	}

	@Override
	public void destroy() {
		this.constraint.destroy();
		super.destroy();
		this.getOwner().destroyActor(this);
	}
}

/** A Pendulum designed to kill the player. */
public class Pendulum implements Saveable {
    /** Used for save purposes. */
	private static final long serialVersionUID = 948316193649268036L;

	/** The {@linkplain AnchorPoint} used to hold up the {@linkplain Weight}. */
	private transient AnchorPoint anchorPoint;

	/** The associated {@linkplain Weight}. */
	private transient Weight weight;

	/** The master {@linkplain ActorGame}. */
	private transient ActorGame game;

	/** The initial position {@linkplain Vector}. */
	private Vector position;

	/** The length of the {@linkplain RopeConstraint} between the {@linkplain AnchorPoint} and the {@linkplain Weight}. */
	private float length;

	/** The radius of the associated {@linkplain Weight}. */
	private float weightRadius;

    /**
     * Creates a new {@linkplain Pendulum}.
     * @param game The master {@linkplain ActorGame}.
     * @param position The anchor point's position {@linkplain Vector}.
     * @param length The length of the {@linkplain RopeConstraint}.
     * @param weightRadius The radius of the {@linkplain Weight}.
     */
	public Pendulum(ActorGame game, Vector position, float length, float weightRadius) {
		this.weightRadius = weightRadius;
		this.game = game;
		this.position = position;
		this.length = length;
		this.create();

	}


    /**
     * Actual creation of the parameters of the {@linkplain GameEntity}, not in
     * the constructor to avoid duplication with the method {@linkplain #reCreate(ActorGame)}
     */
	private void create() {
		this.anchorPoint = new AnchorPoint(this.game, this.position);
		this.weight = new Weight(this.game, this.position.add(-this.length, 0), this.weightRadius);
		this.weight.setConstraint(
				Linker.attachRope(this.game, this.weight.getEntity(), this.anchorPoint.getEntity(), Vector.ZERO, this.length));
	}

	@Override
	public void reCreate(ActorGame game) {
		this.game = game;
		this.create();
	}

	@Override
	public void update(float deltaTime) {
		this.weight.update(deltaTime);
		this.anchorPoint.update(deltaTime);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawShape(new Polyline(this.anchorPoint.getPosition(), this.weight.getPosition()), Transform.I, null,
				Color.decode("#8f5118"), .1f, 1, -10);
		this.anchorPoint.draw(canvas);
		this.weight.draw(canvas);
	}

	@Override
	public void destroy() {
		this.anchorPoint.destroy();
		this.weight.destroy();
		this.game.destroyActor(this);
	}

	@Override
	public Transform getTransform() {
		return Transform.I.translated(this.anchorPoint.getPosition());
	}

	@Override
	public Vector getPosition() {
		return this.anchorPoint.getPosition();
	}

	@Override
	public Vector getVelocity() {
		return Vector.ZERO;
	}

	public void setPosition(Vector position) {
		this.position = position;
		this.anchorPoint.setPosition(position);
		this.weight.setPosition(position.add(-this.length, 0));
	}

	public void setLength(float length) {
		this.length = length;
		this.destroy();
		this.create();
	}

}

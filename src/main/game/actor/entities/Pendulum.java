package main.game.actor.entities;

import java.awt.Color;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.Linker;
import main.game.actor.ObjectGroup;
import main.game.graphics.ImageGraphics;
import main.io.Saveable;
import main.math.Circle;
import main.math.Polyline;
import main.math.RopeConstraint;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

/**
 * A Pendulum designed to kill the player.
 */
class Weight extends GameEntity {
	private ImageGraphics graphics;
	private RopeConstraint constraint;

	Weight(ActorGame game, Vector position, float radius) {
		super(game, false, position);
		radius = Math.abs(radius);
		this.build(new Circle(radius), 1, 1, false, ObjectGroup.OBSTACLE.group);
		this.graphics = this.addGraphics("./res/images/stone.broken.11.png", radius * 2, radius * 2,
				new Vector(.5f, .5f));
	}

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

/**
 * A Pendulum designed to kill the player.
 */
public class Pendulum implements Saveable {

	private static final long serialVersionUID = 948316193649268036L;

	private transient AnchorPoint anchorPoint;
	private transient Weight weight;
	private transient ActorGame game;

	private Vector position;
	private float length, weightRadius;

	public Pendulum(ActorGame game, Vector position, float length, float weightRadius) {
		this.weightRadius = weightRadius;
		this.game = game;
		this.position = position;
		this.length = length;
		create();

	}

	private void create() {
		this.anchorPoint = new AnchorPoint(game, position);
		this.weight = new Weight(game, position.add(-length, 0), weightRadius);
		this.weight.setConstraint(
				Linker.attachRope(game, this.weight.getEntity(), this.anchorPoint.getEntity(), Vector.ZERO, length));
	}

	@Override
	public void reCreate(ActorGame game) {
		this.game = game;
		create();
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
		this.weight.setPosition(position.add(-length, 0));
	}

	public void setLength(float length) {
		this.length = length;
		destroy();
		create();
	}

}

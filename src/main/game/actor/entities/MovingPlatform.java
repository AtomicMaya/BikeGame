package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.graphics.Graphics;
import main.math.ExtendedMath;
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;

/** A moving {@linkplain GenericPlatform}, on a loop. */
public class MovingPlatform extends GameEntity {

	/** Used for save purposes */
	private static final long serialVersionUID = 8938371620409472520L;

	/**
	 * Time to wait at each loop of this {@linkplain MovingPlatform}
	 * displacement.
	 */
	private float pauseTime;

	/** Currently elapsed time. */
	private float elapsedTime = 0.f;

	/** Whether this {@linkplain MovingPlatform} is waiting */
	private boolean wait = false;

	/** The advancement speed of the {@linkplain GenericPlatform}. */
	private float speed;

	/** Whether the {@linkplain MovingPlatform} is going on the right */
	private boolean right;

	/** Whether the {@linkplain MovingPlatform} is going up */
	private boolean up;

	/** Bound of the path of the {@linkplain MovingPlatform} */
	private Vector start, end;

	/** Graphics of the {@linkplain MovingPlatform} */
	private transient Graphics graphics;

	/** Whether the main movement in horizontal */
	private boolean goingHorizontal = true;

	/**
	 * Creates a {@linkplain MovingPlatform}.
	 * @param game The master {@linkplain ActorGame}.
	 * @param position The initial position {@linkplain Vector}.
	 * @param evolution The initial evolution {@linkplain Vector}.
	 * @param distance The distance that the {@linkplain MovingPlatform} will
	 * cover.
	 * @param advancementTime The time the {@linkplain MovingPlatform} will take
	 * to get to it's end point.
	 * @param pauseTime The time it will wait before moving again.
	 */
	public MovingPlatform(ActorGame game, Vector position, Vector evolution, float distance, float advancementTime,
			float pauseTime) {
		super(game, false, position);
		this.pauseTime = pauseTime;
		this.speed = distance / advancementTime;
		this.start = position;
		this.end = position.add(evolution.normalized().mul(distance));

		this.create();
	}

	/**
	 * Creates a {@linkplain MovingPlatform}.
	 * @param game The master {@linkplain ActorGame}.
	 * @param startPosition The start position {@linkplain Vector}.
	 * @param endPosition The end position {@linkplain Vector}.
	 * @param advancementTime The time the {@linkplain MovingPlatform} will take
	 * to get to it's end point.
	 * @param pauseTime The time it will wait before moving again.
	 */
	public MovingPlatform(ActorGame game, Vector startPosition, Vector endPosition, float advancementTime,
			float pauseTime) {
		super(game, false, startPosition);
		this.pauseTime = pauseTime;
		this.speed = ExtendedMath.getDistance(startPosition, endPosition) / advancementTime;
		this.start = startPosition;
		this.end = endPosition;
		
		this.create();
	}

	/**
	 * Actual creation of the parameters of the {@linkplain GameEntity}, not in
	 * the constructor to avoid duplication with the method
	 * {@linkplain #reCreate(ActorGame)}
	 */
	private void create() {
		Shape platformShape = ExtendedMath.createRectangle(5, 1);
		this.build(platformShape, -1, -1, false, ObjectGroup.OBSTACLE.group);
		this.goingHorizontal = Math.abs(start.x - end.x) > Math.abs(start.y - end.y);
		this.graphics = addGraphics("./res/images/metal.3.png", 5, 1f);
	}

	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
		this.create();
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		this.elapsedTime += deltaTime;

		super.getEntity().setAngularPosition(0);
		super.getEntity().setAngularVelocity(0);

		Vector goingTo;
		if (goingHorizontal)
			goingTo = (right) ? ((start.x > end.x) ? start : end) : ((start.x < end.x) ? start : end);
		else
			goingTo = (up) ? ((start.y > end.y) ? start : end) : ((start.y < end.y) ? start : end);

		Vector direction = ExtendedMath.direction(getPosition(), goingTo).mul(-1);

		boolean temp = (goingHorizontal) ? right : up;

		if (right && getPosition().x >= Math.max(start.x, end.x))
			right = false;
		else if (getPosition().x <= Math.min(start.x, end.x))
			right = true;

		if (up && (getPosition().y >= Math.max(start.y, end.y)))
			up = false;
		else if (getPosition().y <= Math.min(start.y, end.y))
			up = true;

		if (temp != ((goingHorizontal) ? right : up)) {
			wait = true;
		}
		if (wait) {
			this.elapsedTime += deltaTime;
			if (elapsedTime > this.pauseTime) {
				elapsedTime = 0;
				wait = false;
			}
			super.getEntity().setVelocity(Vector.ZERO);
		} else
			super.getEntity().setVelocity(direction.mul(speed));
	}

	@Override
	public void destroy() {
		// this.platform.destroy();
		super.destroy();
		super.getOwner().destroyActor(this);
	}

	@Override
	public void draw(Canvas canvas) {
		// this.platform.draw(canvas);
		graphics.draw(canvas);

	}

}

package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.weapons.Explosion;
import main.game.actor.weapons.Missile;
import main.math.*;
import main.math.Shape;
import main.window.Canvas;
import main.window.Image;

import java.awt.*;

/** {@linkplain Enemy} which drop missiles */
public class BoumBarder extends Enemy {

	/** Used for save purpose */
	private static final long serialVersionUID = 3299228244356552595L;

	/** Start and end points of the path of this {@linkplain BoumBarder} */
	private Vector start, end;

	/** Default speed */
	private float speed = 20;

	/** Whether this {@linkplain BoumBarder} is looking */
	private boolean right = true, up = true;

	/** Timer counting when to spawn a {@linkplain Missile} */
	private float missileTimer = 0;

	/** Spawning {@linkplain Missile} parameters */
	private float minMissileSpawnTime = 1f, maxMissileSpawnTime = 4;

	Vector direction, goingTo;

	private boolean goingHorizontal = true;
	
	private final float missilProbability;

	/**
	 * Create a new {@linkplain BoumBarder}
	 * @param game The master {@linkplain ActorGame} {@linkplain}
	 * @param position initial position of tis {@linkplain BoumBarder}
	 */
	public BoumBarder(ActorGame game, Vector position) {
		super(game, position);
		this.setPath(position, position, 1);
		this.missilProbability = .5f;
	}

	/**
	 * Create a new {@linkplain BoumBarder}
	 * @param game The master {@linkplain ActorGame} {@linkplain}
	 * @param startPosition initial position of this {@linkplain BoumBarder}
	 * @param endPosition final position of this {@linkplain BoumBarder}
	 * @param time Time to make a way between startPosition and endPosition
	 * @param minMissileSpawnTime minimum time to spawn a missile
	 * @param maxMissileSpawnTime Maximum time to spawn a missile
	 * @param missileProbabilitySpawn Probability to spawn a missile each second
	 */
	public BoumBarder(ActorGame game, Vector startPosition, Vector endPosition, float time, 
			float minMissileSpawnTime, float maxMissileSpawnTime, float missileProbabilitySpawn) {
		super(game, startPosition);
		this.setPath(startPosition, endPosition, time);
		this.minMissileSpawnTime = minMissileSpawnTime;
		this.maxMissileSpawnTime = maxMissileSpawnTime;
		this.missilProbability = missileProbabilitySpawn;
	}
	
	/**
	 * Set the path of this {@linkplain BoumBarder}
	 * @param start Start location of the path
	 * @param end End location of the path
	 * @param time time to go from start to end, positive {@linkplain BoumBarder}
	 */
	public void setPath(Vector start, Vector end, float time) {
		this.start = start;
		this.end = end;
		this.speed = ExtendedMath.getDistance(start, end) / Math.abs(time);
		this.right = start.x < end.x;
		this.up = start.y < end.y;
		this.goingHorizontal = Math.abs(start.x - end.x) > Math.abs(start.y - end.y);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		super.getEntity().setAngularPosition(0);
		super.getEntity().setAngularVelocity(0);

		Vector goingTo;
		if (goingHorizontal)
			goingTo = (right) ? ((start.x > end.x) ? start : end) : ((start.x < end.x) ? start : end);
		else
			goingTo = (up) ? ((start.y > end.y) ? start : end) : ((start.y < end.y) ? start : end);

		Vector direction = ExtendedMath.direction(getPosition(), goingTo).mul(-1);

		if (right && getPosition().x >= Math.max(start.x, end.x))
			right = false;
		else if (getPosition().x <= Math.min(start.x, end.x))
			right = true;

		if (up && (getPosition().y >= Math.max(start.y, end.y)))
			up = false;
		else if (getPosition().y <= Math.min(start.y, end.y))
			up = true;

		super.getEntity().setVelocity(direction.mul(speed));
		
		this.missileTimer += deltaTime;
		if ((this.missileTimer > this.minMissileSpawnTime && Math.random() < this.missilProbability * deltaTime)
				|| this.missileTimer > this.maxMissileSpawnTime) {
			getOwner().addActor(new Missile(getOwner(), getPosition().add(2.5f, -1f), getPosition().sub(0, 100)));
			this.missileTimer = 0;
		}
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);

		Image i = canvas.getImage("res/images/bomber.png");

		Transform t2 = new Transform(right ? 5 : -5, 0, getPosition().x + (right ? 0 : 5), 0,
				5 * i.getHeight() / i.getWidth(), getPosition().y);
		canvas.drawImage(i, t2, 1, 1);
		canvas.drawShape(new Polyline(start, end), Transform.I, Color.GREEN, Color.GREEN, .1f, 1, 3);

	}

	@Override
	public void kill() {

		start = getPosition();
		end = start.add(right ? 100 : -100, -10000);
		// this.getOwner().addActor(new ParticleEmitter(this.getOwner(),
		// this.getPosition().sub(0, .25f), null, 100,
		// (float) -Math.PI / 2f, (float) Math.PI, 1.2f, .1f, 1, .3f,
		// 0xFF830303, 0x00830303, 2, 20));

		this.getOwner().destroyActor(this);
		this.destroy();
	}

	@Override
	protected Shape getHitbox() {
		return ExtendedMath.createRectangle(5, 1);
	}

	@Override
	public void destroy() {
		super.destroy();
		getOwner().addActor(new Explosion(getOwner(), Vector.ZERO, this, 0));
		getOwner().addActor(new Explosion(getOwner(), new Vector(-1, 0), this, .2f));
		getOwner().addActor(new Explosion(getOwner(), new Vector(2, 0), this, .3f));
	}

}

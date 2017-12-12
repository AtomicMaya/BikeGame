package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.weapons.Explosion;
import main.game.actor.weapons.Missile;
import main.math.ExtendedMath;
import main.math.Shape;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;
import main.window.Image;

/** {@linkplain Enemy} which drop missiles */
public class Bombarder extends Enemy {

	/** Used for save purpose */
	private static final long serialVersionUID = 3299228244356552595L;

	private Vector start, end;

	private float speed = 20;

	private boolean right = true, up = true;

	private float missileTimer = 0;

	private final float minMissileSpawnTime = 1, maxMissileSpawnTime = 4;

	// /** An {@linkplain ArrayList} containing {@linkplain Missile}s fired. */
	// private ArrayList<Missile> missiles = new ArrayList<>();

	public Bombarder(ActorGame game, Vector position) {
		super(game, position);

	}

	/**
	 * Set the path of this {@linkplain Bombarder}
	 * @param start Start location of the path
	 * @param end End location of the path
	 * @param speed Number of meter per second moved by this, positive
	 * {@linkplain Bombarder}
	 */
	public void setPath(Vector start, Vector end, float speed) {
		this.start = start;
		this.end = end;
		this.speed = speed;
		this.right = start.x < end.x;
		this.up = start.y < end.y;

	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		super.getEntity().setAngularPosition(0);
		super.getEntity().setAngularVelocity(0);

		Vector goingTo = new Vector((right) ? Math.max(start.x, end.x) : Math.min(start.x, end.x),
				(up) ? Math.max(start.y, end.y) : Math.min(start.y, end.y));
		Vector direction = ExtendedMath.direction(getPosition(), goingTo).mul(-speed);

		Vector velocity = Vector.ZERO;
		if (right) {
			if (getPosition().x < Math.max(start.x, end.x)) {
				velocity = velocity.add(Vector.X.mul(direction));
			} else if (getPosition().x >= Math.max(start.x, end.x))
				right = false;
		} else {
			if (getPosition().x > Math.min(start.x, end.x))
				velocity = velocity.add(Vector.X.mul(direction));
			else if (getPosition().x <= Math.min(start.x, end.x))
				right = true;
		}

		if (up) {
			if (getPosition().y < Math.max(start.y, end.y)) {
				velocity = velocity.add(Vector.Y.mul(direction));
			} else if (getPosition().x >= Math.max(start.x, end.x))
				up = false;
		} else {
			if (getPosition().y > Math.min(start.y, end.y))
				velocity = velocity.add(Vector.Y.mul(direction));
			else if (getPosition().y <= Math.min(start.y, end.y))
				up = true;
		}
		super.getEntity().setVelocity(velocity);

		missileTimer += deltaTime;
		if ((missileTimer > minMissileSpawnTime && Math.random() < .5f * deltaTime)
				|| missileTimer > maxMissileSpawnTime) {
			getOwner().addActor(new Missile(getOwner(), getPosition().add(2.5f, -1f), getPosition().sub(0, 100)));
			missileTimer = 0;
		}
		// for(Missile m: missiles)m.update(deltaTime);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		// for(Missile m: missiles)m.draw(canvas);
		Image i = canvas.getImage("res/images/bomber.png");

		Transform t2 = new Transform(right ? 5 : -5, 0, getPosition().x + (right ? 0 : 5), 0,
				5 * i.getHeight() / i.getWidth(), getPosition().y);
		// Transform t = Transform.I.scaled(5, 5 * i.getHeight() /
		// i.getWidth()).rotated(right ? 0 : (float) Math.PI)
		// .translated(getPosition());
		canvas.drawImage(i, t2, 1, 1);

	}

	@Override
	public void kill() {
		
		start = getPosition();
		end = start.add(right ? 100 : -100, -10000);
		// this.getOwner().addActor(new ParticleEmitter(this.getOwner(),
		// this.getPosition().sub(0, .25f), null, 100,
		// (float) -Math.PI / 2f, (float) Math.PI, 1.2f, .1f, 1, .3f,
		// 0xFF830303, 0x00830303, 2, 20));

		 getOwner().destroyActor(this);
		 this.destroy();
		System.out.println(start + " "+ end);
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

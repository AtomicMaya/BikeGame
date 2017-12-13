package main.game.actor.weapons;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.actor.entities.GameEntity;
import main.math.*;
import main.window.Canvas;

import java.util.Set;

/** A Missile {@linkplain Weapon}. */
public class Missile extends GameEntity {
	
	/** Used for save purposes */
	private static final long serialVersionUID = 4121913978716092865L;

	/** The direction {@linkplain Vector}. */
	private Vector direction;

	/** The reference to where the image is stored on disk. */
	private String imagePath = "res/images/missile.red.4.png";

	/** The associated {@linkplain BasicContactListener}. */
	private BasicContactListener listener;

	/**
	 * Whether this {@linkplain main.game.actor.sensors.Trigger} is triggered.
	 */
	private boolean triggered = false;

	/** It's a secret ! */
	private float secretProbability = (float) Math.random();

	/**
	 * Creates a {@linkplain Missile}.
	 * @param game The master {@linkplain ActorGame}.
	 * @param position The initial position {@linkplain Vector}.
	 * @param targetPos The position {@linkplain Vector} of this
	 * {@linkplain Missile}'s target.
	 */
	public Missile(ActorGame game, Vector position, Vector targetPos) {
		super(game, false, position);
		this.direction = ExtendedMath.direction(position, targetPos).mul(-1);
		listener = new BasicContactListener();
		this.getEntity().addContactListener(this.listener);
		this.build(ExtendedMath.createRectangle(1, 24 / 96f), 100, -1, false, ObjectGroup.PROJECTILE.group);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		this.getEntity().setVelocity(this.direction.mul(18));
		this.getEntity().setAngularPosition(direction.getAngle());

		if (!this.triggered) {
			Set<Entity> entities = this.listener.getEntities();
			for (Entity entity : entities) {
				if (entity.getCollisionGroup() == ObjectGroup.PLAYER.group
						|| entity.getCollisionGroup() == ObjectGroup.WHEEL.group) {
					getOwner().getPayload().triggerDeath(false);
				} else if (entity.getCollisionGroup() != ObjectGroup.SENSOR.group
						&& entity.getCollisionGroup() != ObjectGroup.CHECKPOINT.group
						&& entity.getCollisionGroup() != ObjectGroup.FINISH.group)
					this.triggered = true;
			}
		}
		if (this.triggered) {
			getOwner().addActor(
					new Explosion(getOwner(), new Vector(0, (float) Math.sin(direction.getAngle())), this, 0));
			destroy();
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (!this.triggered) {
			// draw the missile graphics
			Vector resize = (this.secretProbability < 42 / 1337f) ? new Vector(1, 47 / 50f) : new Vector(1, 24 / 96f);
			canvas.drawImage(
					canvas.getImage((this.secretProbability < 42 / 1337f) ? "res/images/roquette.png" : this.imagePath),
					Transform.I.scaled(resize.x, resize.y).rotated((float) (this.direction.getAngle()))
							.translated(getPosition()),
					1, 12);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.getOwner().destroyActor(this);
	}

}

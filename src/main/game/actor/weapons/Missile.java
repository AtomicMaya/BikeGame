package main.game.actor.weapons;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.actor.entities.GameEntity;
import main.math.*;
import main.math.Shape;
import main.window.Canvas;

import java.awt.*;
import java.util.ArrayList;
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
	 * An {@linkplain ArrayList} containing the links to where the explosion
	 * animation graphics are stored on disk.
	 */
	private ArrayList<String> boomGraphics;

	/**
	 * Whether this {@linkplain main.game.actor.sensors.Trigger} is triggered.
	 */
	private boolean triggered = false;

	/** The time until the explosion has finished animating. */
	private final float animationTime = .5f;

	/** The elapsed animation time. */
	private float elapsedAnimationTime = 0;

	/** How far the animation has progressed. */
	private int graphicsCounter = 0;

	/** It's a secret ! */
	private float secretProbability = (float) Math.random();

	private Shape shape;

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
		shape = ExtendedMath.createRectangle(1, 24 / 96f);
//		boomGraphics = new ArrayList<>(
//				Arrays.asList("./res/images/explosion.bomb.0.png", "./res/images/explosion.bomb.1.png",
//						"./res/images/explosion.bomb.2.png", "./res/images/explosion.bomb.3.png",
//						"./res/images/explosion.bomb.4.png", "./res/images/explosion.bomb.5.png",
//						"./res/images/explosion.bomb.6.png", "./res/images/explosion.bomb.7.png",
//						"./res/images/explosion.bomb.8.png", "./res/images/explosion.bomb.9.png",
//						"./res/images/explosion.bomb.10.png", "./res/images/explosion.bomb.11.png",
//						"./res/images/explosion.bomb.12.png", "./res/images/explosion.bomb.13.png",
//						"./res/images/explosion.bomb.14.png", "./res/images/explosion.bomb.15.png",
//						"./res/images/explosion.bomb.16.png", "./res/images/explosion.bomb.17.png",
//						"./res/images/explosion.bomb.18.png", "./res/images/explosion.bomb.19.png",
//						"./res/images/explosion.bomb.20.png", "./res/images/explosion.bomb.21.png",
//						"./res/images/explosion.bomb.22.png", "./res/images/explosion.bomb.23.png",
//						"./res/images/explosion.bomb.24.png", "./res/images/explosion.bomb.25.png",
//						"./res/images/explosion.bomb.26.png", "./res/images/explosion.bomb.27.png",
//						"./res/images/explosion.bomb.28.png", "./res/images/explosion.bomb.29.png",
//						"./res/images/explosion.bomb.30.png", "./res/images/explosion.bomb.31.png"));

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
//			this.elapsedAnimationTime += deltaTime;
			getOwner().destroyActor(this);
		}
//		this.graphicsCounter = (int) Math
//				.floor(this.elapsedAnimationTime / this.animationTime * this.boomGraphics.size());
//		if (this.graphicsCounter > this.boomGraphics.size() - 1) {
//			this.destroy();
//			this.graphicsCounter = 0;
//		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (!this.triggered) {
			Vector resize = (this.secretProbability < 42 / 404f) ? new Vector(1, 47 / 50f) : new Vector(1, 24 / 96f);
			canvas.drawImage(
					canvas.getImage((this.secretProbability < 42 / 404f) ? "res/images/roquette.png" : this.imagePath),
					Transform.I.scaled(resize.x, resize.y).rotated((float) (this.direction.getAngle()))
							.translated(getPosition()),
					1, 12);
			canvas.drawShape(shape, getTransform(), Color.black, Color.black, 0, .3f, 12000);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		this.getOwner().destroyActor(this);
	}

}

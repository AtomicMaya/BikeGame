package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.graphics.ImageGraphics;
import main.io.Saveable;
import main.math.BasicContactListener;
import main.math.Entity;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;

/** Creates an Exploding/Acid barrel */
public class BoomBarrel extends GameEntity implements Saveable {
    /** Used for save purposes. */
	private static final long serialVersionUID = -1395668954465139357L;

    /** Reference the {@linkplain ImageGraphics}'s graphical representation. */
	private transient ImageGraphics graphics;

    /** An {@linkplain ArrayList} of {@linkplain String} containing the file paths to the resource files. */
	private transient ArrayList<String> boomGraphics;

	/** The full explosion time. Default : {@value} */
	private final float animationTime = .5f;

	/** The elapsed animation time. */
	private float elapsedAnimationTime = 0;

	/** The reference of which animation stage is currently active. */
	private int graphicsCounter;

	/** The {@linkplain BasicContactListener} associated with this {@linkplain BoomBarrel}. */
	private transient BasicContactListener contactListener;

	/** Whether this {@linkplain BoomBarrel} was triggered. Default {@value} */
	private boolean triggered = false;

	/** Whether this {@linkplain BoomBarrel} is explosive. */
	private boolean explosive;

	/** Whether the {@linkplain PlayableEntity} triggered this {@linkplain BoomBarrel}, or if it was a projectile. */ // TODO call class Projectile.
	private boolean wasPlayer = false;

    /**
     * Creates a new {@linkplain BoomBarrel}.
     * @param game The master {@linkplain ActorGame}.
     * @param position The initial position {@linkplain Vector}.
     * @param explosive A {@linkplain boolean} whether this {@linkplain BoomBarrel} is explosive (true) or acid (false).
     */
	public BoomBarrel(ActorGame game, Vector position, boolean explosive) {
		super(game, false, position);
		this.explosive = explosive;
		this.create();
	}

    /**
     *
     */
	private void create() {
		this.build(new Polygon(0, 0, 1, 0, 1, 1.5f, 0, 1.5f), 100, 1, false, ObjectGroup.ENEMY.group);

		this.graphics = addGraphics(this.explosive ? "./res/images/barrel.red.png" : "./res/images/barrel.green.png", 1,
				1.5f);
		if (this.explosive) {
			this.boomGraphics = new ArrayList<>(
					Arrays.asList("./res/images/explosion.bomb.0.png", "./res/images/explosion.bomb.1.png",
							"./res/images/explosion.bomb.2.png", "./res/images/explosion.bomb.3.png",
							"./res/images/explosion.bomb.4.png", "./res/images/explosion.bomb.5.png",
							"./res/images/explosion.bomb.6.png", "./res/images/explosion.bomb.7.png",
							"./res/images/explosion.bomb.8.png", "./res/images/explosion.bomb.9.png",
							"./res/images/explosion.bomb.10.png", "./res/images/explosion.bomb.11.png",
							"./res/images/explosion.bomb.12.png", "./res/images/explosion.bomb.13.png",
							"./res/images/explosion.bomb.14.png", "./res/images/explosion.bomb.15.png",
							"./res/images/explosion.bomb.16.png", "./res/images/explosion.bomb.17.png",
							"./res/images/explosion.bomb.18.png", "./res/images/explosion.bomb.19.png",
							"./res/images/explosion.bomb.20.png", "./res/images/explosion.bomb.21.png",
							"./res/images/explosion.bomb.22.png", "./res/images/explosion.bomb.23.png",
							"./res/images/explosion.bomb.24.png", "./res/images/explosion.bomb.25.png",
							"./res/images/explosion.bomb.26.png", "./res/images/explosion.bomb.27.png",
							"./res/images/explosion.bomb.28.png", "./res/images/explosion.bomb.29.png",
							"./res/images/explosion.bomb.30.png", "./res/images/explosion.bomb.31.png"));
		} else {
			this.boomGraphics = new ArrayList<>(
					Arrays.asList("./res/images/explosion.acid.0.png", "./res/images/explosion.acid.1.png",
							"./res/images/explosion.acid.2.png", "./res/images/explosion.acid.3.png",
							"./res/images/explosion.acid.4.png", "./res/images/explosion.acid.5.png",
							"./res/images/explosion.acid.6.png", "./res/images/explosion.acid.7.png",
							"./res/images/explosion.acid.8.png", "./res/images/explosion.acid.9.png",
							"./res/images/explosion.acid.10.png", "./res/images/explosion.acid.11.png",
							"./res/images/explosion.acid.12.png", "./res/images/explosion.acid.13.png",
							"./res/images/explosion.acid.14.png", "./res/images/explosion.acid.15.png"));
		}
		this.contactListener = new BasicContactListener();
		this.addContactListener(this.contactListener);
	}

	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
		create();
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (this.triggered)
			this.elapsedAnimationTime += deltaTime;

		if (this.contactListener.getEntities().size() > 0)
			for (Entity entity : this.contactListener.getEntities()) {
				if (this.explosive && entity.getCollisionGroup() != ObjectGroup.PROJECTILE.group)
					entity.applyImpulse(new Vector(50, 50), Vector.ZERO);
				if (entity.getCollisionGroup() == ObjectGroup.PLAYER.group
						|| entity.getCollisionGroup() == ObjectGroup.WHEEL.group) {
					this.triggered = true;
					this.wasPlayer = true;
				} else if (entity.getCollisionGroup() == ObjectGroup.PROJECTILE.group)
					this.triggered = true;
			}

		this.graphicsCounter = (int) Math
				.floor(this.elapsedAnimationTime / this.animationTime * this.boomGraphics.size());
		if (this.graphicsCounter > this.boomGraphics.size() - 1) {
			if (wasPlayer)
				this.getOwner().getPayload().triggerDeath(false);
			this.destroy();
			this.graphicsCounter = 0;
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (!this.triggered)
			this.graphics.draw(canvas);
		else {
			this.addGraphics(this.boomGraphics.get(this.graphicsCounter), 4, 4, Vector.ZERO, 1, 10).draw(canvas);

		}
	}

	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}

	public void trigger() {
		this.triggered = true;
	}
}

package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.actor.sensors.ProximitySensor;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;

/** Create new Mines, that explode in your face. */
public class Mine extends GameEntity {
    /** An {@linkplain ArrayList} of {@linkplain String} containing references of file locations. */
	private transient ArrayList<String> stateGraphics, boomGraphics;

	/** Relevant animation times. */
	private final float beepTime, boomAnimationTime;

    /** The number of times the mine will beep before it's explosion. */
	private final int beeps;

    /** The time since the mine was triggered up until it explodes*/
	private float elapsedTime, elapsedBoomTime;

    /** Booleans relevant to the advancement of time. */
	private boolean triggered, blowingUp;

	/** Booleans relevant to the animation. */
	private boolean state, previousState;

    /** Indices of the graphical advancement preceding the explosion.*/
	private int boomGraphicsCounter, currentBeep;

    /** The local {@linkplain ProximitySensor}, so that the {@linkplain PlayableEntity} is the only one able to
     *  trigger these mines. */
	private transient ProximitySensor sensor;

    /**
     * Creates a new {@linkplain Mine} .
     * @param game The master {@linkplain ActorGame}.
     * @param position The initial position {@linkplain Vector}.
     */
	public Mine(ActorGame game, Vector position, float delay) {
		super(game, true, position);

		this.beepTime = delay;
		this.boomAnimationTime = .3f;
		this.beeps = 3;
		this.state = false;
		this.previousState = false;
		this.triggered = false;
		this.create();
	}

	public Mine(ActorGame game, Vector position) {
	    this(game, position, 1.1f);
    }

    /**
     * Actual creation of the parameters of the {@linkplain GameEntity}, not in the
     * constructor to avoid duplication with the method {@linkplain #reCreate(ActorGame)}
     */
	private void create() {
		this.build(new Polygon(0, 0, .5f, 0, .5f, 1, 0, 1), -1, -1, false, ObjectGroup.ENEMY.group);

		this.stateGraphics = new ArrayList<>(Arrays.asList("./res/images/mine.0.png", "./res/images/mine.1.png"));
		this.boomGraphics = new ArrayList<>(
				Arrays.asList("./res/images/mine.explosion.0.png", "./res/images/mine.explosion.1.png",
						"./res/images/mine.explosion.2.png", "./res/images/mine.explosion.3.png",
						"./res/images/mine.explosion.4.png", "./res/images/mine.explosion.5.png",
						"./res/images/mine.explosion.6.png", "./res/images/mine.explosion.7.png",
						"./res/images/mine.explosion.8.png", "./res/images/mine.explosion.9.png",
						"./res/images/mine.explosion.10.png", "./res/images/mine.explosion.11.png",
						"./res/images/mine.explosion.12.png", "./res/images/mine.explosion.13.png",
						"./res/images/mine.explosion.14.png", "./res/images/mine.explosion.15.png"));

		this.triggered = false;
		this.sensor = new ProximitySensor(getOwner(), getPosition().add(-.5f, 1),
				new Polygon(0, 0, 1.5f, 0, 1.5f, 5, 0, 5));
	}

	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
		this.create();
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		this.sensor.update(deltaTime);
		if (this.sensor.getSensorDetectionStatus())
			this.triggered = true;

		if (this.triggered) {
			this.elapsedTime += deltaTime;
			this.state = (int) Math.floor(this.elapsedTime / this.beepTime * this.boomGraphics.size() * this.beeps)
					% this.boomGraphics.size() == 1;
		}

		if (this.state != this.previousState && this.state)
			this.currentBeep += 1;

		if (this.currentBeep >= this.beeps)
			this.blowingUp = true;


		if (this.blowingUp) {
			this.elapsedBoomTime += deltaTime;
			this.boomGraphicsCounter = (int) Math
					.floor(this.elapsedBoomTime / this.boomAnimationTime * (this.boomGraphics.size()));
		}
		if (this.boomGraphicsCounter == this.boomGraphics.size() / 2 && this.sensor.getCollidingEntity() != null)
            this.sensor.getCollidingEntity().applyImpulse( new Vector(5, 25), Vector.ZERO);

		if (this.boomGraphicsCounter > this.boomGraphics.size() - 1) {
			if (this.sensor.getSensorDetectionStatus())
				this.getOwner().getPayload().triggerDeath(false);
			this.destroy();
			this.boomGraphicsCounter = 0;
		}

		this.previousState = this.state;
	}

	@Override
	public void destroy() {
		this.sensor.destroy();
		super.destroy();
		this.getOwner().destroyActor(this);
	}

	@Override
	public void draw(Canvas canvas) {
		if (this.blowingUp)
			this.sensor.addGraphics(this.boomGraphics.get(this.boomGraphicsCounter), 1.5f, 3).draw(canvas);
		if (this.triggered)
			this.addGraphics(this.stateGraphics.get(this.state ? 1 : 0), .5f, 1, Vector.ZERO, 1, 100).draw(canvas);
	}

	@Override
	public void setPosition(Vector newPosition) {
		super.setPosition(newPosition);
		this.sensor.setPosition(newPosition);
	}
}

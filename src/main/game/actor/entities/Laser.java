package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.sensors.ProximitySensor;
import main.game.audio.Audio;
import main.game.graphics.ImageGraphics;
import main.game.graphics.ShapeGraphics;
import main.math.Polyline;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.util.Random;

public class Laser extends GameEntity {
    /** Used for save purposes. */
	private static final long serialVersionUID = 1950911327670926865L;

	/** The master {@linkplain ActorGame}. */
	private transient ActorGame game;

	/** The emitter position {@linkplain Vector}. */
	private Vector startPosition;

	/** The {@linkplain Laser} length. */
	private float distance;

	/** Time values used to delimit animation stages. */
	private float waitTime, pulsateTime, laserTime, elapsedTime;

	/** Number of oscillations. */
	private float oscillationCount, maxOscillationCount;

	/** It's a secret ! */
	private final float secretProbability = 4.2f / 404;

	/** Number of discharges. */
	private int maxFires, firesCount;

	/** Direction of the {@linkplain Laser}. */
	private int direction;

	/** The {@linkplain Color} of the {@linkplain Laser}. */
	private Color color;

	/** The linked {@linkplain ProximitySensor}. */
	private transient ProximitySensor sensor;

	/** Whether the {@linkplain ProximitySensor} is active. */
	private boolean sensorActive;

	/** The {@linkplain Polyline} representing the shape of the {@linkplain Laser}'s beam.*/
	private transient Polyline shape;

	/** The {@linkplain Laser} beam's graphical representation. */
	private transient ShapeGraphics graphics;

	/** The {@linkplain Laser} emitter's graphical representation. */
	private transient ImageGraphics emitterGraphics;

	/** Whether this {@linkplain Laser} is triggered. */
	private boolean triggered;

    /**
     * Creates a new {@linkplain Laser}.
     * @param game The master {@linkplain ActorGame}.
     * @param startPosition The initial position {@linkplain Vector}.
     * @param distance The distance of the {@linkplain Laser}.
     * @param waitTime The duration of the {@linkplain Laser} beam's idling.
     * @param pulsateTime The duration of the {@linkplain Laser} beam's oscillation, which doesn't harm the Player.
     * @param laserTime The duration of the {@linkplain Laser} beam's output.
     * @param maxFires The maximum number of iterations, negative numbers are considered as infinite.
     * @param direction The orientation of the {@linkplain Laser}.
     * @param color The {@linkplain String} value of the {@linkplain Color}.
     */
	public Laser(ActorGame game, Vector startPosition, float distance, float waitTime, float pulsateTime,
			float laserTime, int maxFires, int direction, String color, boolean triggered) {
		super(game, true, startPosition);
		this.game = game;
		this.startPosition = startPosition;
		this.distance = distance;

		this.waitTime = waitTime;
		this.pulsateTime = pulsateTime;
		this.laserTime = laserTime;

		this.maxFires = maxFires;
		this.maxFires = this.maxFires < 0 ? -1 : this.maxFires;

		this.direction = direction;
		this.color = Color.decode(color);

		this.firesCount = 0;
		this.elapsedTime = 0;
		this.oscillationCount = 0;
		this.maxOscillationCount = 3.5f;
		this.sensorActive = false;
		this.triggered = triggered;

		this.create();
	}

    /**
     * Creates a new {@linkplain Laser}.
     * @param game The master {@linkplain ActorGame}.
     * @param startPosition The initial position {@linkplain Vector}.
     * @param distance The distance of the {@linkplain Laser}.
     * @param direction The orientation of the {@linkplain Laser}.
     */
    public Laser(ActorGame game, Vector startPosition, float distance, int direction) {
        this(game, startPosition, distance, 2, 2, 3, -1, direction, "#00FFFF", true);
    }

    /**
     * Creates a new {@linkplain Laser}.
     * @param game The master {@linkplain ActorGame}.
     * @param startPosition The initial position {@linkplain Vector}.
     * @param distance The distance of the {@linkplain Laser}.
     * @param direction The orientation of the {@linkplain Laser}.
     * @param triggered whether the {@linkplain Laser} is initially triggered.
     */
    public Laser(ActorGame game, Vector startPosition, float distance, int direction, boolean triggered) {
        this(game, startPosition, distance, 2, 2, 3, -1, direction, "#00FFFF", triggered);
    }

	/**
     * Actual creation of the parameters of the {@linkplain GameEntity}, not in
     * the constructor to avoid duplication with the method {@linkplain #reCreate(ActorGame)}
     */
	private void create() {
		switch (this.direction) {
			default:
			case 0:
				this.shape = new Polyline(0, 0,  this.distance, 0);
			    break;
			case 1:
				this.shape = new Polyline(0, 0, 0,  this.distance);
			    break;
			case 2:
				this.shape = new Polyline(0, 0, - this.distance, 0);
			    break;
			case 3:
				this.shape = new Polyline(0, 0, 0, - this.distance);
			    break;
		}

		if (this.sensor != null)
            this.sensor.destroy();
		this.sensor = new ProximitySensor(this.game,  this.startPosition, this.shape);
		this.graphics = this.addGraphics(this.shape,  this.color,  this.color.darker(), .3f, 0, -4);

		this.emitterGraphics = this.addGraphics("./res/images/blaster." + (this.direction + 1) + ".png", 1, 1,
				new Vector(.5f, .5f), 1, 2);
	}

	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
		this.game = game;
		create();
	}



    @Override
    public void update(float deltaTime) {
    	super.update(deltaTime);
        this.sensor.update(deltaTime);
        if (this.triggered) {
            if (this.firesCount != this.maxFires || this.maxFires >= 0) {
                this.elapsedTime += deltaTime;
                float randomValue = new Random().nextFloat();
                if (this.elapsedTime < this.waitTime) {
                    this.graphics = this.addGraphics(this.shape, null, null, .3f, 0, 1);
                } else if (this.waitTime < this.elapsedTime && this.elapsedTime < waitTime + pulsateTime) {
                    this.oscillationCount += 1;
                    if (!this.sensor.isOccupied())
                        this.sensor.runAction(() -> new Audio(randomValue < this.secretProbability ? "./res/audio/easter_egg_1.wav" : "./res/audio/laser.wav",
                                0, 30f), this.pulsateTime + this.laserTime);

                    // Make the laser oscillate, warning players that it is charging.
                    if (0 < this.oscillationCount && this.oscillationCount < this.maxOscillationCount) {
                        this.graphics = this.addGraphics(this.shape, this.color.brighter(), this.color.darker(), .4f, .2f,
                                1);
                    } else if (this.oscillationCount > this.maxOscillationCount) {
                        // Reset the counter.
                        this.oscillationCount = -this.maxOscillationCount;
                    } else {
                        this.graphics = this.addGraphics(this.shape, null, null, .3f, 0, 1);
                    }
                } else if (this.waitTime + this.pulsateTime < this.elapsedTime
                        && this.elapsedTime < this.waitTime + this.pulsateTime + this.laserTime) {
                    this.sensorActive = true;
                    this.graphics = this.addGraphics(this.shape, this.color.brighter(), this.color.darker(), .3f, .7f, 1);
                } else if (this.waitTime + this.pulsateTime + this.laserTime < this.elapsedTime) {
                    this.elapsedTime = 0;
                    this.sensorActive = false;
                    this.firesCount += 1;
                }
            } else {
                this.graphics = this.addGraphics(this.shape, null, null, .3f, 0, 1);
            }

            if (this.sensorActive && this.sensor.getSensorDetectionStatus()) {
                this.game.getPayload().triggerDeath(false);
            }
        }

        if (!this.triggered) {
            this.graphics = this.addGraphics(this.shape, null, null, .3f, 0, 1);
        }
	}

	@Override
	public void draw(Canvas canvas) {
		this.graphics.draw(canvas);
		this.emitterGraphics.draw(canvas);
	}

	@Override
	public void destroy() {
		this.sensor.destroy();
		super.destroy();
		super.getOwner().destroyActor(this);
	}

    /**
     * Change the direction of the {@linkplain Laser}.
     * @param direction The new direction of the {@linkplain Laser}.
     * @return a new {@linkplain Polyline}.
     */
	public Polyline changeDirection(int direction) {
		this.direction = direction;
		this.create();
		return this.shape;
	}

    /**
     * Sets a new length for the {@linkplain Laser}'s beam.
     * @param length The new length.
     */
	public void setLength(float length) {
		this.distance = length;
		this.create();
	}

	@Override
	public void setPosition(Vector position) {
		super.setPosition(position);
		this.startPosition = position;
	}

	public void switchState() {
	    this.triggered = !this.triggered;
    }
}

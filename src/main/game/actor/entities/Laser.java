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

	private static final long serialVersionUID = 1950911327670926865L;

	private transient ActorGame game;
	private Vector startPosition;
	private float distance;
	// private float width;
	private float waitTime, pulsateTime, laserTime, elapsedTime;
	private float oscillationCount, maxOscillationCount;
	private final float secretProbability = 4.2f / 404;
	private int maxFires, firesCount;
	private int direction;
	private Color color;

	private transient ProximitySensor sensor;
	private boolean sensorActive;
	private transient Polyline shape;
	private transient ShapeGraphics graphics;
	private transient ImageGraphics emitterGraphics;

	public Laser(ActorGame game, Vector startPosition, float distance, float width, float waitTime, float pulsateTime,
			float laserTime, int maxFires, int direction, String color) {
		super(game, true, startPosition);
		this.game = game;
		this.startPosition = startPosition;
		this.distance = distance;
		// this.width = width;
		this.waitTime = waitTime;
		this.pulsateTime = pulsateTime;
		this.laserTime = laserTime;
		this.maxFires = maxFires;
		this.direction = direction;
		this.color = Color.decode(color);

		this.firesCount = 0;
		this.elapsedTime = 0;
		this.oscillationCount = 0;
		this.maxOscillationCount = 3.5f;
		this.sensorActive = false;

		create();
	}

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

		if ( this.sensor != null)
            this.sensor.destroy();
		this.sensor = new ProximitySensor(this.game,  this.startPosition, this.shape);
		this.graphics = this.addGraphics(this.shape,  this.color,  this.color.darker(), .3f, 0, 1);

		this.emitterGraphics = this.addGraphics("./res/images/blaster." + (this.direction + 1) + ".png", 1, 1,
				new Vector(.5f, .5f), 1, 2);
	}

	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
		this.game = game;
		create();
	}

	public Laser(ActorGame game, Vector startPosition, float distance, int direction) {
		this(game, startPosition, distance, .5f, 2, 4, 3, 1, direction, "#00FFFF");
	}

    @Override
    public void update(float deltaTime) {
        this.sensor.update(deltaTime);
        if(this.firesCount != this.maxFires) {
            this.elapsedTime += deltaTime;
            float randomValue = new Random().nextFloat();
            if (this.elapsedTime < this.waitTime) {
                this.graphics = this.addGraphics(this.shape, null, null, .3f, 0, 1);
            } else if (this.waitTime < this.elapsedTime && this.elapsedTime < waitTime + pulsateTime) {
                this.oscillationCount += 1;
                if(!this.sensor.isOccupied())
                    this.sensor.runAction(() -> new Audio(randomValue < this.secretProbability ? "./res/audio/easter_egg_1.wav": "./res/audio/laser.wav",
                            0, 20f), this.pulsateTime + this.laserTime);

				// So that the laser will blink, so as to warn people that it is
				// charging.
				if (0 < this.oscillationCount && this.oscillationCount < this.maxOscillationCount) {
					this.graphics = this.addGraphics(this.shape, this.color.brighter(), this.color.darker(), .3f, .2f,
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
				this.graphics = this.addGraphics(this.shape, this.color.brighter(), this.color.darker(), .3f, .5f, 1);
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

	public Polyline changeDirection(int direction) {
		this.direction = direction;
		this.create();
		return shape;
	}

	public void setLength(float length) {
		this.distance = length;
		create();
	}

	@Override
	public void setPosition(Vector position) {
		super.setPosition(position);
		this.startPosition = position;
	}
}

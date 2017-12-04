package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Sensor;
import main.game.actor.ShapeGraphics;
import main.math.BasicContactListener;
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;
import main.window.Keyboard;

import java.awt.*;

// TODO remove drawable component -> visual aid

public class KeyboardProximitySensor extends GameEntity implements Sensor {
	private Keyboard keyboard;
	private int key;

	private Shape sensorArea;
	private ShapeGraphics graphics;

	private BasicContactListener contactListener;

	private boolean detectionStatus, previousDetectionStatus = false;
	private boolean keyPressedStatus;
	private boolean sensorOccupied = false;
	private float timeToActionEnd, elapsedActionTime = 0.f;

	public KeyboardProximitySensor(ActorGame game, Vector position, Shape shape, int key) {
		super(game, true, position);
		this.keyboard = game.getKeyboard();
		this.key = key;

		this.sensorArea = shape;
		this.build(this.sensorArea, -1, -1, true);
		this.graphics = this.addGraphics(this.sensorArea, Color.GREEN, Color.GREEN, .1f, 0.25f, 0);

		this.contactListener = new BasicContactListener();
		this.addContactListener(this.contactListener);
	}

	@Override
	public void update(float deltaTime) {
		this.detectionStatus = this.contactListener.getEntities().size() > 0;
		this.keyPressedStatus = this.keyboard.get(this.key).isDown();

		if (this.detectionStatus && this.keyPressedStatus)
			this.graphics = addGraphics(this.sensorArea,
					this.detectionStatus ? Color.RED : Color.GREEN,
					this.detectionStatus ? Color.RED : Color.GREEN, .1f, 0.25f, 0);
		if (this.previousDetectionStatus != this.detectionStatus) {
			this.graphics = addGraphics(this.sensorArea, Color.GREEN,  Color.GREEN, .1f, 0.25f, 0);
		}
		this.previousDetectionStatus = this.detectionStatus;

		if (this.sensorOccupied) {
			this.elapsedActionTime += deltaTime;
			if (this.elapsedActionTime > this.timeToActionEnd) {
				this.sensorOccupied = false;
				this.elapsedActionTime = 0.f;
			}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		this.graphics.draw(canvas);
	}

	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}

	@Override
	public boolean getSensorDetectionStatus() {
		return this.detectionStatus && this.keyPressedStatus;
	}

	@Override
	public void runAction(Runnable runnable, float time) {
		this.sensorOccupied = true;
        this.timeToActionEnd = time > this.timeToActionEnd ? time : this.timeToActionEnd;
		Runner.generateWorker(runnable).execute();
	}

	@Override
	public boolean isOccupied() {
		return this.sensorOccupied;
	}
}

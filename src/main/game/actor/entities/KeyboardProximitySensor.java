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

import static main.game.actor.QuickMafs.generateWorker;

// TODO remove drawable component -> visual aid

public class KeyboardProximitySensor extends GameEntity implements Sensor {
	private ActorGame game;
	private Keyboard keyboard;
	private int key;

	private Shape sensorArea;
	private ShapeGraphics graphics;

	private BasicContactListener contactListener;

	private boolean detectionStatus, previousDetectionStatus = false;
	private boolean keyPressedStatus;
	private boolean sensorOccupied = false;
	float timeToActionEnd, elapsedActionTime = 0.f;

	public KeyboardProximitySensor(ActorGame game, Vector position, Shape shape, int key) {
		super(game, true, position);
		this.game = game;
		this.keyboard = game.getKeyboard();
		this.key = key;

		sensorArea = shape;
		this.build(sensorArea, -1, -1, true);
		graphics = this.addGraphics(sensorArea, Color.GREEN, Color.GREEN, .1f, 0.25f, 0);

		contactListener = new BasicContactListener();
		this.addContactListener(contactListener);
	}

	@Override
	public void update(float deltaTime) {
		detectionStatus = contactListener.getEntities().size() > 0;
		keyPressedStatus = keyboard.get(key).isDown();

		if (detectionStatus && keyPressedStatus)
			graphics = addGraphics(sensorArea,
					detectionStatus && keyPressedStatus ? Color.RED : Color.GREEN,
					detectionStatus && keyPressedStatus ? Color.RED : Color.GREEN, .1f, 0.25f, 0);
		if (previousDetectionStatus != detectionStatus) {
			graphics = addGraphics(sensorArea, Color.GREEN,  Color.GREEN, .1f, 0.25f, 0);
		}
		previousDetectionStatus = detectionStatus;

		if (sensorOccupied) {
			elapsedActionTime += deltaTime;
			if (elapsedActionTime > timeToActionEnd) {
				sensorOccupied = false;
				elapsedActionTime = 0.f;
			}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		graphics.draw(canvas);
	}

	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}

	@Override
	public boolean getSensorDetectionStatus() {
		return detectionStatus && keyPressedStatus;
	}

	@Override
	public void runAction(Runnable runnable, float time) {
		sensorOccupied = true;
		timeToActionEnd = time;
		generateWorker(runnable).execute();
	}

	@Override
	public boolean isOccupied() {
		return sensorOccupied;
	}
}

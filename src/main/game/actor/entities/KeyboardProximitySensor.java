package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Sensor;
import main.game.actor.ShapeGraphics;
import main.math.BasicContactListener;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;
import main.window.Keyboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

// TODO remove drawable component -> visual aid

public class KeyboardProximitySensor extends GameEntity implements Sensor {
	private Polygon sensorArea;
	private float width, height;
	private ActorGame game;
	private ShapeGraphics graphics;
	private boolean detectionStatus, previousDetectionStatus = false;
	private boolean keyPressedStatus;
	private BasicContactListener contactListener;
	private Keyboard keyboard;
	private boolean busy = false;
	float timeToActionEnd, elapsedActionTime = 0.f;

	public KeyboardProximitySensor(ActorGame game, Vector position, float width, float height) {
		super(game, true, position);
		this.game = game;
		this.keyboard = game.getKeyboard();
		this.width = width;
		this.height = height;

		sensorArea = new Polygon(.0f, .0f, width, .0f, width, height, .0f, height);
		this.build(sensorArea, -1, -1, true);
		graphics = this.addGraphics(sensorArea, Color.GREEN, Color.GREEN, .1f, 0.75f, 0);

		contactListener = new BasicContactListener();
		this.addContactListener(contactListener);
	}

	@Override
	public void update(float deltaTime) {
		detectionStatus = contactListener.getEntities().size() > 0;
		keyPressedStatus = keyboard.get(KeyEvent.VK_E).isDown();

		if (detectionStatus && keyPressedStatus)
			graphics = addGraphics(sensorArea,
					detectionStatus && keyPressedStatus ? Color.RED : Color.GREEN,
					detectionStatus && keyPressedStatus ? Color.RED : Color.GREEN, .1f, 0.75f, 0);
		if (previousDetectionStatus != detectionStatus) {
			graphics = addGraphics(sensorArea, Color.GREEN,  Color.GREEN, .1f, 0.75f, 0);
		}
		previousDetectionStatus = detectionStatus;

		if (busy) {
			elapsedActionTime += deltaTime;
			if (elapsedActionTime > timeToActionEnd) {
				busy = false;
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
	private SwingWorker<Void, Void> generateWorker(Runnable action) {
		return new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() {
				action.run();
				return null;
			}
		};
	}

	@Override
	public void setAction(Runnable runnable, float time) {
		busy = true;
		timeToActionEnd = time;
		generateWorker(runnable).execute();
	}

	@Override
	public boolean getIfBusy() {
		return busy;
	}
}

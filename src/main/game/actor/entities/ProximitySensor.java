package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Sensor;
import main.game.actor.ShapeGraphics;
import main.math.BasicContactListener;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

import javax.swing.*;
import java.awt.*;

/**
 * Created on 11/30/2017 at 6:23 PM.
 */
public class ProximitySensor extends GameEntity implements Sensor {
	private Polygon sensorArea;
	private float width, height;
	private ActorGame game;
	private ShapeGraphics graphics;
	private boolean detectionStatus, previousDetectionStatus;
	private BasicContactListener contactListener;
	private boolean busy = false;
	float timeToActionEnd, elapsedActionTime = 0.f;

	public ProximitySensor(ActorGame game, Vector position, float width, float height) {
		super(game, true, position);
		this.game = game;
		this.width = width;
		this.height = height;

		sensorArea = new Polygon(.0f, .0f, width, .0f, width, height, .0f, height);
		build(this.getEntity(), sensorArea, -1, -1, true);
		graphics = addGraphics(this.getEntity(), sensorArea, Color.GREEN, Color.GREEN, .1f, 0.75f, 0);

		contactListener = new BasicContactListener();
		this.getEntity().addContactListener(contactListener);
	}

	@Override
	public void update(float deltaTime) {
		detectionStatus = contactListener.getEntities().size() > 0;
		if (detectionStatus != previousDetectionStatus)
			graphics = addGraphics(this.getEntity(), sensorArea, detectionStatus ? Color.RED : Color.GREEN,
					detectionStatus ? Color.RED : Color.GREEN, .1f, 0.75f, 0);

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
		return detectionStatus;
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

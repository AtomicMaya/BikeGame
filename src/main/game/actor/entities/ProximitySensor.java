package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Sensor;
import main.game.actor.ShapeGraphics;
import main.math.BasicContactListener;
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;

public class ProximitySensor extends GameEntity implements Sensor {
	private Shape sensorArea;

	private ShapeGraphics graphics;
	private boolean detectionStatus, previousDetectionStatus;
	private BasicContactListener contactListener;
	private boolean sensorOccupied = false;
	private float timeToActionEnd = 0, elapsedActionTime = 0.f;

	public ProximitySensor(ActorGame game, Vector position, Shape shape) {
		super(game, true, position);
		this.sensorArea = shape;

		this.build(sensorArea, -1, -1, true);
		graphics = addGraphics(sensorArea, Color.GREEN, Color.GREEN, .1f, 0.25f, 0);

		contactListener = new BasicContactListener();
		this.addContactListener(contactListener);
	}

	@Override
	public void update(float deltaTime) {
		detectionStatus = contactListener.getEntities().size() > 0;
		if (detectionStatus != previousDetectionStatus)
			graphics = addGraphics(sensorArea, detectionStatus ? Color.RED : Color.GREEN,
					detectionStatus ? Color.RED : Color.GREEN, .1f, 0.25f, 0);

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
		return detectionStatus;
	}

	@Override
	public void runAction(Runnable runnable, float time) {
		sensorOccupied = true;
        this.timeToActionEnd = time > this.timeToActionEnd ? time : this.timeToActionEnd;
		ParallelAction.generateWorker(runnable).execute();
	}

	@Override
	public boolean isOccupied() {
		return sensorOccupied;
	}

}

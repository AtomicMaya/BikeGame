package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Sensor;
import main.game.actor.ShapeGraphics;
import main.math.BasicContactListener;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;

import static main.game.actor.QuickMafs.generateWorker;

public class ProximitySensor extends GameEntity implements Sensor {
	private Polygon sensorArea;

	private ActorGame game;
	private ShapeGraphics graphics;
	private boolean detectionStatus, previousDetectionStatus;
	private BasicContactListener contactListener;
	private boolean busy = false;
	float timeToActionEnd, elapsedActionTime = 0.f;

	public ProximitySensor(ActorGame game, Vector position, Polygon shape) {
		super(game, true, position);
		this.game = game;
		this.sensorArea = shape;

		this.build(sensorArea, -1, -1, true);
		graphics = addGraphics(sensorArea, Color.GREEN, Color.GREEN, .1f, 0.75f, 0);

		contactListener = new BasicContactListener();
		this.addContactListener(contactListener);
	}

	@Override
	public void update(float deltaTime) {
		detectionStatus = contactListener.getEntities().size() > 0;
		if (detectionStatus != previousDetectionStatus)
			graphics = addGraphics(sensorArea, detectionStatus ? Color.RED : Color.GREEN,
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

package main.game.actor.sensors;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.actor.ParallelAction;
import main.game.actor.entities.GameEntity;
import main.math.BasicContactListener;
import main.math.Entity;
import main.math.Shape;
import main.math.Vector;

public class ProximitySensor extends GameEntity implements Sensor {
	private Shape sensorArea;

	private boolean detectionStatus, previousDetectionStatus;
	private BasicContactListener contactListener;
	private boolean sensorOccupied = false;
	private float timeToActionEnd = 0, elapsedActionTime = 0.f;

	public ProximitySensor(ActorGame game, Vector position, Shape shape) {
		super(game, true, position);
		this.sensorArea = shape;

		this.build(this.sensorArea, -1, -1, true);

        this.contactListener = new BasicContactListener();
		this.addContactListener(this.contactListener);
	}

	@Override
	public void update(float deltaTime) {
	    if(this.contactListener.getEntities().size() > 0) {
	        for(Entity entity : this.contactListener.getEntities())
	            if (entity.getCollisionGroup() == ObjectGroup.PLAYER.group || entity.getCollisionGroup() == ObjectGroup.WHEEL.group)
	                this.detectionStatus = true;
        }
        this.detectionStatus = this.contactListener.getEntities().size() > 0;
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
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}

	@Override
	public boolean getSensorDetectionStatus() {
		return this.detectionStatus;
	}

	@Override
	public void runAction(Runnable runnable, float time) {
        this.sensorOccupied = true;
        this.timeToActionEnd = time > this.timeToActionEnd ? time : this.timeToActionEnd;
		ParallelAction.generateWorker(runnable).execute();
	}

	@Override
	public boolean isOccupied() {
		return this.sensorOccupied;
	}

}

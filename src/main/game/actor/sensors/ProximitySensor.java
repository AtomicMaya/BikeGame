package main.game.actor.sensors;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.actor.ParallelAction;
import main.game.actor.entities.GameEntity;
import main.math.BasicContactListener;
import main.math.Entity;
import main.math.Shape;
import main.math.Vector;

import java.util.ArrayList;
import java.util.Arrays;

public class ProximitySensor extends GameEntity implements Sensor {
	public Shape sensorArea;

	private boolean detectionStatus, previousDetectionStatus;
	private BasicContactListener contactListener;
	private boolean sensorOccupied = false;
	private ArrayList<Integer> objectGroups;
	private float timeToActionEnd = 0, elapsedActionTime = 0.f;
	private Entity collidingEntity;

	public ProximitySensor(ActorGame game, Vector position, Shape shape, ArrayList<Integer> objectGroups) {
        super(game, true, position);
        this.sensorArea = shape;

        this.build(this.sensorArea, -1, -1, true, ObjectGroup.SENSOR.group);

        this.objectGroups = objectGroups;
        this.contactListener = new BasicContactListener();
        this.addContactListener(this.contactListener);
    }

    public ProximitySensor(ActorGame game, Vector position, Shape shape) {
	    this(game, position, shape, new ArrayList<>(Arrays.asList(ObjectGroup.PLAYER.group, ObjectGroup.WHEEL.group)));
    }

	@Override
	public void update(float deltaTime) {
	    this.detectionStatus = false;
	    if(this.contactListener.getEntities().size() > 0) {
	        for(Entity entity : this.contactListener.getEntities())
	            if (this.objectGroups.contains(entity.getCollisionGroup())) {
	                this.detectionStatus = true;
	                this.collidingEntity = entity;
	        }
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

    public Entity getCollidingEntity() {
        return this.collidingEntity;
    }
}

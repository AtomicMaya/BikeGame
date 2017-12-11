package main.game.actor.sensors;

import main.game.actor.Actor;

public interface Sensor extends Actor {
	
	boolean getSensorDetectionStatus();

	boolean isOccupied();

	void runAction(Runnable action, float time);
}

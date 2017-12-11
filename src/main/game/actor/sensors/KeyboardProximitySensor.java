package main.game.actor.sensors;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.actor.ParallelAction;
import main.game.actor.entities.GameEntity;
import main.math.BasicContactListener;
import main.math.Shape;
import main.math.Vector;
import main.window.Keyboard;

/** A special type of {@linkplain Sensor} that requires {@linkplain Keyboard} interaction. */
public class KeyboardProximitySensor extends GameEntity implements Sensor {
    /** Reference the {@linkplain Keyboard}. */
	private Keyboard keyboard;

	/** Reference the {@linkplain java.awt.event.KeyEvent} that is required for activation. */
	private int key;

	/** The {@linkplain Shape} of the sensor's trigger area. */
	private Shape sensorArea;

	/** The {@linkplain BasicContactListener} associated to this entity. */
	private BasicContactListener contactListener;

	/** The detection status of this {@linkplain Sensor}. */
	private boolean detectionStatus, previousDetectionStatus = false;

	/** Whether the required key has been pressed. */
	private boolean keyPressedStatus;

	/** Whether this {@linkplain Sensor} is running any actions. */
	private boolean sensorOccupied = false;

	/** The time until this {@linkplain Sensor} is inactive again. */
	private float timeToActionEnd, elapsedActionTime = 0.f;

    /**
     * Creates a new {@linkplain KeyboardProximitySensor}.
     * @param game The master {@linkplain ActorGame}.
     * @param position The initial position {@linkplain Vector}.
     * @param shape The {@linkplain Shape} that this {@linkplain Sensor} takes.
     * @param key The key that has to be pressed.
     */
	public KeyboardProximitySensor(ActorGame game, Vector position, Shape shape, int key) {
		super(game, true, position);
		this.keyboard = game.getKeyboard();
		this.key = key;

		this.sensorArea = shape;
		this.build(this.sensorArea, -1, -1, true, ObjectGroup.SENSOR.group);

		this.contactListener = new BasicContactListener();
		this.addContactListener(this.contactListener);
	}

	@Override
	public void update(float deltaTime) {
		this.detectionStatus = this.contactListener.getEntities().size() > 0;
		this.keyPressedStatus = this.keyboard.get(this.key).isDown();

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
		return this.detectionStatus && this.keyPressedStatus;
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

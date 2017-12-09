package main.game.actor.entities.switchers;

import main.game.ActorGame;
import main.game.actor.ParallelAction;
import main.game.actor.Runner;
import main.game.actor.entities.GameEntity;
import main.game.actor.sensors.KeyboardProximitySensor;
import main.game.audio.Audio;
import main.game.graphics.ImageGraphics;
import main.math.Polygon;
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class SimpleLever extends GameEntity implements Switcher, Runner {
	private boolean activated;
	private ActorGame game;
	private KeyboardProximitySensor sensor;

	private Shape shape;
	private ArrayList<ImageGraphics> graphics;
	private ArrayList<Runnable> actions;
	private ArrayList<Float> time;
	boolean isOccupied;
	private float timeToActionEnd, elapsedActionTime = 0.f;

	public SimpleLever(ActorGame game, Vector position) {
		super(game, true, position);
		this.game = game;
		this.activated = false;

		this.shape = new Polygon(0f, 0f, 1.5f, 0f, 1.5f, 1.5f, 0f, 1.5f);

		this.sensor = new KeyboardProximitySensor(this.game, position, this.shape, KeyEvent.VK_E);

		this.graphics = new ArrayList<>();
		this.graphics.add(this.addGraphics("./res/images/lever.red.right.png", 1.5f, 1.5f));
		this.graphics.add(this.addGraphics("./res/images/lever.red.left.png", 1.5f, 1.5f));

		this.actions = new ArrayList<>();
		this.time = new ArrayList<>();
		this.addAction(() -> this.activated = !this.activated, 1.f);
		this.addAction(() -> new Audio("./res/audio/lever_activated.wav", 0, 10f), 1.f);
	}

	@Override
	public void update(float deltaTime) {
		if (this.sensor.getSensorDetectionStatus() && !this.isOccupied) {
			for(int i = 0; i < this.actions.size(); i++) {
				this.runAction(this.actions.get(i), this.time.get(i));
			}
		}
		if (this.isOccupied) {
			this.elapsedActionTime += deltaTime;
			if (this.elapsedActionTime > this.timeToActionEnd) {
				this.isOccupied = false;
				this.elapsedActionTime = 0.f;
			}
		}
		this.sensor.update(deltaTime);
	}

	@Override
	public void destroy() {
		this.game.destroyActor(sensor);
		this.game.destroyActor(this);
	}

	@Override
	public void draw(Canvas canvas) {
		(activated ? graphics.get(1) : graphics.get(0)).draw(canvas);
	}

	@Override
	public void addAction(Runnable action, float expirationTime) {
		this.actions.add(action);
		this.time.add(expirationTime);
	}


    @Override
	public void runAction(Runnable action, float expirationTime) {
		this.isOccupied = true;
        this.timeToActionEnd = expirationTime > this.timeToActionEnd ? expirationTime : this.timeToActionEnd;
		ParallelAction.generateWorker(action).execute();
	}
}

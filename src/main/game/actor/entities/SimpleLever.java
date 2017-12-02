package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Audio;
import main.game.actor.ImageGraphics;
import main.math.Polygon;
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;

import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class SimpleLever extends GameEntity implements Lever {
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

		this.build(this.shape, -1, -1, true);

		this.sensor = new KeyboardProximitySensor(this.game, position, this.shape, KeyEvent.VK_E);

		this.graphics = new ArrayList<>();
		this.graphics.add(this.addGraphics("./res/images/lever.red.right.png", 1.5f, 1.5f));
		this.graphics.add(this.addGraphics("./res/images/lever.red.left.png", 1.5f, 1.5f));

		this.actions = new ArrayList<>();
		this.time = new ArrayList<>();
		addAction(() -> activated = !activated, 1.f);
		addAction(() -> new Audio("./res/audio/lever_activated.wav", 0, 10f), 2.f);

		this.game.addActor(sensor);
	}

	@Override
	public void update(float deltaTime) {
		if (this.sensor.getSensorDetectionStatus() && !isOccupied) {
			for(int i = 0; i < actions.size(); i++) {
				this.runAction(this.actions.get(i), this.time.get(i));
			}
		}
		if (isOccupied) {
			elapsedActionTime += deltaTime;
			if (elapsedActionTime > timeToActionEnd) {
				isOccupied = false;
				elapsedActionTime = 0.f;
			}
		}

		sensor.update(deltaTime);
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

	private void addAction(Runnable action, float expirationTime) {
		this.actions.add(action);
		this.time.add(expirationTime);
	}

	public void addAction(Runnable action) {
		addAction(action, 0f);
	}

	public void runAction(Runnable runnable, float time) {
		isOccupied = true;
		timeToActionEnd = time;
		Runner.generateWorker(runnable).execute();
	}
}

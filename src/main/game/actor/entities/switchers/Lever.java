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

public class Lever extends GameEntity implements Runner {
    /** The master {@linkplain ActorGame}.*/
	private ActorGame game;

    /** The linked {@linkplain KeyboardProximitySensor}.*/
	private KeyboardProximitySensor sensor;

    /** The {@linkplain Shape} of this {@linkplain Lever}.*/
	private Shape shape;

    /** The {@linkplain ArrayList} containing all {@linkplain ImageGraphics} of this {@linkplain Lever}.*/
	private ArrayList<ImageGraphics> graphics;

    /** The {@linkplain ArrayList} containing all {@linkplain Runnable}'s of this {@linkplain Lever}.*/
	private ArrayList<Runnable> actions;

    /** The {@linkplain ArrayList} containing all {@linkplain Float} representing time of this {@linkplain Lever}.*/
	private ArrayList<Float> time;

    /** Whether this {@linkplain Lever} is activated or not. Default {@value}. */
    private boolean activated;

    /** Whether this {@linkplain Runner} is occupied or not. Default {@value}. */
	boolean isOccupied;

	/** Animation parameters. */
	private float timeToActionEnd, elapsedActionTime = 0.f;

    /**
     * Creates a new {@linkplain Lever}.
     * @param game The master {@linkplain ActorGame}.
     * @param position The position {@linkplain Vector} associated to this {@linkplain Lever}.
     */
	public Lever(ActorGame game, Vector position) {
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
		this.game.destroyActor(this.sensor);
		this.game.destroyActor(this);
	}

	@Override
	public void draw(Canvas canvas) {
		(this.activated ? this.graphics.get(1) : this.graphics.get(0)).draw(canvas);
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

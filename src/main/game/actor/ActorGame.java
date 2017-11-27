/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor;

import main.game.Game;
import main.io.FileSystem;
import main.math.*;
import main.window.Canvas;
import main.window.Keyboard;
import main.window.Window;

import java.util.ArrayList;

public class ActorGame implements Game {

	// Viewport properties
	private Vector viewCenter;
	private Vector viewTarget;
	private Positionable viewCandidate;
	private static final float VIEW_TARGET_VELOCITY_COMPENSATION = 0.2f;

	private static final float VIEW_INTERPOLATION_RATIO_PER_SECOND = 0.1f;
	private static final float VIEW_SCALE = 10.0f;

	private ArrayList<Actor> actors = new ArrayList<>();

	private World world;

	private Window window;

	private FileSystem fileSystem;

	private boolean gameFrozen = false;

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {
		if (window == null)
			throw new NullPointerException("Window is null");
		if (fileSystem == null)
			throw new NullPointerException("FileSystem is null");
		world = new World();
		world.setGravity(new Vector(0, -9.81f));
		this.window = window;
		this.fileSystem = fileSystem;
		this.viewCenter = Vector.ZERO;
		this.viewTarget = Vector.ZERO;

		return true;
	}

	@Override
	public void update(float deltaTime) {
		if (gameFrozen)
			return;
		world.update(deltaTime);

		for (Actor actor : actors) {
			actor.update(deltaTime);
		}

		// Update expected viewport center
		if (viewCandidate != null) {
			viewTarget = viewCandidate.getPosition()
					.add(viewCandidate.getVelocity().mul(VIEW_TARGET_VELOCITY_COMPENSATION));
		}
		// Interpolate with previous location
		float ratio = (float) Math.pow(VIEW_INTERPOLATION_RATIO_PER_SECOND, deltaTime);
		viewCenter = viewCenter.mixed(viewTarget, ratio);
		// Compute new viewport
		Transform viewTransform = Transform.I.scaled(VIEW_SCALE).translated(viewCenter);
		window.setRelativeTransform(viewTransform);

		for (Actor actor : actors) {
			actor.draw(window);
		}

	}

	@Override
	public void end() {

	}

	public Keyboard getKeyboard() {
		return window.getKeyboard();
	}

	public Canvas getCanvas() {
		return window;
	}

	public void setViewCandidate(Positionable p) {
		viewCandidate = p;
	}

	public void addActor(Actor actor) {
		actors.add(actor);
	}

	public void destroyActor(Actor actor) {
		this.actors.remove(actor);
	}

	public Entity newEntity(Vector position, boolean fixed) {
		EntityBuilder entityBuilder = world.createEntityBuilder();
		entityBuilder.setFixed(fixed);
		entityBuilder.setPosition(position);
		return entityBuilder.build();
	}

	public Entity newEntity(boolean fixed) {
		EntityBuilder entityBuilder = world.createEntityBuilder();
		entityBuilder.setFixed(fixed);
		return entityBuilder.build();
	}

	public WheelConstraintBuilder createWheelConstraintBuilder() {
		return world.createWheelConstraintBuilder();
	}

	public PrismaticConstraintBuilder createPrismaticConstraintBuilder() { return world.createPrismaticConstraintBuilder(); }

	public boolean isGameFrozen(){
		return gameFrozen;
	}

	public void setGameFreezeStatus(boolean freeze){
		gameFrozen = freeze;
	}
}

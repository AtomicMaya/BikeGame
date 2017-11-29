/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor;

import main.game.Game;
import main.io.FileSystem;
import main.math.*;
import main.window.Keyboard;
import main.window.Window;

import java.util.ArrayList;
import java.util.List;

public class ActorGame implements Game {

	// Viewport properties
	private Vector viewCenter;
	private Vector viewTarget;
	private Positionable viewCandidate;
	private static final float VIEW_TARGET_VELOCITY_COMPENSATION = 0.2f;

	private static final float VIEW_INTERPOLATION_RATIO_PER_SECOND = 0.1f;
	private static final float VIEW_SCALE = 10.0f;

	// list of all actors in the game
	private ArrayList<Actor> actors = new ArrayList<Actor>();

	// our physical world
	private World world;

	// the parameters of the game
	private Window window;
	private FileSystem fileSystem;

	// Weather the game is frozen
	private boolean gameFrozen = false;

	// list to add or remove actors
	private ArrayList<Actor> actorsToRemove = new ArrayList<Actor>(), actorsToAdd = new ArrayList<Actor>();

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

		if (!actorsToRemove.isEmpty()) {
			actors.removeAll(actorsToRemove);
			actorsToRemove.clear();
		}
		if (!actorsToAdd.isEmpty()) {
			actors.addAll(actorsToAdd);
			actorsToAdd.clear();
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
		actors.clear();
	}

	/**
	 * @return associated keyboard controller
	 */
	public Keyboard getKeyboard() {
		return window.getKeyboard();
	}

	/**
	 * @return associated canvas
	 */
	// public Canvas getCanvas() {
	// return window;
	// }

	/**
	 * @param p
	 *            Positionable to follow with the camera
	 */
	public void setViewCandidate(Positionable p) {
		this.viewCandidate = p;
	}

	/**
	 * @param actor
	 *            to add the the game
	 */
	public void addActor(Actor actor) {
		actorsToAdd.add(actor);
	}

	/**
	 * @param actors
	 *            list of actors to add the the game
	 */
	public void addActor(List<Actor> actors) {
		actorsToAdd.addAll(actors);
	}

	/**
	 * @param actor
	 *            to remove from the game
	 */
	public void destroyActor(Actor actor) {
		actorsToRemove.add(actor);
	}

	/**
	 * @param actors
	 *            List of actors to remove from the game
	 */
	public void destroyActor(ArrayList<Actor> actors) {
		actorsToRemove.addAll(actors);
	}

	/**
	 * Create a new Entity in the world
	 *
	 * @param position
	 *            to give to the Entity
	 * @param fixed
	 *            weather the Entity can move or not
	 * @return a new Entity
	 */
	public Entity newEntity(Vector position, boolean fixed) {
		EntityBuilder entityBuilder = world.createEntityBuilder();
		entityBuilder.setFixed(fixed);
		entityBuilder.setPosition(position);
		return entityBuilder.build();
	}

	/**
	 * Create a new Entity in the world
	 *
	 * @param fixed
	 *            weather the Entity can move or not
	 * @return a new Entity
	 */
	public Entity newEntity(boolean fixed) {
		EntityBuilder entityBuilder = world.createEntityBuilder();
		entityBuilder.setFixed(fixed);
		return entityBuilder.build();
	}

	/**
	 * @return a new WheelConstraintBuilder
	 */
	public WheelConstraintBuilder createWheelConstraintBuilder() {
		return world.createWheelConstraintBuilder();
	}

	/**
	 * @return a new PrismaticConstraintBuilder
	 */
	public PrismaticConstraintBuilder createPrismaticConstraintBuilder() {
		return world.createPrismaticConstraintBuilder();
	}

	/**
	 * @return a new DistanceConstraintBuilder
	 */
	public DistanceConstraintBuilder createDistanceContraintBuilder() {
		return world.createDistanceConstraintBuilder();
	}

	public PointConstraintBuilder createPointConstraintBuilder() { return world.createPointConstraintBuilder(); }

	/**
	 * @return wether the game is frozen
	 */
	public boolean isGameFrozen() {
		return gameFrozen;
	}

	/**
	 * Set the frozen status of the game
	 * @param freeze : wether we want to freeze the game
	 */
	public void setGameFreezeStatus(boolean freeze) {
		gameFrozen = freeze;

	}

	protected void setGravity(Vector vector) {
		world.setGravity(vector);
	}
}

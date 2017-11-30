package main.game;

import main.game.actor.Actor;
import main.io.FileSystem;
import main.math.*;
import main.window.Keyboard;
import main.window.Window;

import java.util.ArrayList;
import java.util.Iterator;
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
	private ArrayList<Actor> actors = new ArrayList<>();

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
			for (int i = 0; i< actorsToRemove.size();i++) {
				actorsToRemove.get(i).destroy();
			}
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
		viewCenter = viewCenter.mixed(viewTarget, 1.f - ratio);
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
	 * @param p : the object (Positionable) to follow with the camera
	 */
	public void setViewCandidate(Positionable p) {
		this.viewCandidate = p;
	}

	/**
	 * @param actor : an actor to be added in the game
	 */
	public void addActor(Actor actor) {
		if (!actors.contains(actor))
			actorsToAdd.add(actor);
	}

	/**
	 * @param actors : a list of actors to be added to the game
	 */
	public void addActor(List<Actor> actors) {
		for (Actor a : actors) {
			if (!this.actors.contains(a))
				actorsToAdd.add(a);
		}
		
	}

	/**
	 * @param actor : an actor to be removed from the game
	 */
	public void destroyActor(Actor actor) {
		if (!actorsToRemove.contains(actor))
			actorsToRemove.add(actor);
	}

	/**
	 * @param actors : a list of actors to be removed from the game
	 */
	public void destroyActor(ArrayList<Actor> actors) {
		for (Actor a: actors) {
			if (!actorsToRemove.contains(a))
				actorsToRemove.add(a);
		}
		
	}
	
	/**
	 * @param actors : a list of actor to keep in the game
	 * */
	public void destroyAllActorsExept(Actor actorToKeep) {
		for (Actor actor : actors) {
			// use != because we test the reference
			if (actor != actorToKeep) {
				actorsToRemove.add(actor);
			}
		}
	}
	
	/**
	 * @param actors : a list of actor to keep in the game
	 * */
	public void destroyAllActorsExept(ArrayList<Actor> actorsToKeep) {
		for (Actor actor : actors) {
			if (!actorsToKeep.contains(actor)) {
				actorsToRemove.add(actor);
			}
		}
	}

	/**
	 * Create a new Entity in the world
	 * @param position : position given to the entity
	 * @param fixed : whether the entity can move or not
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
	 * @param fixed : whether the Entity can move or not
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

	/**
	 * @return a new PointConstraintBuilder
	 */
	public PointConstraintBuilder createPointConstraintBuilder() { return world.createPointConstraintBuilder(); }

	/**
	 * @return whether the game is frozen
	 */
	public boolean isGameFrozen() {
		return gameFrozen;
	}

	/**
	 * Set the frozen status of the game
	 * @param freeze : whether or not we want to freeze the game
	 */
	public void setGameFreezeStatus(boolean freeze) {
		gameFrozen = freeze;

	}

	/**
	 * Modify the value of the world's gravity
	 * @param vector : the new gravity values.
	 */
	protected void setGravity(Vector vector) {
		world.setGravity(vector);
	}
}
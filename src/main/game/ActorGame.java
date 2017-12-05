package main.game;

import main.game.actor.Actor;
import main.io.FileSystem;
import main.io.Save;
import main.math.*;
import main.window.Keyboard;
import main.window.Mouse;
import main.window.Window;

import java.io.File;
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
	private static float VIEW_SCALE_MOD = 0.0f;
	private static float VIEW_SCALE_CURRENT = VIEW_SCALE;
	private static float VIEW_SCALE_PREVIOUS = VIEW_SCALE;
	private static final float TRANSLATION_TIME = 3f;

	// list of all actors in the game
	private ArrayList<Actor> actors = new ArrayList<>();

	// main character of the game
	private Actor player;

	// our physical world
	private World world;

	// the parameters of the game
	private Window window;
	private FileSystem fileSystem;

	// Weather the game is frozen
	private boolean gameFrozen = false;

	// list to add or remove actors
	private ArrayList<Actor> actorsToRemove = new ArrayList<>(), actorsToAdd = new ArrayList<>();

	/**
	 * SaveDirectory path
	 */
	private static final String saveDirectory = "saves/";

	public String getSaveDirectory() {
		return saveDirectory;
	}

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
		if (gameFrozen) return;

		world.update(deltaTime);

		for (Actor actor : actors) {
			actor.update(deltaTime);
		}

		if (!actorsToRemove.isEmpty()) {
		    // peut etre plus propre mais ca fait des ConcurrentModificationException
			for (int i = 0; i < actorsToRemove.size(); i++) {
				actorsToRemove.get(i).destroy();
			}
			actors.removeAll(actorsToRemove);
			actorsToRemove.clear();
		}
		if (!actorsToAdd.isEmpty()) {
			actors.addAll(actorsToAdd);
			actorsToAdd.clear();
		}

		if (VIEW_SCALE_CURRENT > VIEW_SCALE + VIEW_SCALE_MOD && !(VIEW_SCALE + VIEW_SCALE_MOD - 0.1f < VIEW_SCALE_CURRENT && VIEW_SCALE_CURRENT < VIEW_SCALE + VIEW_SCALE_MOD + 0.1f)) VIEW_SCALE_CURRENT -= VIEW_SCALE_MOD * deltaTime / TRANSLATION_TIME;
		else if (VIEW_SCALE_CURRENT < VIEW_SCALE + VIEW_SCALE_MOD && !(VIEW_SCALE + VIEW_SCALE_MOD - 0.1f < VIEW_SCALE_CURRENT && VIEW_SCALE_CURRENT < VIEW_SCALE + VIEW_SCALE_MOD + 0.1f)) VIEW_SCALE_CURRENT += VIEW_SCALE_MOD * deltaTime / TRANSLATION_TIME;

		// Update expected viewport center
		if (viewCandidate != null) {
			viewTarget = viewCandidate.getPosition()
					.add(viewCandidate.getVelocity().mul(VIEW_TARGET_VELOCITY_COMPENSATION));
		}
		// Interpolate with previous location
		float ratio = (float) Math.pow(VIEW_INTERPOLATION_RATIO_PER_SECOND, deltaTime);
		viewCenter = viewCenter.mixed(viewTarget, 1.f - ratio);
		// Compute new viewport
		Transform viewTransform = Transform.I.scaled(VIEW_SCALE_CURRENT).translated(viewCenter);
		window.setRelativeTransform(viewTransform);

		for (Actor actor : actors) {
			actor.draw(window);
		}
		VIEW_SCALE_PREVIOUS = VIEW_SCALE_CURRENT;

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

	public Mouse getMouse() {
		return window.getMouse();
	}

    public Vector getGravity() {
        return world.getGravity();
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
		for (Actor a : actors) {
			if (!actorsToRemove.contains(a))
				actorsToRemove.add(a);
		}
	}

	/**
	 * Destroy all actors stored
	 */
	public void destroyAllActors() {
		actorsToRemove.addAll(actors);
	}

	/**
	 * @param actorToKeep : a list of actor to keep in the game
	 */
	public void destroyAllActorsExept(Actor actorToKeep) {
		for (Actor actor : actors) {
			// use != because we test the reference
			if (actor != actorToKeep) {
				actorsToRemove.add(actor);
			}
		}
	}
	
	/**
	 * @param actorsToKeep : a list of actor to keep in the game
	 */
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
	public PointConstraintBuilder createPointConstraintBuilder() {
		return world.createPointConstraintBuilder();
	}

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

	/**
	 * Get the main actor of the game
	 *
	 * @return the main actor of the game
	 */
	public Actor getPayload() {
		return player;
	}

	/***
	 * Set the main actor of the game
	 *
	 * @param player actor which will be the main actor of the game
	 */
	public void setPayload(Actor player) {
		this.player = player;
	}

	/**
	 * Save all actors of the current game
	 * @param saveName path to the folder to save the game
	 */
	public void save(String saveName) {

		// if the save folder does not exist, create it
		File folder = new File(saveDirectory + saveName);
		folder.mkdirs();

		for (int i = 0; i < actors.size(); i++) {
			if (viewCandidate == actors.get(i)) {
				Save.saveParameters(i, fileSystem, new File(folder.getPath() + "/params.param"));
				break;
			}
		}
		int i = 0;
		for (Actor a : actors) {
			// for each actors, save it in a new files
			File file = new File(folder.getPath() + "/actor" + i + ".object");

			Save.saveActor(a, file);

			i++;
		}
	}

	/**
	 * Load all saved actors
	 *
	 * @param saveName name of the save to load
	 */
	public void load(String saveName) {

		File save = new File(saveDirectory + saveName);
		if (save.exists()) {
			File[] files = save.listFiles();
			for (File f : files) {
				if (f.getPath().contains(".object")) {
					Actor a = Save.readSavedActor(this, f);
					if (a != null)
						actorsToAdd.add(a);
				}

			}
			setViewCandidate(
					actors.get(Save.viewCandidateNumberInFile(fileSystem, new File(save.getPath() + "/params.param"))));
		}
	}

	public void setViewScaleModifier(float newModifier) {
	    VIEW_SCALE_MOD = newModifier;
    }

    public float getViewScale() {
        return VIEW_SCALE_CURRENT;
    }

    protected void setViewScale(float newViewScale) {
        System.out.println(VIEW_SCALE + ", " + VIEW_SCALE_CURRENT + ", " + VIEW_SCALE_MOD);
	    VIEW_SCALE_CURRENT = newViewScale;
	    VIEW_SCALE_MOD = (VIEW_SCALE_CURRENT > VIEW_SCALE + VIEW_SCALE_MOD ? VIEW_SCALE_CURRENT - VIEW_SCALE : VIEW_SCALE_CURRENT - VIEW_SCALE);
	    System.out.println(VIEW_SCALE + ", " + VIEW_SCALE_CURRENT + ", " + VIEW_SCALE_MOD);
    }
}
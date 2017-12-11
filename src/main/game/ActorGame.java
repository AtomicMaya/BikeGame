package main.game;

import main.game.actor.Actor;
import main.game.actor.Camera;
import main.game.actor.GameManager;
import main.game.actor.entities.GameEntity;
import main.game.actor.entities.ParticleEmitter;
import main.game.actor.entities.PlayableEntity;
import main.game.graphicalStuff.EndGameGraphics;
import main.io.FileSystem;
import main.io.Save;
import main.math.*;
import main.window.Canvas;
import main.window.Keyboard;
import main.window.Mouse;
import main.window.Window;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.List;

/** Represent a {@linkplain Game}, with its {@linkplain Actor}s */
public class ActorGame implements Game {
    /** The Viewport properties. */
	private Camera camera;

	 /** A {@linkplain ArrayList<Actor>} containing all {@linkplain Actor}.*/
     private LinkedList<Actor> actors = new LinkedList<Actor>();

    // main character of the game
    /** The main {@linkplain Actor} of this {@linkplain Game} */
	private PlayableEntity player;

	/** The {@linkplain World} created with the game */
	private World world;

	/** The {@linkplain Window} where to draw the {@linkplain main.game.graphics.Graphics}. */
	private Window window;

	/** The global {@linkplain FileSystem}.*/
	private FileSystem fileSystem;

    /** Whether the game is frozen. */
	private boolean gameFrozen = false;

	/** {@linkplain ArrayList<Actor>} containing {@linkplain Actor}s to add and to remove from the {@linkplain Game}.*/
	private ArrayList<Actor> actorsToRemove = new ArrayList<>(), actorsToAdd = new ArrayList<>();

	// actor to manage checkpoint, reload, ...
	private GameManager gameManager;

	/** The given save directory : {@value}*/
	private static final String saveDirectory = "saves/";

	/** The player's score. */
	private int score;

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {
		if (window == null)
			throw new NullPointerException("Window is null");
		if (fileSystem == null)
			throw new NullPointerException("FileSystem is null");

		
		this.world = new World();
		this.world.setGravity(new Vector(0, -9.81f));

		this.window = window;
		this.fileSystem = fileSystem;

		this.score = 0;
		this.camera = new Camera(this, window);
		this.gameManager = new GameManager(this);
		return true;
	}

	@Override
	public void update(float deltaTime) {
		gameManager.update(deltaTime);
		if (this.getKeyboard().get(KeyEvent.VK_0).isPressed())
			System.out.println("actors size : "+actors.size() + " world entities : " + world.getEntities().size());

		if (!this.actorsToRemove.isEmpty()) {
			for (int i = 0; i < this.actorsToRemove.size(); i++) {
				this.actorsToRemove.get(i).destroy();
				if (actorsToRemove.get(i).getClass() != ParticleEmitter.class)
					System.out.println("removed : " +actorsToRemove.get(i));
			}
			this.actors.removeAll(this.actorsToRemove);
			this.actorsToRemove.clear();
		}

		if (!this.actorsToAdd.isEmpty()) {
			this.actors.addAll(this.actorsToAdd);
			this.actorsToAdd.clear();
		}

		if (this.gameFrozen) {
			return;
		}

		this.world.update(deltaTime);
		this.camera.update(deltaTime);

		for (int i = this.actors.size() - 1; i >= 0; i--) {
			try {
				this.actors.get(i).update(deltaTime);
			} catch (ConcurrentModificationException e) { e.printStackTrace(); }
		}
		this.gameManager.draw(this.window);
		for (Actor actor : this.actors)
			actor.draw(this.window);
	}

	@Override
	public void end() {
		this.actors.clear();
	}

	/** @return the associated {@linkplain Keyboard} controller. */
	public Keyboard getKeyboard() {
		return this.window.getKeyboard();
	}

	/** @return the associated {@linkplain Canvas}. */
	public Canvas getCanvas() {
		return this.window;
	}

	/** @return the associated {@linkplain Mouse} controller. */
	public Mouse getMouse() {
		return this.window.getMouse();
	}

	/** @return the game's main gravity value. */
	public Vector getGravity() {
		return this.world.getGravity();
	}

	/** @return the {@linkplain #saveDirectory} : {@value #saveDirectory} */
	public String getSaveDirectory() {
		return saveDirectory;
	}

	/**
     * Adds an {@linkplain Actor} to the game.
	 * @param actor An {@linkplain Actor} to be added in the game.
	 */
	public void addActor(Actor actor) {
		if (actor != null && !this.actors.contains(actor) && !this.actorsToAdd.contains(actor))
			this.actorsToAdd.add(actor);
	}

	/**
     * Adds several {@linkplain Actor}s to the game.
	 * @param actors A list of {@linkplain Actor}s to be added to the game.
	 */
	public void addActor(List<Actor> actors) {
		for (Actor a : actors) {
			if (a != null && !this.actors.contains(a) && !this.actorsToAdd.contains(a))
				this.actorsToAdd.add(a);
		}
	}

	/**
	 * @param actor An {@linkplain Actor} to be removed from the game.
	 */
	public void destroyActor(Actor actor) {
		if (!this.actorsToRemove.contains(actor) && this.actors.contains(actor))
			this.actorsToRemove.add(actor);
	}

	/**
	 * @param actors A list of {@linkplain Actor}s to be removed from the game.
	 */
	public void destroyActor(List<Actor> actors) {
		for (Actor a : actors) {
			if (!this.actorsToRemove.contains(a) && this.actors.contains(a))
				this.actorsToRemove.add(a);
		}
	}

	/**
	 * Destroy all stored {@linkplain Actor}s.
	 */
	public void destroyAllActors() {
		// destroy all entities in the world!!! na!
		// TODO remove attomatic de actors		
		while (world.getEntities().size() > 0)
			world.getEntities().get(0).destroy();
		this.destroyActor(actors);
	}

	/**
	 * @param actorToKeep An {@linkplain Actor} to keep in the game.
	 */
	public void destroyAllActorsExcept(Actor actorToKeep) {
		for (Actor actor : this.actors) {
			// use != because we test the reference
			if (actor != actorToKeep) {
				this.actorsToRemove.add(actor);
			}
		}
	}
//
//	/**
//	 * @param actorsToKeep A list of {@linkplain Actor}s to keep in the game.
//	 */
//	public void destroyAllActorsExcept(ArrayList<Actor> actorsToKeep) {
//		for (Actor actor : this.actors) {
//			if (!actorsToKeep.contains(actor)) {
//				this.actorsToRemove.add(actor);
//			}
//		}
//	}

	/**
	 * Create a new {@linkplain Entity} in the world.
	 * @param position A position {@linkplain Vector} given to the {@linkplain Entity}.
	 * @param fixed Whether the {@linkplain Entity} can move or not.
	 * @return a new {@linkplain Entity}.
	 */
	public Entity newEntity(Vector position, boolean fixed) {
		EntityBuilder entityBuilder = this.world.createEntityBuilder();
		entityBuilder.setFixed(fixed);
		entityBuilder.setPosition(position);
		return entityBuilder.build();
	}

	/**
	 * Create a new {@linkplain Entity} in the world.
	 * @param fixed Whether the {@linkplain Entity} can move or not.
	 * @return a new {@linkplain Entity}.
	 */
	public Entity newEntity(boolean fixed) {
		EntityBuilder entityBuilder = this.world.createEntityBuilder();
		entityBuilder.setFixed(fixed);
		return entityBuilder.build();
	}

	/**
	 * @return a new {@linkplain WheelConstraintBuilder}.
	 */
	public WheelConstraintBuilder createWheelConstraintBuilder() {
		return this.world.createWheelConstraintBuilder();
	}

	/**
	 * @return a new {@linkplain PrismaticConstraintBuilder}.
	 */
	public PrismaticConstraintBuilder createPrismaticConstraintBuilder() {
		return this.world.createPrismaticConstraintBuilder();
	}

	/**
	 * @return a new {@linkplain DistanceConstraintBuilder}.
	 */
	public DistanceConstraintBuilder createDistanceContraintBuilder() {
		return this.world.createDistanceConstraintBuilder();
	}

	/**
	 * @return a new {@linkplain PointConstraintBuilder}.
	 */
	public PointConstraintBuilder createPointConstraintBuilder() {
		return this.world.createPointConstraintBuilder();
	}

	/** @return a new {@linkplain WeldConstraintBuilder}.*/
	public WeldConstraintBuilder createWeldConstraintBuilder() {
		return this.world.createWeldConstraintBuilder();
	}

	/** @return a new {@linkplain RopeConstraintBuilder} */
	public RopeConstraintBuilder createRopeConstraintBuilder() {
		return this.world.createRopeConstraintBuilder();
	}

	/**
	 * Computes a list of entities that intersect a segment.
	 * @param start the origin of the segment
	 * @param end the end point
	 * @return start the origin of the segmentend the end point
	 * @see {@linkplain World}
	 */
	public List<Impact> getImpacts(Vector start, Vector end) {
		return world.trace(start, end);
	}

	/**
	 * @return whether the game is frozen.
	 */
	public boolean isGameFrozen() {
		return this.gameFrozen;
	}

	/**
	 * Set the frozen status of the game.
	 * @param freeze : whether or not we want to freeze the game.
	 */
	public void setGameFreezeStatus(boolean freeze) {
		this.gameFrozen = freeze;
	}

	/**
	 * Get the main {@linkplain Actor}, a {@linkplain PlayableEntity} of the
	 * game.
	 * @return the main actor of the game.
	 */
	public PlayableEntity getPayload() {
		return this.player;
	}

	/***
	 * Set the {@linkplain PlayableEntity} of the game.
	 * @param player : The {@linkplain PlayableEntity} which will be the main
	 * {@linkplain Actor} of the game.
	 */
	public void setPayload(PlayableEntity player) {
		this.addActor(player);
		this.player = player;
	}

	/**
	 * Save all {@linkplain Actor}s of the current game.
	 * @param saveName : The path to the folder to save the game.
	 */
	public void save(ArrayList<Actor> actorsToSave, String saveName) {

		// if the save folder does not exist, create it
		File folder = new File(saveDirectory + saveName);
		Save.deleteDirectory(folder);
		folder.mkdirs();

		int n = 0;
		for (Actor a : actorsToSave) {
			File file = new File(folder.getPath() + "/actor" + n + ".object");
			if (Save.saveActor(a, file))
			n++;
		}
//		Save.saveParameters(viewCandidateNumber, playerNumber, this.fileSystem,
//				new File(folder.getPath() + "/params.param"));

		System.out.println("saved sucesfully " + (folder.listFiles().length - 1) + " actors");
	}

	/**
	 * Load all saved {@linkplain Actor}s.
	 * @param saveName : The name of the save to load.
	 */
	public boolean load(String saveName) {

		ArrayList<Actor> toAdd = new ArrayList<>();
		synchronized (toAdd) {

			System.out.println("    - start loading");
			File save = new File(saveDirectory + saveName);
//			try {
//				System.out.println("start wait");
//				toAdd.wait(100);
//				System.out.println("endwait");
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			if (save.exists()) {
				File[] files = save.listFiles();
				for (File f : files) {
					if (f.getPath().contains(".object")) {
						Actor actor = Save.readSavedActor(this, f);
						if (actor != null)
							toAdd.add(actor);
					}
				}

//				int[] params = Save.getParams(fileSystem, new File(save.getPath() + "/params.param"));

//				System.out.println("   - loading params");

//				this.setViewCandidate(toAdd.get(params[0]));
//				this.setPayload((PlayableEntity) toAdd.get(params[1]));
				toAdd.add(new EndGameGraphics(this));
				this.actorsToAdd.addAll(toAdd);
				System.out.println(toAdd.size() + " actors loaded");
				toAdd.clear();
				return true;
			}

		}
		System.out.println("Unexistant save");
		return false;
	}

	/**
	 * Sets the game's view candidate, to be followed by the camera.
	 * @param positionable : the {@linkplain GameEntity} / {@linkplain Actor} to
	 * be followed.
	 */
	public void setViewCandidate(Positionable positionable) {
		this.camera.setViewCandidate(positionable);
	}

	/**
	 * Sets a modifier to the view scale for smooth transition.
	 * @param newModifier : The modifier.
	 */
	public void setViewScaleModifier(float newModifier) {
		this.camera.setViewScaleModifier(newModifier);
	}

	/**
	 * @return the current view scale.
	 */
	public float getViewScale() {
		return camera.getViewScale();
	}

	/**
	 * Directly set a new view scale, with no transition.
	 * @param newViewScale : the new value.
	 */
	protected void setViewScale(float newViewScale) {
		camera.setViewScale(newViewScale);
	}

	/**
	 * @return the {@linkplain Window} relative transform
	 * @see Attachable
	 */
	public Transform getRelativeTransform() {
		return this.window.getRelativeTransform();
	}

	public GameManager getGameManager() {
		return gameManager;
	}

}
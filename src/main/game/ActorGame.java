package main.game;

import main.game.actor.Actor;
import main.game.actor.Camera;
import main.game.actor.GameManager;
import main.game.actor.entities.GameEntity;
import main.game.actor.entities.PlayableEntity;
import main.game.graphicalActors.EndGameGraphics;
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
import java.util.List;

/** Represent a {@linkplain Game}, with its {@linkplain Actor}s */
public class ActorGame implements Game {
	/** The Viewport properties. */
	private Camera camera;

	/** An {@linkplain ArrayList} containing all {@linkplain Actor}. */
	private ArrayList<Actor> actors = new ArrayList<Actor>();

	// main character of the game
	/** The main {@linkplain Actor} of this {@linkplain Game} */
	private PlayableEntity player;

	/** The {@linkplain World} created with the game */
	private World world;

	/**
	 * The {@linkplain Window} where to draw the
	 * {@linkplain main.game.graphics.Graphics}.
	 */
	private Window window;

	/** The global {@linkplain FileSystem}. */
	private FileSystem fileSystem;

	/** Whether the game is frozen. */
	private boolean gameFrozen = false;

	/**
	 * {@linkplain ArrayList<Actor>} containing {@linkplain Actor}s to add and
	 * to remove from the {@linkplain Game}.
	 */
	private ArrayList<Actor> actorsToRemove = new ArrayList<>(), actorsToAdd = new ArrayList<>();

	// actor to manage checkpoint, reload, ...
	private GameManager gameManager;

	/** The given save directory : {@value} */
	private static final String saveDirectory = "saves/";

	/** Whether the cheat mod is activated */
	private boolean cheatModActivated = false;

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

		this.camera = new Camera(this, window);
		this.gameManager = new GameManager(this);
		return true;
	}

	@Override
	public void update(float deltaTime) {

		this.gameManager.update(deltaTime);

		if (this.getKeyboard().get(KeyEvent.VK_CONTROL).isDown() && this.getKeyboard().get(KeyEvent.VK_SHIFT).isDown()
				&& this.getKeyboard().get(KeyEvent.VK_C).isPressed())
			cheatModActivated = !cheatModActivated;

		if (this.cheatModActivated) {
			if (this.getKeyboard().get(KeyEvent.VK_0).isPressed())
				System.out
						.println("actors size : " + actors.size() + " world entities : " + world.getEntities().size());

			if (this.getKeyboard().get(KeyEvent.VK_7).isPressed())
				System.out.println("frozen game :  " + isGameFrozen());
		}
		if (!this.actorsToRemove.isEmpty()) {
			for (int i = 0; i < this.actorsToRemove.size(); i++) {
				this.actorsToRemove.get(i).destroy();
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

		// TODO remove
		if (this.getKeyboard().get(KeyEvent.VK_6).isDown()) {
			for (int i = 0; i < 150; i++)
				this.world.update(deltaTime);
		}
		this.world.update(deltaTime);
		this.camera.update(deltaTime);

		// TODO remove
		if (this.getKeyboard().get(KeyEvent.VK_4).isDown()) {
			window.setRelativeTransform(Transform.I.scaled(40));
		}
		for (int i = this.actors.size() - 1; i >= 0; i--) {
			try {
				this.actors.get(i).update(deltaTime);
			} catch (ConcurrentModificationException e) {
				e.printStackTrace();
			}
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
		// destroy all entities in the world
		while (world.getEntities().size() > 0)
			world.getEntities().get(0).destroy();
		this.destroyActor(actors);
		this.setPayload(null);
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
	// /**
	// * @param actorsToKeep A list of {@linkplain Actor}s to keep in the game.
	// */
	// public void destroyAllActorsExcept(ArrayList<Actor> actorsToKeep) {
	// for (Actor actor : this.actors) {
	// if (!actorsToKeep.contains(actor)) {
	// this.actorsToRemove.add(actor);
	// }
	// }
	// }

	/**
	 * Create a new {@linkplain Entity} in the world.
	 * @param position A position {@linkplain Vector} given to the
	 * {@linkplain Entity}.
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

	/** @return a new {@linkplain WeldConstraintBuilder}. */
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
	 * @see World
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
	 * Load all saved {@linkplain Actor}s.
	 * @param saveName : The name of the save to load.
	 * @return whether the loading process succeeded.
	 */
	public boolean load(String saveName) {

		ArrayList<Actor> toAdd = new ArrayList<>();
		synchronized (toAdd) {

			System.out.println("    - start loading");
			File save = new File(saveDirectory + saveName);
			if (save.exists()) {
				File[] files = save.listFiles();
				for (File f : files) {
					if (f.getPath().contains(".object")) {
						Actor actor = Save.readSavedActor(this, f);
						if (actor != null)
							toAdd.add(actor);
					}
				}
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

	// public boolean saveCurrentActors() {
	//
	// Save.saveParameters(getGameManager().getLastCheckpoint(), new
	// File(saveDirectory + "temp/params.object"));
	// String s =
	// Save.saveCurrent(fileSystem, "", new File(saveDirectory +
	// "temp/params.param"));
	// return true;
	// }

	// public boolean loadTempSave() {
	// load("temp");
	// Save.loadCheckpoint(this, new File(saveDirectory +
	// "temp/params.object"));
	// return true;
	// }

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

	/** @return whether the cheat mode is activated */
	public boolean isCheatModeActivated() {
		return this.cheatModActivated;
	}

}
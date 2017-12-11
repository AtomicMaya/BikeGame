package main.game.actor.sensors;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.actor.entities.GameEntity;
import main.game.graphics.ImageGraphics;
import main.math.Vector;
import main.window.Canvas;

import java.util.Arrays;

/** Generic Checkpoint to save progress. Especially useful in crazy levels. Addendum : Now all levels. */
public class Checkpoint extends Trigger {

    /** Used for save purposes. */
	private static final long serialVersionUID = 111004971082478011L;

	/** The associated {@linkplain ImageGraphics}. */
	private transient ImageGraphics graphics;

	/** The paths to the image resources. */
	private String imagePath, imagePathTriggered;

	/**
	 * Creates a new {@linkplain Checkpoint}.
	 * @param game The master {@linkplain ActorGame}.
	 * @param position The position {@linkplain Vector}.
	 * @param imagePath The {@linkplain String} file path.
	 * @param imagePathTriggered The {@linkplain String} file path for when this {@linkplain Checkpoint} is triggered.
	 */
	public Checkpoint(ActorGame game, Vector position, String imagePath, String imagePathTriggered) {
		super(game, true, position, 1, 10);
		this.imagePath = imagePath;
		this.imagePathTriggered = imagePathTriggered;
		this.create();
	}

	/**
	 * Create a new {@linkplain Checkpoint} / Overloaded.
	 * @param game The master {@linkplain ActorGame}.
	 * @param position The position {@linkplain Vector}.
	 */
	public Checkpoint(ActorGame game, Vector position) {
	    this(game, position, "./res/images/flag.red.png", "./res/images/flag.green.png");
	}

    /**
     * Actual creation of the parameters of the {@linkplain GameEntity}, not in the constructor to
     * avoid duplication with the method {@linkplain #reCreate(ActorGame)}.
     */
	private void create() {
		this.graphics = this.addGraphics(this.imagePath, 1, 1);
		this.addGroupTrigger(Arrays.asList(ObjectGroup.PLAYER, ObjectGroup.WHEEL));
	}

	@Override
	void trigger() {
		this.getOwner().getGameManager().setLastCheckpoint(this);
		this.graphics = this.addGraphics(this.imagePathTriggered, 1, 1);
	}

	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
		create();
	}

	@Override
	public void draw(Canvas canvas) {
		this.graphics.draw(canvas);
	}

	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}

}

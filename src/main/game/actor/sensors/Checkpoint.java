package main.game.actor.sensors;

import java.util.Arrays;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.graphics.ImageGraphics;
import main.math.Vector;
import main.window.Canvas;

/**
 * Generic Checkpoint to save progress. Especially useful in crazy levels.
 */
public class Checkpoint extends Trigger {

	// for save purpose
	private static final long serialVersionUID = 111004971082478011L;

	private transient ImageGraphics graphics;

	private String imagePath, imagePathTriggered;

	/**
	 * Create a new {@linkplain Checkpoint}
	 * @param game {@linkplain ActorGame} where this {@linkplain Checkpoint}
	 * belong
	 * @param position position of this {@linkplain Checkpoint}
	 * @param imagePath path to the image
	 * @param imagePathHover path to the image when triggered
	 */
	public Checkpoint(ActorGame game, Vector position, String imagePath, String imagePathTriggered) {
		super(game, true, position, 1, 10);
		this.imagePath = imagePath;
		this.imagePathTriggered = imagePathTriggered;
		create();
	}

	/**
	 * Create a new {@linkplain Checkpoint}
	 * @param game {@linkplain ActorGame} where this {@linkplain Checkpoint}
	 * belong
	 * @param position position of this {@linkplain Checkpoint}
	 */
	public Checkpoint(ActorGame game, Vector position) {
		super(game, true, position, 1, 10);
		this.imagePath = "./res/images/flag.red.png";
		this.imagePathTriggered = "./res/images/flag.green.png";
		create();
	}

	private void create() {
		this.graphics = this.addGraphics(imagePath, 1, 1);
		addGroupTrigger(Arrays.asList(ObjectGroup.PLAYER, ObjectGroup.WHEEL));
	}

	@Override
	void trigger() {
		// set this checkpoint to be the last checkpoint
		this.getOwner().getGameManager().setLastCheckpoint(this);
		this.graphics = this.addGraphics(imagePathTriggered, 1, 1);
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

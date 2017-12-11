package main.game.actor.sensors;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.math.Vector;

/**
 * A special type of checkpoint that triggers end level events,
 */
public class FinishActor extends Checkpoint {

	// for save purposes
	private static final long serialVersionUID = -3554432863360752840L;

	private float width = -1, height = -1;

	/**
	 * Create a new FinishActor
	 * @param game {@linkplain ActorGame} where this {@linkplain FinishActor}
	 * belong
	 * @param position position of this {@linkplain FinishActor}
	 */
	public FinishActor(ActorGame game, Vector position) {
		super(game, position, "./res/images/flag.red.png", "./res/images/flag.red.png");

		create();
	}

	/**
	 * Create a new FinishActor
	 * @param game {@linkplain ActorGame} where this {@linkplain FinishActor}
	 * belong
	 * @param position position of this {@linkplain FinishActor}
	 * @param width width of the trigger area
	 * @param height height of the area
	 */
	public FinishActor(ActorGame game, Vector position, float width, float height) {
		super(game, position, "./res/images/flag.red.png", "./res/images/flag.red.png");
		this.width = width;
		this.height = height;
		create();
	}

	private void create() {
		setSize(width, height);
		setGroup(ObjectGroup.FINISH);
	}

	@Override
	void trigger() {
		getOwner().getPayload().triggerVictory();
		System.out.println(getOwner().getPayload());
	}

	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
		create();
	}

	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}

}

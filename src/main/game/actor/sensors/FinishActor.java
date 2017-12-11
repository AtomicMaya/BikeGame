package main.game.actor.sensors;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.actor.entities.GameEntity;
import main.math.Vector;

/** A special type of {@linkplain Checkpoint} that triggers end level events. */
public class FinishActor extends Checkpoint {
    /** Used for save purposes. */
	private static final long serialVersionUID = -3554432863360752840L;

	/** The dimensions of this {@linkplain FinishActor}. */
	private float width = -1, height = -1;

    /**
     * Create a new {@linkplain FinishActor}.
     * @param game The master {@linkplain ActorGame}.
     * @param position The position {@linkplain Vector} of this {@linkplain FinishActor}.
     * @param width width of the trigger area.
     * @param height height of the area.
     */
    public FinishActor(ActorGame game, Vector position, float width, float height) {
        super(game, position, "./res/images/flag.red.png", "./res/images/flag.red.png");
        this.width = width;
        this.height = height;
        this.create();
    }
	/**
	 * Create a new {@linkplain FinishActor}.
	 * @param game The master {@linkplain ActorGame}.
	 * @param position The position {@linkplain Vector} of this {@linkplain FinishActor}.
	 */
	public FinishActor(ActorGame game, Vector position) {
		super(game, position, "./res/images/flag.red.png", "./res/images/flag.red.png");
		this.create();
	}

    /**
     * Actual creation of the parameters of the {@linkplain GameEntity}, not in the constructor to
     * avoid duplication with the method {@linkplain #reCreate(ActorGame)}.
     */
	private void create() {
		this.setSize(width, height);
		this.setGroup(ObjectGroup.FINISH);
	}

	@Override
	void trigger() {
		this.getOwner().getPayload().triggerVictory();
	}

	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
		this.create();
	}

	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}

}

package main.game.actor.sensors;

import main.game.ActorGame;
import main.game.actor.entities.PlayableEntity;
import main.math.Vector;

/** Start {@linkplain Checkpoint} where the {@linkplain PlayableEntity} will spawn. */
public class SpawnCheckpoint extends Checkpoint {
    /** Used for save purposes. */
	private static final long serialVersionUID = -3732240693706393283L;

	/** The player {@linkplain PlayableEntity}. */
	private transient PlayableEntity player = null;

	/**
	 * Create a new {@linkplain SpawnCheckpoint}.
	 * @param player The {@linkplain PlayableEntity} of the game, if null will default to a {@linkplain main.game.actor.entities.Bike}.
	 */
	public SpawnCheckpoint(ActorGame game, Vector position, PlayableEntity player) {
		super(game, position, "./res/images/flag.blue.png", "./res/images/flag.blue.png");
		this.player = player;
		this.create();
	}

	private void create() {
		this.setTriggerStatus(true);
		this.getOwner().getGameManager().setStartCheckpoint(this);
		if (player != null) {
			this.getOwner().addActor(player);
			this.getOwner().setPayload(player);
			this.getOwner().setViewCandidate(player);
		}
	}

	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
	    this.create();
	}
}

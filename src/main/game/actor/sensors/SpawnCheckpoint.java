package main.game.actor.sensors;

import main.game.ActorGame;
import main.game.actor.entities.PlayableEntity;
import main.math.Vector;

/** Start {@linkplain Checkpoint} where the {@linkplain PlayableEntity} will spawn. */
public class SpawnCheckpoint extends Checkpoint {
    /** Used for save purposes. */
	private static final long serialVersionUID = -3732240693706393283L;

	/**
	 * Create a new {@linkplain SpawnCheckpoint}.
	 * @param game The master {@linkplain ActorGame}
     * @param position The initial position {@linkplain Vector}.
     */
	public SpawnCheckpoint(ActorGame game, Vector position) {
		super(game, position, "./res/images/flag.blue.png", "./res/images/flag.blue.png");
		this.create();
	}

	private void create() {
		this.setTriggerStatus(true);
		this.getOwner().getGameManager().setStartCheckpoint(this);
	}

	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
	    this.create();
	}
}

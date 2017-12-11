package main.game.actor.sensors;

import main.game.ActorGame;
import main.game.actor.entities.PlayableEntity;
import main.math.Vector;

/** Start checkpoint where the {@linkplain PlayableEntity} will spawn */
public class StartCheckpoint extends Checkpoint {

	// for save purpose
	private static final long serialVersionUID = -3732240693706393283L;

	/**
	 * Create a new {@linkplain StartCheckpoint}
	 * @param player {@linkplain PlayableEntity} of this game, if null will
	 * spawn a bike at this {@linkplain StartCheckpoint} position
	 */
	public StartCheckpoint(ActorGame game, Vector position, PlayableEntity player) {
		super(game, position, "./res/images/flag.blue.png", "./res/images/flag.blue.png");
		this.setTriggerStatus(true);
		game.getGameManager().setStartCheckpoint(this);
		if (player != null) {
			game.addActor(player);
			game.setPayload(player);
			game.setViewCandidate(player);
		}

	}
}

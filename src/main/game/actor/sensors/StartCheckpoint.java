package main.game.actor.sensors;

import main.game.ActorGame;
import main.game.actor.entities.PlayableEntity;
import main.math.Vector;

/** Start checkpoint where the {@linkplain PlayableEntity} will spawn */
public class StartCheckpoint extends Checkpoint {

	// for save purpose
	private static final long serialVersionUID = -3732240693706393283L;

	/** Create a new {@linkplain StartCheckpoint} */
	public StartCheckpoint(ActorGame game, Vector position) {
		super(game, position, "./res/images/flag.blue.png", "./res/images/flag.blue.png");
		this.setTriggerStatus(true);
		game.getGameManager().setLastCheckpoint(this);
	}
}

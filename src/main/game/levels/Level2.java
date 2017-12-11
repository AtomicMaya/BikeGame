/**
 *	Author: Clément Jeannet
 *	Date: 	11 déc. 2017
 */
package main.game.levels;

import main.game.ActorGame;
import main.game.actor.entities.Terrain;
import main.game.actor.sensors.Checkpoint;
import main.game.actor.sensors.FinishActor;
import main.game.actor.sensors.StartCheckpoint;
import main.math.Polyline;
import main.math.Vector;

public class Level2 extends Level {

	public Level2(ActorGame game) {
		super(game);
	}

	@Override
	public void createAllActors() {
		Polyline p = new Polyline(-1000.0f, -1000.0f, -1000.0f, 0.0f, 0.0f, 0.0f, 3.0f, 1.0f, 8.0f, 1.0f, 15.0f, 3.0f,
				16.0f, 3.0f, 25.0f, 0.0f, 35.0f, -5.0f, 50.0f, -5.0f, 55.0f, -4.0f, 65.0f, 0.0f, 6500.0f, -1000.0f);
		addActor(new Terrain(game, null, p, 2));
		addActor(new FinishActor(game, new Vector(-40, 0)));
		addActor(new Checkpoint(game, new Vector(-20, 0)));

	}

	@Override
	public boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public StartCheckpoint getSpawnCheckpoint() {
		return new StartCheckpoint(game, new Vector(4, 3), null);
	}

}

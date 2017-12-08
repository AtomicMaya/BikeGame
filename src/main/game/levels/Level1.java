/**
 *	Author: Clément Jeannet
 *	Date: 	27 nov. 2017
 */
package main.game.levels;

import main.game.ActorGame;
import main.game.actor.entities.Bike;
import main.game.actor.entities.FinishActor;
import main.game.actor.entities.Ground;
import main.math.Polygon;
import main.math.Polyline;
import main.math.Vector;

public class Level1 extends Level {

	private FinishActor finishActor;

	private Bike player;

	public Level1(ActorGame game) {
		super(game);
	}

	@Override
	public void createAllActors() {
		Polyline p = new Polyline(-1000.0f, -1000.0f, -1000.0f, 0.0f, 0.0f, 0.0f, 3.0f, 1.0f, 8.0f, 1.0f, 15.0f, 3.0f,
				16.0f, 3.0f, 25.0f, 0.0f, 35.0f, -5.0f, 50.0f, -5.0f, 55.0f, -4.0f, 65.0f, 0.0f, 6500.0f, -1000.0f);

		Ground ground = new Ground(game, null, p);

		player = new Bike(game, new Vector(4, 5));

		// Crate crate1 = new Crate(game, new Vector(6, 5), "res/images/crate.1.png",
		// false, 1);
		Polygon s = new Polygon(0, 100, 1, 100, 1, -100, 0, -100);
		finishActor = new FinishActor(game, new Vector(0, 0), player, s);

		this.addActor(finishActor);
		// this.addActor(crate1);
		this.addActor(ground);
		this.addActor(player);

		this.setViewCandidate(player);
		this.setPayload(player);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

	}

	@Override
	public boolean isFinished() {
        return finishActor != null && finishActor.isFinished();
    }

}

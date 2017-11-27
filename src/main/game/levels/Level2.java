/**
 *	Author: Clément Jeannet
 *	Date: 	27 nov. 2017
 */
package main.game.levels;

import main.game.actor.ActorGame;
import main.game.actor.crate.Crate;
import main.game.actor.myEntities.Bike;
import main.game.actor.myEntities.FinishActor;
import main.game.actor.myEntities.GameEntity;
import main.game.actor.myEntities.Ground;
import main.math.Polygon;
import main.math.Polyline;
import main.math.Vector;

public class Level2 extends Level {

	public Level2(ActorGame game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createAllActors() {
		Crate un = new Crate(game, new Vector(5, 6), null, false, 1);
		Polyline p = new Polyline(-1000.0f, -1000.0f, -1000.0f, 0.0f, 0.0f, 0.0f, 3.0f, 1.0f, 8.0f, 1.0f, 15.0f, 3.0f,
				16.0f, 3.0f, 25.0f, 0.0f, 35.0f, -5.0f, 50.0f, -5.0f, 55.0f, -4.0f, 65.0f, 0.0f, 6500.0f, -1000.0f);

		Ground ground = new Ground(game, null, p);

		GameEntity player = new Bike(game, new Vector(4, 5));
		Polygon s = new Polygon(0, 100, 1, 100, 1, -100, 0, -100);

		FinishActor a = new FinishActor(game, new Vector(0, 0), player, s);
		this.addActor(a);
		setFinishActor(a);
		addActor(un);
		this.addActor(player);
		addActor(ground);
		setViewCandidate(player);
	}

}

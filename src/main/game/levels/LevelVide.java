/**
 *	Author: Clément Jeannet
 *	Date: 	27 nov. 2017
 */
package main.game.levels;

import main.game.ActorGame;
import main.game.actor.crate.Crate;
import main.game.actor.entities.FinishActor;
import main.math.Polygon;
import main.math.Vector;

public class LevelVide extends Level {

	public LevelVide(ActorGame game) {
		super(game);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createAllActors() {
		Crate un = new Crate(game, new Vector(5, 6), null, false, 1);
		addActor(un);
		setViewCandidate(un);
		Polygon s = new Polygon(0, 100, 1, 100, 1, -100, 0, -100);

		FinishActor a = new FinishActor(game, new Vector(0, 0), un, s);
		
		addActor(a);
	}

}

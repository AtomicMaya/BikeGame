/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor;

import main.game.actor.myEntities.Bike;
import main.game.actor.myEntities.Ground;
import main.io.FileSystem;
import main.math.Polyline;
import main.math.Vector;
import main.window.Window;

public class TestGame extends ActorGame {

	Bike player;

	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);

		// TODO creation objects du program
		Polyline p = new Polyline(-1000.0f, -1000.0f, -1000.0f, 0.0f, 0.0f, 0.0f, 3.0f, 1.0f, 8.0f, 1.0f, 15.0f, 3.0f,
				16.0f, 3.0f, 25.0f, 0.0f, 35.0f, -5.0f, 50.0f, -5.0f, 55.0f, -4.0f, 65.0f, 0.0f, 6500.0f, -1000.0f);

		new Ground(this, null, p);

		player = new Bike(this, new Vector(4, 5));
		
		this.setViewCandidate(player.cycliste);
		return true;

	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

	}
}
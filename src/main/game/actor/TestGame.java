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
		Polyline p = new Polyline(-50f, 0.f, 0.f, -2.f, 50.f, 0.f);

		Ground ground = new Ground(this, null, p);

		player = new Bike(this, new Vector(-0, 5));


		this.setViewCandidate(player);
		this.addActor(ground);
		this.addActor(player);
		return true;

	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		player.update(deltaTime);
	}
}
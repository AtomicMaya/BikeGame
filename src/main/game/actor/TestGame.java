package main.game.actor;

import main.game.actor.myEntities.Bike;
import main.game.actor.myEntities.Ground;
import main.game.actor.myEntities.TestEntity;
import main.io.FileSystem;
import main.math.Polygon;
import main.math.Polyline;
import main.math.Vector;
import main.window.Window;

public class TestGame extends ActorGame {

	Bike player;
	TestEntity test;

	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);

		// TODO creation objects du program
		Polyline p = new Polyline(-50f, 0.f, 0.f, -2.f, 50.f, 0.f);
		Polygon p1 = new Polygon(-10.f, 0.f, -5.f, 5.f, 0.f, -2.5f, -7.5f, -5.f);
		Ground ground = new Ground(this, null, p);

		player = new Bike(this, new Vector(-0, 5));
		test = new TestEntity(this, Vector.ZERO, p1);

		this.setViewCandidate(test);
		this.addActor(ground);
		this.addActor(player);
		this.addActor(test);
		return true;

	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		player.update(deltaTime);
		test.update(deltaTime);
	}

	@Override
	public void end() {
		// Nothing yet
	}
}
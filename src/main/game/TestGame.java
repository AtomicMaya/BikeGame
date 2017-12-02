package main.game;

import java.util.List;

import main.game.actor.Audio;
import main.game.actor.crate.Crate;
import main.game.actor.entities.Bike;
import main.game.actor.entities.FinishActor;
import main.game.actor.entities.Ground;
import main.game.actor.entities.KeyboardProximitySensor;
import main.game.actor.entities.SimpleLever;
import main.game.levels.Level;
import main.io.FileSystem;
import main.math.Polygon;
import main.math.Polyline;
import main.math.Vector;
import main.window.Window;

public class TestGame extends ActorGame {
	private List<Level> levels;
	private FinishActor a;
	private KeyboardProximitySensor sensor;

	private Audio backgroundAudio, backgroundAudio2;


	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);

		// TODO creation objects du program
		//Polygon p1 = new Polygon(-10.f, 0.f, -5.f, 5.f, 0.f, -2.5f, -7.5f, -5.f);

		Polyline p = new Polyline(
				-1000.0f, -1000.0f,
				-1000.0f, 0.0f,
				0.0f, 0.0f,
				3.0f, 1.0f,
				8.0f, 1.0f,
				15.0f, 3.0f,
				16.0f, 3.0f,
				25.0f, 0.0f,
				35.0f, -5.0f,
				50.0f, -5.0f,
				55.0f, -4.0f,
				65.0f, 0.0f,
				6500.0f, -1000.0f
		);

		Ground ground = new Ground(this, null, p);

		Bike player = new Bike(this, new Vector(4, 5));

		Polygon shape = new Polygon(.0f, .0f, 5.f, .0f, 5.f, 5.f, .0f, 5.f);
		//sensor = new ProximitySensor(this, new Vector(12, 3), shape);
		//sensor = new KeyboardProximitySensor(this, new Vector(12, 3), shape, KeyEvent.VK_E);

		SimpleLever lever = new SimpleLever(this, new Vector(12, 3));

		backgroundAudio = new Audio("./res/audio/chiptune_energetic.wav", -1, 0.f);

		Crate crate1 = new Crate(this, new Vector(10 ,4), "res/images/crate.1.png", false, 1);

		/*
		Polygon s = new Polygon(0, 100, 1, 100, 1, -100, 0, -100);
		a = new FinishActor(this, new Vector(7, 0), player, s);
		*/
		//this.addActor(a);
		this.addActor(crate1);
		this.setViewCandidate(player);
		this.addActor(ground);
		this.addActor(player);
		//this.addActor(sensor);
		this.addActor(lever);
		return true;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}

	@Override
	public void end() {
		backgroundAudio.destroy();
		//backgroundAudio2.destroy();
	}
}
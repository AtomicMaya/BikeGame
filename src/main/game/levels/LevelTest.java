package main.game.levels;

import main.game.ActorGame;
import main.game.actor.entities.*;
import main.game.actor.sensors.Checkpoint;
import main.game.actor.sensors.FinishActor;
import main.game.actor.sensors.SpawnCheckpoint;
import main.game.graphics.BetterTextGraphics;
import main.math.ExtendedMath;
import main.math.Polygon;
import main.math.Polyline;
import main.math.Vector;

public class LevelTest extends Level {

	private FinishActor finishActor;

//	private Bike player;
	BetterTextGraphics btg;
	SpawnCheckpoint sc;

	public LevelTest(ActorGame game) {
		super(game);
	}

	@Override
	public void createAllActors() {
		Polyline p = new Polyline(-1000, -1000.0f, -1000.0f, 0.0f, 0.0f, 0.0f, 3.0f, 1.0f, 8.0f, 1.0f, 15.0f, 3.0f,
				16.0f, 3.0f, 25.0f, 0.0f, 35.0f, -5.0f, 50.0f, -5.0f, 55.0f, -4.0f, 65.0f, 0.0f, 6500.0f, -1000.0f);

		Terrain terrain = new Terrain(game, null, p, TerrainType.NORMAL);

//		player = new Bike(game, new Vector(-12, 5));

		// Crate crate1 = new Crate(game, new Vector(6, 5),
		// "res/images/crate.1.png",
		// false, 1);
		Polygon s = new Polygon(0, 100, 1, 100, 1, -100, 0, -100);
		finishActor = new FinishActor(game, new Vector(-8, 0));

		this.addActor(finishActor);
		// this.addActor(crate1);
		this.addActor(terrain);
		Trampoline t = new Trampoline(game, new Vector(5, 6), 5, 1);
		Polygon p1 = new Polygon(0, 0, 0, 3f, 1.5f, 3f, 1.5f, 0);
//		Liquid l = new Liquid(game, new Vector(-9, 2), p1, true);
//		Mine m = new Mine(game, new Vector(-3, -1));
		GravityWell gw = new GravityWell(game, new Vector(-50,0), new Vector(0,.4f), ExtendedMath.createRectangle(3, 7), (float) Math.PI);
		addActor(gw);
//		addActor(pp);
		addActor(new Checkpoint(game, new Vector(-26, 0)));
		 sc = new SpawnCheckpoint(game, new Vector(-30, 0), null);
//		 addActor(sc);
		// addActor(m);
		this.addActor(terrain);
//		this.addActor(player);
		this.addActor(t);
//		addActor(l);
		BoomBarrel bb = new BoomBarrel(game, new Vector(-16, 0), true);
		addActor(bb);
		// addActor(l);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);


	}

	@Override
	public boolean isFinished() {
		return getPayload() != null && getPayload().getVictoryStatus() | getPayload().getDeathStatus();
	}

    @Override
    public void dispose() {

    }


	@Override
	public SpawnCheckpoint getSpawnCheckpoint() {
		return sc;
	}

}

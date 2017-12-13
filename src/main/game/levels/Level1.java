package main.game.levels;

import main.game.ActorGame;
import main.game.actor.entities.*;
import main.game.actor.entities.collectable.Coin;
import main.game.actor.sensors.Checkpoint;
import main.game.actor.sensors.FinishActor;
import main.game.actor.sensors.SpawnCheckpoint;
import main.math.Polygon;
import main.math.Polyline;
import main.math.Vector;

public class Level1 extends Level {

	private FinishActor finishActor;


	public Level1(ActorGame game) {
		super(game);
	}

	@Override
	public void createAllActors() {
        Polyline polyline = new Polyline(-30, -100,
                -30.f, 0.f,
                0.f, 0.f,
                14.f, 0.f,
                15.f, -5.f,
                15.f, -7.f,
                37.f, -7f,
                37.f, -5.f,
                38.f, 0.f,
                39.95f, 1.05f,
                41.49f, 1.42f,
                43.58f, 1.42f,
                45.342f, 0.9636f,
                48.f, 0.f,
                49.727f, -1.103f,
                52.755f, -2.148f,
                55.458f, -2.653f,
                58.125f, -2.689f,
                60.576f, -1.824f,
                63.099f, -0.850f,
                65.396f, 0.1734f,
                67.890f, 0.9973f,
                70.225f, 1.2262f,
                72.582f, 1.2033f,
                75.603f, 0.6311f,
                78.021f, -0.337f,
                80.833f, -1.139f,
                85.224f, -3.029f,
                88.975f, -3.269f,
                92.806f, -3.349f,
                96.237f, -1.433f,
                100.f, 0.f,
                102.5f, 0.f,
                102.5f, -1.f,
                107.5f, -1.f,
                107.5f, -100);

        Terrain terrain = new Terrain(this.game, new Vector(0, 0), polyline, TerrainType.ICE);
        TriggeredPlatform platform = new TriggeredPlatform(this.game, new Vector(22.5f, 1), new Vector(0, 0),
                0, 0, 0, 0, 0, new Polygon(.0f, .0f, 6.f, .0f, 6.f, 1.f, .0f, 1.f), 6, 1);

        Liquid lava = new Liquid(this.game, new Vector(15, -7), new Polygon(0, 0, 22, 0, 22, 2, 0, 2), true);

        Pendulum pendulum = new Pendulum(this.game, new Vector(25, 12), 8, 1);

        Checkpoint checkpoint1 = new Checkpoint(this.game, new Vector(42.5f, 1.5f));

        TriggeredPlatform platform1 = new TriggeredPlatform(this.game, new Vector(49f, 6), new Vector(0, 0),
                0, 0, 0, 0, 0, new Polygon(.0f, .0f, 6.f, .0f, 6.f, 1.f, .0f, 1.f), 6, 1);

        TriggeredPlatform platform2 = new TriggeredPlatform(this.game, new Vector(39f, 11), new Vector(0, 0),
                0, 0, 0, 0, 0, new Polygon(.0f, .0f, 6.f, .0f, 6.f, 1.f, .0f, 1.f), 6, 1);
        TriggeredPlatform platform3 = new TriggeredPlatform(this.game, new Vector(51f, 17), new Vector(0, 0),
                0, 0, 0, 0, 0, new Polygon(.0f, .0f, 6.f, .0f, 6.f, 1.f, .0f, 1.f), 6, 1);

        Coin smallCoin1 = new Coin(this.game, new Vector(41, 12.5f), false);
        Coin smallCoin2 = new Coin(this.game, new Vector(42, 12.5f), false);
        Coin bigCoin1 = new Coin(this.game, new Vector(53f, 18.5f), true);

        Mine mine1 = new Mine(this.game, new Vector(48, -1.25f));
        Mine mine2 = new Mine(this.game, new Vector(52.75f, -3.14f));
        Mine mine3 = new Mine(this.game, new Vector(56.5f, -3.67f));
        Mine mine4 = new Mine(this.game, new Vector(59, -3.3f));
        Mine mine5 = new Mine(this.game, new Vector(60.5f, -2.75f), 1.5f);
        Mine mine6 = new Mine(this.game, new Vector(76, -.8f), 1.2f);
        Mine mine7 = new Mine(this.game, new Vector(81, -2.6f));
        Mine mine8 = new Mine(this.game, new Vector(91, -4.3f));

        Checkpoint checkpoint2 = new Checkpoint(this.game, new Vector(100.5f, 0));

        addActor(terrain);
        addActor(platform);
        addActor(lava);
        addActor(pendulum);
        addActor(checkpoint1);
        addActor(platform1);
        addActor(platform2);
        addActor(platform3);
        addActor(smallCoin1);
        addActor(smallCoin2);
        addActor(bigCoin1);
        addActor(mine1); addActor(mine2);
        addActor(mine3); addActor(mine4);
        addActor(mine5); addActor(mine6);
        addActor(mine7); addActor(mine8);
        addActor(checkpoint2);
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
        return new SpawnCheckpoint(this.game, new Vector(1, 0));
	}

}

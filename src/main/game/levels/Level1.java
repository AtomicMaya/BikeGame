package main.game.levels;

import main.game.ActorGame;
import main.game.actor.entities.*;
import main.game.actor.entities.collectable.Coin;
import main.game.actor.sensors.Checkpoint;
import main.game.actor.sensors.FinishActor;
import main.game.actor.sensors.SpawnCheckpoint;
import main.game.graphics.GraphicalDrawer;
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
        Polyline polyline = new Polyline(-30, -100, -30.f, 0.f, 0.f, 0.f, 14.f, 0.f, 15.f, -5.f, 15.f, -7.f,
                37.f, -7f, 37.f, -5.f, 38.f, 0.f, 39.95f, 1.05f, 41.49f, 1.42f, 43.58f, 1.42f, 45.342f, 0.9636f,
                48.f, 0.f, 49.727f, -1.103f, 52.755f, -2.148f, 55.458f, -2.653f, 58.125f, -2.689f, 60.576f, -1.824f,
                63.099f, -0.850f, 65.396f, 0.1734f, 67.890f, 0.9973f, 70.225f, 1.2262f, 72.582f, 1.2033f,
                75.603f, 0.6311f, 78.021f, -0.337f, 80.833f, -1.139f, 85.224f, -3.029f, 88.975f, -3.269f,
                92.806f, -3.349f, 96.237f, -1.433f, 100.f, 0.f, 102.5f, 0.f,
                102.5f, -1.f,
                107.5f, -1.f,
                107.5f, 0f,
                140f, 0f,
                140f, -100);

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

        MovingPlatform movingPlatform = new MovingPlatform(this.game, new Vector(102.5f, -1), new Vector(0, 1), 50, 18, 5);

        Obstacle obstacle1 = new Obstacle(this.game, new Vector(107.5f, 0), new Polygon(0.f, 0.f,
                0.9616f, -6.042f, 7.6158f, -8.276f, 16.360f, -8.204f, 25.272f, -10.34f, 31.734f, -8.564f, 37.544f, -8.540f,
                40.f, 0.f, 39.f, 4.f, 39.687f, 7.8852f, 40.067f, 11.302f, 38.041f, 16.154f, 38.506f, 19.445f,
                40.151f, 25.478f, 39.331f, 26.866f, 40.525f, 29.002f, 39.646f, 32.960f, 40.939f, 37.671f, 44.206f, 40.687f,
                49.106f, 42.132f, 52.5f, 45.f, 52.5f, 50.f, 0.f, 50.f, 1.3629f, 47.911f, 1.8026f, 45.210f, 2.4936f, 42.634f,
                1.8654f, 41.315f, 3.4988f, 38.111f, 1.0488f, 36.666f, 2.1167f, 34.719f, 1.1323f, 31.893f, 2.f, 32.f,
                0.8776f, 24.444f, 1.3232f, 22.280f, 1.1004f, 20.274f, 1.7052f, 18.333f, 0.8139f, 16.518f, 1.3232f, 14.608f,
                0.5275f, 11.712f, 1.7371f, 7.3195f, 0.9094f, 6.6511f, 1.7371f, 5.4415f));

        Obstacle obstacle2 = new Obstacle(this.game, new Vector(107.5f, 60), new Polygon(-22.5f, 30.f,
                -20.73f, 25.446f, -18.96f, 21.999f, -16.91f, 17.959f, -14.49f, 12.789f, -11.15f, 8.5880f, -7.919f, 4.8178f,
                -4.418f, 2.0710f, 0.f, 0.f, 52.5f, 0.f, 58.337f, -0.815f, 60.430f, -2.213f, 62.241f, -4.111f,
                63.409f, -6.455f, 64.139f, -9.524f, 64.185f, -12.60f, 64.145f, -15.26f, 63.574f, -18.10f, 62.8f, -20.f,
                61.6f, -22.f, 59.174f, -24.05f, 55.626f, -25.49f, 49.934f, -25.74f, 48.433f, -26.77f, 47.644f, -28.67f,
                47.486f, -31.43f, 48.670f, -33.17f, 47.723f, -34.51f, 48.591f, -36.17f, 46.854f, -38.46f, 48.4f, -40.f,
                47.328f, -43.19f, 48.276f, -46.20f, 48.6f, -50.f, 102.5f, -50.f, 102.5f, 30.f));

        GraphicalDrawer drawer = new GraphicalDrawer();


        GravityWell gravityWell = new GravityWell(this.game, new Vector(102.5f, 50), new Vector(.1f, .06f), new Polygon(0, 0, 52.5f, 0, 52.5f, 10, 0, 10), 0);

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
        addActor(movingPlatform);
        addActor(obstacle1);
        addActor(obstacle2);
        //addActor(gravityWell);
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
        return new SpawnCheckpoint(this.game, new Vector(140, 50), null);
	}

}

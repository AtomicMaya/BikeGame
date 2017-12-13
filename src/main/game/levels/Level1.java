package main.game.levels;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.entities.*;
import main.game.actor.entities.collectable.Ammo;
import main.game.actor.entities.collectable.Coin;
import main.game.actor.entities.switchers.Lever;
import main.game.actor.sensors.Checkpoint;
import main.game.actor.sensors.FinishActor;
import main.game.actor.sensors.SpawnCheckpoint;
import main.game.graphics.GraphicalDrawer;
import main.game.graphics.ShapeGraphics;
import main.math.Polygon;
import main.math.Polyline;
import main.math.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

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
                92.806f, -3.349f, 96.237f, -1.433f, 99.f, 0.f, 102.5f, 0.f,
                102.5f, -1.f,
                107.5f, -1.f,
                107.5f, 0f,
                120, 0f,
                120f, -100);

        Polyline polylineInside = new Polyline(0, -100, 0, 0, 60, 0, 60, -2, 72, -2, 72, 0, 105, 0, 105, -1,
                110, -1, 110, 0, 150, 0, 150, -100);

        Obstacle obstacle = new Obstacle(this.game, new Vector(-30, -7), new Polygon(0.f, -7.f,
                25.f, 0.f, 23.440f, 4.2403f, 24.159f, 5.2519f, 23.440f, 6.8225f, 21.603f, 8.3664f, 23.413f, 11.792f,
                22.535f, 15.013f, 24.372f, 18.314f, 24.744f, 21.934f, 22.428f, 23.531f, 18.116f, 24.144f,
                2.8457f, 23.851f, -0.534f, 21.854f, 0.f, 19.272f));

        Terrain terrain = new Terrain(this.game, new Vector(0, 0), polyline, TerrainType.ICE);

        Terrain insideTerrain = new Terrain(this.game, new Vector(120, 0), polylineInside, TerrainType.STONE);

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

        Ammo gunAmmo = new Ammo(this.game, new Vector(25, 3), false);

        Coin smallCoin = new Coin(this.game, new Vector(51.5f   , 7.5f), false);
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

        Coin smallCoin3 = new Coin(this.game, new Vector(69, 2), false);
        Coin smallCoin4 = new Coin(this.game, new Vector(71, 2), false);

        Checkpoint checkpoint2 = new Checkpoint(this.game, new Vector(101f, 0));

        Lever lever = new Lever(this.game, new Vector(99.5f, 0));
        TriggeredPlatform platform4 = new TriggeredPlatform(this.game, new Vector(102.5f, -1), new Vector(0, 1), 50, 18, 5, 3, 1);

        lever.addAction(() -> platform4.triggerAction(), 1);

        Coin smallCoin5 = new Coin(this.game, new Vector(103, 6), false);
        Coin smallCoin6 = new Coin(this.game, new Vector(106.5f, 12), false);
        Coin smallCoin7 = new Coin(this.game, new Vector(103, 18), false);
        Coin smallCoin8 = new Coin(this.game, new Vector(106.5f, 24), false);
        Coin smallCoin9 = new Coin(this.game, new Vector(103, 30), false);
        Coin smallCoin10 = new Coin(this.game, new Vector(106.5f, 36), false);
        Coin smallCoin11 = new Coin(this.game, new Vector(103, 42), false);
        Coin smallCoin12 = new Coin(this.game, new Vector(106.5f, 50), false);

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
                47.328f, -43.19f, 48.276f, -46.20f, 48.6f, -50.f, 50.5f, -50, 50.5f, -49, 51.5f, -49, 51.5f, -50, 107.5f, -50,
                107.56f, -49, 108.5f, -49, 108.5f, -50, 110f, -50.f, 110f, -23f, 100, -23f, 100, -18, 110, -18, 110, 20,
                118, 20, 118, -7, 119.0932f, -13.3424f, 122, -20, 180, -20, 180, 30));

        GraphicalDrawer drawer = new GraphicalDrawer();
        drawer.addGraphics(new ShapeGraphics(new Polygon(0, 0, 65, 0, 65, 16, 0, 16), Color.decode("#d3d3d3"), null, 0, 1, -5), new Vector(107.5f, 50));
        drawer.addGraphics(new ShapeGraphics(new Polygon(0, 0, 200, 0, 200, 100, 0, 100), Color.decode("#d3d3d3"), null, 0, 1, -5), new Vector(120, -5));

        GravityWell gravityWell = new GravityWell(this.game, new Vector(107.5f, 50), new Vector(.1f, .06f), new Polygon(0, 0, 52.5f, 0, 52.5f, 10, 0, 10), 0);

        Checkpoint checkpoint3 = new Checkpoint(this.game, new Vector(161, 34.5f));

        Coin bigCoin2 = new Coin(this.game, new Vector(148, 15), true);

        Checkpoint checkpoint4 = new Checkpoint(this.game, new Vector(155, 0));

        Liquid acid = new Liquid(this.game, new Vector(180, -2), new Polygon(0, 0, 12, 0, 12, 2, 0, 2), false);

        Laser laser = new Laser(this.game, new Vector(160, 10), 9.75f, 3, false);
        Laser laser1 = new Laser(this.game, new Vector(162, 10), 9.75f, 3, false);
        Laser laser2 = new Laser(this.game, new Vector(164, 10), 9.75f, 3, false);
        Laser laser3 = new Laser(this.game, new Vector(170, 0), 9.8f, 1, false);
        Laser laser4 = new Laser(this.game, new Vector(172, 0), 9.8f, 1, false);
        Laser laser5 = new Laser(this.game, new Vector(174, 0), 9.8f, 1, false);
        Laser laser6 = new Laser(this.game, new Vector(201, 0), 9.8f, 1, false);
        Laser laser7 = new Laser(this.game, new Vector(202, 0), 9.8f, 1, false);
        Laser laser8 = new Laser(this.game, new Vector(203, 0), 9.8f, 1, false);
        Laser laser9 = new Laser(this.game, new Vector(205, 10), 9.75f, 3, false);
        Laser laser10 = new Laser(this.game, new Vector(206, 0), 9.8f, 1, false);
        Laser laser11 = new Laser(this.game, new Vector(207, 10), 9.75f, 3, false);
        Laser laser12 = new Laser(this.game, new Vector(208, 0), 9.8f, 1, false);

        ArrayList<Actor> lasers = new ArrayList<>(Arrays.asList(laser, laser1, laser2, laser3, laser4, laser5, laser6, laser7, laser8, laser9, laser10, laser11, laser12));

        Ammo gunAmmo1 = new Ammo(this.game, new Vector(166f, 7f), false);
        Coin smallCoin13 = new Coin(this.game, new Vector(181, 3), false);
        Coin smallCoin14 = new Coin(this.game, new Vector(184, 4), false);
        Coin smallCoin15 = new Coin(this.game, new Vector(187, 4), false);
        Coin smallCoin16 = new Coin(this.game, new Vector(190, 3), false);

        TriggeredPlatform platform5 = new TriggeredPlatform(this.game, new Vector(158, 0), new Vector(0, -1),
                11, 3, 10000, 1, 1, new Polygon(0, 0, 1, 0, 1, 11, 0, 11), 1, 11);

        Lever lever1 = new Lever(this.game, new Vector(152, 0));
        lever1.addAction(() -> {
            for(Actor actor : lasers)
                ((Laser) actor).switchState();
            platform5.triggerAction();
        }, 15);

        TriggeredPlatform platform6 = new TriggeredPlatform(this.game, new Vector(215, 0), new Vector(0, -1),
                11, 3, 10000, 1, 1, new Polygon(0, 0, 1, 0, 1, 11, 0, 11), 1, 11);

        Lever lever2 = new Lever(this.game, new Vector(212, 0));
        lever2.addAction(() -> {
            for(Actor actor : lasers)
                ((Laser) actor).switchState();
            platform6.triggerAction();
        }, 15);

        Checkpoint checkpoint5 = new Checkpoint(this.game, new Vector(219, 0));

        TriggeredPlatform platform7 = new TriggeredPlatform(this.game, new Vector(225, -1), new Vector(0, 1),
                30, 10, 5, 2, 1);

        Lever lever3 = new Lever(this.game, new Vector(222, 0));
        lever3.addAction(() -> platform7.triggerAction(), 15);

        Coin smallCoin17 = new Coin(this.game, new Vector(225.5f, 6), false);
        Coin smallCoin18 = new Coin(this.game, new Vector(228.5f, 12), false);
        Coin smallCoin19 = new Coin(this.game, new Vector(225.5f, 18), false);
        Coin smallCoin20 = new Coin(this.game, new Vector(228.5f, 24), false);
        Coin smallCoin21 = new Coin(this.game, new Vector(225.5f, 30), false);

        Obstacle obstacle3 = new Obstacle(this.game, new Vector(230, 0), new Polygon(0.f, 0.f, 2.5711f,
                -2.647f, 5.2256f, -3.676f, 8.3136f, -2.539f, 16.006f, -2.593f, 26.245f, -1.672f, 31.879f, -3.514f,
                37.622f, -2.593f, 44.069f, -3.351f, 50.142f, -1.997f, 54.692f, -4.056f, 58.485f, -2.539f,
                59.785f, 0.f, 59.893f, 12.683f, 58.864f, 14.146f, 60.327f, 20.972f, 60.706f, 30.f, 0.f, 30.f,
                0.7833f, 27.907f, 0.9458f, 26.227f, 1.3250f, 23.843f, 0.5124f, 22.814f, 1.5417f, 21.297f,
                0.7833f, 19.618f, 1.6501f, 18.101f, 0.5666f, 16.530f, 2.0835f, 13.713f, 0.6208f, 11.112f,
                0.4041f, 9.2708f, 0.7833f, 7.9706f,1.6501f, 7.0496f, 0.5124f, 6.0745f, 1.0000f, 4.9368f, 0.6749f, 2.9323f));

        TriggeredPlatform platform8 = new TriggeredPlatform(this.game, new Vector(217.5f, 36), new Vector(0, 0), 0, 0, 0, 0, 0);

        TriggeredPlatform platform9 = new TriggeredPlatform(this.game, new Vector(216.5f, 37), new Vector(0, -1),
                5, 1, 5000, 15, 1, new Polygon(0, 0, 1, 0, 1, 5, 0, 5), 1, 5);

        TriggeredPlatform platform10 = new TriggeredPlatform(this.game, new Vector(217.5f, 53), new Vector(-1, 0),
                8, 2, 5000, 0, 1, new Polygon(0, 0, 8, 0, 8, 1, 0, 1), 8, 1);

        BoomBarrel boomBarrel = new BoomBarrel(this.game, new Vector(219, 55f), true);
        BoomBarrel boomBarrel1 = new BoomBarrel(this.game, new Vector(222.5f, 55f), true);
        BoomBarrel boomBarrel2 = new BoomBarrel(this.game, new Vector(224f, 55f), true);

        Coin bigCoin3 = new Coin(this.game, new Vector(209, 39f), true);
        Coin bigCoin4 = new Coin(this.game, new Vector(211, 39f), true);
        Coin bigCoin5 = new Coin(this.game, new Vector(213, 39f), true);

        Lever lever4 = new Lever(this.game, new Vector(219.5f, 37));
        lever4.addAction(() -> {
            platform9.triggerAction();
            platform10.triggerAction();
            boomBarrel.setPosition(new Vector(219, 75f));
            boomBarrel1.setPosition(new Vector(222.25f, 75f));
            boomBarrel2.setPosition(new Vector(223.5f, 75f));
        }, 10);

        this.finishActor = new FinishActor(this.game, new Vector(245,30));

        this.addActors(new ArrayList<>(Arrays.asList(terrain, insideTerrain)));
        this.addActors(new ArrayList<>(Arrays.asList(obstacle, obstacle1, obstacle2, obstacle3)));
        this.addActors(new ArrayList<>(Arrays.asList(platform, platform1, platform2, platform3, platform4, platform5,
                platform6, platform7, platform8, platform9, platform10)));
        this.addActors(new ArrayList<>(Arrays.asList(checkpoint1, checkpoint2, checkpoint3, checkpoint4, checkpoint5)));
        this.addActors(new ArrayList<>(Arrays.asList(lever, lever1, lever2, lever3, lever4)));
        this.addActors(new ArrayList<>(Arrays.asList(lava, acid)));
        this.addActors(new ArrayList<>(Arrays.asList(smallCoin, smallCoin1, smallCoin2, smallCoin3, smallCoin4, smallCoin5,
                smallCoin6, smallCoin7, smallCoin8, smallCoin9, smallCoin10, smallCoin11, smallCoin12, smallCoin13,
                smallCoin14, smallCoin15, smallCoin16, smallCoin17, smallCoin18, smallCoin19, smallCoin20, smallCoin21)));
        this.addActors(new ArrayList<>(Arrays.asList(bigCoin1, bigCoin2, bigCoin3, bigCoin4, bigCoin5)));
        this.addActors(new ArrayList<>(Arrays.asList(gunAmmo, gunAmmo1)));
        this.addActors(new ArrayList<>(Arrays.asList(mine1, mine2, mine3, mine4, mine5, mine6, mine7, mine8)));
        this.addActors(lasers);
        this.addActor(pendulum);
        this.addActor(gravityWell);
        this.addActors(new ArrayList<>(Arrays.asList(boomBarrel, boomBarrel1, boomBarrel2)));
        this.addActor(drawer);
        this.addActor(this.finishActor);
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
        return new SpawnCheckpoint(this.game, new Vector(0, 0));
	}

}

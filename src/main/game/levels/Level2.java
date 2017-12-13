package main.game.levels;

import main.game.ActorGame;
import main.game.actor.DepthValue;
import main.game.actor.entities.*;
import main.game.actor.entities.collectable.Coin;
import main.game.actor.entities.switchers.Lever;
import main.game.actor.sensors.Checkpoint;
import main.game.actor.sensors.FinishActor;
import main.game.actor.sensors.SpawnCheckpoint;
import main.game.graphicalActors.GraphicalDrawer;
import main.game.graphicalActors.Preset;
import main.game.graphicalActors.Scenery;
import main.game.graphics.ShapeGraphics;
import main.math.Polygon;
import main.math.Polyline;
import main.math.Vector;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Level2 extends Level {
    public Level2(ActorGame game) {
        super(game);
    }

    @Override
    public void createAllActors() {
        Polyline polyline = new Polyline(-20, -100, -20.f, 0.f, 0.f, 0.f, 4.f, 0.f, 5.1235f, -0.558f,
                6.2645f, -1.410f, 7.3693f, -2.514f, 7.9308f, -3.728f, 8.5104f, -4.905f, 9.1986f, -5.648f, 10.176f, -6.644f,
                11.263f, -7.894f, 12.984f, -9.125f, 14.729f, -10.10f, 17.798f, -11.37f, 21.583f, -11.80f, 24.f, -12.f,
                49.720f, -11.98f, 49.947f, -12.55f, 50.f, -14.f, 49.843f, -15.25f, 49.804f, -16.92f, 49.904f, -18.90f,
                49.594f, -20.38f, 49.726f, -23.30f, 49.835f, -25.09f, 49.532f, -27.53f, 49.570f, -30.87f, 50.f, -32.f,
                50.f, -36.f, 55.008f, -35.96f, 55.030f, -34.96f, 59.777f, -35.21f, 63.989f, -35.25f, 66.875f, -34.84f,
                68.718f, -34.72f, 70.954f, -34.24f, 74.f, -34.f, 77.283f, -33.38f, 80.698f, -32.40f, 84.383f, -30.02f,
                87.089f, -25.03f, 88.488f, -18.40f, 88.442f, -14.49f, 85.643f, -8.146f, 83.870f, -5.626f, 79.112f, -4.600f,
                78.272f, -1.801f, 77.665f, 1.2306f, 77.769f, 5.6930f, 77.183f, 9.5060f, 79.138f, 15.323f, 79.285f, 19.625f,
                78.503f, 24.562f, 79.725f, 27.153f, 79.334f, 29.500f, 79.040f, 32.482f, 79.774f, 36.881f, 79.334f, 44.556f,
                80.f, 48.f, 83.077f, 49.955f, 145.f, 50.f, 145, -100);

        Polygon polygon = new Polygon(-20, 6f, 0.f, 5.9989f, 4.9507f, 4.6117f, 8.3218f, 3.0666f,
                11.973f, 1.5214f, 16.047f, 0.6787f, 20.963f, 0.f, 27.003f, -1.006f, 34.448f, -2.832f, 38.381f, -2.130f,
                42.876f, -1.006f, 45.295f, 0.1723f, 47.792f, 0.9596f, 49.758f, 3.7689f, 50.084f, 5.7324f, 49.461f, 8.2208f,
                50.f, 10.f, 49.507f, 10.812f, 49.414f, 11.669f, 49.565f, 12.676f, 49.970f, 14.353f, 47.722f, 14.377f,
                35.748f, 14.517f, 28.741f, 14.802f, 27.146f, 15.030f, 25.f, 15.f, 17.689f, 15.087f, 12.277f, 15.827f,
                9.3151f, 17.138f, 5.f, 20.f, 3.0486f, 24.487f, 2.9916f, 29.386f, 3.3198f, 36.557f, 4.4709f, 42.313f,
                7.2336f, 49.219f, 12.298f, 54.745f, 18.514f, 55.666f, 24.961f, 55.436f, 30.716f, 57.738f, 33.249f, 90f,
                80, 90, 85, 60, 150f, 60f, 150f, 60f, -20.f, 150.f);

        Terrain terrain1 = new Terrain(this.game, Vector.ZERO, polyline, TerrainType.NORMAL);

        Terrain terrain2 = new Terrain(this.game, Vector.ZERO, polygon, TerrainType.NORMAL);

        Obstacle obstacle1 = new Obstacle(this.game, new Vector(55, -12), new Polygon(0.48f, 0.f, 0.f, -0.66f, 0.12f, -1.6f, 0.f, -2.5f,
                0.42f, -3.46f, 0.14f, -4.14f, 0.14f, -5.1f, 0.56f, -5.7f, 0.16f, -6.38f, 0.38f, -7.16f,
                0.2f, -8.24f, 0.52f, -9.02f, 0.22f, -11.56f, 0.34f, -12.58f, 0.18f, -13.84f, 1.34f, -14.6f,
                4.4f, -14.44f, 5.06f, -14.8f, 5.42f, -14.64f, 7.94f, -14.26f, 9.62f, -13.64f, 11.38f, -13.04f,
                13.92f, -12.66f, 17.364f, -13.09f, 19.031f, -11.12f, 19.947f, -9.290f, 19.892f, -7.484f, 20.058f, -5.623f,
                19.503f, -4.123f, 20.336f, -2.789f, 19.725f, -0.900f, 18.f, 0.f, 14.475f, -0.067f, 10.76f, 0.f,
                8.56f, -0.12f, 6.24f, 0.12f, 2.72f, -0.36f));

        Obstacle obstacle2 = new Obstacle(this.game, new Vector(22, 22), new Polygon(0.f, 0.f, -1.76f, 0.36f, -3.04f, 0.82f, -3.94f, 1.3f,
                -4.72f, 2.16f, -5.f, 3.f, -4.94f, 4.28f, -4.68f, 5.14f, -5.f, 6.f, -4.68f, 7.06f,
                -5.f, 8.f, -4.54f, 8.86f, -5.02f, 10.44f, -5.f, 11.f, 1.f, 11.f, 1.f, 16.f,
                -5.f, 16.f, -5.f, 17.f, -4.9f, 17.54f, -4.72f, 18.26f, -4.78f, 18.96f, -4.72f, 19.94f,
                -4.44f, 21.32f, -4.72f, 22.26f, -4.1f, 23.58f, -2.7f, 23.96f, -1.14f, 23.96f, 4.16f, 23.96f,
                5.38f, 23.76f, 7.36f, 23.8f, 9.4f, 23.94f, 11.7f, 23.54f, 12.24f, 23.62f, 12.76f, 23.64f,
                14.f, 24.f, 17.34f, 23.6f, 18.02f, 23.34f, 18.f, 22.f, 18.362f, 19.871f, 18, 18, 18, 14f,
                37, 14, 37f, 18, 38.360f, 24.388f, 40.f, 26.f, 62.f, 26.f, 63.046f, 23.191f, 61.077f, 20.180f,
                62.351f, 18.173f, 61.424f, 15.586f, 62.390f, 14.119f, 61.086f, 11.224f, 62.119f, 9.1781f, 62.360f, 7.3250f,
                62.051f, 5.5492f, 63.239f, 4.4296f, 60.807f, 0.8779f, 59.400f, 0.f, 54.844f, 0.4147f, 50.906f, -1.168f,
                46.814f, 0.4919f, 42.684f, -0.936f, 38.012f, 0.3375f, 32.762f, -0.318f, 28.786f, 1.1482f, 22.570f, -0.627f,
                20.061f, 0.8393f, 15.583f, 0.3375f, 11.645f, -1.361f, 10.448f, 1.4184f, 5.8932f, -0.704f));

        TriggeredPlatform platformDoor = new TriggeredPlatform(this.game, new Vector(-2, -1), null, 0, 0, 0, 0, 0, new Polygon(0, 0, 1, 0, 1, 7, 0, 7),1, 7);

        Checkpoint checkpoint1 = new Checkpoint(this.game, new Vector(45, -12));

        TriggeredPlatform platform1 = new TriggeredPlatform(this.game, new Vector(83, -18), null, 0,
                0, 0, 0, 0, new Polygon(0, 0, 5, 0, 5, 1, 0, 1), 5, 1);
        //GravityWell well = new GravityWell(this.game, new Vector(75, -8), new Vector(0, 0.06f), new Polygon(0, 0, 7, 0, 7, -32, 0, -32), (float) Math.PI * 1.5f);

        TriggeredPlatform platform2 = new TriggeredPlatform(this.game, new Vector(77, -28), null, 0,
                0, 0, 0, 0, new Polygon(0, 0, 5, 0, 5, 1, 0, 1), 5, 1);

        Checkpoint checkpoint2 = new Checkpoint(this.game, new Vector(60, -35.25f));

        TriggeredPlatform platform3 = new TriggeredPlatform(this.game, new Vector(50, -36), new Vector(0, 1), 50, 28, 5, 2, 2);
        TriggeredPlatform platform4 = new TriggeredPlatform(this.game, new Vector(50, -14), new Vector(-1, 0), 5, 1, 1000, 9, 2);

        GraphicalDrawer drawer = new GraphicalDrawer();
        drawer.addGraphics(new ShapeGraphics(new Polygon(0, 0, 70, 0, 70, 30, 0, 30), Color.decode("#d3d3d3"), null, 0, 1, DepthValue.BACKGROUND_LOW.value), new Vector(-25, -16));

        BoumBarder boumbarder1 = new BoumBarder(this.game, new Vector(55, 10));
        boumbarder1.setPath(new Vector(55, 10), new Vector(70, 10), 2);

        Lever lever1 = new Lever(this.game, new Vector(57, -35));
        lever1.addAction(() -> {
            platform3.triggerAction();
            platform4.triggerAction();
        }, 10);

        Checkpoint checkpoint3 = new Checkpoint(this.game, new Vector(26, 15));

        TriggeredPlatform platform5 = new TriggeredPlatform(this.game, new Vector(20, 14), new Vector(0, -1),
                11, 3, 10000, 1, 1, new Polygon(0, 0, 1, 0, 1, 9, 0, 9), 1, 9);

        TriggeredPlatform platform6 = new TriggeredPlatform(this.game, new Vector(6, 16), null, 0,0,0,0,0, new Polygon(0, 0, 5, 0, 5, 3, 0, 3), 5, 3);

        TriggeredPlatform platform7 = new TriggeredPlatform(this.game, new Vector(12.5f, 25), null, 0, 0, 0,0 ,0);

        TriggeredPlatform platform8 = new TriggeredPlatform(this.game, new Vector(3, 31), null, 0, 0, 0,0, 0);

        TriggeredPlatform platform9 = new TriggeredPlatform(this.game, new Vector(18, 32), new Vector(0, -1),
                7, 3, 10000, 1, 1, new Polygon(0, 0, 1, 0, 1, 7, 0, 7), 1, 7);

        TriggeredPlatform platform10 = new TriggeredPlatform(this.game, new Vector(12.5f, 38), null, 0, 0, 0, 0, 0);

        Laser laser1 = new Laser(this.game, new Vector(12, 54.5f), 38.35f, 3, false);
        Laser laser2 = new Laser(this.game, new Vector(3.5f, 23), 15.5f, 0, false);
        Laser laser3 = new Laser(this.game, new Vector(4.5f, 43), 14.25f, 0, false);

        Lever lever2 = new Lever(this.game, new Vector(23, 15));
        lever2.addAction(() -> {
            platform5.triggerAction();
            laser1.switchState();
            laser2.switchState();
            laser3.switchState();
        }, 500);

        Lever lever3 = new Lever(this.game, new Vector(5, 32));
        lever3.addAction(() -> platform9.triggerAction(), 500);

        TriggeredPlatform platform11 = new TriggeredPlatform(this.game, new Vector(26, 45), new Vector(0, 1),
                12, 3, 10, 1, 1, new Polygon(0, 0, 1, 0, 1, 12, 0, 12), 1, 12);

        Lever lever4 = new Lever(this.game, new Vector(24, 46));
        lever4.addAction(() -> {
            platform11.triggerAction();
            laser1.switchState();
            laser2.switchState();
            laser3.switchState();
        }, 500);

        Checkpoint checkpoint4 = new Checkpoint(this.game, new Vector(30, 45.75f));

        BoumBarder boumbarder2 = new BoumBarder(this.game, new Vector(35, 70), new Vector(59, 70), .5f, .4f, 1f, .65f);

        BoumBarder boumbarder3 = new BoumBarder(this.game, new Vector(62, 55), new Vector(80, 70), .5f, .4f, 1f, .65f);

        TriggeredPlatform platform12 = new TriggeredPlatform(this.game, new Vector(39.5f, 44.5f), null, 0, 0, 0, 0, 0);

        Liquid liquid = new Liquid(this.game, new Vector(40, 36), new Polygon(0, 0, 19, 0, 19, 4, 0, 4), true);

        FinishActor finishActor = new FinishActor(this.game, new Vector(115, 50));

        Scenery scenery = new Scenery(this.game, Preset.Ash);

        Coin smallCoin1 = new Coin(this.game, new Vector(58, -10), false);
        Coin smallCoin2 = new Coin(this.game, new Vector(60, -10), false);
        Coin smallCoin3 = new Coin(this.game, new Vector(62, -10), false);
        Coin smallCoin4 = new Coin(this.game, new Vector(50.5f, 2), false);
        Coin smallCoin5 = new Coin(this.game, new Vector(53.5f, 8), false);
        Coin smallCoin6 = new Coin(this.game, new Vector(50.5f, 14), false);
        Coin bigCoin1 = new Coin(this.game, new Vector(19, 35), true);
        Coin bigCoin2 = new Coin(this.game, new Vector(21, 35), true);
        Coin smallCoin7 = new Coin(this.game, new Vector(44, 52), false);
        Coin smallCoin8 = new Coin(this.game, new Vector(48, 53), false);
        Coin smallCoin9 = new Coin(this.game, new Vector(52, 53), false);
        Coin smallCoin10 = new Coin(this.game, new Vector(56, 52), false);

        addActors(new ArrayList<>(Arrays.asList(terrain1, terrain2, obstacle1, obstacle2)));
        addActor(drawer);
        addActors(new ArrayList<>(Arrays.asList(checkpoint1, checkpoint2, checkpoint3, checkpoint4)));
        addActors(new ArrayList<>(Arrays.asList(platformDoor, platform1, platform2, platform3, platform4, platform5, platform6, platform7,
                platform8, platform9, platform10, platform11, platform12)));
        addActors(new ArrayList<>(Arrays.asList(lever1, lever2, lever3, lever4)));
        addActors(new ArrayList<>(Arrays.asList(laser1, laser2, laser3)));
        addActors(new ArrayList<>(Arrays.asList(boumbarder1, boumbarder2, boumbarder3)));
        addActors(new ArrayList<>(Arrays.asList(smallCoin1, smallCoin2, smallCoin3, smallCoin4, smallCoin5, smallCoin6,
                smallCoin7, smallCoin8, smallCoin9, smallCoin10)));
        addActor(bigCoin1); addActor(bigCoin2);
        addActor(liquid);
        addActor(finishActor);
        addActor(scenery);
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

package main.game.levels;

import main.game.ActorGame;
import main.game.actor.entities.*;
import main.game.actor.entities.collectable.Coin;
import main.game.actor.entities.switchers.SimpleLever;
import main.game.actor.sensors.Checkpoint;
import main.game.actor.sensors.FinishActor;
import main.game.actor.sensors.SpawnCheckpoint;
import main.game.audio.Audio;
import main.game.graphicalStuff.Preset;
import main.game.graphicalStuff.Scenery;
import main.game.graphics.BetterTextGraphics;
import main.game.graphics.GraphicalDrawer;
import main.math.Polygon;
import main.math.Polyline;
import main.math.Vector;

/** The Tutorial Level
 */
public class Level0 extends Level {
    private Audio backgroundAudio;

    public Level0(ActorGame game) {
        super(game);
    }

    @Override
    public void createAllActors() {
        Polyline groundBody = new Polyline(-30, -1000,
                -30.f, -7.f,
                -5, 0,
                20.f, 0.f,
                22.5f, -9.f,
                60.f, -7.f,
                61.f, -4.f,
                75.f, -3.f,
                88, 6,
                100.f, 6.f,
                100.f, -2.f,
                130.f, -2.f,
                130.f, 7.f,
                150.f, 7.f,
                150.f, 11.f,
                200.f, 11.f,
                200, -1000);

        Terrain terrain = new Terrain(this.game, null, groundBody, TerrainType.NORMAL);

        Obstacle obstacle = new Obstacle(this.game, new Vector(-30, -7), new Polygon(0.f, -7.f,
                25.f, 0.f, 23.440f, 4.2403f, 24.159f, 5.2519f, 23.440f, 6.8225f, 21.603f, 8.3664f, 23.413f, 11.792f,
                22.535f, 15.013f, 24.372f, 18.314f, 24.744f, 21.934f, 22.428f, 23.531f, 18.116f, 24.144f,
                2.8457f, 23.851f, -0.534f, 21.854f, 0.f, 19.272f));

        Obstacle obstacle2 = new Obstacle(this.game, new Vector(75, -3), new Polygon(0.f, 0.f, 2.62f, 0.74f,
                5.1f, 2.8f, 7.f, 3.f, 7.86f, 4.18f, 9.62f, 4.74f, 10.54f, 5.8f, 12.08f, 6.24f, 12.62f, 7.84f,
                13.f, 9.f, 11.64f, 9.12f, 9.5f, 9.44f, 7.64f, 10.4f, 5.5f, 11.36f, 3.f, 11.66f, 0.66f, 11.76f,
                -1.f, 12.f, -0.58f, 9.3f, 0.f, 8.f, 0.18f, 6.64f, 0.24f, 5.06f, 0.3f, 3.66f));

        TriggeredPlatform platform = new TriggeredPlatform(this.game, new Vector(62, 3), new Vector(0, 0),
                0, 0, 0, 0, 0, new Polygon(.0f, .0f, 6.f, .0f, 6.f, 1.f, .0f, 1.f), 6, 1);

        Terrain muddyTerrain = new Terrain(this.game, new Vector(130, 7), new Polygon(0, 0, 20, 4, 20, 0), TerrainType.MUD);
        SimpleLever lever = new SimpleLever(this.game, new Vector(94, 6));

        TriggeredPlatform triggeredPlatform = new TriggeredPlatform(this.game, new Vector(100, 5), new Vector(1, 0),
                25, 14, 3, 4, 10);
        lever.addAction(() -> triggeredPlatform.triggerAction(), 2);

        Coin coin = new Coin(this.game, new Vector(114.5f, 7), false);
        Liquid lava = new Liquid(this.game, new Vector(100, -2), new Polygon(0, 0, 30, 0, 30, 2, 0, 2), true);
        FinishActor finish = new FinishActor(this.game, new Vector(175, 11));

        GraphicalDrawer graphicsDrawer = new GraphicalDrawer();

        Checkpoint checkpoint1 = new Checkpoint(this.game, new Vector(62, -4));
        Checkpoint checkpoint2 = new Checkpoint(this.game, new Vector(88, 6));

        BetterTextGraphics btgForward, btgBrake, btgTilt, btgJump, btgSpace, btgDoubleJump, btgLever, btgPlatform, btgLava, btgCoin, btgMud, btgFinish;

        btgForward = new BetterTextGraphics(this.game, "Advance with W", .75f, new Vector(0,6), .6f);
        btgBrake = new BetterTextGraphics(this.game, "Brake with S",.75f, new Vector(2, 5), .6f);
        btgTilt = new BetterTextGraphics(this.game, "Tilt with A, D", .75f, new Vector(22, 4), .6f);
        btgJump = new BetterTextGraphics(this.game, "Jump with Q", .75f, new Vector(48, -6), .6f);
        btgSpace = new BetterTextGraphics(this.game, "Change orientation with space", .75f, new Vector(55, 0), .6f);
        btgDoubleJump = new BetterTextGraphics(this.game, "Reach for the skies ! Press Q twice.", .75f, new Vector(52, 5), .6f);
        btgLever = new BetterTextGraphics(this.game, "Levers can be activated by pressing E", .75f, new Vector(88, 9), .6f);
        btgPlatform = new BetterTextGraphics(this.game, "Keep on moving to stay on track !", .75f, new Vector(100, 10), .6f);
        btgLava = new BetterTextGraphics(this.game, "The Bee Gees on fire ! Burnin' alive !", .75f, new Vector(105, 1), .6f);
        btgCoin = new BetterTextGraphics(this.game, "Collectibles are awesome !", .75f, new Vector(110  , 9), .6f);
        btgMud = new BetterTextGraphics(this.game, "Mud, good for pies, less so for traction !", .75f, new Vector(130, 13), .6f);
        btgFinish = new BetterTextGraphics(this.game, "You haven't died yet ! Or have you ?", .75f, new Vector(155, 13), .6f);
        
        graphicsDrawer.addGraphics(btgForward);
        graphicsDrawer.addGraphics(btgBrake);
        graphicsDrawer.addGraphics(btgTilt);
        graphicsDrawer.addGraphics(btgJump);
        graphicsDrawer.addGraphics(btgSpace);
        graphicsDrawer.addGraphics(btgDoubleJump);
        graphicsDrawer.addGraphics(btgLever);
        graphicsDrawer.addGraphics(btgPlatform);
        graphicsDrawer.addGraphics(btgLava);
        graphicsDrawer.addGraphics(btgCoin);
        graphicsDrawer.addGraphics(btgMud);
        graphicsDrawer.addGraphics(btgFinish);

        //this.backgroundAudio = new Audio("./res/audio/chiptune_energetic.wav", 10f);

        Scenery scenery = new Scenery(this.game, Preset.Breezy);

        this.addActor(terrain);
        this.addActor(obstacle);
        this.addActor(obstacle2);
        this.addActor(muddyTerrain);
        this.addActor(checkpoint1);
        this.addActor(platform);
        this.addActor(checkpoint2);
        this.addActor(lever);
        this.addActor(triggeredPlatform);
        this.addActor(coin);
        this.addActor(lava);
        this.addActor(finish);
        this.addActor(scenery);
        this.addActor(graphicsDrawer);
    }

    @Override
    public void dispose() {
        if (this.backgroundAudio != null) this.backgroundAudio.destroy();
    }

    @Override
    public boolean isFinished() {
        return getPayload() != null && getPayload().getVictoryStatus() | getPayload().getDeathStatus();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
    }

	@Override
	public SpawnCheckpoint getSpawnCheckpoint() {
		return new SpawnCheckpoint(this.game, new Vector(1, 0));
	}
}

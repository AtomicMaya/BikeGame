package main.game.levels;

import main.game.ActorGame;
import main.game.actor.entities.*;
import main.game.actor.entities.collectable.Coin;
import main.game.actor.entities.switchers.SimpleLever;
import main.game.actor.sensors.FinishActor;
import main.game.actor.sensors.SpawnCheckpoint;
import main.game.audio.Audio;
import main.game.graphicalStuff.Preset;
import main.game.graphicalStuff.Scenery;
import main.game.graphics.BetterTextGraphics;
import main.math.Polygon;
import main.math.Polyline;
import main.math.Vector;
import main.window.Canvas;

/** The Tutorial Level
 */
public class Level0 extends Level {
    private Bike player;
    private BetterTextGraphics btgForward, btgBrake, btgTilt, btgJump, btgCheckpoin, btgSpace, btgDoubleJump, btgLever, btgPlatform, btgLava, btgCoin, btgMud, btgFinish;
    private Audio backgroundAudio;

    public Level0(ActorGame game) {
        super(game);
    }

    @Override
    public void createAllActors() {
    	System.out.println("init level");
        Polyline groundBody = new Polyline(-3, -1000,
                0.f, 0.f,
                20.f, 0.f,
                22.5f, -11.f,
                60.f, -9.f,
                61.f, -4.f,
                75.f, -3.f,
                74.f, 6.f,
                100.f, 6.f,
                100.f, -2.f,
                130.f, -2.f,
                130.f, 7.f,
                150.f, 7.f,
                150.f, 11.f,
                200.f, 11.f,
                200, -1000);

        Terrain terrain = new Terrain(this.game, null, groundBody, TerrainType.NORMAL);
        TriggeredPlatform platform = new TriggeredPlatform(this.game, new Vector(62, 3), new Vector(0, 0),
                0, 0, 0, 0, 0, new Polygon(.0f, .0f, 6.f, .0f, 6.f, 1.f, .0f, 1.f), 6, 1);

        Terrain muddyTerrain = new Terrain(this.game, new Vector(130, 7), new Polygon(0, 0, 20, 4, 20, 0), TerrainType.MUD);
        this.player = new Bike(this.game, new Vector(1, 2));
        SimpleLever lever = new SimpleLever(this.game, new Vector(94, 6));

        TriggeredPlatform triggeredPlatform = new TriggeredPlatform(this.game, new Vector(100, 5), new Vector(1, 0),
                25, 14, 3, 4, 10);
        lever.addAction(() -> triggeredPlatform.triggerAction(), 2);

        Coin coin = new Coin(this.game, new Vector(114.5f, 7), false);
        Liquid lava = new Liquid(this.game, new Vector(100, -2), new Polygon(0, 0, 30, 0, 30, 2, 0, 2), true);
        FinishActor finish = new FinishActor(this.game, new Vector(175, 11));




        this.btgForward = new BetterTextGraphics(this.game, "Advance with W", .75f, new Vector(1,6), .6f);
        this.btgBrake = new BetterTextGraphics(this.game, "Brake with S",.75f, new Vector(3, 4), .6f);
        this.btgTilt = new BetterTextGraphics(this.game, "Tilt with A, D", .75f, new Vector(22, 4), .6f);
        this.btgJump = new BetterTextGraphics(this.game, "Jump with Q", .75f, new Vector(50, -7), .6f);
        this.btgSpace = new BetterTextGraphics(this.game, "Change orientation with space", .75f, new Vector(55, -1), .6f);
        this.btgDoubleJump = new BetterTextGraphics(this.game, "Reach for the skies ! Press Q twice.", .75f, new Vector(56, 8), .6f);
        this.btgLever = new BetterTextGraphics(this.game, "Levers can be activated by pressing E", .75f, new Vector(88, 9));
        this.btgPlatform = new BetterTextGraphics(this.game, "Keep on moving to stay on track !", .75f, new Vector(95, 10));
        this.btgLava = new BetterTextGraphics(this.game, "The Bee Gees on fire ! Burnin' alive !", .75f, new Vector(105, 1));
        this.btgCoin = new BetterTextGraphics(this.game, "Collectibles are awesome !", .75f, new Vector(108, 9));
        this.btgMud = new BetterTextGraphics(this.game, "Mud, good for pies, less so for traction !", .75f, new Vector(130, 13));
        this.btgFinish = new BetterTextGraphics(this.game, "You haven't died yet ! Or have you ?", .75f, new Vector(155, 13));

        this.backgroundAudio = new Audio("./res/audio/chiptune_energetic.wav", 10f);

        Scenery scenery = new Scenery(this.game, Preset.Breezy);

        this.addActor(terrain);
        this.addActor(muddyTerrain);
        this.addActor(this.player);
        this.addActor(platform);
        this.addActor(lever);
        this.addActor(triggeredPlatform);
        this.addActor(coin);
        this.addActor(lava);
        this.addActor(finish);
        this.addActor(scenery);
    }

    @Override
    public void dispose() {
        if (this.backgroundAudio != null) { this.backgroundAudio.destroy();System.out.println("destroy audio");}
        System.out.println("dispose");
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
    public void draw(Canvas canvas) {
        this.btgForward.draw(canvas);
        this.btgBrake.draw(canvas);
        this.btgTilt.draw(canvas);
        this.btgJump.draw(canvas);
        this.btgSpace.draw(canvas);
        this.btgDoubleJump.draw(canvas);
        this.btgLever.draw(canvas);
        this.btgPlatform.draw(canvas);
        this.btgLava.draw(canvas);
        this.btgCoin.draw(canvas);
        this.btgMud.draw(canvas);
        this.btgFinish.draw(canvas);
    }

	@Override
	public SpawnCheckpoint getSpawnCheckpoint() {
		return new SpawnCheckpoint(game, new Vector(1, 2), player);
	}
}

package main.game;

import main.game.GameObjects.Rectangle;
import main.game.actor.Audio;
import main.game.actor.crate.Crate;
import main.game.actor.entities.*;
import main.game.actor.graphicalStuff.Cloud;
import main.game.actor.graphicalStuff.Preset;
import main.game.actor.graphicalStuff.Scenery;
import main.game.levels.Level;
import main.io.FileSystem;
import main.math.Polygon;
import main.math.Polyline;
import main.math.Vector;
import main.window.Window;

import java.util.List;

public class TestGame extends ActorGame {
	private List<Level> levels;
	private FinishActor a;
	private ProximitySensor sensor;

	private Audio backgroundAudio, backgroundAudio2;
	private TriggeredPlatform platform;
    private ParticleEmitter emitter;
    private BetterTextGraphics betterTextGraphics;
    private Window window;
    private Cloud cloud;
    private Scenery scenery;
    private Bike player;

	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);

		// TODO creation objects du program
		//Polygon p1 = new Polygon(-10.f, 0.f, -5.f, 5.f, 0.f, -2.5f, -7.5f, -5.f);

        this.window = window;
		Polyline p = new Polyline(
				-1000.0f, -1000.0f,
				-1000.0f, 0.0f,
				0.0f, 0.0f,
				3.0f, 1.0f,
				8.0f, 1.0f,
				15.0f, 3.0f,
				20.0f, 3.0f,
				20.0f, -5.0f,
				35.0f, -5.0f,
				50.0f, -5.0f,
				55.0f, -4.0f,
				65.0f, 0.0f,
				6500.0f, -1000.0f
		);

		Ground ground = new Ground(this, null, p);

		player = new Bike(this, new Vector(4, 5));

		Polygon shape = new Polygon(.0f, .0f, 5.f, .0f, 5.f, 3.f, .0f, 3.f);
		//sensor = new KeyboardProximitySensor(this, new Vector(12, 3), shape, KeyEvent.VK_E);

        this.platform = new TriggeredPlatform(this, new Vector(20, 2), new Vector(1, 0),6,5, 2, 3, 2);

		SimpleLever lever = new SimpleLever(this, new Vector(12, 3));
		lever.addAction(() -> this.platform.triggerAction(), 1);
		lever.addAction(() -> this.setViewScaleModifier(20f), 1);

		//this.backgroundAudio = new Audio("./res/audio/chiptune_energetic.wav", 0.f);

       // BetterTextGraphics betterTextGraphics = new BetterTextGraphics(this, new Vector(-2, 2), "Test some random words", 6, 10, 3);
        Crate c1 = new Crate(this, new Vector(10, 7), "/res/images/box.1.enabled.png", true, 1);
        Crate c2 = new Crate(this, new Vector(11, 7), "/res/images/box.4.png", true, 1);
        Crate c3 = new Crate(this, new Vector(12, 7), "/res/images/box.1.enabled.png", true, 1);
        Crate c4 = new Crate(this, new Vector(13, 7), "/res/images/box.4.png", true, 1);
        Crate c5 = new Crate(this, new Vector(14, 7), "/res/images/box.1.enabled.png", true, 1);
        Crate c6 = new Crate(this, new Vector(15, 7), "/res/images/box.4.png", true, 1);
        Crate c7 = new Crate(this, new Vector(16, 7), "/res/images/box.1.enabled.png", true, 1);
        Crate c8 = new Crate(this, new Vector(17, 7), "/res/images/box.4.png", true, 1);
        Crate c9 = new Crate(this, new Vector(18, 7), "/res/images/box.1.enabled.png", true, 1);


		GraphicalButton button = new GraphicalButton(this, new Vector(.8f, 4.2f), "One does not simply code a video game !", 3.3f);
        //button.setNewGraphics("./res/images/button.white.1.png", "./res/images/button.white.1.png");
        //button.addOnClickAction(() -> player.character.triggerYayAnimation(), 5);



        //this.betterTextGraphics = new BetterTextGraphics(new Vector(0, 0), "Some text !", .5f, 5, 3);
        cloud = new Cloud(new Vector(3, 4), new Rectangle(2, 4), new Vector(1, 0));


		//Crate crate1 = new Crate(this, new Vector(6,5), "res/crate.1.png", false, 1);

        emitter = new ParticleEmitter(this, new Vector(0,6), 800, (float) Math.PI * 0.5f, 1f, 3, 0xFFFFFF00, 0xFFFF0000);

        System.out.println(Preset.Breezy.getObjectCount());

        sensor = new ProximitySensor(this, new Vector(0,0), shape);
        this.setViewScale(30f);

        //scenery = new Scenery(new Vector(0,0), this.getViewScale());

		/*
		Polygon s = new Polygon(0, 100, 1, 100, 1, -100, 0, -100);
		a = new FinishActor(this, new Vector(7, 0), player, s);
		*/
		//this.addActor(a);
		//this.addActor(crate1);
		this.setViewCandidate(player);
		this.setPayload(player);
		this.addActor(ground);
		this.addActor(player);
		this.addActor(sensor);
		this.addActor(lever);
		this.addActor(platform);
		this.addActor(button);
		this.addActor(emitter);
		this.addActor(c1);
		this.addActor(c2);
		this.addActor(c3);
		this.addActor(c4);
		this.addActor(c5);
		this.addActor(c6);
		this.addActor(c7);
		this.addActor(c8);
		this.addActor(c9);
		return true;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		//scenery.setViewPointPosition(player.getPosition(), this.getViewScale());
		//scenery.draw(window);
       // scenery.update(deltaTime);

	}

	@Override
	public void end() {
		//backgroundAudio.destroy();
		//backgroundAudio2.destroy();
	}
}
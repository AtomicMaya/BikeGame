package main.game.actor.entities.collectable;

import main.game.ActorGame;
import main.game.actor.entities.GameEntity;
import main.game.actor.entities.switchers.Switcher;
import main.game.actor.sensors.ProximitySensor;
import main.game.graphics.ImageGraphics;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;

public class Coin extends GameEntity implements Switcher {
    private ActorGame game;

    private ArrayList<ImageGraphics> graphics;
    private ProximitySensor sensor;
    private Polygon shape;
    private float elapsedAnimationTime;
    private final float animationTime;
    private int graphicsCounter;

    public Coin(ActorGame game, Vector position) {
        super(game, true, position);
        this.game = game;

        this.graphics = new ArrayList<>(Arrays.asList(this.addGraphics("./res/images/coin.gold.1.png", .5f, .5f),
                this.addGraphics("./res/images/coin.gold.2.png", .5f, .5f),
                this.addGraphics("./res/images/coin.gold.3.png", .5f, .5f),
                this.addGraphics("./res/images/coin.gold.4.png", .5f, .5f),
                this.addGraphics("./res/images/coin.gold.5.png", .5f, .5f),
                this.addGraphics("./res/images/coin.gold.6.png", .5f, .5f),
                this.addGraphics("./res/images/coin.gold.7.png", .5f, .5f),
                this.addGraphics("./res/images/coin.gold.8.png", .5f, .5f),
                this.addGraphics("./res/images/coin.gold.9.png", .5f, .5f),
                this.addGraphics("./res/images/coin.gold.10.png", .5f, .5f)));

        this.shape = new Polygon(0, 0, 1, 0, 1, 1, 0, 1);
        this.sensor = new ProximitySensor(game, position, shape);


        this.animationTime = 1.5f;
        this.graphicsCounter = 0;
        this.elapsedAnimationTime = 0;
    }

    @Override
    public void update(float deltaTime) {
        if (this.sensor.getSensorDetectionStatus()) {
            this.game.addToScore(30);
            this.destroy();
        }

        this.elapsedAnimationTime += deltaTime;
        if (this.elapsedAnimationTime > this.animationTime) this.elapsedAnimationTime = 0;
        this.graphicsCounter = (int) (this.elapsedAnimationTime / this.animationTime * this.graphics.size());

        this.sensor.update(deltaTime);
    }

    @Override
    public void reCreate(ActorGame game) {
       super.reCreate(game);
    }


    @Override
    public void draw(Canvas canvas) {
        graphics.get(graphicsCounter).draw(canvas);
    }

    @Override
    public void destroy() {
        this.sensor.destroy();
        super.destroy();
        this.game.destroyActor(this);
    }
}

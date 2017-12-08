package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ImageGraphics;
import main.game.actor.Runner;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;

public class Coin extends GameEntity implements Lever, Runner {
    private ActorGame game;

    private ArrayList<ImageGraphics> graphics;
    private ProximitySensor sensor;
    private Polygon shape;
    private ArrayList<Runnable> actions;
    private ArrayList<Float> time;
    private float timeToActionEnd, elapsedActionTime = 0.f, elapsedAnimationTime;
    private final float animationTime;
    private int graphicsCounter;
    private boolean isOccupied;

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

        this.actions = new ArrayList<>();
        this.time = new ArrayList<>();

        this.animationTime = 1.5f;
        this.graphicsCounter = 0;
        this.elapsedAnimationTime = 0;
    }

    @Override
    public void update(float deltaTime) {
        if (this.sensor.getSensorDetectionStatus() && !this.isOccupied) {
            for(int i = 0; i < this.actions.size(); i++) {
                this.runAction(this.actions.get(i), this.time.get(i));
            }
        }
        if (this.isOccupied) {
            this.elapsedActionTime += deltaTime;
            if (this.elapsedActionTime > this.timeToActionEnd) {
                this.isOccupied = false;
                this.elapsedActionTime = 0.f;
            }
        }

        this.elapsedAnimationTime += deltaTime;
        if (this.elapsedAnimationTime > this.animationTime) this.elapsedAnimationTime = 0;
        this.graphicsCounter = (int) (elapsedAnimationTime / this.animationTime * this.graphics.size());

        this.sensor.update(deltaTime);
    }

    @Override
    public void reCreate(ActorGame game) {
       //super.reCreate(game);
    }

    @Override
    public void addAction(Runnable action, float expirationTime) {
        this.actions.add(action);
        this.time.add(expirationTime);
    }

    @Override
    public void runAction(Runnable action, float expirationTime) {
        this.isOccupied = true;
        this.timeToActionEnd = expirationTime > this.timeToActionEnd ? expirationTime : this.timeToActionEnd;
        ParallelAction.generateWorker(action).execute();
    }

    @Override
    public void draw(Canvas canvas) {
        graphics.get(graphicsCounter).draw(canvas);
    }


}

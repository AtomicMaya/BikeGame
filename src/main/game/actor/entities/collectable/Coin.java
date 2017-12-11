package main.game.actor.entities.collectable;

import main.game.ActorGame;
import main.game.actor.entities.GameEntity;
import main.game.actor.sensors.ProximitySensor;
import main.game.graphics.ImageGraphics;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;

/** A collectible coin, that, when collected, gets added to the score. */
public class Coin extends GameEntity implements Collectable {
    /** The master {@linkplain ActorGame}.*/
    private ActorGame game;

    /** Contains the graphics. */
    private ArrayList<ImageGraphics> graphics;

    /** The affected {@linkplain ProximitySensor}. */
    private ProximitySensor sensor;

    /** The {@linkplain Polygon}, the geometric representation of thix {@linkplain Coin}. */
    private Polygon shape;

    /** The time that has already passed for the {@linkplain Coin}'s animation. */
    private float elapsedAnimationTime;

    /** The total animation time. */
    private final float animationTime = 1.5f;

    /** The current index for the {@linkplain ArrayList graphics} iteration. */
    private int graphicsCounter;

    /** If this {@linkplain Coin} is a special type of coin that gives a higher score bonus. */
    private boolean isBigCoin;

    /**
     * Creates a coin.
     * @param game The {@linkplain ActorGame} where this {@linkplain Coin} exists.
     * @param position This {@linkplain Coin}'s position {@linkplain Vector}.
     * @param isBigCoin Whether this {@linkplain Coin} is a special coin.
     */
    public Coin(ActorGame game, Vector position, boolean isBigCoin) {
        super(game, true, position);
        this.game = game;
        this.isBigCoin = isBigCoin;

        if(isBigCoin) {
            this.graphics = new ArrayList<>(Arrays.asList(
                    this.addGraphics("./res/images/coin.gold.big.1.png", 1.5f, 1.5f),
                    this.addGraphics("./res/images/coin.gold.big.2.png", 1.5f, 1.5f),
                    this.addGraphics("./res/images/coin.gold.big.3.png", 1.5f, 1.5f),
                    this.addGraphics("./res/images/coin.gold.big.4.png", 1.5f, 1.5f),
                    this.addGraphics("./res/images/coin.gold.big.5.png", 1.5f, 1.5f),
                    this.addGraphics("./res/images/coin.gold.big.6.png", 1.5f, 1.5f),
                    this.addGraphics("./res/images/coin.gold.big.7.png", 1.5f, 1.5f),
                    this.addGraphics("./res/images/coin.gold.big.8.png", 1.5f, 1.5f)
			));
            this.shape = new Polygon(0, 0, 1.5f, 0, 1.5f, 1.5f, 0, 1.5f);

        } else {
            this.graphics = new ArrayList<>(Arrays.asList(
                    this.addGraphics("./res/images/coin.gold.1.png", .75f, .75f),
                    this.addGraphics("./res/images/coin.gold.2.png", .75f, .75f),
                    this.addGraphics("./res/images/coin.gold.3.png", .75f, .75f),
                    this.addGraphics("./res/images/coin.gold.4.png", .75f, .75f),
                    this.addGraphics("./res/images/coin.gold.5.png", .75f, .75f),
                    this.addGraphics("./res/images/coin.gold.6.png", .75f, .75f),
                    this.addGraphics("./res/images/coin.gold.7.png", .75f, .75f),
                    this.addGraphics("./res/images/coin.gold.8.png", .75f, .75f),
                    this.addGraphics("./res/images/coin.gold.9.png", .75f, .75f),
                    this.addGraphics("./res/images/coin.gold.10.png", .75f, .75f)
            ));
            this.shape = new Polygon(0, 0, .75f, 0, .75f, .75f, 0, .75f);
        }

        this.sensor = new ProximitySensor(game, position, this.shape);
        this.graphicsCounter = 0;
        this.elapsedAnimationTime = 0;
    }

    @Override
    public void update(float deltaTime) {
        this.sensor.update(deltaTime);

        if (this.sensor.getSensorDetectionStatus()) {
            this.game.addToScore(this.isBigCoin ? 200 : 20);
            this.destroy();
        }

        this.elapsedAnimationTime += deltaTime;
        if (this.elapsedAnimationTime > this.animationTime) this.elapsedAnimationTime = 0;
        this.graphicsCounter = (int) (this.elapsedAnimationTime / this.animationTime * this.graphics.size());

    }

    @Override
    public void reCreate(ActorGame game) {
       super.reCreate(game);
    }

    @Override
    public void draw(Canvas canvas) {
        this.graphics.get(this.graphicsCounter).draw(canvas);
    }

    @Override
    public void destroy() {
        this.sensor.destroy();
        super.destroy();
        this.game.destroyActor(this);
    }
}

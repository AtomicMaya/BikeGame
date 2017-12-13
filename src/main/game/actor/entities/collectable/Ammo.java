package main.game.actor.entities.collectable;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.actor.entities.GameEntity;
import main.game.actor.sensors.ProximitySensor;
import main.game.graphics.ImageGraphics;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;

/** A collectible form of ammunition, that when collected, gets added to the players inventory. */
public class Ammo extends GameEntity implements Collectable {

    /** Used for save purposes. */
    private static final long serialVersionUID = 2177024134409652665L;

    /** The master {@linkplain ActorGame}. */
    private transient ActorGame game;

    /** Contains the graphics. */
    private transient ArrayList<ImageGraphics> graphics;

    /** The affected {@linkplain ProximitySensor}. */
    private transient ProximitySensor sensor;

    /** The {@linkplain Polygon}, the geometric representation of this {@linkplain Ammo}. */
    private transient Polygon shape;

    /** The time that has already passed for the {@linkplain Ammo}'s animation. */
    private transient float elapsedAnimationTime;

    /** The total animation time. */
    private transient final float animationTime = 1f;

    /** The current index for the {@linkplain ArrayList graphics} iteration. */
    private transient int graphicsCounter;

    /** If this {@linkplain Coin} is a special type of coin that gives a higher score bonus. */
    private boolean isRocketAmmo;

    /**
     * Creates a {@linkplain Ammo}.
     * @param game The master {@linkplain ActorGame}.
     * @param position This {@linkplain Ammo}'s position {@linkplain Vector}.
     * @param isRocketAmmo Whether this {@linkplain Ammo} is a special type of ammo.
     */
    public Ammo(ActorGame game, Vector position, boolean isRocketAmmo) {
        super(game, true, position);
        this.game = game;
        this.isRocketAmmo = isRocketAmmo;
        this.create();
    }

    /**
     * Actual creation of the parameters of the {@linkplain GameEntity}, not in the constructor to
     * avoid duplication with the method {@linkplain #reCreate(ActorGame)}.
     */
    private void create() {
        this.graphics = new ArrayList<>(Arrays.asList(this.addGraphics("./res/images/ammo.1.png", 1f, 1f),
                this.addGraphics("./res/images/ammo.2.png", 1f, 1f),
                this.addGraphics("./res/images/ammo.3.png", 1f, 1f),
                this.addGraphics("./res/images/ammo.4.png", 1f, 1f),
                this.addGraphics("./res/images/ammo.5.png", 1f, 1f),
                this.addGraphics("./res/images/ammo.6.png", 1f, 1f)));
        this.shape = new Polygon(0, 0, 1f, 0, 1f, 1f, 0, 1f);

        this.build(this.shape, -1, -1, true, ObjectGroup.SENSOR.group);
        this.sensor = new ProximitySensor(game, getPosition(), this.shape);
        this.graphicsCounter = 0;
        this.elapsedAnimationTime = 0;
    }

    @Override
    public void update(float deltaTime) {
        this.sensor.update(deltaTime);

        if (this.sensor.getSensorDetectionStatus()) {
            this.game.getPayload().addAmmo(this.isRocketAmmo?1:2, this.isRocketAmmo);
            this.destroy();
        }

        this.elapsedAnimationTime += deltaTime;
        if (this.elapsedAnimationTime > this.animationTime)
            this.elapsedAnimationTime = 0;
        this.graphicsCounter = (int) (this.elapsedAnimationTime / this.animationTime * this.graphics.size());
    }

    @Override
    public void reCreate(ActorGame game) {
        super.reCreate(game);
        this.create();
    }

    @Override
    public void destroy() {
        this.sensor.destroy();
        super.destroy();
        this.game.destroyActor(this);
    }

    @Override
    public void draw(Canvas canvas) {
        this.graphics.get(this.graphicsCounter).draw(canvas);
    }

}

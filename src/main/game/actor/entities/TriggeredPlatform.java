package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Linker;
import main.game.actor.entities.switchers.Lever;
import main.math.Circle;
import main.math.Polygon;
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;

/** A {@linkplain GenericPlatform} that can be triggered by a {@linkplain Lever}. */
public class TriggeredPlatform extends GameEntity {

    /** The displacement time. */
    private float loopTime, pauseTime;

    /** The elapsed time counter. */
    private float elapsedTime = 0.f;

    /** The speed to be given to the {@linkplain GenericPlatform}. */
    private float speed;
    private Platform platform;
    private Vector evolution;

    /** Whether this {@linkplain TriggeredPlatform} has been triggered. */
    private boolean triggered;

    /** The loop count. */
    private int maxLoops, currentLoopCount;

    /** The delay before this {@linkplain TriggeredPlatform} starts moving. */
    private float delay;

    /**
     * Creates a new {@linkplain TriggeredPlatform}.
     * @param game The master {@linkplain ActorGame}.
     * @param position The initial position {@linkplain Vector}.
     * @param evolution The initial evolution {@linkplain Vector}.
     * @param distance The distance this {@linkplain TriggeredPlatform} should move.
     * @param advancementTime The time it will take this {@linkplain TriggeredPlatform} to get to its end points.
     * @param pauseTime The time this {@linkplain TriggeredPlatform} will wait at its end points.
     * @param delay The delay before this {@linkplain TriggeredPlatform} starts its first movement.
     * @param loops The number of loops this {@linkplain TriggeredPlatform} should do.
     * @param shape The {@linkplain Shape} of this {@linkplain TriggeredPlatform}.
     * @param width The width of this {@linkplain TriggeredPlatform}.
     * @param height The height of this {@linkplain TriggeredPlatform}.
     */
    public TriggeredPlatform(ActorGame game, Vector position, Vector evolution, float distance, float advancementTime, float pauseTime, float delay, int loops, Shape shape, float width, float height) {
        super(game, false, position);
        this.loopTime = advancementTime;
        this.pauseTime = pauseTime;
        this.speed = distance / advancementTime;
        this.evolution = evolution;
        this.delay = delay;
        this.maxLoops = loops;
        this.currentLoopCount = 0;

        this.platform = new Platform(game, position, shape, width, height);

        this.build(new Circle(0.1f), 10f, -1, false);
        this.platform.setConstraint(Linker.attachPrismatically(game, this.getEntity(), this.platform.getEntity(), Vector.ZERO));
    }

    /**
     * Creates a new {@linkplain TriggeredPlatform} / Overloaded.
     * @param game The master {@linkplain ActorGame}.
     * @param position The initial position {@linkplain Vector}.
     * @param evolution The initial evolution {@linkplain Vector}.
     * @param distance The distance this {@linkplain TriggeredPlatform} should move.
     * @param advancementTime The time it will take this {@linkplain TriggeredPlatform} to get to its end points.
     * @param pauseTime The time this {@linkplain TriggeredPlatform} will wait at its end points.
     * @param delay The delay before this {@linkplain TriggeredPlatform} starts its first movement.
     * @param loops The number of loops this {@linkplain TriggeredPlatform} should do.
     */
    public TriggeredPlatform(ActorGame game, Vector position, Vector evolution, float distance, float advancementTime, float pauseTime, float delay, int loops) {
        this(game, position, evolution, distance, advancementTime, pauseTime, delay,
                loops, new Polygon(.0f, .0f, 5.f, .0f, 5.f, 1.f, .0f, 1.f), 5, 1);
    }

    @Override
    public void update(float deltaTime) {
        if (this.triggered) {
            this.elapsedTime += deltaTime;
            if (this.currentLoopCount < this.maxLoops) {
                if (this.delay < this.elapsedTime && this.elapsedTime < this.loopTime + this.delay) {
                    this.platform.setPosition(this.evolution.mul(this.speed * deltaTime, this.speed * deltaTime));
                } else if (this.loopTime + this.pauseTime + this.delay < this.elapsedTime && this.elapsedTime < 2 * this.loopTime + this.pauseTime + this.delay) {
                    this.platform.setPosition(this.evolution.mul(-this.speed * deltaTime, -this.speed * deltaTime));
                } else if (this.elapsedTime > 2 * (this.loopTime + this.pauseTime) + this.delay){
                    this.elapsedTime = 0.f;
                    this.currentLoopCount += 1;
                }
            } else {
                this.currentLoopCount = 0;
                this.triggered = false;
            }
        }
        this.platform.update(deltaTime);
    }

    @Override
    public void destroy() {
        this.platform.destroy();
        super.destroy();
        super.getOwner().destroyActor(this);
    }

    @Override
    public void draw(Canvas canvas) {
        this.platform.draw(canvas);
    }

    /** Allows for a {@linkplain Lever} to trigger this {@linkplain TriggeredPlatform}. */
    public void triggerAction() {
        this.triggered = true;
    }

    public void setSize(float width, float height) {

    }

}

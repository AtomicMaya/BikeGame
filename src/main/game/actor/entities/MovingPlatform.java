package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Linker;
import main.math.Circle;
import main.math.Polygon;
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;

/** A moving {@linkplain GenericPlatform}, on a loop. */
public class MovingPlatform extends GameEntity {
    
	
	/** */
	private static final long serialVersionUID = 8938371620409472520L;

	/** References to the {@linkplain MovingPlatform} displacement. */
    private float loopTime, pauseTime;

    /** Currently elapsed time. */
    private float elapsedTime = 0.f;

    /** The advancement speed of the {@linkplain GenericPlatform}. */
    private float speed;

    /** The associated {@linkplain GenericPlatform}. */
    private transient Platform platform;

    /** The unitary evolution {@linkplain Vector}. */
    private Vector evolution;

    /**
     * Creates a {@linkplain MovingPlatform}.
     * @param game The master {@linkplain ActorGame}.
     * @param position The initial position {@linkplain Vector}.
     * @param evolution The initial evolution {@linkplain Vector}.
     * @param distance The distance that the {@linkplain MovingPlatform} will cover.
     * @param advancementTime The time the {@linkplain MovingPlatform} will take to get to it's end point.
     * @param pauseTime The time it will wait before moving again.
     */
    public MovingPlatform(ActorGame game, Vector position, Vector evolution, float distance, float advancementTime,
                          float pauseTime) {
        super(game, false, position);
        this.loopTime = advancementTime;
        this.pauseTime = pauseTime;
        this.speed = distance / advancementTime;
        this.evolution = evolution;

        this.create();
    }

    /**
     * Actual creation of the parameters of the {@linkplain GameEntity}, not in the
     * constructor to avoid duplication with the method {@linkplain #reCreate(ActorGame)}
     */
    private void create() {
        Shape platformShape = new Polygon(.0f, .0f, 5.f, .0f, 5.f, 1.f, .0f, 1.f);
        this.platform = new Platform(getOwner(), getPosition(), platformShape);

        this.build(new Circle(0.1f), -1f, -1, false);
        this.platform.setConstraint(
                Linker.attachPrismatically(getOwner(), this.getEntity(), platform.getEntity(), Vector.ZERO));
    }

    @Override
    public void reCreate(ActorGame game) {
        super.reCreate(game);
        this.create();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.elapsedTime += deltaTime;
        if (0 < this.elapsedTime && this.elapsedTime < this.loopTime) {
            this.platform.setPosition(this.evolution.mul(this.speed * deltaTime, this.speed * deltaTime));
        } else if (this.loopTime + this.pauseTime < this.elapsedTime
                && this.elapsedTime < 2 * this.loopTime + this.pauseTime) {
            this.platform.setPosition(this.evolution.mul(-this.speed * deltaTime, -this.speed * deltaTime));
        } else if (this.elapsedTime > 2 * (this.loopTime + this.pauseTime)) {
            this.elapsedTime = 0.f;
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

}

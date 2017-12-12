package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.graphics.ShapeGraphics;
import main.math.BasicContactListener;
import main.math.Entity;
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.util.Random;

/** A Gravity Well, which affects all objects in the world space. */
public class GravityWell extends GameEntity {
    /** The associated {@linkplain BasicContactListener}*/
    private BasicContactListener listener;

    /** The associated {@linkplain ParticleEmitter}'s emission time. */
    private final float particleEmissionTime;

    /** Reference the {@linkplain GravityWell}'s graphical representation. */
    private ShapeGraphics graphics;

    /** References the elapsed time. */
    private float elapsedTime;

    /** The master {@linkplain ActorGame}. */
    private ActorGame game;

    /** The force {@linkplain Vector} to be applied to {@linkplain GameEntity}s that enter the {@linkplain GravityWell}. */
    private Vector force;

    /** The position {@linkplain Vector} of this {@linkplain GravityWell}. */
    private Vector position;

    /** References the {@linkplain Shape} of this {@linkplain GravityWell}. */
    private Shape shape;

    /** References the direction in which this {@linkplain GravityWell} points. */
    private float direction;

    /**
     * Creates a new {@linkplain GravityWell}.
     * @param game The master {@linkplain ActorGame}.
     * @param position The initial position {@linkplain Vector}.
     * @param appliedForce The force {@linkplain Vector}.
     * @param shape The initial {@linkplain Shape} of the {@linkplain GravityWell}.
     * @param direction The direction in which this {@linkplain GravityWell} points.
     */
    public GravityWell(ActorGame game, Vector position, Vector appliedForce, Shape shape, float direction) {
        super(game, true, position);

        this.game = game;
        this.position = position;

        this.particleEmissionTime = 3;
        this.elapsedTime = 0;
        this.force = appliedForce;
        this.shape = shape;
        this.direction = direction;
        this.build(shape, -1, -1, true, ObjectGroup.SENSOR.group);
        this.graphics = addGraphics(shape, Color.CYAN, null, 0, .1f, -2);
        this.listener = new BasicContactListener();
        this.addContactListener(this.listener);
    }

    @Override
    public void update(float deltaTime) {
        this.elapsedTime += deltaTime;

        if(this.elapsedTime > this.particleEmissionTime) {
            this.game.addActor(new ParticleEmitter(this.game, this.position.add(this.shape.sample(new Random())), null, 75,
                    this.direction, .05f, 1, .1f, 2, 0, 0xcc0000ff, 0x0000ffff, 2, 3));
            this.elapsedTime = 0;
        }

        if (this.listener.getEntities().size() > 0) {
            for (Entity entity : this.listener.getEntities())
                entity.applyImpulse(this.force, null);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        this.graphics.draw(canvas);
    }

    @Override
    public void destroy() {
        super.destroy();
        super.getOwner().destroyActor(this);
    }
}

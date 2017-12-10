package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.actor.sensors.ProximitySensor;
import main.game.graphics.ShapeGraphics;
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.util.Random;

/**
 * Created on 12/8/2017 at 7:27 PM.
 */
public class GravityWell extends GameEntity {
    private ProximitySensor sensor;
    private final float particleEmissionTime;
    private ShapeGraphics graphics;
    private float elapsedTime;
    private ActorGame game;
    private Vector force, position;
    private Shape shape;
    private float direction;

    public GravityWell(ActorGame game, Vector position, Vector appliedForce, Shape shape, float direction) {
        super(game, true, position);

        this.game = game;
        this.sensor = new ProximitySensor(game, position, shape);
        this.position = position;

        this.particleEmissionTime = 3;
        this.elapsedTime = 0;
        this.force = appliedForce;
        this.shape = shape;
        this.direction = direction;
        this.build(shape, -1, -1, true, ObjectGroup.SENSOR.group);
        this.graphics = addGraphics(shape, Color.CYAN, null, 0, .1f, 0);
    }

    @Override
    public void update(float deltaTime) {
        this.elapsedTime += deltaTime;

        if(this.elapsedTime > this.particleEmissionTime) {
            this.game.addActor(new ParticleEmitter(this.game, this.position.add(this.shape.sample(new Random())), null, 75,
                    this.direction, .05f, 1, .1f, 2, 0, 0xcc0000ff, 0x0000ffff, 2, 3));
            this.elapsedTime = 0;
        }

        this.sensor.update(deltaTime);
        if(this.sensor.getSensorDetectionStatus()) {
            ((GameEntity) this.game.getPayload()).getEntity().setVelocity(this.force);
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

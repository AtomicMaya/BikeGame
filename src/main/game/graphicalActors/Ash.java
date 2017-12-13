package main.game.graphicalActors;

import main.game.actor.DepthValue;
import main.math.Rectangle;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.util.Random;

/** Ash particles, that make the world a bit catastrophic. */
public class Ash implements GraphicalObjects {

    /** A {@linkplain String} that is the file path as referencefor drawing.*/
    private String graphics;

    /** {@linkplain Vector}s describing semi-physical attributes. */
    private Vector position, speed;

    /** The dimensions of the image. */
    private float length, height;

    /** The time till this {@linkplain Ash} resets */
    private float timeTillDeath, elapsedTime;

    /**
     * Creates a new {@linkplain Ash}.
     * @param position The position {@linkplain Vector}.
     * @param shape The {@linkplain Rectangle} shape of the {@linkplain Ash}.
     * @param speed The speed {@linkplain Vector} of this {@linkplain Ash}.
     */
    public Ash(Vector position, Rectangle shape, Vector speed) {
        this.graphics = "./res/images/ash.png";
        this.position = position;
        this.length = shape.getHeight();
        this.height = shape.getLength();
        this.speed = speed;
        this.timeTillDeath = new Random().nextFloat() * 5;
    }

    @Override
    public void update(float deltaTime) {
        this.position = this.position.add(this.speed.mul(deltaTime));
        this.elapsedTime += deltaTime;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawImage(canvas.getImage(this.graphics), Transform.I.translated(this.position).scaled(this.length, this.height), 1, DepthValue.BACKGROUND_DEEP.value);
    }

    @Override
    public void setPosition(Vector position) {
        this.position = position;
    }

    @Override
    public boolean getIfResets() {
        if (this.elapsedTime > this.timeTillDeath) {
            this.elapsedTime = 0;
            return true;
        } else
            return false;
    }

    @Override
    public Vector getPosition() {
        return this.position;
    }


    @Override
    public Vector getSpeed() {
        return this.speed;
    }
}

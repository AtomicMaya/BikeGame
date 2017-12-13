package main.game.graphicalActors;

import main.math.Rectangle;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

/** A Cloud, that whizzes by in the sky. */
public class Cloud implements GraphicalObjects {

    /** A {@linkplain String} that is the file path as referencefor drawing.*/
    private String graphics;

    /** {@linkplain Vector}s describing semi-physical attributes. */
    private Vector position, speed;

    /** The dimensions of the image. */
    private float length, height;

    /** The time till this {@linkplain BlowingLeaf} resets */
    private float timeTillDeath, elapsedTime;

    /**
     * Creates a new {@linkplain Cloud}.
     * @param position The position {@linkplain Vector}.
     * @param shape The {@linkplain Rectangle} shape of the {@linkplain Cloud}.
     * @param speed The speed {@linkplain Vector} of this {@linkplain Cloud}.
     */
    public Cloud(Vector position, Rectangle shape, Vector speed) {
        this.graphics = "./res/images/cloud.png";
        this.position = position;
        this.length = shape.getHeight();
        this.height = shape.getLength();
        this.speed = speed;
        this.timeTillDeath = 10;
    }

    @Override
    public void update(float deltaTime) {
        this.position = this.position.add(this.speed.mul(deltaTime));
        this.elapsedTime += deltaTime;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawImage(canvas.getImage(this.graphics), Transform.I.translated(this.position).scaled(this.length, this.height), 1, -20);
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

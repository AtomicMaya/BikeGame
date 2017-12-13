package main.game.graphicalActors;

import main.game.actor.DepthValue;
import main.math.Rectangle;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/** Draws a cute Snowflake */
public class Snowflake implements GraphicalObjects {

    /** An {@linkplain ArrayList} containing the file paths as references for animation.*/
    private ArrayList<String> file;

    /** The animation counter. */
    private int graphicsCounter;

    /** {@linkplain Vector}s describing semi-physical attributes. */
    private Vector position, speed;

    /** The dimensions of the image. */
    private float length, height;

    /** How long the animation has been taking place. */
    private float elapsedAnimationTime;

    /** The time that the animation will take to complete and reset. */
    private final float animationTime;

    /** The time till this {@linkplain Snowflake} resets */
    private float timeTillDeath, elapsedTime;

    /**
     * Creates a new {@linkplain Snowflake}.
     * @param position The position {@linkplain Vector}.
     * @param shape The {@linkplain Rectangle} shape of the {@linkplain Snowflake}.
     * @param speed The speed {@linkplain Vector} of this {@linkplain Snowflake}.
     */
    public Snowflake(Vector position, Rectangle shape, Vector speed) {
        this.file = new ArrayList<>();
        this.file.addAll(Arrays.asList("./res/images/snowflake.0.png", "./res/images/snowflake.1.png"));
        this.position = position;
        this.length = shape.getHeight();
        this.height = shape.getLength();
        this.speed = speed;
        this.animationTime = .1f;
        this.elapsedAnimationTime = 0;
        this.timeTillDeath = new Random().nextFloat() * 10;
    }

    @Override
    public void update(float deltaTime) {
        this.position = this.position.add(this.speed.mul(deltaTime));
        this.elapsedAnimationTime += deltaTime;
        if (this.elapsedAnimationTime > this.animationTime)
            this.elapsedAnimationTime = 0;
        this.graphicsCounter = ((int) (this.elapsedAnimationTime / this.animationTime * this.file.size()));
        this.elapsedTime += deltaTime;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawImage(canvas.getImage(this.file.get(this.graphicsCounter)), Transform.I.scaled(length, height).translated(this.position), .7f, DepthValue.BACKGROUND_MEDIUM.value);
    }

    @Override
    public Vector getSpeed() {
        return this.speed;
    }

    @Override
    public Vector getPosition() {
        return this.position;
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
}

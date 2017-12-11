package main.game.graphicalStuff;

import main.game.GameObjects.Rectangle;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class BlowingLeaf implements GraphicalObjects {
    private ArrayList<String> file;
    private int graphicsCounter, initialOffset;
    private Vector position, speed;
    private float length, height, elapsedAnimationTime;
    private final float animationTime;
    private float timeTillDeath, elapedTime;

    public BlowingLeaf(Vector position, Rectangle shape, Vector speed) {
        this.file = new ArrayList<>();
        this.file.addAll(Arrays.asList("./res/images/leaf.1.png", "./res/images/leaf.2.png", "./res/images/leaf.3.png",
                "./res/images/leaf.4.png", "./res/images/leaf.5.png", "./res/images/leaf.6.png", "./res/images/leaf.7.png",
                "./res/images/leaf.8.png", "./res/images/leaf.9.png", "./res/images/leaf.10.png"));
        this.position = position;
        this.length = shape.getHeight();
        this.height = shape.getLength();
        this.speed = speed;
        this.initialOffset = new Random().nextInt(this.file.size() - 1);
        this.animationTime = 1.05f;
        this.elapsedAnimationTime = 0;
        this.timeTillDeath = 10;
    }

    @Override
    public void update(float deltaTime) {
        this.position = this.position.add(this.speed.mul(deltaTime));
        this.elapsedAnimationTime += deltaTime;
        if (this.elapsedAnimationTime > this.animationTime)
            this.elapsedAnimationTime = 0;
        this.graphicsCounter = ((int) (this.elapsedAnimationTime / this.animationTime * this.file.size()) + this.initialOffset) % this.file.size();
        this.elapedTime += deltaTime;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawImage(canvas.getImage(this.file.get(this.graphicsCounter)), Transform.I.scaled(length, height).translated(this.position), .7f, -20);
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
        if (this.elapedTime > this.timeTillDeath) {
            this.elapedTime = 0;
            return true;
        } else
            return false;
    }
}
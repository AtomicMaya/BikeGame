package main.game.actor.graphicalStuff;

import main.game.GameObjects.Rectangle;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Created on 12/4/2017 at 5:28 PM.
 */
public class BlowingLeaf implements GraphicalObjects {
    private ArrayList<String> file;
    private int graphicsCounter, initialOffset;
    private Vector position, speed;
    private float length, height;
    private float animationTime, elapsedAnimationTime;

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
    }

    @Override
    public void update(float deltaTime) {
        this.position = this.position.add(this.speed.mul(deltaTime));
        this.elapsedAnimationTime += deltaTime;
        if (this.elapsedAnimationTime > this.animationTime) this.elapsedAnimationTime = 0;
       this.graphicsCounter = ((int) (elapsedAnimationTime / this.animationTime * this.file.size()) + this.initialOffset) % this.file.size();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawImage(canvas.getImage(this.file.get(this.graphicsCounter)), Transform.I.translated(this.position).scaled(this.length, this.height), 1, 0);
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
}
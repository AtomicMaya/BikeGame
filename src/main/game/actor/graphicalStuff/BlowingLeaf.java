package main.game.actor.graphicalStuff;

import main.game.GameObjects.Rectangle;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created on 12/4/2017 at 5:28 PM.
 */
public class BlowingLeaf implements GraphicalObjects {
    private ArrayList<String> file;
    private int graphicsCounter;
    private Vector position, speed;
    private float length, height;

    public BlowingLeaf(Vector position, Rectangle shape, Vector speed) {
        this.file = new ArrayList<>();
        this.file.addAll(Arrays.asList("./res/images/leaf.1.png", "./res/images/leaf.2.png", "./res/images/leaf.3.png",
                "./res/images/leaf.4.png", "./res/images/leaf.5.png", "./res/images/leaf.6.png", "./res/images/leaf.7.png",
                "./res/images/leaf.8.png", "./res/images/leaf.9.png", "./res/images/leaf.10.png"));
        this.position = position;
        this.length = shape.getHeight();
        this.height = shape.getLength();
        this.speed = speed;
        this.graphicsCounter = 0;
    }

    @Override
    public void callNextGraphics() {
        this.graphicsCounter += 1;
        if (this.graphicsCounter >= this.file.size() - 1) this.graphicsCounter = 0;
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
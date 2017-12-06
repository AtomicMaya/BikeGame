package main.game.actor.graphicalStuff;

import main.game.GameObjects.Rectangle;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

public class Cloud implements GraphicalObjects {
    private String file;
    private Vector position, speed;
    private float length, height;

    public Cloud(Vector position, Rectangle shape, Vector speed) {
        this.file = "./res/images/cloud.png";
        this.position = position;
        this.length = shape.getHeight();
        this.height = shape.getLength();
        this.speed = speed;
    }

    @Override
    public void update(float deltaTime) {
        this.position = this.position.add(this.speed.mul(deltaTime));
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawImage(canvas.getImage(this.file), Transform.I.translated(this.position).scaled(this.length, this.height), 1, 0);
    }

    @Override
    public Vector getSpeed() {
        return this.speed;
    }

    @Override
    public void setPosition(Vector position) {
        this.position = position;
    }

    @Override
    public Vector getPosition() {
        return position;
    }
}

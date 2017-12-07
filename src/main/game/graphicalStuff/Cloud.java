package main.game.graphicalStuff;

import main.game.ActorGame;
import main.game.GameObjects.Rectangle;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

public class Cloud implements GraphicalObjects {
    private String graphics;
    private Vector position, speed;
    private float length, height;
    private ActorGame game;

    public Cloud(ActorGame game, Vector position, Rectangle shape, Vector speed) {
        this.game = game;
        this.graphics = "./res/images/cloud.png";
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
        canvas.drawImage(canvas.getImage(this.graphics), Transform.I.translated(this.position).scaled(this.length, this.height), 1, -20);
    }

    @Override
    public void setPosition(Vector position) {
        this.position = position;
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

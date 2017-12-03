package main.game.actor.entities;

import main.game.ActorGame;
import main.math.Circle;
import main.math.Polygon;
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;

public class TriggeredPlatform extends GameEntity {
    private float loopTime, pauseTime;
    private float elapsedTime = 0.f;
    private float speed;
    private Platform platform;
    private Vector evolution;
    private boolean triggered;
    private int maxLoops, currentLoopCount;
    private float delay;

    public TriggeredPlatform(ActorGame game, Vector position, Vector evolution, float distance, float advancementTime, float pauseTime, float delay, int loops) {
        super(game, false, position);
        this.loopTime = advancementTime;
        this.pauseTime = pauseTime;
        this.speed = distance / advancementTime;
        this.evolution = evolution;
        this.delay = delay;
        this.maxLoops = loops;
        this.currentLoopCount = 0;

        Shape platformShape = new Polygon(.0f, .0f, 5.f, .0f, 5.f, 1.f, .0f, 1.f);
        platform = new Platform(game, position, platformShape);

        this.build(new Circle(0.1f), -1f, -1, false);
        platform.attach(this.getEntity(), Vector.ZERO);

        game.addActor(platform);
    }

    @Override
    public void update(float deltaTime) {
        if (triggered) {
            elapsedTime += deltaTime;
            if (this.currentLoopCount < this.maxLoops) {
                if (delay < elapsedTime && elapsedTime < loopTime + delay) {
                    platform.setPosition(evolution.mul(speed * deltaTime, speed * deltaTime));
                } else if (loopTime + pauseTime + delay < elapsedTime && elapsedTime < 2 * loopTime + pauseTime + delay) {
                    platform.setPosition(evolution.mul(-speed * deltaTime, -speed * deltaTime));
                } else if (elapsedTime > 2 * (loopTime + pauseTime) + delay){
                    elapsedTime = 0.f;
                    this.currentLoopCount += 1;
                }
            } else {
                this.currentLoopCount = 0;
                this.triggered = false;
            }
        }
        platform.update(deltaTime);
    }

    @Override
    public void destroy() {
        super.destroy();
        super.getOwner().destroyActor(this);
    }

    @Override
    public void draw(Canvas canvas) {
        platform.draw(canvas);
    }

    public void triggerAction() {
        this.triggered = true;
    }

}

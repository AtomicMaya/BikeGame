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
        this.platform = new Platform(game, position, platformShape);

        this.build(new Circle(0.1f), -1f, -1, false);
        this.platform.setConstraint(Linker.attachPrismatically(game, this.getEntity(), this.platform.getEntity(), Vector.ZERO));

        game.addActor(this.platform);
    }

    @Override
    public void update(float deltaTime) {
        if (this.triggered) {
            this.elapsedTime += deltaTime;
            if (this.currentLoopCount < this.maxLoops) {
                if (this.delay < this.elapsedTime && this.elapsedTime < this.loopTime + this.delay) {
                    this.platform.setPosition(this.evolution.mul(this.speed * deltaTime, this.speed * deltaTime));
                } else if (this.loopTime + this.pauseTime + this.delay < this.elapsedTime && this.elapsedTime < 2 * this.loopTime + this.pauseTime + this.delay) {
                    this.platform.setPosition(this.evolution.mul(-this.speed * deltaTime, -this.speed * deltaTime));
                } else if (this.elapsedTime > 2 * (this.loopTime + this.pauseTime) + this.delay){
                    this.elapsedTime = 0.f;
                    this.currentLoopCount += 1;
                }
            } else {
                this.currentLoopCount = 0;
                this.triggered = false;
            }
        }
        this.platform.update(deltaTime);
    }

    @Override
    public void destroy() {
        this.platform.destroy();
        super.destroy();
        super.getOwner().destroyActor(this);
    }

    @Override
    public void draw(Canvas canvas) {
        this.platform.draw(canvas);
    }

    public void triggerAction() {
        this.triggered = true;
    }

}

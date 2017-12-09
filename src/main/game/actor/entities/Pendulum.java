package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.Linker;
import main.game.actor.ObjectGroup;
import main.game.graphics.ImageGraphics;
import main.math.*;
import main.window.Canvas;

import java.awt.*;

/**
 * A Pendulum designed to kill the player.
 */
class Weight extends GameEntity {
    private ImageGraphics graphics;
    private RopeConstraint constraint;

    Weight(ActorGame game, Vector position) {
        super(game, false, position);

        this.build(new Circle(2), 1, 1, false, ObjectGroup.OBSTACLE.group);
        this.graphics = this.addGraphics("./res/images/stone.broken.11.png", 2f, 2f, new Vector(1, 1));
    }

    public void setConstraint(RopeConstraint constraint) {
        this.constraint = constraint;
    }

    @Override
    public void draw(Canvas canvas) {
        this.graphics.draw(canvas);
    }

    @Override
    public void destroy() {
        this.constraint.destroy();
        this.destroy();
        this.getOwner().destroyActor(this);
    }
}

/**
 * A Pendulum designed to kill the player.
 */
public class Pendulum implements Actor {
    private AnchorPoint anchorPoint;
    private Weight weight;
    private ActorGame game;


    public Pendulum(ActorGame game, Vector anchorPosition, Vector thisPosition, float length) {
        this.anchorPoint = new AnchorPoint(game, anchorPosition);
        this.game = game;

        this.weight = new Weight(game, thisPosition);
        this.weight.setConstraint(Linker.attachRope(game, this.anchorPoint.getEntity(), this.weight.getEntity(), Vector.ZERO, length));

    }

    @Override
    public void update(float deltaTime) {
        this.weight.update(deltaTime);
        this.anchorPoint.update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawShape(new Polyline(this.anchorPoint.getPosition(), this.weight.getPosition().sub(1, 1)),
                Transform.I, null, Color.decode("#8f5118"), .1f, 1, -10);
        this.anchorPoint.draw(canvas);
        this.weight.draw(canvas);
    }

    @Override
    public void destroy() {
        this.anchorPoint.destroy();
        this.weight.destroy();
        this.game.destroyActor(this);
    }

    @Override
    public Transform getTransform() {
        return Transform.I.translated(this.anchorPoint.getPosition());
    }

    @Override
    public Vector getPosition() {
        return this.anchorPoint.getPosition();
    }

    @Override
    public Vector getVelocity() {
        return Vector.ZERO;
    }
}

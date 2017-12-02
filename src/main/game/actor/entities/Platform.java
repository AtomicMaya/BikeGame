package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ImageGraphics;
import main.math.*;
import main.window.Canvas;

/**
 * Created on 12/2/2017 at 3:31 PM.
 */
public class Platform extends GameEntity {
    private Shape shape;
    private PrismaticConstraint constraint;
    private ImageGraphics graphics;


    public Platform(ActorGame game, Vector position, Shape shape) {
        super(game, true, position);
        this.shape = shape;

        this.build(shape, 100.f, 1.f, false);
        graphics = addGraphics("./res/images/stone.3.png", 5.f, 1.f);
    }

    protected void attach(Entity mobile, Vector anchor) {
        PrismaticConstraintBuilder builder = super.getOwner().createPrismaticConstraintBuilder();
        builder.setFirstEntity(mobile);
        builder.setSecondEntity(this.getEntity());
        builder.setFirstAnchor(anchor);
        builder.setSecondAnchor(Vector.ZERO);
        builder.setLimitEnabled(true);
        builder.setMotorEnabled(true);
        builder.setMotorSpeed(0);
        builder.setMotorMaxTorque(10f);
        builder.setLowerTranslationLimit(0);
        builder.setUpperTranslationLimit(0);
        builder.setAxis(Vector.Y);
        builder.setInternalCollision(false);
        constraint = builder.build();
    }

    public void setPosition(Vector vector) {
        this.getEntity().setPosition(this.getPosition().add(vector));
    }

    @Override
    public void destroy() {
        super.destroy();
        super.getOwner().destroyActor(this);
    }

    @Override
    public void draw(Canvas canvas) {
        graphics.draw(canvas);
    }
}

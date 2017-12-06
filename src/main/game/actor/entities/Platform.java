package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ImageGraphics;
import main.math.*;
import main.window.Canvas;

/**
 * A platform, can move.
 */
public class Platform extends GameEntity {
    private Shape shape;
    private PrismaticConstraint constraint;
    private ImageGraphics graphics;

    /**
     * Creates a new Platform
     * @param game : The game in which the platform exists
     * @param position : The position at which the platform is instantiated
     * @param shape : The shape of the platform
     */
    public Platform(ActorGame game, Vector position, Shape shape) {
        super(game, true, position);
        this.shape = shape;

        this.build(shape, 100.f, 1.f, false, 5);
        graphics = addGraphics("./res/images/stone.3.png", 5.f, 1.f);
    }

    /**
     * Attaches this platform to a moving entity
     * @param mobile : The moving entity
     * @param anchor : Where on the moving entity this platform should be anchored
     */
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

    /**
     * Sets the position of the platform
     * @param vector : The differential value between this objects position and it's previous position
     */
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

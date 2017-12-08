package main.game.actor;

import main.game.ActorGame;
import main.math.*;

/**
 * Created on 12/7/2017 at 1:08 AM.
 */
public class Linker {
    /**
     * Attaches this platform to a moving entity
     * @param game :
     * @param anchor : The moving entity
     *               @param attached :
     * @param anchorPoint : Where on the moving entity this platform should be anchored
     */
    public static PrismaticConstraint attachPrismatically(ActorGame game, Entity anchor, Entity attached, Vector anchorPoint) {
        PrismaticConstraintBuilder builder = game.createPrismaticConstraintBuilder();
        builder.setFirstEntity(anchor);
        builder.setSecondEntity(attached);
        builder.setFirstAnchor(anchorPoint);
        builder.setSecondAnchor(Vector.ZERO);
        builder.setLimitEnabled(true);
        builder.setMotorEnabled(true);
        builder.setMotorSpeed(0);
        builder.setMotorMaxTorque(10f);
        builder.setLowerTranslationLimit(0);
        builder.setUpperTranslationLimit(0);
        builder.setAxis(Vector.Y);
        builder.setInternalCollision(false);
        return builder.build();
    }

    public static WeldConstraint attachWeldilly(ActorGame game, Entity anchor, Entity attached, Vector anchorPoint,
                                                float referenceAngle, float frequency, float damping) {
        WeldConstraintBuilder builder = game.createWeldConstraintBuilder();
        builder.setFirstEntity(anchor);
        builder.setSecondEntity(attached);
        builder.setFirstAnchor(Vector.ZERO);
        builder.setSecondAnchor(anchorPoint);
        builder.setReferenceAngle(referenceAngle);
        builder.setFrequency(frequency);
        builder.setDamping(damping);
        builder.setInternalCollision(false);
        return builder.build();
    }
}

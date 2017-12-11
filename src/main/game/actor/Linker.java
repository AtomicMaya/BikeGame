package main.game.actor;

import main.game.ActorGame;
import main.math.*;

/**
 * Created on 12/7/2017 at 1:08 AM.
 */
public class Linker {
    /**
     * Attaches an {@linkplain Entity} to another using a {@linkplain PrismaticConstraint}.
     * @param game The master {@linkplain ActorGame}.
     * @param anchor The fixture {@linkplain Entity}.
     * @param attached The moving {@linkplain Entity}.
     * @param anchorPoint Where on the moving entity this {@linkplain PrismaticConstraint} should be anchored.
     * @return a new {@linkplain PrismaticConstraint}.
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

    /**
     * Attaches an {@linkplain Entity} to another using a {@linkplain WeldConstraint}.
     * @param game The master {@linkplain ActorGame}.
     * @param anchor The fixture {@linkplain Entity}.
     * @param attached The moving {@linkplain Entity}.
     * @param anchorPoint Where on the moving entity this {@linkplain WeldConstraint} should be anchored.
     * @param referenceAngle The reference angle.
     * @param frequency The {@linkplain WeldConstraint} frequency.
     * @param damping The {@linkplain WeldConstraint} damping.
     * @return a new {@linkplain WeldConstraint}.
     * */
    public static WeldConstraint attachWeldConstraint(ActorGame game, Entity anchor, Entity attached, Vector anchorPoint,
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

    /**
     * Attaches an {@linkplain Entity} to another using a {@linkplain RopeConstraint}.
     * @param game The master {@linkplain ActorGame}.
     * @param anchor The fixture {@linkplain Entity}.
     * @param attached The moving {@linkplain Entity}.
     * @param anchorPoint Where on the moving entity this {@linkplain RopeConstraint} should be anchored.
     * @param length The length of the rope.
     * @return a new {@linkplain RopeConstraint}.
     */
    public static RopeConstraint attachRope(ActorGame game, Entity anchor, Entity attached, Vector anchorPoint, float length) {
    	length = Math.abs(length);
        RopeConstraintBuilder builder = game.createRopeConstraintBuilder();
        builder.setFirstEntity(anchor);
        builder.setFirstAnchor(anchorPoint);
        builder.setSecondEntity(attached);
        builder.setSecondAnchor(Vector.ZERO);
        builder.setMaxLength(length);
        builder.setInternalCollision(true);
        return builder.build();
    }
}

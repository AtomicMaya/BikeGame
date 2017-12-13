package main.game.actor.entities;

import main.game.actor.DepthValue;
import main.game.graphics.Graphics;
import main.math.ExtendedMath;
import main.math.Shape;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;

/** Generates a graphical {@linkplain Particle}. */
public class Particle implements Graphics {
    /** {@linkplain java.util.Arrays} of {@linkplain Integer}s containing a 32-bit representation of an ARGB {@linkplain Color}. */
    private int[] currentColor, startColor, endColor;

    /** The {@linkplain Shape} of the {@linkplain Particle}. */
    private Shape shape;

    /** Variables concerning the life time of the particle. */
    private float lifeTime, elapsedLifeTime;

    /** Whether this {@linkplain Particle} will be destroyed the next time it updates. */
    private boolean flaggedForDestruction;

    /** {@linkplain Vector}s representing semi-physical properties. */
    private Vector position, speed, gravity;

    /** Color evolution modifiers. */
    private float modA, modR, modG, modB;

    /**
     * Creates a new {@linkplain Particle}.
     * @param position The initial position {@linkplain Vector}.
     * @param shape The {@linkplain Shape} of the {@linkplain Particle}.
     * @param startColor The start color of the gradient affecting the {@linkplain Particle}.
     * @param endColor The end color of the gradient affecting the {@linkplain Particle}.
     * @param lifeTime The time that the {@linkplain Particle} will remain alive.
     * @param speed The speed {@linkplain Vector} that helps propel this {@linkplain Particle}.
     * @param gravity In what sense this {@linkplain Particle} will be affected by real world gravity, often reduced to a fraction.
     */
    public Particle(Vector position, Shape shape, int startColor, int endColor, float lifeTime,
                    Vector speed, Vector gravity) {
        this.lifeTime = lifeTime;
        this.elapsedLifeTime = 0;
        this.flaggedForDestruction = false;
        this.position = position;
        this.speed = speed;
        this.gravity = gravity;

        this.startColor = new int[]{ExtendedMath.getAlpha(startColor), ExtendedMath.getRed(startColor), ExtendedMath.getGreen(startColor), ExtendedMath.getBlue(startColor)};
        this.endColor = new int[]{ExtendedMath.getAlpha(endColor), ExtendedMath.getRed(endColor), ExtendedMath.getGreen(endColor), ExtendedMath.getBlue(endColor)};
        this.currentColor = this.startColor.clone();

        // Checks that the start color element is bigger that the end color element, and modulates accordingly.
        this.modA = this.startColor[0] >= this.endColor[0] ? -1 : 1;
        this.modR = this.startColor[1] >= this.endColor[1] ? -1 : 1;
        this.modG = this.startColor[2] >= this.endColor[2] ? -1 : 1;
        this.modB = this.startColor[3] >= this.endColor[3] ? -1 : 1;

        this.shape = shape;
    }

    /**
     * @param color A 32-bit integer representation of a color.
     * @return a hex-{@linkplain String} representation of a 32-bit color. */
    private String getColor(int[] color) {
        return String.format("#%02x%02x%02x", ExtendedMath.validate8bit(color[1]),  ExtendedMath.validate8bit(color[2]),  ExtendedMath.validate8bit(color[3]));
    }

    /**
     * @param color A 32-bit integer representation of a color.
     * @return the alpha percentage of the given ARGB 32-bit color. */
    private float getAlpha(int[] color) {
        return color[0] / 255f;
    }

    /** @return whether this particle should be destroyed on the next update. */
    public boolean isFlaggedForDestruction() {
        return this.flaggedForDestruction;
    }


    public void update(float deltaTime) {
        this.elapsedLifeTime += deltaTime;

        // The remaining life of the particle, in %, used to calculate the evolution of the color gradient.
        float lifePercent = this.elapsedLifeTime / this.lifeTime;

        if (this.elapsedLifeTime > this.lifeTime) {
            this.flaggedForDestruction = true;
        }

        this.speed = this.speed.add(this.gravity.mul(deltaTime));
        this.position = this.position.add(this.speed.mul(deltaTime));

        this.currentColor = new int[] {
                (int) (this.startColor[0] + this.modA * Math.abs(this.startColor[0] - this.endColor[0]) * lifePercent),
                (int) (this.startColor[1] + this.modR * Math.abs(this.startColor[1] - this.endColor[1]) * lifePercent),
                (int) (this.startColor[2] + this.modG * Math.abs(this.startColor[2] - this.endColor[2]) * lifePercent),
                (int) (this.startColor[3] + this.modB * Math.abs(this.startColor[3] - this.endColor[3]) * lifePercent)
        };
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawShape(shape, Transform.I.translated(this.position), Color.decode(this.getColor(this.currentColor)), null, 0f, this.getAlpha(this.currentColor), DepthValue.FRONT_OBSTACLE_DEAP.value-.05f);
    }
}

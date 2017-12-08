package main.game.actor.entities;

import main.game.graphics.Graphics;
import main.math.ExtendedMath;
import main.math.Shape;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;

public class Particle implements Graphics {
    private int[] currentColor, startColor, endColor;
    private Shape shape;

    private float lifeTime, elapsedLifeTime;
    private boolean flaggedForDestruction;
    private Vector position, speed, gravity;
    private float modA, modR, modG, modB;


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

        this.modA = this.startColor[0] >= this.endColor[0] ? -1 : 1;
        this.modR = this.startColor[1] >= this.endColor[1] ? -1 : 1;
        this.modG = this.startColor[2] >= this.endColor[2] ? -1 : 1;
        this.modB = this.startColor[3] >= this.endColor[3] ? -1 : 1;

        this.shape = shape;
    }

    private String getColor(int[] color) {
        return String.format("#%02x%02x%02x", ExtendedMath.validate8bit(color[1]),  ExtendedMath.validate8bit(color[2]),  ExtendedMath.validate8bit(color[3]));
    }


    private float getAlpha(int[] color) {
        return color[0] / 255f;
    }

    public boolean isFlaggedForDestruction() {
        return this.flaggedForDestruction;
    }

    public void update(float deltaTime) {
        this.elapsedLifeTime += deltaTime;
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
        canvas.drawShape(shape, Transform.I.translated(this.position), Color.decode(this.getColor(this.currentColor)), null, 0f, this.getAlpha(this.currentColor), -0.05f);
    }
}

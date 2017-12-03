package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.QuickMafs;
import main.game.actor.ShapeGraphics;
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;

public class Particle extends GameEntity {
    private ShapeGraphics graphics;
    private int[] currentColor, startColor, endColor;
    private Shape shape;

    private float lifeTime, elapsedLifeTime;
    private boolean flaggedForDestruction;
    private Vector speed, gravity;
    private float modA, modR, modG, modB;


    public Particle(ActorGame game, Vector position, Shape shape, int startColor, int endColor, float lifeTime, Vector speed, Vector gravity) {
        super(game, true, position);
        this.lifeTime = lifeTime;
        this.elapsedLifeTime = 0;
        this.flaggedForDestruction = false;
        this.speed = speed;
        this.gravity = gravity;

        this.startColor = new int[]{QuickMafs.getAlpha(startColor), QuickMafs.getRed(startColor), QuickMafs.getGreen(startColor), QuickMafs.getBlue(startColor)};
        this.endColor = new int[]{QuickMafs.getAlpha(endColor), QuickMafs.getRed(endColor), QuickMafs.getGreen(endColor), QuickMafs.getBlue(endColor)};
        this.currentColor = this.startColor.clone();

        this.modA = this.startColor[0] >= this.endColor[0] ? -1 : 1;
        this.modR = this.startColor[1] >= this.endColor[1] ? -1 : 1;
        this.modG = this.startColor[2] >= this.endColor[2] ? -1 : 1;
        this.modB = this.startColor[3] >= this.endColor[3] ? -1 : 1;


        this.shape = shape;
        this.graphics = addGraphics(this.shape, Color.decode(this.getColor(this.currentColor)), null, 0f, this.getAlpha(this.currentColor), -0.05f);
        this.build(shape, -1, -1, true);

        game.addActor(this);
    }

    private String getColor(int[] color) {
        return String.format("#%02x%02x%02x", QuickMafs.validate8bit(color[1]),  QuickMafs.validate8bit(color[2]),  QuickMafs.validate8bit(color[3]));
    }


    private float getAlpha(int[] color) {
        return color[0] / 255f;
    }

    public boolean isFlaggedForDestruction() {
        return this.flaggedForDestruction;
    }

    @Override
    public void update(float deltaTime) {
        this.elapsedLifeTime += deltaTime;
        float lifePercent = 1 - this.elapsedLifeTime / this.lifeTime;

        if (this.elapsedLifeTime > this.lifeTime) {
            this.flaggedForDestruction = true;
        }

        this.speed = this.speed.add(this.gravity.mul(deltaTime));
        this.getEntity().setPosition(this.getPosition().add(this.speed.mul(deltaTime)));

        this.currentColor = new int[] {
                (int) (this.startColor[0] + this.modA * Math.abs(this.startColor[0] - this.endColor[0]) * lifePercent),
                (int) (this.startColor[1] + this.modR * Math.abs(this.startColor[1] - this.endColor[1]) * lifePercent),
                (int) (this.startColor[2] + this.modG * Math.abs(this.startColor[2] - this.endColor[2]) * lifePercent),
                (int) (this.startColor[3] + this.modB * Math.abs(this.startColor[3] - this.endColor[3]) * lifePercent)
        };

        this.graphics = addGraphics(this.shape, Color.decode(this.getColor(this.currentColor)), null, 0f, this.getAlpha(this.currentColor), -0.05f);
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

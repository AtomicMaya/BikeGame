package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.graphics.ImageGraphics;
import main.math.Polygon;
import main.math.Shape;
import main.math.Vector;
import main.math.WeldConstraint;
import main.window.Canvas;

public class TrampolinePlatform extends GameEntity {
    private ImageGraphics graphics;
    private float width, height;
    private WeldConstraint leftConstraint, rightConstraint;

    public TrampolinePlatform(ActorGame game, Vector anchorPosition, float width, float height) {
        super(game, false, anchorPosition);
        this.width = width;
        this.height = height;
        create();
    }

    private void create() {
        Shape shape = new Polygon(0, 0, this.width, 0, this.width, this.height, 0, this.height);
        this.build(shape, 1.f, 1f, false, ObjectGroup.TERRAIN.group);

        this.graphics = this.addGraphics("/res/images/wood.3.png", this.width, this.height, Vector.ZERO, .9f, 1);
    }

    @Override
    public void reCreate(ActorGame game) {
        super.reCreate(game);
        create();
    }

    @Override
    public void draw(Canvas canvas) {
        this.graphics.draw(canvas);
    }

    @Override
    public void destroy() {
        this.leftConstraint.destroy();
        this.rightConstraint.destroy();
        super.destroy();
        super.getOwner().destroyActor(this);
    }

    public void setLeftConstraint(WeldConstraint constraint) {
        this.leftConstraint = constraint;
    }

    public WeldConstraint getLeftConstraint() {
        return this.leftConstraint;
    }

    public void setRightConstraint(WeldConstraint constraint) {
        this.rightConstraint = constraint;
    }

    public WeldConstraint getRightConstraint() {
        return this.rightConstraint;
    }
}

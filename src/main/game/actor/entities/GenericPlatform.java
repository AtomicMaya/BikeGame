package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.graphics.ImageGraphics;
import main.math.Constraint;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

public class GenericPlatform extends GameEntity {
    private ImageGraphics graphics;
    private float width, height;
    private Constraint constraint;

    public GenericPlatform(ActorGame game, Vector anchorPosition, float width, float height) {
        super(game, false, anchorPosition);
        this.width = width;
        this.height = height;
        create();
    }

    private void create() {
        Polygon shape = new Polygon(0, 0, this.width, 0, this.width, this.height, 0, this.height);
        this.build(shape, 1.f, 1f, false, ObjectGroup.OBSTACLE.group);

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
        this.constraint.destroy();
        super.destroy();
        super.getOwner().destroyActor(this);
    }

    public void setConstraint(Constraint constraint) {
        this.constraint = constraint;
    }

    public Constraint getConstraint() {
        return this.constraint;
    }
}

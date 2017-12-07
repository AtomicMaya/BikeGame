package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ImageGraphics;
import main.math.Polygon;
import main.math.Shape;
import main.math.Vector;
import main.math.WeldConstraint;
import main.window.Canvas;

public class TrampolinePlatform extends GameEntity {
    private ImageGraphics graphics;
    private float width, height;
    private ActorGame game;
    private WeldConstraint constraint;

    public TrampolinePlatform(ActorGame game, Vector anchorPosition, float width, float height) {
        super(game, false, anchorPosition);
        this.game = game;
        this.width = width;
        this.height = height;
        create();
    }

    private void create() {
        Shape shape = new Polygon(0, 0, this.width, 0, this.width, this.height, 0, this.height);
        this.build(shape, 1.f, -1, false, CollisionGroups.TERRAIN.group);

        this.graphics = this.addGraphics("/res/images/wood.3.png", this.width, this.height);
    }

    @Override
    public void reCreate(ActorGame game) {
        super.reCreate(game);
        this.game = game;
        create();
    }

    @Override
    public void draw(Canvas canvas) {
        this.graphics.draw(canvas);
    }

    @Override
    public void destroy() {
        super.destroy();
        super.getOwner().destroyActor(this);
    }

    public void setConstraint(WeldConstraint constraint) {
        this.constraint = constraint;
    }
}

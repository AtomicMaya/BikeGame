package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.graphics.ImageGraphics;
import main.math.PrismaticConstraint;
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;

/** A platform that can move. */
public class Platform extends GameEntity {
    private Shape shape;
    private PrismaticConstraint constraint;
    private ImageGraphics graphics;

    public Platform(ActorGame game, Vector position, Shape shape, float width, float height) {
        super(game, true, position);
        this.shape = shape;

        this.build(shape, 100.f, 1.f, false, ObjectGroup.TERRAIN.group);
        this.graphics = addGraphics("./res/images/metal.3.png", width, height);
    }

    /**
     * Creates a new Platform
     * @param game : The game in which the platform exists
     * @param position : The position at which the platform is instantiated
     * @param shape : The shape of the platform
     */

    public Platform(ActorGame game, Vector position, Shape shape) {
        this(game, position, shape, 5, 1);
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
        this.graphics.draw(canvas);
    }

    public void setConstraint(PrismaticConstraint constraint) {
        this.constraint = constraint;
    }
}

package main.game.actor.entities;

import main.game.ActorGame;
import main.game.graphics.ImageGraphics;
import main.math.Circle;
import main.math.Vector;
import main.window.Canvas;

/** A Generic {@linkplain AnchorPoint} on which different objects can be placed. */
public class AnchorPoint extends GameEntity {
    /** The associated {@linkplain ImageGraphics}. */
    private ImageGraphics graphics;

    /**
     * Creates a new {@linkplain AnchorPoint}.
     * @param game The master {@linkplain ActorGame}.
     * @param position The associated position {@linkplain Vector}.
     */
    public AnchorPoint(ActorGame game, Vector position) {
        super(game, true, position);
        this.build(new Circle(.1f), -1, -1, true);
        this.graphics = this.addGraphics("./res/images/stone.broken.11.png", 1, 1,
                new Vector(0, 0), 1, 0);
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
}

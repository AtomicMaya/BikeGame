package main.game.actor.entities;

import main.game.ActorGame;
import main.game.graphics.ImageGraphics;
import main.math.Circle;
import main.math.Vector;
import main.window.Canvas;

/**
 * Created on 12/7/2017 at 9:28 AM.
 */
public class AnchorPoint extends GameEntity {
    private ImageGraphics graphics;
    public AnchorPoint(ActorGame game, Vector position) {
        super(game, true, position);
        this.build(new Circle(.1f), -1, -1, true);
        this.graphics = this.addGraphics("./res/images/stone.broken.11.png", 1, 1, new Vector(.5f, .5f), 1, 0);
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

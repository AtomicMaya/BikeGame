package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.DepthValue;
import main.game.graphics.ImageGraphics;
import main.math.Circle;
import main.math.Vector;
import main.window.Canvas;

/** A Generic {@linkplain AnchorPoint} on which different objects can be placed. */
public class AnchorPoint extends GameEntity {
	
	/** Used for save purpose */
	private static final long serialVersionUID = 3526526586018619136L;
	
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
                new Vector(.5f, .5f), 1, DepthValue.FRONT_OBSTACLE_MEDIUM.value);
        
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

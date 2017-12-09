package main.game.actor.sensors;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.actor.entities.GameEntity;
import main.game.graphics.ImageGraphics;
import main.math.*;
import main.window.Canvas;

/**
 * Generic Checkpoint to save progress.
 * Especially useful in crazy levels.
 */
public class Checkpoint extends GameEntity {
    private transient ActorGame game;
    private BasicContactListener contactListener;

    private ImageGraphics graphics;
    private Shape shape;

    public Checkpoint(ActorGame game, Vector position) {
        super(game, true, position);
        this.game = game;

        create();

    }

    private void create() {
        this.contactListener = new BasicContactListener();
        this.addContactListener(this.contactListener);

        this.shape = new Polygon(0, 0, 1, 0, 1, 10, 0, 10);
        this.graphics = this.addGraphics("./res/images/flag.red.png", 1, 1);
        build(this.shape, -1, -1, true, ObjectGroup.CHECKPOINT.group);
    }

    @Override
    public void reCreate(ActorGame game) {
        super.reCreate(game);
        create();
    }

    @Override
    public void update(float deltaTime) {
        if (this.contactListener.getEntities().size() > 0) {
            for (Entity entity : this.contactListener.getEntities())
                if (entity.getCollisionGroup() == ObjectGroup.PLAYER.group) {
                    this.game.getPayload().triggerCheckpoint();
                    this.graphics = this.addGraphics("./res/images/flag.green.png", 1, 1);
                }
        }
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

}

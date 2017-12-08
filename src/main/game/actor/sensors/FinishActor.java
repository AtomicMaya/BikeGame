package main.game.actor.sensors;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.actor.entities.GameEntity;
import main.game.graphics.ImageGraphics;
import main.math.*;
import main.window.Canvas;

/**
 * A special type of checkpoint that triggers end level events,
 */
public class FinishActor extends GameEntity {

    private transient ActorGame game;
	private BasicContactListener contactListener;

	private ImageGraphics graphics;
	private String filepath;
	private float width, height;
	private Shape shape;

	public FinishActor(ActorGame game, Vector position) {
		super(game, true, position);
		this.filepath = "./res/images/flag.red.png";
        this.width = 1;
        this.height = 1;
        this.shape = new Polygon(0, 0, 1, 0, 1, 10, 0, 10);

		this.game = game;
		build(this.shape, -1, -1, true, ObjectGroup.FINISH.group);

		create();
	}

	public FinishActor(ActorGame game, Vector position, Shape shape, String filepath, float width, float height) {
        super(game, true, position);
        this.filepath = filepath;
        this.width = width;
        this.height = height;

        this.game = game;
        build(shape, -1, -1, true, ObjectGroup.FINISH.group);

        create();
    }

	private void create() {
		this.contactListener = new BasicContactListener();
		this.addContactListener(this.contactListener);

		this.graphics = this.addGraphics(this.filepath, this.width, this.height);
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
	            if (entity.getCollisionGroup() == ObjectGroup.PLAYER.group)
                    (this.game.getPayload()).triggerVictory();
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

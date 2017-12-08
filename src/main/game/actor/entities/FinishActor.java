package main.game.actor.entities;

import main.game.ActorGame;
import main.math.BasicContactListener;
import main.math.Entity;
import main.math.Shape;
import main.math.Vector;

import java.util.ArrayList;

public class FinishActor extends GameEntity {

    private transient ActorGame game;
	private BasicContactListener contactListener;

	private boolean finish = false;

	public FinishActor(ActorGame game, Vector position, Shape shape) {
		super(game, true, position);

		this.game = game;
		build(shape, -1, -1, true, ObjectGroup.FINISH.group);

		create();
	}

	public FinishActor(ActorGame game, Vector position, Shape shape, String newGraphics) {
	    this(game, position, shape);

    }

	private void create() {
		this.contactListener = new BasicContactListener();
		this.addContactListener(this.contactListener);
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
                    ((PlayableEntity) this.game.getPayload()).triggerVictory();
        }
	}

	public boolean isFinished() {
		return finish;
	}

	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}

	public ArrayList<ArrayList<String>> getRepresentation() {
		ArrayList<ArrayList<String>> r = new ArrayList<>();
		ArrayList<String> classe = new ArrayList<>();
		classe.add("\"" + this.getClass().toString() + "\"");
		ArrayList<String> player = new ArrayList<>();
		//  player.add("\"player\" : \"" + this.player.getClass().toString() + "\"");
		r.add(classe);
		r.add(player);
		return r;

	}

}

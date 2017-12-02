package main.game.actor.entities;

import main.game.ActorGame;
import main.math.BasicContactListener;
import main.math.Entity;
import main.math.Shape;
import main.math.Vector;

import java.util.ArrayList;

public class FinishActor extends GameEntity {

	private GameEntity player;
	private BasicContactListener contactListener;

	private boolean finish = false;

	public FinishActor(ActorGame game, Vector position, GameEntity player, Shape shape) {
		super(game, true, position);

		build(shape, -1, -1, true);

		this.player = player;
		create();
	}

	private void create() {
		contactListener = new BasicContactListener();
		this.addContactListener(contactListener);
	}

	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
		create();
	}

	@Override
	public void update(float deltaTime) {
		finish = contactListener.getEntities().contains(player.getEntity());
	}

	public void setPlayer(GameEntity player) {
		this.player = player;
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
		player.add("\"player\" : \"" + this.player.getClass().toString() + "\"");
		r.add(classe);
		r.add(player);
		return r;

	}

}

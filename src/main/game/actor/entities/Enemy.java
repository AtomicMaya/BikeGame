package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;

/** Represent an {@linkplain Enemy}, killable by the player */
public abstract class Enemy extends GameEntity {
	
	/** Used for save purposes */
	private static final long serialVersionUID = 5478906395592240743L;
	
	/**
	 * Create a new {@linkplain Enemy}
	 * @param game The master {@linkplain ActorGame}
	 * @param position Initial position of this {@linkplain Enemy}
	 * */
	public Enemy(ActorGame game, Vector position) {
		super(game, false, position);
		this.create();
	}
	
    /**
     * Actual creation of the parameters of the {@linkplain GameEntity}, not in the
     * constructor to avoid duplication with the method {@linkplain #reCreate(ActorGame)}
     */
	private void create() {
		this.build(this.getHitbox(), -1, -1, false, ObjectGroup.ENEMY.group);
	}
	
	@Override
	public void update(float deltaTime) {
		if (!getEntity().isAlive()) {
			this.kill();
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
//		canvas.drawShape(getHitbox(), getTransform(), Color.GREEN, null, 0, .4f, 20);
		
	}
	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
		create();
	}

	/** Method called to kill this {@linkplain Enemy} */
	public abstract void kill();
	
	/** Gets the hitbox of this {@linkplain Enemy}.
     * @return the {@linkplain Shape} of the hitbox. */
	protected abstract Shape getHitbox();
}

package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.menu.LevelEditor;
import main.game.actor.Actor;
import main.game.actor.sensors.SpawnCheckpoint;
import main.math.ExtendedMath;
import main.math.Vector;
import main.window.Canvas;

/** Use in the {@linkplain LevelEditor} to build and add a {@linkplain SpawnCheckpoint} */
public class SpawnBuilder extends ActorBuilder {

	/** Unique {@linkplain SpawnCheckpoint} created and added to the game */
	private SpawnCheckpoint spawn;

	/** Position to give to the {@linkplain SpawnCheckpoint} */
	private Vector position;

	/**
	 * Whether this {@linkplain SpawnBuilder} has finished building its
	 * {@linkplain SpawnCheckpoint}
	 */
	private boolean isDone = false;

	/** Create a new {@linkplain SpawnBuilder}
	 *  @param game The master {@linkplain ActorGame}
	 */
	public SpawnBuilder(ActorGame game) {
		super(game);
		this.spawn = new SpawnCheckpoint(game, getHalfFlooredMousePosition(), null);
	}

	@Override
	public void update(float deltaTime, float zoom) {
		if (isLeftPressed() || isRightPressed()) {
			isDone = true;
		}
		if (!isDone) {
			position = getHalfFlooredMousePosition();
			spawn.setPosition(position);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		spawn.draw(canvas);
	}

	@Override
	public Actor getActor() {
		reCreate();
		return spawn;
	}

	public SpawnCheckpoint getSpawn() {
		reCreate();
		return spawn;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		spawn.destroy();
		spawn = new SpawnCheckpoint(getOwner(), position, null);
	}

	@Override
	public boolean isHovered() {
		return ExtendedMath.isInRectangle(position, position.add(1, 1), getMousePosition());
	}

	@Override
	public void destroy() {
		this.spawn.destroy();
	}

	@Override
	public void edit() {
		this.isDone = false;
	}

}

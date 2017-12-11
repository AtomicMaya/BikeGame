/**
 *	Author: Clément Jeannet
 *	Date: 	6 déc. 2017
 */
package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.sensors.StartCheckpoint;
import main.math.ExtendedMath;
import main.math.Vector;
import main.window.Canvas;

public class SpawnBuilder extends ActorBuilder {

	private StartCheckpoint spawn;
	private Vector position;
	private ActorGame game;

	private boolean isDone = false;
	private boolean hover = false;

	public SpawnBuilder(ActorGame game) {
		super(game);
		this.game = game;
		this.spawn = new StartCheckpoint(game, getFlooredMousePosition(), null);
	}

	@Override
	public void update(float deltaTime, float zoom) {
		if (isLeftPressed() || isRightPressed()) {
			isDone = true;
		}
		if (!isDone) {
			position = getFlooredMousePosition();
			spawn.setPosition(position);
		} else
			hover = ExtendedMath.isInRectangle(position, position.add(1, 1), game.getMouse().getPosition());

		if (hover && isRightPressed())
			isDone = false;
	}

	@Override
	public void draw(Canvas canvas) {
		spawn.draw(canvas);
	}

	@Override
	public Actor getActor() {
		return spawn;
	}

	public StartCheckpoint getSpawn() {
		return spawn;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		spawn.destroy();
		spawn = new StartCheckpoint(game, position, null);
	}

	@Override
	public boolean isHovered() {
		return hover;
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

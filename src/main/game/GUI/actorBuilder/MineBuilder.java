/**
 *	Author: Clément Jeannet
 *	Date: 	10 déc. 2017
 */
package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.entities.Mine;
import main.math.ExtendedMath;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

public class MineBuilder extends ActorBuilder {

	private Vector position;

	private Mine mine;

	private boolean isDone = false;
	private boolean hover = false;

	public MineBuilder(ActorGame game) {
		super(game);
		mine = new Mine(game, getFlooredMousePosition());
	}

	@Override
	public void update(float deltaTime, float zoom) {

		if (!isDone) {
			position = getFlooredMousePosition();
			if (isLeftPressed()) {
				isDone = true;
			}
			mine.setPosition(position);
		} else
			hover = ExtendedMath.isInRectangle(position, position.add(1, 1), getMousePosition());
		if (hover && isRightPressed())
			isDone = false;
		mine.update(deltaTime);
	}

	@Override
	public void draw(Canvas canvas) {
		if (mine != null)
			mine.draw(canvas);
		// 0 null, 0, .5f, 42);
		canvas.drawImage(canvas.getImage("res/images/mine.0.png"), Transform.I.scaled(.5f, 1).translated(position), 1,
				42);

	}

	@Override
	public Actor getActor() {
		return mine;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		mine.destroy();
		mine = new Mine(getOwner(), position);
	}

	@Override
	public boolean isHovered() {
		return hover;
	}

	@Override
	public void destroy() {
		this.mine.destroy();
	}

	@Override
	public void edit() {
		this.isDone = false;
	}

}

/**
 * Author: Clément Jeannet Date: 10 déc. 2017
 */
package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.menu.LevelEditor;
import main.game.actor.Actor;
import main.game.actor.entities.Mine;
import main.math.ExtendedMath;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

/**
 * Use in the {@linkplain LevelEditor} to build and add a new {@linkplain Mine}
 */
public class MineBuilder extends ActorBuilder {

	/** Position to give to the {@linkplain Mine} */
	private Vector position;

	/** {@linkplain Mine} created by this {@linkplain MineBuilder} */
	private Mine mine;

	/**
	 * Whether this {@linkplain MineBuilder} has finished building its
	 * {@linkplain Mine}
	 */
	private boolean isDone = false;

	/**
	 * Create a new {@linkplain MineBuilder}
	 * @param game The master {@linkplain ActorGame}
	 */
	public MineBuilder(ActorGame game) {
		super(game);
		position = getHalfFlooredMousePosition();
		mine = new Mine(game, position);
	}

	@Override
	public void update(float deltaTime, float zoom) {
		super.update(deltaTime, zoom);
		if (!isDone) {
			position = getHalfFlooredMousePosition();
			if (isLeftPressed()) {
				isDone = true;
			}
			mine.setPosition(position);
		}
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
		return ExtendedMath.isInRectangle(position, position.add(1, 1), getMousePosition());
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

package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.menu.LevelEditor;
import main.game.actor.Actor;
import main.game.actor.entities.Trampoline;
import main.math.ExtendedMath;
import main.math.Vector;
import main.window.Canvas;

/**
 * Use in the {@linkplain LevelEditor} to build and add a new {@linkplain Trampoline}
 */
public class TrampolineBuilder extends ActorBuilder {

	/** Position to give to the {@linkplain Trampoline} */
	private Vector position;

	/** {@linkplain Trampoline} created by this {@linkplain MineBuilder} */
	private Trampoline trampoline;

	/**
	 * Whether this {@linkplain TrampolineBuilder} has finished building its
	 * {@linkplain Trampoline}
	 */
	private boolean isDone = false;

	/**
	 * Create a new {@linkplain TrampolineBuilder}
	 * @param game The master {@linkplain ActorGame}
	 */
	public TrampolineBuilder(ActorGame game) {
		super(game);
		trampoline = new Trampoline(getOwner(), getHalfFlooredMousePosition(), 5, 1);
		position = getHalfFlooredMousePosition();
	}

	@Override
	public void update(float deltaTime, float zoom) {
		super.update(deltaTime, zoom);
		if (!isDone) {
			position = getHalfFlooredMousePosition();
			if (isLeftPressed()) {
				isDone = true;
			}
			trampoline.setPosition(position);
		} else

		if (isHovered() && isRightPressed())
			isDone = false;
	}

	@Override
	public void draw(Canvas canvas) {
		if (trampoline != null)
			trampoline.draw(canvas);

	}

	@Override
	public Actor getActor() {
		reCreate();
		return trampoline;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		trampoline.destroy();
		trampoline = new Trampoline(getOwner(), position, 5, 1);
	}

	@Override
	public boolean isHovered() {
		return ExtendedMath.isInRectangle(position.sub(-.5f, -.5f), position.add(4.5f, .5f), getMousePosition());
	}

	@Override
	public void destroy() {
		this.trampoline.destroy();
	}

	@Override
	public void edit() {
		this.isDone = false;
	}

}

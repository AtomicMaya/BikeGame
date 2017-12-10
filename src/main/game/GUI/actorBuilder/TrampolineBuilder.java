package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.entities.Trampoline;
import main.math.ExtendedMath;
import main.math.Vector;
import main.window.Canvas;

public class TrampolineBuilder extends ActorBuilder {

	private Vector position;

	private Trampoline trampoline;

	private boolean isDone = false;
	private boolean hover = false;

	public TrampolineBuilder(ActorGame game) {
		super(game);
		trampoline = new Trampoline(getOwner(), getFlooredMousePosition(), 5, 1);
	}

	@Override
	public void update(float deltaTime, float zoom) {

		if (!isDone) {
			position = getFlooredMousePosition();
			if (isLeftPressed()) {
				isDone = true;
			}
			trampoline.setPosition(position);
		} else
			hover = ExtendedMath.isInRectangle(position, position.add(5, 1), getMousePosition());
		if (hover && isRightPressed())
			isDone = false;
	}

	@Override
	public void draw(Canvas canvas) {
		if (trampoline != null)
			trampoline.draw(canvas);

	}

	@Override
	public Actor getActor() {
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
		return hover;
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

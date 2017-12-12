package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.NumberField;
import main.game.GUI.menu.LevelEditor;
import main.game.actor.Actor;
import main.game.actor.entities.Laser;
import main.game.actor.entities.Pendulum;
import main.math.ExtendedMath;
import main.math.Vector;
import main.window.Canvas;

import java.awt.event.KeyEvent;

/** Use in the {@linkplain LevelEditor} to build and add a new {@linkplain Pendulum} */
public class PenduleBuilder extends ActorBuilder {

	/** {@linkplain Pendulum} created and returned by {@link #getActor()} */
	private Pendulum pendule;
	
	/** Position to give to the {@linkplain Pendulum} */
	private Vector position;

	/** {@linkplain NumberField} used to give to the {@linkplain Pendulum} its size */
	private NumberField length, radius;
	
	/** Absolute position on screen of the {@linkplain NumberField} */
	private Vector lengthNumberFieldPos = new Vector(26, 6), radiusPos = new Vector(26, 8);
	
	/** {@linkplain Comment} of the {@linkplain NumberField} */
	private Comment lengthComment, radiusComment;

	/**
	 * Whether this {@linkplain PenduleBuilder} has finished building its
	 * {@linkplain Pendulum}
	 */
	private boolean isDone = false;

	/** Whether the {@linkplain Laser} is placed */
	private boolean placed = false;

	/**
	 * Create a new {@linkplain PenduleBuilder}
	 * @param game The master {@linkplain ActorGame}
	 */
	public PenduleBuilder(ActorGame game) {
		super(game);

		pendule = new Pendulum(game, getHalfFlooredMousePosition(), 5, 1);

		length = new NumberField(game, lengthNumberFieldPos, 5);

		lengthComment = new Comment(game, "Rope length");
		lengthComment.setParent(length);
		lengthComment.setAnchor(new Vector(-6, 0));

		radius = new NumberField(game, radiusPos, 1);

		radiusComment = new Comment(game, "Weight radius");
		radiusComment.setParent(length);
		radiusComment.setAnchor(new Vector(-6, 0));

		position = getHalfFlooredMousePosition();
	}

	@Override
	public void update(float deltaTime, float zoom) {
		super.update(deltaTime, zoom);
		if (!placed) {
			position = getHalfFlooredMousePosition();
			if (isLeftPressed()) {
				placed = true;
			}
			pendule.setPosition(position);
		}

		if (!isDone()) {
			length.update(deltaTime, zoom);
			if (length.isHovered())
				lengthComment.update(deltaTime, zoom);
			radius.update(deltaTime, zoom);
			if (length.isHovered())
				radiusComment.update(deltaTime, zoom);

			if (getOwner().getKeyboard().get(KeyEvent.VK_ENTER).isPressed()) {
				isDone = true;
				pendule = new Pendulum(getOwner(), position, length.getNumber(), radius.getNumber());
			}
		}

		if (isHovered() && isRightPressed())
			isDone = false;
	}

	@Override
	public void draw(Canvas canvas) {
		pendule.draw(canvas);
		if (!isDone()) {
			length.draw(canvas);
			if (length.isHovered())
				lengthComment.draw(canvas);
			radius.draw(canvas);
			if (radius.isHovered())
				radiusComment.draw(canvas);
		}
	}

	@Override
	public Actor getActor() {
		return pendule;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		pendule.destroy();
		pendule = new Pendulum(getOwner(), position, length.getNumber(), radius.getNumber());
	}

	@Override
	public boolean isHovered() {
		return ExtendedMath.isInRectangle(position.sub(1, 1), position.add(1, 1), getMousePosition());
	}

	@Override
	public void destroy() {
		this.pendule.destroy();
	}

	@Override
	public void edit() {
		this.placed = false;
		this.isDone = false;
	}

}

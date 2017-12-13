package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.NumberField;
import main.game.GUI.menu.LevelEditor;
import main.game.actor.Actor;
import main.game.actor.entities.crate.Crate;
import main.math.ExtendedMath;
import main.math.Vector;
import main.window.Canvas;

import java.awt.event.KeyEvent;

/** Use in the {@linkplain LevelEditor} to build and add a new {@linkplain Crate} */
public class CrateBuilder extends ActorBuilder {

	/** {@linkplain Crate} created and returned by {@link #getActor()} */
	private Crate crate;

	/** Position to give to the {@linkplain Crate} */
	private Vector position;

	/** {@linkplain NumberField} used to give to the {@linkplain Crate} its size */
	private NumberField height, width;

	/** Absolute position on screen of the {@linkplain NumberField} */
	private Vector heightNumberFieldPos = new Vector(26, 6), widthNumberFieldPos = new Vector(26, 8);

	/** {@linkplain Comment} of the {@linkplain NumberField} */
	private Comment heightComment, widthComments;

	/**
	 * Whether this {@linkplain CrateBuilder} has finished building its
	 * {@linkplain Crate}
	 */
	private boolean isDone = false;

	/** Whether the {@linkplain Crate} is placed */
	private boolean placed = false;

	/**
	 * Create a new {@linkplain CrateBuilder}
	 * @param game The master {@linkplain ActorGame}
	 */
	public CrateBuilder(ActorGame game) {
		super(game);

		crate = new Crate(game, getHalfFlooredMousePosition(), null, false, 1);

		height = new NumberField(game, heightNumberFieldPos, 1);

		heightComment = new Comment(game, "Crate Height");
		heightComment.setParent(height);
		heightComment.setAnchor(new Vector(-6, 0));

		width = new NumberField(game, widthNumberFieldPos, 1);

		widthComments = new Comment(game, "Crate Width");
		widthComments.setParent(width);
		widthComments.setAnchor(new Vector(-6, 0));

		position = getHalfFlooredMousePosition();
	}

	@Override
	public void update(float deltaTime, float zoom) {
		if (!placed) {
			position = getHalfFlooredMousePosition();
			if (isLeftPressed()) {
				placed = true;
			}
			crate.setPosition(position);

		} else if (isHovered() && isRightPressed())
			placed = false;
		if (!isDone()) {
			height.update(deltaTime, zoom);
			width.update(deltaTime, zoom);

			heightComment.update(deltaTime, zoom);
			widthComments.update(deltaTime, zoom);

			if (getOwner().getKeyboard().get(KeyEvent.VK_ENTER).isPressed()) {
				crate.setSize(width.getNumber(), height.getNumber());
				isDone = true;
			}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		crate.draw(canvas);
		if (!isDone()) {
			height.draw(canvas);
			if (height.isHovered())
				heightComment.draw(canvas);
			width.draw(canvas);
			if (width.isHovered())
				widthComments.draw(canvas);
		}
	}

	@Override
	public Actor getActor() {
		reCreate();
		return crate;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		crate.destroy();
		crate = new Crate(getOwner(), position, null, false, width.getNumber(), height.getNumber());
	}

	@Override
	public boolean isHovered() {
		return ExtendedMath.isInRectangle(position, position.add(width.getNumber(), height.getNumber()),
				getMousePosition());
	}

	@Override
	public void destroy() {
		this.crate.destroy();
	}

	@Override
	public void edit() {
		this.placed = false;
		this.isDone = false;
	}

}

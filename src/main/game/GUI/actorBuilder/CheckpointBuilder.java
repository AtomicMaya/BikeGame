package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.NumberField;
import main.game.actor.Actor;
import main.game.actor.sensors.Checkpoint;
import main.game.actor.sensors.SpawnCheckpoint;
import main.math.ExtendedMath;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * {@linkplain ActorBuilder} which build and add to the game a new
 * {@linkplain CheckpointBuilder}
 */
public class CheckpointBuilder extends ActorBuilder {

	/** {@linkplain Checkpoint} created and returned by {@link #getActor()} */
	private Checkpoint checkpoint;

	/** Position to give to the {@linkplain Checkpoint} */
	private Vector position;
	
	/**
	 * {@linkplain NumberField} used to give to the {@linkplain Checkpoint} its size of trigger
	 */
	protected NumberField height, width;
	
	/** Absolute position on screen of the {@linkplain NumberField} */
	private Vector heightNumberFieldPos = new Vector(26, 6), widthNumberFieldPos = new Vector(26, 8);
	
	/** {@linkplain Comment} of the {@linkplain NumberField} */
	private Comment heightComment, widthComments;

	/**
	 * Whether this {@linkplain CheckpointBuilder} has finished building its
	 * {@linkplain Checkpoint}
	 */
	private boolean isDone = false;
	
	/** Whether the {@linkplain Checkpoint} is placed */
	private boolean placed = false;

	/**
	 * Create a new {@linkplain CheckpointBuilder}
	 * @param game The master {@linkplain ActorGame}
	 */
	public CheckpointBuilder(ActorGame game) {
		super(game);
		this.checkpoint = new Checkpoint(game, getHalfFlooredMousePosition());

		height = new NumberField(game, heightNumberFieldPos, 10);

		heightComment = new Comment(game, "Area of trigger height");
		heightComment.setParent(height);
		heightComment.setAnchor(new Vector(-6, 0));

		width = new NumberField(game, widthNumberFieldPos, 1);

		widthComments = new Comment(game, "Area of trigger width");
		widthComments.setParent(width);
		widthComments.setAnchor(new Vector(-6, 0));
	}

	@Override
	public void update(float deltaTime, float zoom) {
		if (!placed) {
			position = getHalfFlooredMousePosition();
			if (isLeftPressed()) {
				placed = true;
			}
			checkpoint.setPosition(position);

		} else if (isHovered() && isRightPressed())
			placed = false;
		if (!isDone) {

			height.update(deltaTime, zoom);
			width.update(deltaTime, zoom);

			heightComment.update(deltaTime, zoom);
			widthComments.update(deltaTime, zoom);

			if (getOwner().getKeyboard().get(KeyEvent.VK_ENTER).isPressed()) {
				checkpoint.setSize(width.getNumber(), height.getNumber());
				isDone = true;
			}
		}

		if (isHovered() && isRightPressed())
			isDone = false;
	}

	@Override
	public void draw(Canvas canvas) {
		checkpoint.draw(canvas);

		if (!isDone()) {
			height.draw(canvas);
			if (height.isHovered())
				heightComment.draw(canvas);
			width.draw(canvas);
			if (width.isHovered())
				widthComments.draw(canvas);
			canvas.drawShape(ExtendedMath.createRectangle(width.getNumber(), height.getNumber()),
					Transform.I.translated(position), new Color(0, 255, 255), Color.BLACK, .02f, .6f, 1337);
		}
	}

	@Override
	public Actor getActor() {
		return checkpoint;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		checkpoint.destroy();
		checkpoint = new SpawnCheckpoint(getOwner(), position, null);
		checkpoint.setSize(width.getNumber(), height.getNumber());
	}

	@Override
	public boolean isHovered() {
		return ExtendedMath.isInRectangle(position, position.add(1, 1), getMousePosition());
	}

	@Override
	public void destroy() {
		this.checkpoint.destroy();
	}

	@Override
	public void edit() {
		this.isDone = false;
	}
}

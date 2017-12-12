/**
 * Author: Clément Jeannet Date: 11 déc. 2017
 */
package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.NumberField;
import main.game.GUI.menu.LevelEditor;
import main.game.actor.Actor;
import main.game.actor.sensors.FinishActor;
import main.math.ExtendedMath;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

/** Use in the {@linkplain LevelEditor} to build and add a unique {@linkplain FinishActor} */
public class FinishBuilder extends ActorBuilder {

	/** {@linkplain FinishActor} created and returned by {@link #getActor()} */
	private FinishActor finish;

	/** Position to give to the {@linkplain FinishActor} */
	private Vector position;
	
	/**
	 * {@linkplain NumberField} used to give to the {@linkplain FinishActor} its size of trigger
	 */
	protected NumberField height, width;
	
	/** Absolute position on screen of the {@linkplain NumberField} */
	private Vector heightNumberFieldPos = new Vector(26, 6), widthNumberFieldPos = new Vector(26, 8);
	
	/** {@linkplain Comment} of the {@linkplain NumberField} */
	private Comment heightComment, widthComments;

	/**
	 * Whether this {@linkplain CheckpointBuilder} has finished building its
	 * {@linkplain FinishActor}
	 */
	private boolean isDone = false;
	
	/** Whether the {@linkplain FinishActor} is placed */
	private boolean placed = false;

	/**
	 * Create a new {@linkplain FinishBuilder}
	 * @param game The master {@linkplain ActorGame}
	 */
	public FinishBuilder(ActorGame game) {
		super(game);

		this.finish = new FinishActor(game, getHalfFlooredMousePosition());

		height = new NumberField(game, heightNumberFieldPos, 3, 1, 10);

		heightComment = new Comment(game, "Height of the area of trigger");
		heightComment.setParent(height);
		heightComment.setAnchor(new Vector(-6, 0));

		width = new NumberField(game, widthNumberFieldPos, 3, 1, 1);

		widthComments = new Comment(game, "Width of the area of trigger");
		widthComments.setParent(width);
		widthComments.setAnchor(new Vector(-6, 0));

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
			finish.setPosition(position);

		} else if (isHovered() && isRightPressed())
			placed = false;
		if (!isDone) {

			height.update(deltaTime, zoom);
			width.update(deltaTime, zoom);

			heightComment.update(deltaTime, zoom);
			widthComments.update(deltaTime, zoom);

			if (getOwner().getKeyboard().get(KeyEvent.VK_ENTER).isPressed()) {
				finish.setSize(width.getNumber(), height.getNumber());
				isDone = true;
			}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		finish.draw(canvas);

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
		return finish;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		finish.destroy();
		finish = new FinishActor(getOwner(), position);
		finish.setSize(width.getNumber(), height.getNumber());
	}

	@Override
	public boolean isHovered() {
		return ExtendedMath.isInRectangle(position, position.add(1, 1), getMousePosition());
	}

	@Override
	public void destroy() {
		this.finish.destroy();
	}

	@Override
	public void edit() {
		this.isDone = false;
	}
}

/**
 *	Author: Clément Jeannet
 *	Date: 	11 déc. 2017
 */
package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.NumberField;
import main.game.actor.Actor;
import main.game.actor.sensors.FinishActor;
import main.math.ExtendedMath;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

public class FinishBuilder extends ActorBuilder {

	private FinishActor finish;
	private Vector position;
	private ActorGame game;

	// number field stuff
	protected NumberField height, width;
	private Vector heightNumberFieldPos = new Vector(26, 6), widthNumberFieldPos = new Vector(26, 8);
	private Comment heightComment, widthComments;

	private boolean isDone = false;
	private boolean hover = false;
	private boolean placed = false;

	public FinishBuilder(ActorGame game) {
		super(game);
		this.game = game;
		this.finish = new FinishActor(game, getFlooredMousePosition());

		height = new NumberField(game, heightNumberFieldPos, 3, 1, 10);

		heightComment = new Comment(game, "Height of the area of trigger");
		heightComment.setParent(height);
		heightComment.setAnchor(new Vector(-6, 0));

		width = new NumberField(game, widthNumberFieldPos, 3, 1, 1);

		widthComments = new Comment(game, "Width of the area of trigger");
		widthComments.setParent(width);
		widthComments.setAnchor(new Vector(-6, 0));

		position = getFlooredMousePosition();
	}

	@Override
	public void update(float deltaTime, float zoom) {
		if (!placed) {
			position = getFlooredMousePosition();
			if (isLeftPressed()) {
				placed = true;
			}
			finish.setPosition(position);

		} else if (hover && isRightPressed())
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
		hover = ExtendedMath.isInRectangle(position, position.add(1, 1), game.getMouse().getPosition());

		if (hover && isRightPressed())
			isDone = false;
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
		finish = new FinishActor(game, position);
		finish.setSize(width.getNumber(), height.getNumber());
	}

	@Override
	public boolean isHovered() {
		return hover;
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

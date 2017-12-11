/**
 *	Author: Clément Jeannet
 *	Date: 	4 déc. 2017
 */
package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.NumberField;
import main.game.actor.Actor;
import main.game.actor.entities.crate.Crate;
import main.math.ExtendedMath;
import main.math.Vector;
import main.window.Canvas;

import java.awt.event.KeyEvent;

public class CrateBuilder extends ActorBuilder {

	private Crate crate;
	private Vector position;

	// number field stuff
	private NumberField height, width;
	private Vector heightNumberFieldPos = new Vector(26, 6), widthNumberFieldPos = new Vector(26, 8);
	private Comment heightComment, widthComments;

	private boolean isDone = false;
	private boolean hover = false;
	private boolean placed = false;

	public CrateBuilder(ActorGame game) {
		super(game);

		crate = new Crate(game, getFlooredMousePosition(), null, false, 1);

		height = new NumberField(game, heightNumberFieldPos, 3, 1, 1);

		heightComment = new Comment(game, "Crate Height");
		heightComment.setParent(height);
		heightComment.setAnchor(new Vector(-6, 0));

		width = new NumberField(game, widthNumberFieldPos, 3, 1, 1);

		widthComments = new Comment(game, "Crate Width");
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
			crate.setPosition(position);

		} else if (hover && isRightPressed())
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

		hover = ExtendedMath.isInRectangle(position, position.add(width.getNumber(), height.getNumber()),
				getMousePosition());
		if (hover && isRightPressed())
			isDone = false;
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
		return hover;
	}

	@Override
	public void destroy() {
		this.crate.destroy();
	}

	@Override
	public void edit() {
		this.placed = false;
	}

}

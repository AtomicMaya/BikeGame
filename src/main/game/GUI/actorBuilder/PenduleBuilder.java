/**
 *	Author: Clément Jeannet
 *	Date: 	10 déc. 2017
 */
package main.game.GUI.actorBuilder;

import java.awt.event.KeyEvent;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.NumberField;
import main.game.actor.Actor;
import main.game.actor.entities.BoomBarrel;
import main.game.actor.entities.Pendulum;
import main.game.actor.entities.crate.Crate;
import main.math.ExtendedMath;
import main.math.Vector;
import main.window.Canvas;

public class PenduleBuilder extends ActorBuilder {

	private Pendulum pendule;
	private Vector position;

	// number field stuff
	private NumberField length, radius;
	private Vector lengthNumberFieldPos = new Vector(26, 6), radiusPos = new Vector(26, 8);
	private Comment lengthComment, radiusComment;

	private boolean isDone = false;
	private boolean hover = false;
	private boolean placed = false;

	public PenduleBuilder(ActorGame game) {
		super(game);

		pendule = new Pendulum(game, getFlooredMousePosition(), 5, 1);

		length = new NumberField(game, lengthNumberFieldPos, 3, 1, 5);

		lengthComment = new Comment(game, "Rope length");
		lengthComment.setParent(length);
		lengthComment.setAnchor(new Vector(-6, 0));

		radius = new NumberField(game, radiusPos, 3, 1, 1);

		radiusComment = new Comment(game, "Weight radius");
		radiusComment.setParent(length);
		radiusComment.setAnchor(new Vector(-6, 0));

	}

	@Override
	public void update(float deltaTime, float zoom) {
		if (!placed) {
			position = getFlooredMousePosition();
			if (isLeftPressed()) {
				placed = true;
			}
			pendule.setPosition(position);

		} else if (hover && isRightPressed())
			placed = false;
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

		hover = ExtendedMath.isInRectangle(position.sub(1, 1), position.add(1, 1), getMousePosition());
		if (hover && isRightPressed())
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
		return hover;
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

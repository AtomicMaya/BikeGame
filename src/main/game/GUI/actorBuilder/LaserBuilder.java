/**
 *	Author: Clément Jeannet
 *	Date: 	9 déc. 2017
 */
package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.GraphicalButton;
import main.game.GUI.NumberField;
import main.game.actor.Actor;
import main.game.actor.entities.Laser;
import main.game.graphics.ShapeGraphics;
import main.math.ExtendedMath;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

public class LaserBuilder extends ActorBuilder {

	private Laser laser;
	private Vector position;

	private GraphicalButton changeDirection;
	private Vector changeDirectionPos = new Vector(18, 8);

	private String commentText = "Change direction";
	private int direction = 0;

	private boolean isDone = false;
	private boolean placed = false;

	private NumberField length;
	private Vector lengthPos = new Vector(26, 6);
	private Comment lengthComment;
	private String lengthCommentText = "Laser length";

	public LaserBuilder(ActorGame game) {
		super(game);
		changeDirection = new GraphicalButton(game, changeDirectionPos, commentText, 1);
		changeDirection.addOnClickAction(() -> {
			direction++;
			direction = direction > 3 ? 0 : direction;
			laser.changeDirection(direction);
		});

		length = new NumberField(game, lengthPos, 3, 1, 5);

		lengthComment = new Comment(game, lengthCommentText);
		lengthComment.setParent(length);
		lengthComment.setAnchor(new Vector(-6, 0));

		laser = new Laser(getOwner(), getFlooredMousePosition(), 5, direction);
	}

	@Override
	public void update(float deltaTime, float zoom) {
		if (!placed) {
			position = getFlooredMousePosition().add(.5f, .5f);
			if (isLeftPressed()) {
				placed = true;
			}
			laser.setPosition(position);
		}
		if (!isDone) {

			changeDirection.update(deltaTime, zoom);
			length.update(deltaTime, zoom);
			if (length.isHovered())
				lengthComment.update(deltaTime, zoom);
			if (getOwner().getKeyboard().get(KeyEvent.VK_ENTER).isPressed()) {
				laser.changeDirection(direction);
				laser.setLength(length.getNumber());
				laser.setPosition(position);
				isDone = true;
			}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		laser.draw(canvas);
		ShapeGraphics s = new ShapeGraphics(laser.changeDirection(direction), Color.BLUE, Color.decode("#00FFFF"), .3f,
				.5f, 42);
		s.setRelativeTransform(Transform.I.translated(position));
		s.draw(canvas);

		if (!isDone) {
			changeDirection.draw(canvas);
			length.draw(canvas);
			if (length.isHovered())
				lengthComment.draw(canvas);
		}
	}

	@Override
	public boolean isHovered() {
		return ExtendedMath.isInRectangle(position, position.add(1, 1), getMousePosition());
	}

	@Override
	public void destroy() {
		laser.destroy();
	}

	@Override
	public Actor getActor() {
		return laser;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		laser.destroy();
		laser = new Laser(getOwner(), position, length.getNumber(), direction);
	}

	@Override
	public void edit() {
		this.isDone = false;
		this.placed = false;
	}

}

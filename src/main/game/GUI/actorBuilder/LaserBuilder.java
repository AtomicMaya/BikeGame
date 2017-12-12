/**
 * Author: Clément Jeannet Date: 9 déc. 2017
 */
package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.GraphicalButton;
import main.game.GUI.NumberField;
import main.game.GUI.menu.LevelEditor;
import main.game.actor.Actor;
import main.game.actor.entities.Laser;
import main.game.graphics.ShapeGraphics;
import main.math.ExtendedMath;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

/** Use in the {@linkplain LevelEditor} to build and add a new {@linkplain Laser} */
public class LaserBuilder extends ActorBuilder {
	
	/** {@linkplain Laser} created and returned by {@link #getActor()} */
	private Laser laser;
	
	/** Position to give to the {@linkplain Laser} */
	private Vector position;

	/** {@linkplain GraphicalButton} used to change the {@link #direction} of the {@linkplain Laser}*/
	private GraphicalButton changeDirection;
	
	/** Absolute position on screen of the {@linkplain GraphicalButton} {@link #changeDirection} */
	private Vector changeDirectionPos = new Vector(18, 8);

	/** Text of the {@linkplain GraphicalButton} {@link #changeDirection} */
	private String commentText = "Change direction";
	
	/** Current value of the direction of the {@linkplain Laser} */
	private int direction = 0;

	/**
	 * Whether this {@linkplain LaserBuilder} has finished building its
	 * {@linkplain Laser}
	 */
	private boolean isDone = false;
	
	/** Whether the {@linkplain Laser} is placed */
	private boolean placed = false;

	/** {@linkplain NumberField} used to give to the {@linkplain Laser} its length */
	private NumberField length;
	
	/** Absolute position on screen of the {@linkplain NumberField} */
	private Vector lengthPos = new Vector(26, 6);
	
	/** {@linkplain Comment} of the {@linkplain NumberField} */
	private Comment lengthComment;
	
	/** Text of the {@linkplain Comment} */
	private String lengthCommentText = "Laser length";

	/**
	 * Create a new {@linkplain LaserBuilder}
	 * @param game The master {@linkplain ActorGame}
	 */
	public LaserBuilder(ActorGame game) {
		super(game);
		changeDirection = new GraphicalButton(game, changeDirectionPos, commentText, 1);
		changeDirection.addOnClickAction(() -> {
			direction++;
			direction = direction > 3 ? 0 : direction;
			laser.changeDirection(direction);
		});

		length = new NumberField(game, lengthPos, 5);

		lengthComment = new Comment(game, lengthCommentText);
		lengthComment.setParent(length);
		lengthComment.setAnchor(new Vector(-6, 0));

		laser = new Laser(getOwner(), getHalfFlooredMousePosition(), 5, direction);
		position = getHalfFlooredMousePosition().add(.5f, .5f);
	}

	@Override
	public void update(float deltaTime, float zoom) {
		super.update(deltaTime, zoom);
		if (!placed) {
			position = getHalfFlooredMousePosition().add(.5f, .5f);
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
		return ExtendedMath.isInRectangle(position.sub(.5f, .5f), position.add(.5f, .5f), getMousePosition());
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

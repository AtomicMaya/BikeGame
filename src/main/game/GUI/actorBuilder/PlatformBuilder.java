/**
 * Author: Clément Jeannet Date: 6 déc. 2017
 */
package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.NumberField;
import main.game.GUI.menu.LevelEditor;
import main.game.actor.Actor;
import main.game.actor.entities.MovingPlatform;
import main.math.*;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

/** Use in the {@linkplain LevelEditor} to build and add a new {@linkplain MovingPlatform} */
public class PlatformBuilder extends ActorBuilder {

	/** {@linkplain MovingPlatform} created and returned by {@link #getActor()} */
	private MovingPlatform platform;
	
	/** Position parameters of the {@linkplain MovingPlatform} */
	private Vector start, end;
	
	/** Evolution {@linkplain Vector} of the {@linkplain MovingPlatform} */
	private Vector evolution;
	
	/** Distance between {@link #start} and {@link #end} */
	private float distance = 1;

	/**
	 * Whether this {@linkplain PlatformBuilder} has finished building its
	 * {@linkplain MovingPlatform}
	 */
	private boolean isDone = false;

	/** {@linkplain NumberField} for the parameters */
	private NumberField waitTime, timeToGo;
	
	/** {@linkplain NumberField} position */
	private Vector waitTimePos = new Vector(26, 8), timeToGoPos = new Vector(26, 6);
	
	/** {@linkplain NumberField} {@linkplain Comment} */
	private Comment waitTimeComment, timeToGoComment;
	
	/** {@linkplain Comment} text */
	private String waitTimeCommentText = "Time to wait", timeToGoCommentText = "Time to make a way";

	/**
	 * Create a new {@linkplain PlatformBuilder}
	 * @param game The master {@linkplain ActorGame}
	 */
	public PlatformBuilder(ActorGame game) {
		super(game);

		waitTime = new NumberField(game, waitTimePos, 1);

		waitTimeComment = new Comment(game, waitTimeCommentText);
		waitTimeComment.setParent(waitTime);
		waitTimeComment.setAnchor(new Vector(-6, 0));

		timeToGo = new NumberField(game, timeToGoPos, 3);

		timeToGoComment = new Comment(game, timeToGoCommentText);
		timeToGoComment.setParent(timeToGo);
		timeToGoComment.setAnchor(new Vector(-6, 0));
	}

	@Override
	public void update(float deltaTime, float zoom) {

		if (!isDone) {
			if (isLeftPressed() && !timeToGo.isHovered() && !waitTime.isHovered()) {
				if (start == null)
					start = getHalfFlooredMousePosition();
				else if (end == null && !start.equals(end)) {
					end = getHalfFlooredMousePosition();
					distance = ExtendedMath.getDistance(start, end);
					evolution = new Vector(start.x - end.x, start.y - end.y).div(-distance);
				} else if (start.equals(getHalfFlooredMousePosition())) {
					start = null;
					end = null;
				} else if (end.equals(getHalfFlooredMousePosition()))
					end = null;
			}
			if (getOwner().getKeyboard().get(KeyEvent.VK_ENTER).isPressed()) {
				if (platform != null)
					platform.destroy();
				platform = new MovingPlatform(getOwner(), start, evolution, distance, timeToGo.getNumber(),
						waitTime.getNumber());
				isDone = true;
			}
			waitTime.update(deltaTime, zoom);
			if (waitTime.isHovered())
				waitTimeComment.update(deltaTime, zoom);
			timeToGo.update(deltaTime, zoom);
			if (timeToGo.isHovered())
				timeToGoComment.update(deltaTime, zoom);
		}
	}

	@Override
	public void draw(Canvas canvas) {

		if (!isDone) {
			Vector tempStart = (start == null) ? getHalfFlooredMousePosition() : start;
			Vector tempEnd = (end == null) ? getHalfFlooredMousePosition() : end;

			canvas.drawShape(new Circle(.1f), Transform.I.translated(tempStart), new Color(22, 84, 44), null, 0, 1, 15);
			canvas.drawShape(new Circle(.1f), Transform.I.translated(tempEnd), new Color(22, 84, 44), null, 0, 1, 15);

			canvas.drawShape(ExtendedMath.createRectangle(5, 1), Transform.I.translated(tempStart),
					new Color(200, 200, 200), null, 0, .8f, 40);
			canvas.drawShape(new Polyline(tempStart, tempEnd), Transform.I, null, new Color(32, 246, 14), .1f, 1, 14);

			waitTime.draw(canvas);
			if (waitTime.isHovered())
				waitTimeComment.draw(canvas);
			timeToGo.draw(canvas);
			if (timeToGo.isHovered())
				timeToGoComment.draw(canvas);

		} else if (start != null && end != null)
			canvas.drawShape(new Polyline(start, end), Transform.I, null, new Color(32, 246, 14), .1f, 1, 14);
		if (platform != null)
			platform.draw(canvas);
	}

	@Override
	public Actor getActor() {
		return platform;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {

		platform.destroy();
		platform = new MovingPlatform(getOwner(), start, evolution, distance, timeToGo.getNumber(),
				waitTime.getNumber());

	}

	@Override
	public boolean isHovered() {
		return ExtendedMath.isInRectangle(platform.getPosition(), platform.getPosition().add(5, 1), getMousePosition());
	}

	@Override
	public void destroy() {
		this.platform.destroy();
	}

	@Override
	public void edit() {
		this.isDone = false;
	}

}

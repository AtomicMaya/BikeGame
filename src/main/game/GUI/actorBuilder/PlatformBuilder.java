/**
 *	Author: Clément Jeannet
 *	Date: 	6 déc. 2017
 */
package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.NumberField;
import main.game.actor.Actor;
import main.game.actor.entities.MovingPlatform;
import main.math.*;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

public class PlatformBuilder extends ActorBuilder {

	private MovingPlatform platform;
	private Vector start, end, evolution;
	private float distance = 1;

	private boolean isDone = false;

	private NumberField waitTime, timeToGo;
	private Vector waitTimePos = new Vector(26, 8), timeToGoPos = new Vector(26, 6);
	private Comment waitTimeComment, timeToGoComment;
	private String waitTimeCommentText = "Time to wait", timeToGoCommentText = "Time to make a way";

	private boolean hover = false;

	public PlatformBuilder(ActorGame game) {
		super(game);

		waitTime = new NumberField(game, waitTimePos, 3, 1, 1);

		waitTimeComment = new Comment(game, waitTimeCommentText);
		waitTimeComment.setParent(waitTime);
		waitTimeComment.setAnchor(new Vector(-6, 0));

		timeToGo = new NumberField(game, timeToGoPos, 3, 1, 3);

		timeToGoComment = new Comment(game, timeToGoCommentText);
		timeToGoComment.setParent(timeToGo);
		timeToGoComment.setAnchor(new Vector(-6, 0));
	}

	@Override
	public void update(float deltaTime, float zoom) {

		if (!isDone) {
			if (isLeftPressed() && !timeToGo.isHovered() && !waitTime.isHovered()) {
				if (start == null)
					start = getFlooredMousePosition();
				else if (end == null && !start.equals(end)) {
					end = getFlooredMousePosition();
					distance = ExtendedMath.getDistance(start, end);
					evolution = new Vector(start.x - end.x, start.y - end.y).div(-distance);
				} else if (start.equals(getFlooredMousePosition())) {
					start = null;
					end = null;
				} else if (end.equals(getFlooredMousePosition()))
					end = null;
			}
			if (getOwner().getKeyboard().get(KeyEvent.VK_ENTER).isPressed()) {
				// isDone = !(timeToGo.hasFocus() & waitTime.hasFocus());
				// if (isDone)
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
		} else
			hover = ExtendedMath.isInRectangle(platform.getPosition(), platform.getPosition().add(5, 1),
					getMousePosition());
	}

	@Override
	public void draw(Canvas canvas) {

		if (!isDone) {
			Vector tempStart = (start == null) ? getFlooredMousePosition() : start;
			Vector tempEnd = (end == null) ? getFlooredMousePosition() : end;

			canvas.drawShape(new Circle(.1f), Transform.I.translated(tempStart), new Color(22, 84, 44), null, 0, 1, 15);
			canvas.drawShape(new Circle(.1f), Transform.I.translated(tempEnd), new Color(22, 84, 44), null, 0, 1, 15);

			canvas.drawShape(ExtendedMath.createRectangle(5, 1), Transform.I.translated(tempStart), new Color(200,200,200), null,
					0, .8f, 40);
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
		return hover;
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

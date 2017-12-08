/**
 *	Author: Clément Jeannet
 *	Date: 	6 déc. 2017
 */
package main.game.actor.actorBuilder;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.entities.MovingPlatform;
import main.math.*;
import main.window.Canvas;

import java.awt.*;

public class PlatformBuilder extends ActorBuilder {

	private MovingPlatform platform;
	private Vector start, end;

	private boolean isDone = false;

	public PlatformBuilder(ActorGame game) {
		super(game);

	}

	@Override
	public void update(float deltaTime) {
		if (!isDone) {
			if (isLeftPressed()) {
				if (start == null)
					start = getFlooredMousePosition();
				else if (end == null) {
					end = getFlooredMousePosition();
					float distance = ExtendedMath.getDistance(start, end);
					Vector v = new Vector(start.x - end.x, start.y - end.y).div(-distance);
					platform = new MovingPlatform(getOwner(), start, v, distance, .1f, .1f);
					isDone = true;
				}
			}
		}
		if (platform != null) {
			platform.update(deltaTime);
		}
	}

	@Override
	public void draw(Canvas canvas) {

		Vector tempStart = (start == null) ? getFlooredMousePosition() : start;
		Vector tempEnd = (end == null) ? getFlooredMousePosition() : end;

		if (!tempStart.equals(tempEnd))
			canvas.drawShape(new Polyline(tempStart, tempEnd), Transform.I, null, new Color(32, 246, 14), .1f, 1, 14);

		canvas.drawShape(new Circle(.1f), Transform.I.translated(tempStart), new Color(22, 84, 44), null, 0, 1, 15);
		canvas.drawShape(new Circle(.1f), Transform.I.translated(tempEnd), new Color(22, 84, 44), null, 0, 1, 15);

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
		// TODO Auto-generated method stub

	}

}

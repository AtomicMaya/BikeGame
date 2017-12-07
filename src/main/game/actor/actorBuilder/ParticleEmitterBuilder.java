/**
 *	Author: Clément Jeannet
 *	Date: 	7 déc. 2017
 */
package main.game.actor.actorBuilder;

import main.NumberField;
import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.entities.ParticleEmitter;
import main.math.Circle;
import main.math.Polyline;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;

public class ParticleEmitterBuilder extends ActorBuilder {

	private NumberField e;
	private Vector position, startAngle, endAngle;

	private boolean isDone = false;

	public ParticleEmitterBuilder(ActorGame game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		if (!isDone) {
			if (position == null)
				position = getFlooredMousePosition();
			else if (startAngle == null)
				startAngle = getFlooredMousePosition();
			else if (endAngle == null)
				endAngle = getFlooredMousePosition();
			isDone = true;
		}
	}

	@Override
	public void draw(Canvas canvas) {

		Vector tempPos = (position == null) ? getFlooredMousePosition() : position;
		Vector tempStart = (startAngle == null) ? getFlooredMousePosition() : startAngle;
		Vector tempEnd = (endAngle == null) ? getFlooredMousePosition() : endAngle;

		if (!tempStart.equals(tempPos))
			canvas.drawShape(new Polyline(tempStart, tempPos), Transform.I, null, new Color(32, 246, 14), .1f, 1, 14);
		if (!tempEnd.equals(tempPos) && !tempEnd.equals(tempEnd))
			canvas.drawShape(new Polyline(tempEnd, tempPos), Transform.I, null, new Color(32, 246, 14), .1f, 1, 14);

		canvas.drawShape(new Circle(.1f), Transform.I.translated(tempPos), new Color(22, 84, 44), null, 0, 1, 15);
		canvas.drawShape(new Circle(.1f), Transform.I.translated(tempStart), new Color(22, 84, 44), null, 0, 1, 15);
		canvas.drawShape(new Circle(.1f), Transform.I.translated(tempEnd), new Color(22, 84, 44), null, 0, 1, 15);

	}

	@Override
	public Actor getActor() {
		ParticleEmitter p = new ParticleEmitter(getOwner(), null, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
		return p;
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

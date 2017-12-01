package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ImageGraphics;
import main.game.actor.QuickMafs;
import main.game.actor.ShapeGraphics;
import main.math.Circle;
import main.math.Polygon;
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;

/**
 * Created on 12/1/2017 at 5:14 PM.
 */
public class MovingPlatform extends GameEntity {
	private ShapeGraphics railGraphics, anchor1Graphics, anchor2Graphics;
	private ImageGraphics platformGraphics;
	private ActorGame game;
	private Shape shape;
	private float loopTime, pauseTime;
	private float elapsedTime = 0.f;

	public MovingPlatform(ActorGame game, Vector startPosition, Vector endPosition, float loopTime, float pauseTime) {
		super(game, true, startPosition);

		this.shape = new Polygon(0f, 0f, 5f, 0f, 5f, 1f, 0f, 1f);
		this.loopTime = loopTime;
		this.pauseTime = pauseTime;

		Polygon rail = new Polygon(startPosition, startPosition.add(-.1f, .1f), endPosition.add(-1.f, .1f), endPosition);
		Circle anchor1 = new Circle(.5f);
		Circle anchor2 = new Circle(.5f, endPosition);
		railGraphics = addGraphics(rail, Color.LIGHT_GRAY, Color.DARK_GRAY, .2f, 1.f, 0f);
		anchor1Graphics = addGraphics(anchor1, Color.LIGHT_GRAY, Color.DARK_GRAY, .1f, 1.f, 0.1f);
		anchor2Graphics = addGraphics(anchor2, Color.LIGHT_GRAY, Color.DARK_GRAY, .1f, 1.f, 0.1f);
		platformGraphics = addGraphics("./res/images/stone.3.png", 5.f, 1.f);

		build(this.shape);


	}

	private Vector getNewPosition(Vector anchor, Vector initial, Vector goal, boolean paused) {
		float fullDistance = QuickMafs.getDistance(anchor, goal);
		float currentDistance = QuickMafs.getDistance(anchor, initial);
		currentDistance += currentDistance * (paused ? elapsedTime - pauseTime : elapsedTime) / loopTime;
		return new Vector(0,0);
		//TODO Finish
	}

	@Override
	public void update(float deltaTime) {
		elapsedTime += deltaTime;
		if (elapsedTime < loopTime) {

		} else if (elapsedTime < loopTime + pauseTime) {
		} else if (elapsedTime < 2 * loopTime + pauseTime) {
		} else if (elapsedTime < 2 * loopTime + 2 * pauseTime) {

		} else {
			elapsedTime = 0.f;
		}

	}

	@Override
	public void destroy() {
		super.destroy();
	}

	@Override
	public void draw(Canvas canvas) {

	}
}

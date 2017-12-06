/**
 *	Author: Clément Jeannet
 *	Date: 	5 déc. 2017
 */
package main.game.actor.actorBuilder;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.QuickMafs;
import main.game.actor.entities.BetterTextGraphics;
import main.game.actor.entities.Ground;
import main.math.Circle;
import main.math.Polyline;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

public class GroundBuilder extends ActorBuilder {

	private ArrayList<Vector> points = new ArrayList<>();
	private BetterTextGraphics textGraphics;
	private String text = "CLick where to put the line";

	private ActorGame game;

	private Polyline ground;

	public GroundBuilder(ActorGame game) {
		super(game);
		this.game = game;
		points.add(new Vector(-10, -10));
		points.add(new Vector(-10, 0));
		points.add(new Vector(10, 0));
		points.add(new Vector(10, -10));
		textGraphics = new BetterTextGraphics(game, text, 1);
	}

	@Override
	public void draw(Canvas canvas) {
		for (Vector v : points) {
			canvas.drawShape(new Circle(.1f), Transform.I.translated(v), Color.ORANGE, null, 0, 1, 12);
		}
		if (points.size() > 1)
			canvas.drawShape(ground, Transform.I, Color.decode("#6D5D49"), Color.decode("#548540"), .1f, 1, -10);

		canvas.drawShape(new Circle(.1f), Transform.I.translated(game.getMouse().getPosition()), new Color(22, 84, 44),
				null, 0, 1, 15);
	}

	@Override
	public void update(float deltaTime) {
		if (game.getMouse().getLeftButton().isPressed()) {

			Polygon p = new Polygon();
			for (Vector v : points) {
				p.addPoint((int) v.x, (int) v.y);
			}
			Vector v = QuickMafs.floor(game.getMouse().getPosition());
			// if (points.size() > 2) {
			// if (!p.contains(new Point((int) v.x, (int) v.y)))
			// addPoint(v);
			// } else
			addPoint(v);
			points = QuickMafs.sortVectorByX(points);
			System.out.println(points.size());
		}
		if (points.size() > 1)
			ground = new Polyline(points);
		if (game.getKeyboard().get(KeyEvent.VK_ESCAPE).isPressed())
			points.clear();
	}

	private void addPoint(Vector v) {
		if (points.contains(v))
			points.remove(v);
		else
			points.add(v);

	}

	@Override
	public Actor getActor() {
		return new Ground(game, Vector.ZERO, new Polyline(points));
	}

}

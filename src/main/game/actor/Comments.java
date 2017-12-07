/**
 *	Author: Clément Jeannet
 *	Date: 	6 déc. 2017
 */
package main.game.actor;

import main.game.ActorGame;
import main.math.Node;
import main.math.Polygon;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;

public class Comments extends Node implements Graphics {

	private String text;
	private float fontSize = 1f;

	private float zoom = 1;
	private Vector position = new Vector(-4, -12);
	private ActorGame game;

	public Comments(ActorGame game, String text) {
		this.game = game;
		this.text = text;
	}

	/**
	 * Update this Comment
	 * 
	 * @param zoom, default is 1
	 */
	public void update(float zoom) {
		this.zoom = zoom;

		setRelativeTransform(Transform.I.translated(position));
		if (getParent() == null)
			setRelativeTransform(getRelativeTransform().scaled(zoom).translated(game.getCanvas().getPosition()));
	}

	@Override
	public void draw(Canvas canvas) {
		float l = (text.length() + .4f) / 2;

		Vector shift = new Vector(-l + l / 2, 0).mul(zoom);
		canvas.drawShape(new Polygon(0, 0, 0, 1, l, 1, l, 0), getTransform().translated(shift),
				new Color(100, 100, 100), Color.BLACK, .02f, .5f, 1000);

		Vector v = new Vector(-l + l / 2 + .13f, .1f).mul(zoom);
		canvas.drawText((text.length() == 0) ? "1" : text, fontSize, getTransform().translated(v), Color.BLACK, null, 0,
				false, false, Vector.ZERO, 1, 1001);
	}

	public void setPosition(Vector newPosition) {
		this.position = newPosition;
	}
}

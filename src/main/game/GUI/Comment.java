/**
 *	Author: Clément Jeannet
 *	Date: 	6 déc. 2017
 */
package main.game.GUI;

import main.game.ActorGame;
import main.math.Polygon;
import main.math.Positionable;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;

/** Comment which can be attached to a {@linkplain Positionable} */
public class Comment extends GUIComponent {

	private String text;
	private float fontSize = 1f;

	public Comment(ActorGame game, String text) {
		super(game, new Vector(-8, 0)); // default position on the
										// screen
										// TODO better
		this.text = text;
	}

	@Override
	public void draw(Canvas canvas) {
		float l = (text.length() + .4f) / 2;

		Vector shift = new Vector(-l + l / 2, 0);
		canvas.drawShape(new Polygon(0, 0, 0, 1, l, 1, l, 0), getTransform(),
				new Color(100, 100, 100), Color.BLACK, .02f, .5f, 1338);

		canvas.drawText((text.length() == 0) ? "1" : text, fontSize, getTransform(), Color.BLACK, null, 0, false, false,
				new Vector(0f, 0), 1, 1339);
	}

	/**
	 * Set the text of this {@linkplain Comment}
	 * @param newText : text to be displayed
	 */
	public void setText(String newText) {
		this.text = newText;
	}

	@Override
	public boolean isHovered() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}

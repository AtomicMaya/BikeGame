package main.game.GUI;

import main.game.ActorGame;
import main.math.Polygon;
import main.math.Positionable;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;

/** Comment which can be attached to a {@linkplain Positionable} */
public class Comment extends GUIComponent {

	/** Text display by this {@linkplain Comment} */
	private String text;

	/** Font size of the {@link #text} */
	private float fontSize = 1f;

	/**
	 * Create a new {@linkplain Comment}, with default anchor Vector(0, 0)
	 * </br> <b>Warning</b> has to be updated if an other anchor is set
	 * @param game The master {@linkplain ActorGame}
	 * @param Text text to display, can be null
	 */
	public Comment(ActorGame game, String text) {
		super(game, new Vector(0, 0)); 
		this.text = text;
	}

	@Override
	public void draw(Canvas canvas) {
		float l = (text.length() + .2f) / 2;

		canvas.drawShape(new Polygon(-l/2f, 0, -l/2f, 1, l/2f, 1, l/2f, 0), getTransform(), new Color(100, 100, 100), Color.BLACK,
				.02f, .5f, 1338);

		canvas.drawText((text.length() == 0) ? "1" : text, fontSize, getTransform(), Color.BLACK, null, 0, false, false,
				new Vector(.5f, 0), 1, 1339);
	}

	/**
	 * Set the text of this {@linkplain Comment}
	 * @param newText : text to be displayed
	 */
	public void setText(String newText) {
		this.text = newText;
	}
	
	/** 
	 * Set the anchor, (relative position to its parent)
	 * correspond to the center of this {@linkplain Comment} in x 
	 * </br> For example, if its width is 4 , and its anchor is (-10,0), it will go from -12 to -8, 
	 * relative to its parent, or the Vector (0,0) if no parent
	 */
	@Override
	public void setAnchor(Vector anchor) {
		super.setAnchor(anchor);
	}

	@Override
	public boolean isHovered() {
		// TODO if we want to hover a Comment for something
		return false;
	}

	@Override
	public void destroy() {
		// nothing to destroy 
	}
}

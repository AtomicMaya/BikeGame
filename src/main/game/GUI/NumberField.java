package main.game.GUI;

import main.game.ActorGame;
import main.math.ExtendedMath;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

/** Field to enter a number which can be then recover */
public class NumberField extends GUIComponent {

	private String text;
	private ActorGame game;
	private float fontSize = 1f;

	// private Vector position = Vector.ZERO;
	private boolean focus = false;

	private float height, width;

	private boolean hover = false;
	private float zoom = 1f;

	private float clignote = 0;
	private final float timeClignotMax = .6f;

	/**
	 * Create a new {@linkplain NumberField}
	 * @param game : {@linkplain ActorGame} where this belong
	 * @param position : position on the screen
	 * @param width : width of the background
	 * @param height : height of the background
	 * @param value : initial value of the field
	 */
	public NumberField(ActorGame game, Vector position, float width, float height, int value) {
		super(game, position);
		this.game = game;
		this.width = width;
		this.height = height;
		this.text = "" + value;
	}

	/**
	 * Simulates a single time step.
	 * @param zoom, 1 for default
	 * @param deltaTime elapsed time since last update, in seconds, non-negative
	 */
	public void update(float deltaTime, float zoom) {
		super.update(deltaTime, zoom);
		this.zoom = zoom;
		clignote += deltaTime;
		clignote = (clignote > timeClignotMax) ? 0 : clignote;

		hover = ExtendedMath.isInRectangle(getPosition(), getPosition().add(width * zoom, height * zoom),
				getMousePosition());

		if (isLeftPressed())
			focus = hover;

		if (focus) {
			if (text.length() < 3)
				for (int i = 48; i < 58; i++) {
					if (game.getKeyboard().get(i).isPressed()) {
						if (text.contains(".0"))
							text = text.charAt(0) + ".";
						text += i - 48;
					}
				}
			if (game.getKeyboard().get(KeyEvent.VK_BACK_SPACE).isPressed() && text.length() > 0) {
				text = text.substring(0, text.length() - 1);

			}
			if (game.getKeyboard().get(46).isPressed() && !text.contains(".") && text.length() < 2) {
				text += ".";
			}
			if (game.getKeyboard().get(KeyEvent.VK_ENTER).isPressed()) {
				focus = false;
			}
		}
	}

	/** @return the number entered in the field */
	public float getNumber() {
		return Float.parseFloat((text.isEmpty()) ? "1" : text);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawShape(new Polygon(0, 0, 0, height, width, height, width, 0), getTransform(),
				(focus) ? Color.RED : Color.LIGHT_GRAY, Color.BLACK, .03f * zoom, (focus) ? .5f : .7f, 59);

		float l = (text.length() == 0) ? 1 : text.length();

		Vector clignotePos = new Vector(width / 2 + text.length() * .22f, .1f).mul(zoom);

		if (clignote < timeClignotMax / 2f && focus)
			canvas.drawShape(new Polygon(0, 0, 0, .8f, 0.06f, .8f, .06f, 0), getTransform().translated(clignotePos),
					Color.BLACK, null, 0, 1, 60);

		Vector v = new Vector(width / 2 - l * .26f, .13f).mul(zoom);
		canvas.drawText((text.length() == 0) ? " " : text, fontSize, getTransform().translated(v), Color.BLACK, null, 0,
				false, false, Vector.ZERO, 1, 60);

	}

	/**
	 * Set the font size
	 * @param size : font size
	 */
	public void setFontSize(float size) {
		this.fontSize = size;
	}

	/***
	 * Set the number in the {@linkplain NumberField}
	 * @param number : number to set in the field
	 */
	public void setNumber(float number) {
		text = "" + number;
	}

	/** @return whether this {@linkplain NumberField} has the focus */
	public boolean hasFocus() {
		return !focus;
	}

	/**
	 * @return whether this {@linkplain NumberField} is hovered by the
	 * {@linkplain Mouse}
	 */
	public boolean isHovered() {
		return this.hover;
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}
}

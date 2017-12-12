package main.game.GUI;

import main.game.ActorGame;
import main.math.ExtendedMath;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

/** Field to enter a number which can then be then recovered */
public class NumberField extends GUIComponent {

	/** Text display by this {@linkplain NumberField} */
	private String text;

	/** Font size of the text displayed */
	private float fontSize = 1f;
	
	/** Default value for the height */
	private static final float defaultfontSize = 1;

	/** Whether this {@linkplain NumberField} has the focus. */
	private boolean focus = false;

	/** Size of this {@linkplain NumberField}. */
	private float height = 1, width = 3;
	
	/** Default value for the width */
	private static final float defaultWidth = 3;
	
	/** Default value for the height */
	private static final float defaultHeight = 1;

	/**
	 * Whether this {@linkplain NumberField} is hovered by the
	 * {@linkplain Mouse}.
	 */
	private boolean hover = false;

	/** Timer for the blink */
	private float clignote = 0;

	/** Time the cursor take to blink */
	private final float timeClignotMax = .6f;

	/** Return value of this {@linkplain NumberField}, 1 if empty */
	private int value;

	/**
	 * Create a new {@linkplain NumberField}
	 * @param game : {@linkplain ActorGame} where this belong
	 * @param position : position on the screen
	 * @param value : initial value of the field
	 */
	public NumberField(ActorGame game, Vector position, int value) {
		super(game, position);
		this.width = defaultWidth;
		this.height = defaultHeight;
		this.fontSize = defaultfontSize;
		this.text = "" + value;
		this.value = value;
	}

	/**
	 * Simulates a single time step.
	 * @param zoom, 1 for default
	 * @param deltaTime elapsed time since last update, in seconds, non-negative
	 */
	public void update(float deltaTime, float zoom) {
		super.update(deltaTime, zoom);
		clignote += deltaTime;
		clignote = (clignote > timeClignotMax) ? 0 : clignote;

		hover = ExtendedMath.isInRectangle(getPosition(), getPosition().add(width * zoom, height * zoom),
				getMousePosition());

		if (isLeftPressed())
			focus = hover;

		if (focus) {
			if (text.length() < 3)
				for (int i = 48; i < 58; i++) {
					if (getOwner().getKeyboard().get(i).isPressed()) {
						if (text.contains(".0"))
							text = text.charAt(0) + ".";
						text += i - 48;
					}
				}
			if (getOwner().getKeyboard().get(KeyEvent.VK_BACK_SPACE).isPressed() && text.length() > 0) {
				text = text.substring(0, text.length() - 1);

			}
			if (getOwner().getKeyboard().get(46).isPressed() && !text.contains(".") && text.length() < 2) {
				text += ".";
			}
			if (getOwner().getKeyboard().get(KeyEvent.VK_ENTER).isPressed()) {
				focus = false;
			}
		}
	}

	/** @return the number entered in the field */
	public float getNumber() {
		return Float.parseFloat((text.isEmpty()) ? "" + value : text);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawShape(new Polygon(0, 0, 0, height, width, height, width, 0), getTransform(),
				(focus) ? Color.RED : Color.LIGHT_GRAY, Color.BLACK, .03f * getZoom(), (focus) ? .5f : .7f, 59);

		float l = (text.length() == 0) ? 1 : text.length();

		Vector clignotePos = new Vector(width / 2 + text.length() * .22f, .1f).mul(getZoom());

		if (clignote < timeClignotMax / 2f && focus)
			canvas.drawShape(new Polygon(0, 0, 0, .8f, 0.06f, .8f, .06f, 0), getTransform().translated(clignotePos),
					Color.BLACK, null, 0, 1, 60);

		Vector v = new Vector(width / 2 - l * .26f, .13f).mul(getZoom());
		canvas.drawText((text.length() == 0) ? " " : text, fontSize, getTransform().translated(v), Color.BLACK, null, 0,
				false, false, Vector.ZERO, 1, 60);

	}

	/** 
	 * Set the size of this {@linkplain NumberField},
	 * @param width New width, if negative, keep the previous value (default to {@value #defaultWidth}
	 * @param height New height, if negative, keep the previous value (default to {@value #defaultHeight}
	 * */
	public void reSize(float width, float height) {
		this.width = (width < 0) ? this.width : width;
		this.height = (height < 0) ? this.height : height;
	}

	/**
	 * Set the font size
	 * @param size : font size, if negative, keep the previous value (default to {@value #defaultfontSize}
	 */
	public void setFontSize(float size) {
		this.fontSize = (size < 0) ? this.fontSize : size;
	}

	/***
	 * Set the number in the {@linkplain NumberField}
	 * @param number : number to set in the field
	 */
	public void setNumber(float number) {
		text = "" + number;
	}

	/** @return whether this {@linkplain NumberField} has the focus (the last click of the {@linkplain Mouse} was on this) */
	public boolean hasFocus() {
		return focus;
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
		// nothing to destroy here
	}
}

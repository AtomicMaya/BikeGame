package main.game.GUI;

import main.game.ActorGame;
import main.game.actor.ParallelAction;
import main.game.graphics.BetterTextGraphics;
import main.math.ExtendedMath;
import main.math.Polygon;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.util.ArrayList;

/** Button wich can be clicked to execute an action */
public class GraphicalButton extends GUIComponent {

	/** Path to the images */
	private String idleGraphics, hoverGraphics;
	
	/** List of time, for when this button is supposed to be busy */
	private ArrayList<Float> time = new ArrayList<>();
	
	/** List of actions to run when this button is clicked */
	private ArrayList<Runnable> actions = new ArrayList<>();

	/** Drowning parameters */
	private float alpha = .6f, depth = -.02f;

	/** Whether this button is hovered */
	private boolean hovered;
	
	/** Whether this button has been clicked */
	private boolean clicked;
	
	/** Whether this button is busy */
	private boolean buttonBusy = false;

	/** Timer */
	private float timeToActionEnd = 0.f, elapsedActionTime = 0.f;
	
	/** Text graphics */
	private BetterTextGraphics textGraphics;

	/** Default size */
	private float width = 1, height = 1;

	/** Default offset of the text */
	private final Vector defaultTextOffset;
	
	/** Actual shift of the text */
	private Vector shiftText = Vector.ZERO;
	
	/** Whether we forced the text shift */
	private boolean forcedTextShift = false;

	/** Background shape */
	private Polygon shape;
	
	/** Font size */
	private float fontSize;

	/** 
	 * Create a new {@linkplain GraphicalButton}
	 * @param game The master {@linkplain ActorGame}
	 * @param text Text to display
	 * @param fontSize Font size
	 * */
	public GraphicalButton(ActorGame game, Vector position, String text, float fontSize) {
		super(game, position);
		this.fontSize = fontSize;
		defaultTextOffset = new Vector(fontSize / 4f, fontSize / 4f);

		shiftText = new Vector(defaultTextOffset.x, defaultTextOffset.y);
		this.setText(text, fontSize);
	}

	/** 
	 * Create a new {@linkplain GraphicalButton}
	 * @param game The master {@linkplain ActorGame}
	 * @param width With of the {@linkplain GraphicalButton}
	 * @param height Height of the {@linkplain GraphicalButton}
	 * */
	public GraphicalButton(ActorGame game, Vector position, float width, float height) {
		super(game, position);
		this.defaultTextOffset = new Vector(0, 0);// does not matter
		this.width = width;
		this.height = height;
		forceShape(width, height);
	}

	@Override
	public void update(float deltaTime, float zoom) {
		super.update(deltaTime, zoom);

		if (textGraphics != null) {
			textGraphics.setAnchor(shiftText.mul(zoom));
			textGraphics.setText(textGraphics.getText(), fontSize * zoom);
		}

		if (ExtendedMath.isInRectangle(getPosition(), getPosition().add(new Vector(width * zoom, height * zoom)),
				getMousePosition())) {
			this.hovered = true;
			this.clicked = getOwner().getMouse().getLeftButton().isReleased();
		} else {
			this.hovered = false;
		}

		if (this.clicked & !this.buttonBusy) {
			this.buttonBusy = true;
			for (int i = 0; i < this.actions.size(); i++) {
				this.runAction(this.actions.get(i), this.time.get(i));
			}
			this.clicked = false;
		}

		if (this.buttonBusy) {
			this.elapsedActionTime += deltaTime;
			if (this.elapsedActionTime > this.timeToActionEnd) {
				this.buttonBusy = false;
				this.elapsedActionTime = 0.f;
			}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		Transform t = new Transform(width * getZoom(), getTransform().m01, getTransform().m02, getTransform().m10,
				height * getZoom(), getTransform().m12);
		if (this.hovered) {
			if (hoverGraphics != null) {
				canvas.drawImage(canvas.getImage(hoverGraphics), t, 1, depth);
			} else
				canvas.drawShape(shape, getTransform(), Color.RED, Color.ORANGE, .1f * getZoom(), alpha, depth);
		} else {
			if (idleGraphics != null) {
				canvas.drawImage(canvas.getImage(idleGraphics), t, 1, depth);
			} else
				canvas.drawShape(shape, getTransform(), Color.GREEN, Color.ORANGE, .1f * getZoom(), alpha, depth);
		}
		if (textGraphics != null) {
			textGraphics.draw(canvas);
		}
	}

	/**
	 * Sets new graphics to the button
	 * 
	 * @param idleGraphics : Graphics when the button is Idle
	 * @param hoverGraphics : Graphics when the button is Hovered
	 */
	public void setNewGraphics(String idleGraphics, String hoverGraphics) {
		this.idleGraphics = idleGraphics;
		this.hoverGraphics = hoverGraphics;
	}

	/**
	 * Adds runnable actions to this button
	 * 
	 * @param action : the action to run
	 * @param expirationTime : When this button shouldn't be considered busy
	 * anymore.
	 */
	public void addOnClickAction(Runnable action, float expirationTime) {
		this.actions.add(action);
		this.time.add(expirationTime);
	}

	/**
	 * Adds runnable actions to this button
	 * 
	 * @param action : the action to run, with default stop spam time
	 */
	public void addOnClickAction(Runnable action) {
		this.actions.add(action);
		this.time.add(.1f);
	}

	/**
	 * Runs a runnable action in parallel to this thread.
	 * 
	 * @param runnable : the action to run
	 * @param time : When the action should expire.
	 */
	private void runAction(Runnable runnable, float time) {
		this.buttonBusy = true;
		this.timeToActionEnd = time > this.timeToActionEnd ? time : this.timeToActionEnd;
		ParallelAction.generateWorker(runnable).execute();
	}

	/**
	 * Change the text and the fontSize
	 * 
	 * @param text new text
	 * @param fontSize new font size
	 */
	public void setText(String text, float fontSize) {

		if (text != null) {
			textGraphics = new BetterTextGraphics(getOwner(), (text == "") ? textGraphics.getText() : text, fontSize,
					shiftText);
			textGraphics.setDepth(depth + 0.6f);
			textGraphics.setParent(this);
		}
		forceShape(-1, -1);
	}

	/**
	 * Force a width and a height to this button, a negative parameter will take
	 * the default/previous one
	 * 
	 * @param width width of the button
	 * @param height height of the button
	 */
	public void forceShape(float width, float height) {
		if (textGraphics != null) {

			this.width = (width < 0) ? textGraphics.getTotalWidth() + shiftText.x * 2f : width;
			this.height = (height < 0) ? textGraphics.getCharSize() + shiftText.y * 2f : height;

			if (!forcedTextShift)
				shiftText = new Vector((this.width - textGraphics.getTotalWidth()) / 2f,
						textGraphics.getCharSize() / 4f);
		} else {
			this.width = (width < 0) ? this.width : width;
			this.height = (height < 0) ? this.height : height;
		}
		shape = new Polygon(0, 0, this.width, 0, this.width, this.height, 0, this.height);

	}

	/**
	 * Force the start and end offset
	 * 
	 * @param offset shift
	 */
	public void forceStartEndOffset(Vector offset) {
		if (textGraphics != null) {
			forcedTextShift = true;
			if (offset != null)
				shiftText = offset;
			else
				shiftText = new Vector(textGraphics.getCharSize() / 4f, textGraphics.getCharSize() / 4f);
			width = textGraphics.getTotalWidth();
			height = textGraphics.getCharSize();

			forceShape(width, height);
		}
	}

	/**
	 * @return weather this button is hovered
	 */
	public boolean isHovered() {
		return this.hovered;
	}

	/**
	 * Add a space between each char in the text
	 * 
	 * @param value space value
	 */
	public void forceInbetweenCharOffset(float value) {
		if (textGraphics != null) {
			textGraphics.setInBetweenCharTextOffset(value);
			forceShape(-1, -1);
		}
	}

	/** @return this {@linkplain GraphicalButton} width */
	public float getWidth() {
		return width;
	}
	
	/** @return this {@linkplain GraphicalButton} height */
	public float getHeight() {
		return height;
	}

    /**
     * Sets the viewDepth of the {@linkplain GraphicalButton}.
     * @param depth : The wanted depth.
     */

	public void setDepth(float depth) {
		this.depth = depth;
		this.setNewGraphics(idleGraphics, hoverGraphics);
		if (textGraphics != null)
			textGraphics.setDepth(depth + 0.01f);
	}

	@Override
	public void destroy() {
		// nothing to destroy
	}
}

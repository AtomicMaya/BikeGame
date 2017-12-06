package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Graphics;
import main.math.Polygon;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;
import main.window.Mouse;

import java.awt.*;
import java.util.ArrayList;

public class GraphicalButton extends GameEntity {
	private Mouse mouse;

	private ArrayList<Graphics> graphics = new ArrayList<>();
	private String idleGraphics, hoverGraphics;
	private ArrayList<Float> time = new ArrayList<>();
	private ArrayList<Runnable> actions = new ArrayList<>();

	private float alpha = .6f, depth = -.02f;
	private float minX, minY, maxX, maxY;

	private boolean hovered, clicked;
	private boolean buttonBusy = false;

	private float timeToActionEnd = 0.f, elapsedActionTime = 0.f;
	private BetterTextGraphics textGraphics;

	private Vector maxPosition;

	private final Vector defaultTextOffset;
	private Vector shiftText = Vector.ZERO;
	private boolean forcedTextShift = false;

	public GraphicalButton(ActorGame game, Vector position, String text, float fontSize) {
		super(game, true, position);
		this.mouse = game.getMouse();

		defaultTextOffset = new Vector(fontSize / 4f, fontSize / 4f);

		shiftText = new Vector(defaultTextOffset.x, defaultTextOffset.y);
		this.setText(text, fontSize);
	}

	public GraphicalButton(ActorGame game, Vector position, float width, float height) {
		super(game, true, position);
		this.mouse = game.getMouse();
		defaultTextOffset = new Vector(0, 0);// does not matter
		forceShape(width, height);
	}

	private void changeStuff(Vector position, Polygon shape) {
		this.graphics = new ArrayList<>();
		if (this.idleGraphics == null) {
			this.graphics.add(addGraphics(shape, Color.GREEN, Color.ORANGE,
					.1f * (textGraphics == null ? 1 : textGraphics.getCharSize()), alpha, depth));
			this.graphics.add(addGraphics(shape, Color.RED, Color.ORANGE,
					.1f * (textGraphics == null ? 1 : textGraphics.getCharSize()), alpha, depth));
		} else
			this.setNewGraphics(idleGraphics, hoverGraphics);

		this.maxPosition = shape.getPoints().get((shape.getPoints().size()) / (2));
		this.minX = position.x;
		this.minY = position.y;
		this.maxX = this.minX + maxPosition.x;
		this.maxY = this.minY + maxPosition.y;
	}

	@Override
	public void update(float deltaTime) {
		Vector mousePosition = this.mouse.getPosition();
		float mouseX = mousePosition.x, mouseY = mousePosition.y;
		if (this.minX <= mouseX && mouseX <= this.maxX && this.minY < mouseY && mouseY < this.maxY) {
			this.hovered = true;
			this.clicked = this.mouse.getLeftButton().isPressed();
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
		if (this.hovered)
			this.graphics.get(1).draw(canvas);
		else
			this.graphics.get(0).draw(canvas);
		if (textGraphics != null) {
			textGraphics.setRelativeTransform(Transform.I.translated(shiftText));
			textGraphics.draw(canvas);
		}
	}

	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
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
		this.graphics = new ArrayList<>();
		this.graphics
				.add(addGraphics(idleGraphics, this.maxX - this.minX, this.maxY - this.minY, Vector.ZERO, 1, depth));
		this.graphics
				.add(addGraphics(hoverGraphics, this.maxX - this.minX, this.maxY - this.minY, Vector.ZERO, 1, depth));
	}

	/**
	 * Adds runnable actions to this button
	 * 
	 * @param action : the action to run
	 * @param expirationTime : When this button shouldn't be considered busy
	 *            anymore.
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

	@Override
	public void setPosition(Vector position) {
		super.setPosition(position);
		this.minX = position.x;
		this.minY = position.y;
		this.maxX = this.minX + maxPosition.x;
		this.maxY = this.minY + maxPosition.y;
	}

	/**
	 * Change the text and the fontSize
	 * 
	 * @param text new text
	 * @param fontSize new font size
	 */
	public void setText(String text, float fontSize) {

		textGraphics = new BetterTextGraphics(getOwner(), (text == null) ? textGraphics.getText() : text, fontSize);
		textGraphics.setDepth(depth+.1f);
		textGraphics.setParent(this);

		forceShape(-1, -1);
	}

	/**
	 * Force a width and a height to this button, a negative parameter will take the
	 * default/previous one
	 * 
	 * @param width width of the button
	 * @param height height of the button
	 */
	public void forceShape(float width, float height) {
		if (textGraphics != null) {

			width = (width < 0) ? textGraphics.getTotalWidth() + shiftText.x * 2f : width;
			height = (height < 0) ? textGraphics.getCharSize() + shiftText.y * 2f : height;

			if (!forcedTextShift)
				shiftText = new Vector(textGraphics.getCharSize() / 4f, textGraphics.getCharSize() / 4f);
		} else {
			width = (width < 0) ? maxX - minX : width;
			height = (height < 0) ? maxY - minY : height;
		}
		Polygon shape = new Polygon(0, 0, width, 0, width, height, 0, height);

		changeStuff(getPosition(), shape);
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
			float width = textGraphics.getTotalWidth();
			float height = textGraphics.getCharSize();

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

	public float getWidth() {
		return maxX - minX;
	}

	public void setDepth(float depth) {
		this.depth = depth;
		this.setNewGraphics(idleGraphics, hoverGraphics);
	}
}

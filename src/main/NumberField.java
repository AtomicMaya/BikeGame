/**
 *	Author: Clément Jeannet
 *	Date: 	6 déc. 2017
 */
package main;

import main.game.ActorGame;
import main.game.actor.Graphics;
import main.game.actor.QuickMafs;
import main.math.Node;
import main.math.Polygon;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

public class NumberField extends Node implements Graphics {

	private String text;
	private ActorGame game;
	private float fontSize = 1f;

	private Vector position;
	private boolean focus = false;

	private float height, width;

	private boolean hover = false;
	private float zoom = 1f;

	public NumberField(ActorGame game, Vector position, float width, float height, float value) {
		this.game = game;
		this.width = width;
		this.height = height;
		if (position != null)
			this.position = position;
		this.text = "" + value;
		setRelativeTransform(Transform.I.translated(position));

	}

	public void update(float deltaTime) {
		hover = QuickMafs.isInRectangle(getPosition(), getPosition().add(width * zoom, height * zoom),
				game.getMouse().getPosition());
		
		if (game.getMouse().getLeftButton().isPressed())
			focus = hover;

		if (focus) {
			if (text.length() < 5)
				for (int i = 48; i < 57; i++) {
					if (game.getKeyboard().get(i).isPressed()) {
						text += i - 48;
					}
				}
			if (game.getKeyboard().get(KeyEvent.VK_BACK_SPACE).isPressed() && text.length() > 0) {
				text = text.substring(0, text.length() - 1);

			}
			if (game.getKeyboard().get(46).isPressed() && !text.contains(".")) {
				text += ".";
			}
			if (game.getKeyboard().get(KeyEvent.VK_ENTER).isPressed()) {
				focus = false;
			}

		}
		setRelativeTransform(Transform.I.translated(position).scaled(zoom).translated(game.getCanvas().getPosition()));
	}

	public float getNumber() {
		return Float.parseFloat((text == "") ? "1" : text);
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.drawShape(new Polygon(0, 0, 0, height, width, height, width, 0), getTransform(), Color.LIGHT_GRAY,
				Color.BLACK, .02f, .5f, 59);
		float l = (text.length() == 0) ? 1 : text.length();

		Vector v = new Vector(width / 2 - l * .26f, .13f).mul(zoom);;

		canvas.drawText((text.length() == 0) ? "1" : text, fontSize, getTransform().translated(v), Color.BLACK, null, 0,
				false, false, Vector.ZERO, 1, 60);

	}

	public void setFontSize(float size) {
		this.fontSize = size;
	}

	public void setNumber(float n) {
		text = "" + n;
	}

	public void setZoom(float zoom) {
		this.zoom = zoom;
	}

	public boolean finishTyping() {
		return !focus;
	}

	public boolean isHovered() {
		return this.hover;
	}

}

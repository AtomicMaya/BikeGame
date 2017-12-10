/**
 *	Author: Clément Jeannet
 *	Date: 	9 déc. 2017
 */
package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.GraphicalButton;
import main.game.actor.Actor;
import main.game.actor.entities.Liquid;
import main.math.*;
import main.math.Polygon;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

public class LiquidBuilder extends ActorBuilder {

	private boolean isDone = false;
	private Vector start, end;

	private Liquid liquid;
	private Polygon shape;
	private Vector position;

	private GraphicalButton askLava;
	private Vector askLavaPos = new Vector(22, 8);
	private Comment askLavaComment;

	private String acideText = "Change for acid", lavaText = "Change for lava";
	private boolean isLava = true;

	private boolean hover = false;

	public LiquidBuilder(ActorGame game) {
		super(game);

		askLava = new GraphicalButton(getOwner(), askLavaPos, "Acid or lava", 1);
		askLava.setAnchor(askLavaPos);
		askLava.addOnClickAction(() -> {
			isLava = !isLava;
			askLavaComment.setText(isLava ? acideText : lavaText);

			if (liquid != null) {
				liquid.destroy();
				liquid = new Liquid(getOwner(), position, shape, isLava);
			}

		});
		askLavaComment = new Comment(game, acideText);
		askLavaComment.setParent(askLava);
		askLavaComment.setAnchor(new Vector(-10, 0));
	}

	@Override
	public void update(float deltaTime, float zoom) {

		if (!isDone) {
			if (isLeftPressed()) {
				if (start == null)
					start = getFlooredMousePosition();
				else if (end == null) {
					end = getFlooredMousePosition();
					shape = ExtendedMath.createRectangle(start, end);
					position = new Vector(start.x < end.x ? start.x : end.x, start.y < end.y ? start.y : end.y);
					liquid = new Liquid(getOwner(), position, shape, isLava);
				}
			}
			if (liquid != null && getOwner().getKeyboard().get(KeyEvent.VK_ENTER).isPressed())
				isDone = true;
			askLava.update(deltaTime, zoom);
			if (askLava.isHovered())
				askLavaComment.update(deltaTime, zoom);
		} else
			hover = ExtendedMath.isInRectangle(start, end, getMousePosition());
		if (liquid != null) {
			liquid.update(deltaTime);

		}
		if (isDone && isHovered() && isRightPressed())
			isDone = false;
	}

	@Override
	public void draw(Canvas canvas) {
		if (liquid != null)
			liquid.draw(canvas);
		if (!isDone()) {
			askLava.draw(canvas);
			if (askLava.isHovered())
				askLavaComment.draw(canvas);

			Vector tempStart = (start == null) ? getFlooredMousePosition() : start;
			Vector tempEnd = (end == null) ? getFlooredMousePosition() : end;

			if (!tempStart.equals(tempEnd))
				canvas.drawShape(new Polyline(tempStart, tempEnd), Transform.I, null, new Color(32, 246, 14), .1f, 1,
						14);

			canvas.drawShape(new Circle(.1f), Transform.I.translated(tempStart), new Color(22, 84, 44), null, 0, 1, 15);
			canvas.drawShape(new Circle(.1f), Transform.I.translated(tempEnd), new Color(22, 84, 44), null, 0, 1, 15);
		}
	}

	@Override
	public boolean isHovered() {
		return hover;
	}

	@Override
	public void destroy() {
		if (liquid != null)
			liquid.destroy();
	}

	@Override
	public Actor getActor() {

		return liquid;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		liquid.destroy();
		liquid = new Liquid(getOwner(), position, shape, isLava);
	}

	@Override
	public void edit() {
		this.isDone = false;
	}

}

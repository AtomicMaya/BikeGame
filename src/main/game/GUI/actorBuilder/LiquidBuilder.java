/**
 * Author: Clément Jeannet Date: 9 déc. 2017
 */
package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.GraphicalButton;
import main.game.GUI.menu.LevelEditor;
import main.game.actor.Actor;
import main.game.actor.entities.Liquid;
import main.math.*;
import main.math.Polygon;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Use in the {@linkplain LevelEditor} to build and add a new {@linkplain Liquid}
 */
public class LiquidBuilder extends ActorBuilder {

	/**
	 * Whether this {@linkplain LiquidBuilder} has finished building its
	 * {@linkplain Liquid}
	 */
	private boolean isDone = false;
	
	/** Area {@linkplain Vector} parameter */
	private Vector start, end;

	/** {@linkplain Liquid} created and returned by {@link #getActor()} */
	private Liquid liquid;
	
	/** Shape of this {@linkplain Liquid} */
	private Polygon shape;
	
	/** Position to give to the {@linkplain Liquid} */
	private Vector position;

	/** {@linkplain GraphicalButton} used to ask the user if the {@linkplain Liquid} is acid or lava */
	private GraphicalButton askLava;
	
	/** Absolute position on screen of the {@linkplain GraphicalButton} {@link #askLava} */
	private Vector askLavaPos = new Vector(22, 8);
	
	/** {@linkplain Comment} of the {@linkplain GraphicalButton} {@link #askLava} */
	private Comment askLavaComment;

	/** Text value of the {@linkplain Comment} {@link #askLavaComment} */
	private String acideText = "Change for acid", lavaText = "Change for lava";
	
	/** Whether this {@linkplain Liquid} is lava or acid */
	private boolean isLava = true; 

	/**
	 * Create a new {@linkplain LiquidBuilder}
	 * @param game The master {@linkplain ActorGame}
	 */
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
		super.update(deltaTime, zoom);
		if (!isDone) {
			if (isLeftPressed()) {
				if (start == null)
					start = getHalfFlooredMousePosition();
				else if (end == null) {
					end = getHalfFlooredMousePosition();
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
		}
		if (liquid != null) {
			liquid.update(deltaTime);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (liquid != null)
			liquid.draw(canvas);
		if (!isDone()) {
			askLava.draw(canvas);
			if (askLava.isHovered())
				askLavaComment.draw(canvas);

			Vector tempStart = (start == null) ? getHalfFlooredMousePosition() : start;
			Vector tempEnd = (end == null) ? getHalfFlooredMousePosition() : end;

			if (!tempStart.equals(tempEnd))
				canvas.drawShape(new Polyline(tempStart, tempEnd), Transform.I, null, new Color(32, 246, 14), .1f, 1,
						14);

			canvas.drawShape(new Circle(.1f), Transform.I.translated(tempStart), new Color(22, 84, 44), null, 0, 1, 15);
			canvas.drawShape(new Circle(.1f), Transform.I.translated(tempEnd), new Color(22, 84, 44), null, 0, 1, 15);
		}
	}

	@Override
	public boolean isHovered() {
		if (start == null | end == null)
			return false;
		return ExtendedMath.isInRectangle(start, end, getMousePosition());

	}

	@Override
	public void destroy() {
		if (liquid != null)
			liquid.destroy();
	}

	@Override
	public Actor getActor() {
		reCreate();
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

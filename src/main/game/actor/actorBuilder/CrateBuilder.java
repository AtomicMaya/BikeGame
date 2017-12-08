/**
 *	Author: Clément Jeannet
 *	Date: 	4 déc. 2017
 */
package main.game.actor.actorBuilder;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.Comment;
import main.game.actor.NumberField;
import main.game.actor.crate.Crate;
import main.game.actor.menu.LevelEditor;
import main.math.Vector;
import main.window.Canvas;

import java.awt.event.KeyEvent;

public class CrateBuilder extends ActorBuilder {

	private Crate crate;
	private Vector position;

	boolean placed = false;

	private ActorGame game;
	private LevelEditor lv;

	// number field stuff
	private NumberField height, width;
	private Vector heightNumberFieldPos, widthNumberFieldPos;
	private Comment heightComment, widthComments;

	private boolean isWriting = true;
	private boolean hoover = false;

	public CrateBuilder(ActorGame game, LevelEditor lv) {
		super(game);
		this.game = game;
		this.lv = lv;
		crate = new Crate(game, getFlooredMousePosition(), null, false, 1);

		heightNumberFieldPos = new Vector(26, 6);
		height = new NumberField(game, heightNumberFieldPos, 3, 1, 1);

		heightComment = new Comment(game, "Crate Height");
		heightComment.setParent(height);
		heightComment.setPosition(new Vector(-6, 0));

		widthNumberFieldPos = new Vector(26, 8);
		width = new NumberField(game, widthNumberFieldPos, 3, 1, 1);

		widthComments = new Comment(game, "Crate Width");
		widthComments.setParent(width);
		widthComments.setPosition(new Vector(-6, 0));
	}

	@Override
	public void update(float deltaTime) {

		if (!placed) {
			position = getFlooredMousePosition();
			if (isLeftPressed()) {
				placed = true;
			}
			crate.setPosition(position);

		}
		if (!isDone()) {
			height.update(lv.getZoom(), deltaTime);
			width.update(lv.getZoom(), deltaTime);

			heightComment.update(lv.getZoom());
			widthComments.update(lv.getZoom());

			if (game.getKeyboard().get(KeyEvent.VK_ENTER).isPressed()) {
				isWriting = !(height.hasFocus() & width.hasFocus());
				crate.setSize(width.getNumber(), height.getNumber());
			}
		} else
			hoover = QuickMafs.isInRectangle(position, position.add(width.getNumber(), height.getNumber()),
					getFlooredMousePosition());
		if (hoover && isRightPressed())
			isWriting = true;
	}

	@Override
	public void draw(Canvas canvas) {
		crate.draw(canvas);
		if (!isDone()) {
			height.draw(canvas);
			if (height.isHovered())
				heightComment.draw(canvas);
			width.draw(canvas);
			if (width.isHovered())
				widthComments.draw(canvas);
		}
	}

	@Override
	public Actor getActor() {
		return crate;
	}

	@Override
	public boolean isDone() {
		return placed & !isWriting;
	}

	@Override
	public void reCreate() {
		crate.destroy();
		crate = new Crate(game, position, null, false, width.getNumber(), height.getNumber());
	}

	@Override
	public boolean isHovered() {
		return hoover;
	}

	@Override
	public void destroy() {
		this.crate.destroy();
	}

}

/**
 *	Author: Clément Jeannet
 *	Date: 	4 déc. 2017
 */
package main.game.actor.actorBuilder;

import main.Comments;
import main.NumberField;
import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.crate.Crate;
import main.game.actor.menu.LevelEditor;
import main.game.actor.menu.ParametersMenu;
import main.math.Vector;
import main.window.Canvas;

import java.awt.event.KeyEvent;

public class CrateBuilder extends ActorBuilder {

	private Crate crate;
	private Vector position;

	boolean placed = false;

	private ParametersMenu pm;
	private ActorGame game;
	private LevelEditor lv;

	private NumberField height, width;
	private Vector heightPos, widthPos;

	private boolean isWriting = true;
	private Comments heightComment, widthComments;

	public CrateBuilder(ActorGame game, LevelEditor lv) {
		super(game);
		this.game = game;
		this.lv = lv;
		crate = new Crate(game, getFlooredMousePosition(), null, false, 1);

		heightPos = new Vector(26, 6);
		height = new NumberField(game, heightPos, 3, 1, 1);

		heightComment = new Comments(game, "Crate Height");

		widthPos = new Vector(26, 8);
		width = new NumberField(game, widthPos, 3, 1, 1);

		widthComments = new Comments(game, "Crate Width");
	}

	@Override
	public void update(float deltaTime) {
		// crate.update(deltaTime);

		if (!placed) {
			position = getFlooredMousePosition();
			if (isLeftPressed()) {
				placed = true;
			}
			crate.setPosition(position);

		}
		if (!isDone()) {
			

			height.setZoom(lv.getZoom());
			width.setZoom(lv.getZoom());
			height.update(deltaTime);
			width.update(deltaTime);
			
			heightComment.setZoom(lv.getZoom());
			widthComments.setZoom(lv.getZoom());
			heightComment.update(deltaTime);
			widthComments.update(deltaTime);

			if (game.getKeyboard().get(KeyEvent.VK_ENTER).isPressed()) {
				isWriting = !(height.finishTyping() & width.finishTyping());
				crate.setSize(width.getNumber(), height.getNumber());
			}
		}
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

}

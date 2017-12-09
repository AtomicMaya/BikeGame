/**
 *	Author: Clément Jeannet
 *	Date: 	8 déc. 2017
 */
package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.NumberField;
import main.game.GUI.menu.LevelEditor;
import main.game.actor.Actor;
import main.game.actor.entities.Trampoline;
import main.math.ExtendedMath;
import main.math.Vector;
import main.window.Canvas;

import java.awt.event.KeyEvent;

public class TrampolineBuilder extends ActorBuilder {

	private Vector position;

	boolean placed = false;

	private ActorGame game;
	private LevelEditor lv;
	
	private Trampoline rfTrampoline;
	private NumberField height, width;
	private Vector heightNumberFieldPos, widthNumberFieldPos;
	private Comment heightComment, widthComments;
	
	private boolean isWriting = true;
	private boolean hoover = false;
	
	public TrampolineBuilder(ActorGame game) {
		super(game);
		rfTrampoline = new Trampoline(game, getFlooredMousePosition(), 5, 1);
		
		heightNumberFieldPos = new Vector(26, 6);
		height = new NumberField(game, heightNumberFieldPos, 3, 1, 1);

		heightComment = new Comment(game, "Crate Height");
		heightComment.setParent(height);
		heightComment.setAnchor(new Vector(-6, 0));

		widthNumberFieldPos = new Vector(26, 8);
		width = new NumberField(game, widthNumberFieldPos, 3, 1, 1);

		widthComments = new Comment(game, "Crate Width");
		widthComments.setParent(width);
		widthComments.setAnchor(new Vector(-6, 0));
	}

	@Override
	public void draw(Canvas canvas) {
		rfTrampoline.draw(canvas);
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
	public void update(float deltaTime, float zoom) {

		if (!placed) {
			position = getFlooredMousePosition();
			if (isLeftPressed()) {
				placed = true;
			}
			rfTrampoline.setPosition(position);

		}
		if (!isDone()) {
			height.update(deltaTime, lv.getZoom());
			width.update(deltaTime, lv.getZoom());

			heightComment.update(deltaTime, lv.getZoom());
			widthComments.update(deltaTime, lv.getZoom());

			if (game.getKeyboard().get(KeyEvent.VK_ENTER).isPressed()) {
				isWriting = !(height.hasFocus() & width.hasFocus());
			//	rfTrampoline.setSize(width.getNumber(), height.getNumber());
			}
		} else
			hoover = ExtendedMath.isInRectangle(position, position.add(width.getNumber(), height.getNumber()),
					getFlooredMousePosition());
		if (hoover && isRightPressed())
			isWriting = true;
	}

	@Override
	public Actor getActor() {
		return rfTrampoline;
	}

	@Override
	public boolean isDone() {
		return placed & !isWriting;
	}

	@Override
	public void reCreate() {
		rfTrampoline.destroy();
		rfTrampoline = new Trampoline(getOwner(), position, width.getNumber(), height.getNumber());
	}

	@Override
	public boolean isHovered() {
		return hoover;
	}

	@Override
	public void destroy() {
		this.rfTrampoline.destroy();
	}

}

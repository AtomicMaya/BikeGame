/**
 *	Author: Clément Jeannet
 *	Date: 	10 déc. 2017
 */
package main.game.GUI.actorBuilder;

import java.awt.event.KeyEvent;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.GraphicalButton;
import main.game.actor.Actor;
import main.game.actor.entities.BoomBarrel;
import main.game.actor.entities.Liquid;
import main.game.actor.entities.Trampoline;
import main.math.ExtendedMath;
import main.math.Vector;
import main.window.Canvas;

public class BoomBarrelBuilder extends ActorBuilder {

	private BoomBarrel boumBarel;
	private boolean isDone = false, hover = false;
	private Vector position;

	// parameters
	private GraphicalButton askExplosive;
	private Vector askExplosivPos = new Vector(22, 8);
	private Comment askExplosiveComment;

	private String acideText = "Change for acid", explosiveText = "Change for explosive";
	private boolean isExplosive = false;
	private boolean placed = false;

	public BoomBarrelBuilder(ActorGame game) {
		super(game);
		boumBarel = new BoomBarrel(game, getFlooredMousePosition(), false);

		askExplosive = new GraphicalButton(getOwner(), askExplosivPos, "Acid or explosive", 1);
		askExplosive.setAnchor(askExplosivPos);
		askExplosive.addOnClickAction(() -> {
			isExplosive = !isExplosive;
			askExplosiveComment.setText(isExplosive ? acideText : explosiveText);

			if (boumBarel != null) {
				boumBarel.destroy();
				boumBarel = new BoomBarrel(game, position, isExplosive);
			}

		});
		askExplosiveComment = new Comment(game, acideText);
		askExplosiveComment.setParent(askExplosive);
		askExplosiveComment.setAnchor(new Vector(-10, 0));
		
		position = getFlooredMousePosition();
	}

	@Override
	public void update(float deltaTime, float zoom) {

		if (!placed) {
			position = getFlooredMousePosition();
			if (isLeftPressed()) {
				placed = true;
			}
			boumBarel.setPosition(position);
		} else if (hover && isRightPressed())
			placed = false;
		if (!isDone) {
			askExplosive.update(deltaTime, zoom);
			if (askExplosive.isHovered())
				askExplosiveComment.update(deltaTime, zoom);

			if (boumBarel != null && getOwner().getKeyboard().get(KeyEvent.VK_ENTER).isPressed())
				isDone = true;

		}
		hover = ExtendedMath.isInRectangle(position, position.add(1, 2), getMousePosition());
		if (hover && isRightPressed())
			isDone = false;
	}

	@Override
	public void draw(Canvas canvas) {
		if (boumBarel != null)
			boumBarel.draw(canvas);
		if (!isDone) {
			askExplosive.draw(canvas);
			if (askExplosive.isHovered())
				askExplosiveComment.draw(canvas);
		}

	}

	@Override
	public Actor getActor() {
		return boumBarel;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		boumBarel.destroy();
		boumBarel = new BoomBarrel(getOwner(), position, isExplosive);
	}

	@Override
	public boolean isHovered() {
		return hover;
	}

	@Override
	public void destroy() {
		this.boumBarel.destroy();
	}

	@Override
	public void edit() {
		this.placed = false;
		this.isDone = false;
	}
}

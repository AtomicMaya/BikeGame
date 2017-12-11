/**
 *	Author: Clément Jeannet
 *	Date: 	11 déc. 2017
 */
package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.GraphicalButton;
import main.game.actor.Actor;
import main.game.actor.entities.collectable.Coin;
import main.math.ExtendedMath;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

import java.awt.event.KeyEvent;

public class CoinBuilder extends ActorBuilder {

	private boolean isDone = false;

	private Coin coin;
	private Polygon shape;
	private Vector position;

	private GraphicalButton askBigCoin;
	private Vector askBigCoinPos = new Vector(22, 8);
	private Comment askBigCoinComment;

	private String normalText = "Change for normal", bigCoinText = "Change for big coin";
	private boolean isBigCoin = false;

	private boolean hover = false;
	private boolean placed = false;

	public CoinBuilder(ActorGame game) {
		super(game);

		askBigCoin = new GraphicalButton(getOwner(), askBigCoinPos, "Is a big coin?", 1);
		askBigCoin.setAnchor(askBigCoinPos);
		askBigCoin.addOnClickAction(() -> {
			isBigCoin = !isBigCoin;
			askBigCoinComment.setText(isBigCoin ? normalText : bigCoinText);

			if (coin != null) {
				coin.destroy();
				coin = new Coin(game, position, isBigCoin);
			}

		});
		askBigCoinComment = new Comment(game, normalText);
		askBigCoinComment.setParent(askBigCoin);
		askBigCoinComment.setAnchor(new Vector(-10, 0));
		coin = new Coin(game, getFlooredMousePosition(), false);
	}

	@Override
	public void update(float deltaTime, float zoom) {

		if (!placed) {
			position = getFlooredMousePosition();
			if (isLeftPressed()) {
				placed = true;
			}
			coin.setPosition(position);
		}
		if (!isDone) {
			if (coin != null && getOwner().getKeyboard().get(KeyEvent.VK_ENTER).isPressed())
				isDone = true;
			askBigCoin.update(deltaTime, zoom);
			if (askBigCoin.isHovered())
				askBigCoinComment.update(deltaTime, zoom);
		}
		hover = ExtendedMath.isInRectangle(position, position.add((isBigCoin) ? 2 : 1, (isBigCoin) ? 2 : 1),
				getMousePosition());
		if (coin != null) {
			coin.update(deltaTime);

		}
		if (isHovered() && isRightPressed()) {
			placed = false;
			isDone = false;
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (coin != null)
			coin.draw(canvas);
		if (!isDone()) {
			askBigCoin.draw(canvas);
			if (askBigCoin.isHovered())
				askBigCoinComment.draw(canvas);
		}
	}

	@Override
	public boolean isHovered() {
		return hover;
	}

	@Override
	public void destroy() {
		if (coin != null)
			coin.destroy();
	}

	@Override
	public Actor getActor() {
		return coin;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		coin.destroy();
		coin = new Coin(getOwner(), position, isBigCoin);
	}

	@Override
	public void edit() {
		this.isDone = false;
	}
}

package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.GraphicalButton;
import main.game.GUI.menu.LevelEditor;
import main.game.actor.Actor;
import main.game.actor.entities.collectable.Coin;
import main.math.ExtendedMath;
import main.math.Vector;
import main.window.Canvas;

import java.awt.event.KeyEvent;

/** Use in the {@linkplain LevelEditor} to create and add a {@linkplain Coin} to the game. */
public class CoinBuilder extends ActorBuilder {

	/**
	 * Whether this {@linkplain CoinBuilder} has finished building its
	 * {@linkplain Coin}
	 */
	private boolean isDone = false;

	/** {@linkplain Coin} created and returned by {@link #getActor()} */
	private Coin coin;
	
	/** Position to give to the {@linkplain Coin} */
	private Vector position;

	/** {@linkplain GraphicalButton} used to change whether the {@linkplain Coin} is a big {@linkplain Coin} or not*/
	private GraphicalButton askBigCoin;
	
	/** Absolute position on screen of the {@linkplain GraphicalButton} {@link #askBigCoin} */
	private Vector askBigCoinPos = new Vector(22, 8);
	
	/** {@linkplain Comment} of this {@linkplain GraphicalButton} {@link #askBigCoin} */
	private Comment askBigCoinComment;

	/** Text value of the {@linkplain GraphicalButton} {@link #askBigCoin}*/
	private String normalText = "Change for normal", bigCoinText = "Change for big coin";
	
	/** Whether this {@linkplain Coin} is a big {@linkplain Coin}*/
	private boolean isBigCoin = false;

	/** Whether the {@linkplain Coin} is placed */
	private boolean placed = false;

	/** 
	 * Create a new {@linkplain CoinBuilder} 
	 *  @param game The master {@linkplain ActorGame}
	 *  */
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
		coin = new Coin(game, getHalfFlooredMousePosition(), false);
		position = getHalfFlooredMousePosition();
	}

	@Override
	public void update(float deltaTime, float zoom) {
		super.update(deltaTime, zoom);
		if (!placed) {
			position = getHalfFlooredMousePosition();
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
		
		if (coin != null) {
			coin.update(deltaTime);

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
		return ExtendedMath.isInRectangle(position, position.add((isBigCoin) ? 2 : 1, (isBigCoin) ? 2 : 1),
				getMousePosition());
	}

	@Override
	public void destroy() {
		if (coin != null)
			coin.destroy();
	}

	@Override
	public Actor getActor() {
		reCreate();
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
		this.placed = false;
	}
}

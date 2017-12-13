package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.GUIComponent;
import main.game.GUI.GraphicalButton;
import main.game.GUI.menu.LevelEditor;
import main.game.actor.Actor;
import main.game.actor.entities.BoomBarrel;
import main.math.ExtendedMath;
import main.math.Vector;
import main.window.Canvas;

import java.awt.event.KeyEvent;

/**
 * Use in the {@linkplain LevelEditor} to build and add a new
 * {@linkplain BoomBarrel}
 */
public class BoomBarrelBuilder extends ActorBuilder {

	/** {@linkplain BoomBarrel} created and returned by {@link #getActor()}*/
	private BoomBarrel boomBarrel;

	/**
	 * Whether this {@linkplain BoomBarrelBuilder} has finished building its
	 * {@linkplain BoomBarrel}
	 */
	private boolean isDone = false;

	/**
	 * Whether this {@linkplain GUIComponent} is hovered by the
	 * {@linkplain main.window.Mouse}
	 */
	private boolean hover = false;
	
	/** Position to give to the {@linkplain BoomBarrel} */
	private Vector position;

	// parameters
	/** {@linkplain GraphicalButton} used to set whether the {@link #boomBarrel} is explosive or acid */
	private GraphicalButton askExplosive;
	
	/** Absolute position on screen of the {@linkplain GraphicalButton} {@linkplain #askExplosive} */
	private Vector askExplosivePos = new Vector(18, 8);
	
	/** {@linkplain Comment} associated to the {@linkplain GraphicalButton} {@link #askExplosive} */
	private Comment askExplosiveComment;

	/** Texts description of the {@linkplain Comment} {@link #askExplosiveComment} */
	private String acideText = "Change for acid", explosiveText = "Change for explosive";
	
	/** Whether the {@linkplain BoomBarrel} is explosive 
	 * @see #askExplosive}
	 * */
	private boolean isExplosive = false;
	
	/** Whether the {@linkplain BoomBarrel} is placed */
	private boolean placed = false;

	/**
	 * Create a new {@linkplain BoomBarrelBuilder}
	 * @param game The master {@linkplain ActorGame}
	 * */
	public BoomBarrelBuilder(ActorGame game) {
		super(game);
		boomBarrel = new BoomBarrel(game, getHalfFlooredMousePosition(), false);

		askExplosive = new GraphicalButton(getOwner(), askExplosivePos, "Acid or explosive", 1);
		askExplosive.setAnchor(askExplosivePos);
		askExplosive.addOnClickAction(() -> {
			isExplosive = !isExplosive;
			askExplosiveComment.setText(isExplosive ? acideText : explosiveText);

			if (boomBarrel != null) {
				boomBarrel.destroy();
				boomBarrel = new BoomBarrel(game, position, isExplosive);
			}

		});
		askExplosiveComment = new Comment(game, acideText);
		askExplosiveComment.setParent(askExplosive);
		askExplosiveComment.setAnchor(new Vector(-10, 0));

		position = getHalfFlooredMousePosition();
	}

	@Override
	public void update(float deltaTime, float zoom) {

		if (!placed) {
			position = getHalfFlooredMousePosition();
			if (isLeftPressed()) {
				placed = true;
			}
			boomBarrel.setPosition(position);
		} else if (hover && isRightPressed())
			placed = false;
		if (!isDone) {
			askExplosive.update(deltaTime, zoom);
			if (askExplosive.isHovered())
				askExplosiveComment.update(deltaTime, zoom);

			if (boomBarrel != null && getOwner().getKeyboard().get(KeyEvent.VK_ENTER).isPressed())
				isDone = true;

		}
		hover = ExtendedMath.isInRectangle(position, position.add(1, 2), getMousePosition());
		if (hover && isRightPressed())
			isDone = false;
	}

	@Override
	public void draw(Canvas canvas) {
		if (boomBarrel != null)
			boomBarrel.draw(canvas);
		if (!isDone) {
			askExplosive.draw(canvas);
			if (askExplosive.isHovered())
				askExplosiveComment.draw(canvas);
		}

	}

	@Override
	public Actor getActor() {
		reCreate();
		return boomBarrel;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		boomBarrel.destroy();
		boomBarrel = new BoomBarrel(getOwner(), position, isExplosive);
	}

	@Override
	public boolean isHovered() {
		return hover;
	}

	@Override
	public void destroy() {
		this.boomBarrel.destroy();
	}

	@Override
	public void edit() {
		this.placed = false;
		this.isDone = false;
	}
}

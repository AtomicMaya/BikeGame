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
	private BoomBarrel boumBarel;

	/**
	 * Whether this {@linkplain BoomBarrelBuilder} has finished building its
	 * {@linkplain BoomBarrel}
	 */
	private boolean isDone = false;

	/**
	 * Whether this {@linkplain GUIComponent} is hovered by the
	 * {@linkplain Mouse}
	 */
	private boolean hover = false;
	
	/** Position to give to the {@linkplain BoomBarrel} */
	private Vector position;

	// parameters
	/** {@linkplain GraphicalButton} used to set whether the {@link #boumBarel} is explosive or acid */
	private GraphicalButton askExplosive;
	
	/** Absolute position on screen of the {@linkplain GraphicalButton} {@link #askExplosive} */
	private Vector askExplosivPos = new Vector(18, 8);
	
	/** {@linkplain Comment} associated to the {@linkplain GraphicalButton} {@link #askExplosive} */
	private Comment askExplosiveComment;

	/** Texts description of the {@linkplain Comment} {@link #askExplosiveComment} */
	private String acideText = "Change for acid", explosiveText = "Change for explosive";
	
	/** Whether the {@linkplain BoomBarrel} is explosive 
	 * @see {@link #askExplosive}
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
		boumBarel = new BoomBarrel(game, getHalfFlooredMousePosition(), false);

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

		position = getHalfFlooredMousePosition();
	}

	@Override
	public void update(float deltaTime, float zoom) {

		if (!placed) {
			position = getHalfFlooredMousePosition();
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

package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.GUIComponent;
import main.game.GUI.NumberField;
import main.game.actor.Actor;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;

/** Used to create an {@linkplain Actor} in the game. */
public abstract class ActorBuilder extends GUIComponent {

	private ArrayList<NumberField> parameters = new ArrayList<>();
	ArrayList<Comment> descriptions = new ArrayList<>();

	/**
	 * Create an {@linkplain ActorBuilder} to add an {@linkplain Actor} to the
	 * game.
	 * @param game {@linkplain ActorGame} master of {@link this}
	 * @param numberFields {@linkplain NumberField}s for the parameters of this
	 * {@link ActorBuilder}, can be null
	 * @param description {@linkplain Comment}s for this
	 * {@linkplain ActorBuilder}'s parameters {@linkplain NumberField}s, can be
	 * null, has to be the same size than numberFields
	 */
	public ActorBuilder(ActorGame game) {
		super(game, Vector.ZERO);

	}

	@Override
	public void update(float deltaTime, float zoom) {
		for (int i = 0; i < parameters.size(); i++) {
			parameters.get(i).update(deltaTime, zoom);
			if (parameters.get(i).isHovered()) {
				descriptions.get(i).update(deltaTime, zoom);
			}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		for (int i = 0; i < parameters.size(); i++) {
			parameters.get(i).draw(canvas);
			if (parameters.get(i).isHovered()) {
				descriptions.get(i).draw(canvas);
			}
		}
	}

	/**
	 * @return the {@linkplain Actor} created by the {@linkplain ActorBuilder}.
	 */
	public abstract Actor getActor();

	/**
	 * @return whether this {@linkplain ActorBuilder} finished building its
	 * {@linkplain Actor}
	 */
	public abstract boolean isDone();

	/** Method called to recreate what's needed after we test the game. */
	public abstract void reCreate();

	/** Start editing this {@linkplain ActorBuilder}. */
	public abstract void edit();

}

package main.game.graphicalActors;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.math.Node;
import main.math.Transform;
import main.window.Canvas;

import java.util.Random;

/** Displays a cool message when the {@linkplain main.game.actor.entities.PlayableEntity} dies. */
public class EndGameGraphics extends Node implements Actor {
    /** The master {@linkplain ActorGame}. */
	private ActorGame game;

	/** The dimensions of this {@linkplain EndGameGraphics}. */
	private float width, height;

	/** The path to the image file to be displayed. */
	private String filename;

	/** Whether or not this message has been destroyed. */
	private boolean displayed = false;

	/** Various ratios in accordance with the screen dimensions. */
	private float widthRatio = 0.85f, heightRatio = 0.6f;

    /**
     * Creates a new {@linkplain EndGameGraphics}.
     * @param game The master {@linkplain ActorGame}.
     */
	public EndGameGraphics(ActorGame game) {
		this.filename = "";
		this.game = game;
		this.width = game.getViewScale() * widthRatio;
		this.height = game.getViewScale() * heightRatio;
		this.setParent(game.getCanvas());
	}

	@Override
	public void update(float deltaTime) {
		if (this.game.getPayload() != null)
			if (this.game.getPayload().getVictoryStatus() && !this.displayed)
				this.displayVictoryMessage();
			else if (this.game.getPayload().getDeathStatus() && !this.displayed)
				this.displayDeathMessage();
			else if (!this.game.getPayload().getDeathStatus() && !this.game.getPayload().getVictoryStatus())
				this.resetGraphics();
	}

	@Override
	public void draw(Canvas canvas) {
		this.width = this.game.getViewScale() * this.widthRatio;
		this.height = this.game.getViewScale() * this.heightRatio;
		this.setRelativeTransform(Transform.I.scaled(this.widthRatio, this.heightRatio));

		canvas.drawImage(canvas.getImage(this.filename), getTransform().translated(-this.width / 2, -this.height / 4),
				1, 1000);
	}

	/** Displays a death message on the {@linkplain Canvas}, with a rare twist ! */
	public void displayDeathMessage() {
		this.displayed = true;
		boolean secretDiceRoll = new Random().nextFloat() < 2 * 4.2 / 404;
		boolean killedByGravity = game.getPayload().getIfWasKilledByGravity();
		if (killedByGravity)
			this.filename = secretDiceRoll ? "./res/images/fatality.easter.egg.png" : "./res/images/fatality.1.png";
		else
			this.filename = "./res/images/fatality.2.png";
	}

	/** Displays a victory message on the {@linkplain Canvas}. */
	public void displayVictoryMessage() {
		this.displayed = true;
		this.filename = "/res/images/victory.png";
	}

	/** Resets the graphics display. */
	public void resetGraphics() {
		this.displayed = false;
		this.filename = "";
	}
}

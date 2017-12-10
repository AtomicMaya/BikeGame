package main.game.graphicalStuff;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.math.Node;
import main.math.Transform;
import main.window.Canvas;

import java.util.Random;

public class EndGameGraphics extends Node implements Actor {

	private ActorGame game;
	private float width, height;
	private String filename;
	private boolean displayed = false;

	private float widthRatio = 0.85f;
	private float heightRatio = 0.6f;

	public EndGameGraphics(ActorGame game) {
		this.filename = "";
		this.game = game;
		this.width = game.getViewScale() * widthRatio;
		this.height = game.getViewScale() * heightRatio;
		this.setParent(game.getCanvas());
	}

	@Override
	public void update(float deltaTime) {
		if (game.getPayload() != null)
			if (game.getPayload().getDeathStatus() && !this.displayed)
				this.displayDeathMessage();
			else if (game.getPayload().getVictoryStatus() && !this.displayed)
				this.displayVictoryMessage();
			else if (!game.getPayload().getDeathStatus() && !game.getPayload().getVictoryStatus())
				this.resetGraphics();
	}

	@Override
	public void draw(Canvas canvas) {
		this.width = game.getViewScale() * widthRatio;
		this.height = game.getViewScale() * heightRatio;
		this.setRelativeTransform(Transform.I.scaled(widthRatio, heightRatio));// .translated(-width/2,-height/2));

		canvas.drawImage(canvas.getImage(this.filename), getTransform().translated(-width / 2, -height / 4), // .scaled(width,
				// height),
				1, 1000);
	}

	public void displayDeathMessage() {
		this.displayed = true;
		boolean secretDiceRoll = new Random().nextFloat() < 2 * 4.2 / 404;
		boolean killedByGravity = game.getPayload().getIfWasKilledByGravity();
		if (killedByGravity)
			this.filename = secretDiceRoll ? "./res/images/fatality.easter.egg.png" : "./res/images/fatality.1.png";
		else
			this.filename = "./res/images/fatality.2.png";
	}

	public void displayVictoryMessage() {
		this.displayed = true;
		this.filename = "/res/images/victory.png";
	}

	public void resetGraphics() {
		this.displayed = false;
		this.filename = "";
	}
}

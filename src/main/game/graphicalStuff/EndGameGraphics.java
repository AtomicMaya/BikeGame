package main.game.graphicalStuff;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.graphics.Graphics;
import main.math.Node;
import main.math.Transform;
import main.window.Canvas;

public class EndGameGraphics extends Node implements Graphics, Actor {

	private ActorGame game;
	private float width, height;
	private String filename;

	public EndGameGraphics(ActorGame game, String fileName) {
		this.filename = fileName;
		this.game = game;
		this.width = game.getViewScale() * 0.85f;
		this.height = game.getViewScale() * 0.6f;
	}

	@Override
	public void draw(Canvas canvas) {
		this.setRelativeTransform(
				Transform.I.scaled(this.width, this.height).translated(this.game.getCameraPosition().sub(this.width / 2, this.height / 2)));
		canvas.drawImage(canvas.getImage(this.filename), getTransform(), 1, 1000);
	}
}

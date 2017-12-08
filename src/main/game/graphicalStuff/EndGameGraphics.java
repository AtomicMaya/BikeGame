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
	String filename;

	public EndGameGraphics(ActorGame game, String fileName) {
		this.filename = fileName;
		this.game = game;
		width = game.getViewScale() * 0.75f;
		height = game.getViewScale() * 0.6f;
	}

	@Override
	public void draw(Canvas canvas) {
		this.setRelativeTransform(
				Transform.I.scaled(width, height).translated(game.getCameraPosition().sub(width / 2, height / 2)));
		canvas.drawImage(canvas.getImage(filename), getTransform(), 1, 1000);
	}
}

/**
 *	Author: Clément Jeannet
 *	Date: 	4 déc. 2017
 */
package main.game.actor.menu;

import java.awt.Color;
import java.util.ArrayList;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.Graphics;
import main.window.Canvas;
import main.window.Window;

public class LevelEditor implements Graphics {

	private ArrayList<Actor> actors = new ArrayList<>();
	private ActorGame game;
	private ActorMenu actorMenu;

	public LevelEditor(ActorGame game, Window window) {
		this.game = game;
		this.actorMenu = new ActorMenu(game, window, Color.LIGHT_GRAY);
	}

	public void update(float deltaTime) {
		actorMenu.update(deltaTime);
	}

	@Override
	public void draw(Canvas canvas) {
		actorMenu.draw(canvas);
	}

	public ArrayList<Actor> getActors() {
		return actors;
	}

}

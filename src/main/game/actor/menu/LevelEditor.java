/**
 *	Author: Clément Jeannet
 *	Date: 	4 déc. 2017
 */
package main.game.actor.menu;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.Graphics;
import main.game.actor.actorBuilder.ActorBuilder;
import main.window.Canvas;
import main.window.Window;

import java.awt.*;
import java.util.ArrayList;

public class LevelEditor implements Graphics {

	private ArrayList<ActorBuilder> actors = new ArrayList<>();
	private ActorGame game;
	private ActorMenu actorMenu;

	public LevelEditor(ActorGame game, Window window) {
		this.game = game;
		this.actorMenu = new ActorMenu(game, this, window, Color.LIGHT_GRAY);
	}

	public void update(float deltaTime) {
		actorMenu.update(deltaTime);
		for (Actor actor : actors) {
			actor.update(deltaTime);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		actorMenu.draw(canvas);
		for (Actor actor : actors) {
			actor.draw(canvas);
		}
	}

	public ArrayList<Actor> getActors() {
		return null;
	}

	public void addActor(ActorBuilder actor) {
		actors.add(actor);
	}

}

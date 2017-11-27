/**
 *	Author: Clément Jeannet
 *	Date: 	23 nov. 2017
 */
package main.game.levels;

import java.util.ArrayList;

import main.game.actor.Actor;
import main.game.actor.ActorGame;
import main.game.actor.myEntities.FinishActor;
import main.math.Node;

public abstract class Level extends Node {

	private ArrayList<Actor> actors = new ArrayList<Actor>();

	protected ActorGame game;

	private Actor viewCAndidate;

	
	private FinishActor finishActor;
	
	public Level(ActorGame game) {
		this.game = game;
	}

	public abstract void createAllActors();
	
	public void addActor(Actor actor) {
		actors.add(actor);
	}

	public void end() {
		game.detroyActor(actors);
		for (Actor a : actors) {
			a.destroy();
		}
	}

	public void setViewCandidate(Actor a) {
		this.viewCAndidate = a;
	}
	
	public ArrayList<Actor> getActors(){
		return actors;
	}
	
	public Actor getViewCandidate() {
		return viewCAndidate;
	}
	
	public void setFinishActor(FinishActor a) {
		this.finishActor = a;
	}

	public FinishActor getFinishActor() {
		return finishActor;
	}
}

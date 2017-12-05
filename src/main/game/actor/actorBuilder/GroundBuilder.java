/**
 *	Author: Clément Jeannet
 *	Date: 	5 déc. 2017
 */
package main.game.actor.actorBuilder;

import java.util.ArrayList;

import main.game.ActorGame;
import main.math.Vector;

public class GroundBuilder extends ActorBuilder {

	ArrayList<Vector> points = new ArrayList<>();
	public GroundBuilder(ActorGame game) {
		super(game);
	}

}

/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.crate;

import main.game.actor.ActorGame;
import main.game.actor.GameEntity;
import main.game.actor.Graphics;
import main.math.Node;
import main.math.Vector;

public class Crate extends GameEntity {

	private Graphics graphic;

	/**
	 * Create a Crate
	 * 
	 * @param g
	 *            Obligé de mettre un node et pas un Graphics, ou on peut pas faire
	 *            de setParent dessus, faudrait mettre Graphics mais 
	 */
	public Crate(ActorGame game, Vector position, Node g, boolean fixed, float size) {
		super(game, fixed, position);
		g.setParent(getEntity());
	}

}

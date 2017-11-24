/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.myEntities;

import main.game.actor.Actor;
import main.game.actor.ActorGame;
import main.game.actor.Graphics;
import main.math.Entity;
import main.math.Polyline;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;

import static main.game.actor.myEntities.EntityBuilder.*;

public class Ground extends GameEntity {
	Graphics g;
	public Ground(ActorGame game, Vector position, Polyline p) {
		super(game, true, (position == null) ? Vector.ZERO : position);
		build(getEntity(),p,-1,-1,false);
		g = addGraphics(getEntity(), p,null,Color.BLACK,.1f,1,1);
	}

	@Override
	public void update(float deltaTime) {


	}

	@Override
	public void draw(Canvas window){
		g.draw(window);
	}

}

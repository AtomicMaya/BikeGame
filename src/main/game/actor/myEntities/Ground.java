/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.myEntities;

<<<<<<< HEAD
import java.awt.Color;

=======
>>>>>>> c676db3c3a87c3e6cdbdffa421f197e731810636
import main.game.actor.ActorGame;
import main.game.actor.ShapeGraphics;
import main.math.Polyline;
import main.math.Vector;
import main.window.Canvas;

public class Ground extends GameEntity {

	private ShapeGraphics graphics;
	public Ground(ActorGame game, Vector position, Polyline shape) {
		super(game, true, (position == null) ? Vector.ZERO : position);
		EntityBuilder.build(this.getEntity(), shape, .6f, -1, false);
		graphics = EntityBuilder.addGraphics(getEntity(), shape, null, Color.black, .1f, 1, 0);
	}

	@Override
	public void draw(Canvas window){
		graphics.draw(window);
	}




}

/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.crate;

import main.game.actor.ActorGame;
import main.game.actor.ImageGraphics;
import main.game.actor.myEntities.EntityBuilder;
import main.game.actor.myEntities.GameEntity;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

public class Crate extends GameEntity {

	private ImageGraphics graphic;

	public Crate(ActorGame game, Vector position, String imagePath, boolean fixed, float size) {
		super(game, fixed, position);
		imagePath = (imagePath == null || imagePath == "") ? "res/crate.1.png" : imagePath;
		Polygon square = new Polygon(0, 0, size, 0, size, size, 0, size);
		EntityBuilder.build(getEntity(), square);
		graphic = new ImageGraphics(imagePath, size, size);
		graphic.setParent(getEntity());
	}

	@Override
	public void draw(Canvas canvas) {
		graphic.draw(canvas);
	}
	
	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}

}

/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.crate;

import main.game.ActorGame;
import main.game.actor.ImageGraphics;
import main.game.actor.entities.GameEntity;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

/**
 * Part 4.5, Test de l'architecture: Crate
 */
public class Crate extends GameEntity {

	// keep reference to our images
	private ImageGraphics graphic;

	/**
	 * Create a new Crate
	 * 
	 * @param game
	 *            ActorGame where the Crate evolve
	 * @param position
	 *            initial position of the Crate
	 * @param imagePath
	 *            path to the image to give to the Crate, if null, default image
	 * @param fixed
	 *            weather the crate is fixed
	 * @param size
	 *            of the crate
	 */
	public Crate(ActorGame game, Vector position, String imagePath, boolean fixed, float size) {
		super(game, fixed, position);
		imagePath = (imagePath == null || imagePath == "") ? "res/images/crate.1.png" : imagePath;
		Polygon square = new Polygon(0, 0, size, 0, size, size, 0, size);
		build(square);
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

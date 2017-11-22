/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.crate;

import main.game.actor.ActorGame;
import main.game.actor.GameEntity;
import main.game.actor.ImageGraphics;
import main.math.Vector;
import main.window.Canvas;

public class Crate extends GameEntity {

	private ImageGraphics graphic;

	public Crate(ActorGame game, Vector position, String imagePath, boolean fixed, float size) {
		super(game, fixed, position);
		graphic = new ImageGraphics(imagePath, size, size);
		graphic.setParent(getEntity());
	}

	@Override
	public void draw(Canvas canvas) {
		graphic.draw(canvas);
	}

}

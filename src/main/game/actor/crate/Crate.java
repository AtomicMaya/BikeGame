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

import java.io.Serializable;

/**
 * Part 4.5, Test de l'architecture: Crate
 */
public class Crate extends GameEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -677571549932797093L;

	// keep reference to our images
	private transient ImageGraphics graphic;

	// keep the argument in case of save
	private String imagePath;
	private float width, height;

	/**
	 * Create a new Crate
	 * 
	 * @param game ActorGame where the Crate evolve
	 * @param position initial position of the Crate
	 * @param imagePath path to the image to give to the Crate, if null, default
	 *            image
	 * @param fixed weather the crate is fixed
	 * @param size of the crate
	 */
	public Crate(ActorGame game, Vector position, String imagePath, boolean fixed, float size) {
		this(game, position, imagePath, fixed, size, size);
	}

	public Crate(ActorGame game, Vector position, String imagePath, boolean fixed, float width, float height) {
		super(game, fixed, position);
		this.imagePath = (imagePath == null || imagePath.equals("")) ? "res/images/crate.1.png" : imagePath;
		this.width = width;
		this.height = height;

		create();
	}

	/**
	 * Actual creation of the parameters of the GameEntity, not in the constructor
	 * to avoid duplication with the method reCreate
	 */
	private void create() {
		imagePath = (imagePath == null || imagePath == "") ? "res/images/crate.1.png" : imagePath;

		Polygon square = new Polygon(0, 0, 0, height, width, height, width, 0);
		this.build(square);
		graphic = this.addGraphics(this.imagePath, width, height);
	}

	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
		create();
	}

	@Override
	public void draw(Canvas canvas) {
		graphic.draw(canvas);
	}

	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
		create();
	}

	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}

}

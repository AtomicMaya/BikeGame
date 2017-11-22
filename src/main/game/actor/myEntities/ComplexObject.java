/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.myEntities;

import java.util.ArrayList;

import main.game.actor.Graphics;
import main.math.Vector;
import main.window.Canvas;

public abstract class ComplexObject implements Graphics {

	private ArrayList<ComplexObject> entities = new ArrayList<ComplexObject>();
 
	protected void addEntity(ComplexObject entity) {
		entities.add(entity);
	}

	@Override
	public void draw(Canvas window) {
		for (ComplexObject entity : entities) {
			entity.draw(window);
		}
	}
	
	public abstract void update(float deltaTime);

	public abstract Vector getPosition();
	public abstract Vector getVelocity();
}

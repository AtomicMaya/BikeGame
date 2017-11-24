/**
 *	Author: Clément Jeannet
 *	Date: 	20 nov. 2017
 */
package main.game.actor.myEntities;

import java.util.ArrayList;

import main.game.actor.*;
import main.math.*;
import main.window.Canvas;

public abstract class SimpleEntity implements Actor{

	private Entity entity;
	private ArrayList<Graphics> graphics = new ArrayList<Graphics>();
	private PartBuilder partBuilder = null;
	private Shape shape = null; // in case we need it

	protected SimpleEntity(Entity entity) {
		this.entity = entity;
	}

	/*public ArrayList<Graphics> getGraphics() {
		return graphics;
	}*/

	public Entity getEntity(){
		return entity;
	}
	@Override
	public void draw(Canvas window) {
		for (Graphics g : graphics)
			g.draw(window);
	}

	public void setShape(Shape s) {
		shape = s;
		if (s != null) {
			partBuilder = entity.createPartBuilder();
			partBuilder.setShape(s);
			partBuilder.setFriction(.4f);
			partBuilder.build();
		}
	}

	public void setFriction(float friction) {
		partBuilder = entity.createPartBuilder();
		partBuilder.setShape(shape);
		partBuilder.setFriction(friction);
		partBuilder.build();
	}

	public void setDensity(float density) {
		partBuilder = entity.createPartBuilder();
		partBuilder.setShape(shape);
		partBuilder.setDensity(density);
		partBuilder.build();
	}

	public void setGohst(boolean ghost) {
		partBuilder = entity.createPartBuilder();
		partBuilder.setShape(shape);
		partBuilder.setGhost(ghost);
		partBuilder.build();
	}

	/**
	 * Add Graphics to a given entity
	 */
	protected void setGraphics(ArrayList<Graphics> graphics) {
		for (Graphics g : graphics)
			setGraphics(g);
	}

	protected void setGraphics(Graphics g) {
		if (g != null) {
			if (g instanceof ShapeGraphics) {
				((ShapeGraphics) g).setParent(entity);
			} else if (g instanceof ImageGraphics) {
				((ImageGraphics) g).setParent(entity);
			}
			this.graphics.add(g);
		}
	}

	@Override
	public Vector getPosition() {
		return entity.getPosition();
	}

	@Override
	public void update(float deltaTime) {}

	@Override
	public Vector getVelocity() {
		return entity.getVelocity();
	}

	@Override
	public Transform getTransform(){
		return entity.getTransform();
	}
}

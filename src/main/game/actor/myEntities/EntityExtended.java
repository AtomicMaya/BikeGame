/**
 *	Author: Clément Jeannet
 *	Date: 	20 nov. 2017
 */
package main.game.actor.myEntities;

import java.util.ArrayList;

import main.game.actor.Graphics;
import main.game.actor.ImageGraphics;
import main.game.actor.ShapeGraphics;
import main.math.Entity;
import main.math.EntityBuilder;
import main.math.PartBuilder;
import main.math.Shape;
import main.math.Vector;
import main.math.World;
import main.window.Canvas;

public abstract class EntityExtended {

	private Entity entity;
	private ArrayList<Graphics> graphics = new ArrayList<Graphics>();
	private PartBuilder partBuilder = null;
	private Shape shape = null; // in case we need it

	public final int ID;

	public EntityExtended(Entity e, Graphics g, PartBuilder pb, int id) {
		this.entity = e;
		this.graphics.add(g);
		this.partBuilder = pb;
		this.ID = id;
	}

	protected EntityExtended(World world, Vector position, boolean fixed, int id) {
		// create entity
		EntityBuilder entityBuilder = world.createEntityBuilder();
		entityBuilder.setFixed(fixed);
		entityBuilder.setPosition(position);
		entity = entityBuilder.build();

		this.ID = id;
	}

	public Entity getEntity() {
		return entity;
	}

	public ArrayList<Graphics> getGraphics() {
		return graphics;
	}

	public int getId() {
		return ID;
	}

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
}

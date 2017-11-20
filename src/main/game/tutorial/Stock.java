/**
 *	Author: Clément Jeannet
 *	Date: 	20 nov. 2017
 */
package main.game.tutorial;

import java.util.ArrayList;

import main.game.actor.Graphics;
import main.game.actor.ImageGraphics;
import main.game.actor.ShapeGraphics;
import main.math.Entity;
import main.math.PartBuilder;
import main.window.Window;

abstract class Stock extends ArrayList<ArrayList<Object>> {
	/**
	 * Because a serialVersionUID is needed
	 */
	private static final long serialVersionUID = 69L;

	/**
	 * Add an entity and its objects to this
	 * 
	 * @param entity
	 *            The entity, not null
	 * @param graphics
	 *            ArrayList containing the graphics associated with the entity, may
	 *            be null
	 * @param partBuilder
	 *            PartBuilder associated with the entity, may be null
	 * @param id
	 *            integer used to find and identify the entity
	 */
	public void add(Entity entity, ArrayList<Graphics> graphics, PartBuilder partBuilder, int id) {
		ArrayList<Object> alO = new ArrayList<Object>();
		alO.add(entity);
		alO.add(graphics);
		alO.add(partBuilder);
		alO.add(id);
		this.add(alO);
	}

	/**
	 * Add an entity and its objects to this
	 * 
	 * @param entity
	 *            The entity, not null
	 * @param graphic
	 *            Graphics associated with the entity, may be null
	 * @param partBuilder
	 *            PartBuilder associated with the entity, may be null
	 * @param id
	 *            integer used to find and identify the entity
	 */
	public void add(Entity entity, Graphics graphic, PartBuilder partBuilder, int id) {
		ArrayList<Object> alO = new ArrayList<Object>();
		alO.add(entity);
		// add the graphic to an ArrayList for storage purpose
		ArrayList<Graphics> g = new ArrayList<Graphics>();
		g.add(graphic);

		alO.add(g);
		alO.add(partBuilder);
		alO.add(id);
		this.add(alO);
	}

	/**
	 * Add an entity and its associated object to the storage
	 * 
	 * @param entityAndAssociatedObject
	 */
	@Override
	public boolean add(ArrayList<Object> entityAndAssociatedObject) {
		if (isGoodFormat(entityAndAssociatedObject)) {
			int i = 0;
			for (ArrayList<Object> o : this) {
				// check if id is used
				if (o.get(3) == entityAndAssociatedObject.get(3)) {
					break;
				}
				i++;
			}
			if (entityAndAssociatedObject.get(1) == null)
				entityAndAssociatedObject.set(1, new ArrayList<Graphics>());
			if (i == size()) {
				// id is not used, add the element
				super.add(entityAndAssociatedObject);
			} else {
				// id id already used, replace the element
				super.set(i, entityAndAssociatedObject);
			}
			return true;
		} else
			throw new ArrayStoreException("The ArrayList has not the good type of Objects ");
	}

	/**
	 * Add a graphics object to an entity given its id
	 * 
	 * @param g
	 *            The graphics object
	 * @param id
	 *            The id of the entity
	 */
	public void add(Graphics g, int id) {
		for (ArrayList<Object> alO : this) {
			if ((int) alO.get(3) == id) {
				@SuppressWarnings("unchecked") // has been checked
				ArrayList<Graphics> a = (ArrayList<Graphics>) alO.get(1);
				a.add(g);
				addGraphics((Entity) alO.get(0), g);
				break;
			}
		}
	}

	/**
	 * Get an entity given its id
	 * 
	 * @param id
	 *            The id of the entity
	 * @return The entity
	 */
	public Entity getEntity(int id) {
		for (ArrayList<Object> alO : this) {
			if ((int) alO.get(3) == id) {
				return (Entity) alO.get(0);
			}
		}
		return null;
	}

	/**
	 * Get the graphics of an entity given its id
	 * 
	 * @param id
	 *            The id of the entity
	 * @return An ArrayList containing the graphics
	 */
	@SuppressWarnings("unchecked") // checked when initializing
	public ArrayList<Graphics> getGraphics(int id) {
		for (ArrayList<Object> alO : this) {
			if ((int) alO.get(3) == id) {
				return (ArrayList<Graphics>) alO.get(1);
			}
		}
		return null;
	}

	/**
	 * Get the partBuilder of an entity given its id
	 * 
	 * @param id
	 *            The id of the entity
	 * @return The PartBuilder of the entity
	 */
	public PartBuilder getPartBuilder(int id) {
		for (ArrayList<Object> alO : this) {
			if ((int) alO.get(3) == id) {
				return (PartBuilder) alO.get(2);
			}
		}
		return null;
	}

	/**
	 * Draw all Graphics stored
	 * 
	 * @param window
	 *            canvas target, not null
	 */
	@SuppressWarnings("unchecked") // it has been checked !!!
	public void drawAll(Window window) {
		for (ArrayList<Object> alO : this) {
			ArrayList<Graphics> allG = (ArrayList<Graphics>) alO.get(1);
			for (Graphics g : allG) {
				g.draw(window);
			}
		}
	}

	/**
	 * Test if a given ArrayList contains the good objects to store
	 * 
	 * @return true if it is good
	 */
	@SuppressWarnings("unchecked") // it's checked !!!
	private boolean isGoodFormat(ArrayList<Object> alO) {
		if (alO.size() != 4)
			return false;

		// the first is not null and an entity
		if (alO.get(0) == null || alO.get(0).getClass() != Entity.class)
			return false;

		// the second is null or an ArrayList
		if (alO.get(1) != null && alO.get(1).getClass() != ArrayList.class)
			return false;

		// each of those can be null or Graphics
		if (alO.get(1) != null) {
			for (Object o : (ArrayList<Object>) alO.get(1)) {
				if (!(o == null || o instanceof Graphics)) {
					return false;
				}
			}
		}
		// the third is null or a partBuilder
		if (alO.get(2) != null && alO.get(2).getClass() != PartBuilder.class)
			return false;

		// the last is an integer
		if (alO.get(3).getClass() != Integer.class)
			return false;

		return true;
	}

	/**
	 * Add Graphics to a given entity
	 */
	protected void addGraphics(Entity e, Graphics g) {
		if (g != null) {
			if (g instanceof ShapeGraphics) {
				((ShapeGraphics) g).setParent(e);
			} else if (g instanceof ImageGraphics) {
				((ImageGraphics) g).setParent(e);
			}
		}
	}
}

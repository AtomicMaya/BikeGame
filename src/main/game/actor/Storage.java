/**
 *	Author: Clément Jeannet
 *	Date: 	20 nov. 2017
 */
package main.game.actor;

import java.awt.Color;
import java.util.ArrayList;

import main.game.actor.myEntities.EntityExtended;
import main.game.actor.myEntities.RectangleEntity;
import main.game.actor.myEntities.SphereEntity;
import main.game.actor.myEntities.SquareEntity;
import main.math.Entity;
import main.math.Polyline;
import main.math.Vector;
import main.math.World;
import main.window.Canvas;

public class Storage extends ArrayList<EntityExtended> {

	World world;

	private static final long serialVersionUID = -6113927270093267701L;

	public Storage(World w) {
		super();
		this.world = w;
	}

	@Override
	public boolean add(EntityExtended ee) {
		return super.add(ee);

	}

	public void newSquare(Vector position, float size, boolean fixed, int id) {
		this.add(new SquareEntity(world, position, size, fixed, id));
	}

	public void newSquare(Vector position, String imagePath, float size, boolean fixed, int id) {
		this.add(new SquareEntity(world, position, imagePath, size, fixed, id));
	}

	public void newRectangle(Vector position, float width, float height, boolean fixed, int id) {
		this.add(new RectangleEntity(world, position, width, height, fixed, id));
	}

	public void newRectangle(Vector position, String imagePath, float width, float height, boolean fixed, int id) {
		this.add(new RectangleEntity(world, position, imagePath, width, height, fixed, id));
	}

	public void newSphere(Vector position, boolean fixed, int id) {
		this.add(new SphereEntity(world, position, fixed, id));
	}

	public void newSphere(Vector position, float radius, boolean fixed, Color fillColor, Color outlineColor,
			float thickness, float alpha, float depth, int id) {
		this.add(new SphereEntity(world, position, depth, fixed, fillColor, outlineColor, thickness, alpha, depth, id));
	}

	public void newSphere(Vector position, float radius, String imagePath, boolean fixed, int id) {
		this.add(new SphereEntity(world, position, radius, imagePath, fixed, id));
	}

	public void newGround(Vector position, ArrayList<Vector> alV, Color color, float thickness, int id) {
		this.add(new Ground(world, position, alV, color, thickness, id));
	}

	public void newGround(Vector position, Polyline p, Color color, float thickness, int id) {
		this.add(new Ground(world, position, p, color, thickness, id));
	}

	public Entity getEntity(int id) {
		for (EntityExtended e : this) {
			if (e.getId() == id)
				return e.getEntity();
		}
		return null;
	}

	public void drawAll(Canvas window) {
		for (EntityExtended e : this) {
			e.draw(window);
		}
	}

	// TODO setStuff instead of getGraphics
	public ArrayList<Graphics> getGraphics(int id) {
		for (EntityExtended e : this) {
			if (e.getId() == id)
				return e.getGraphics();
		}
		return null;
	}

	public void setFriction(int id, float friction) {
		for (EntityExtended e : this) {
			if (e.getId() == id)
				e.setFriction(friction);
		}
	}
}

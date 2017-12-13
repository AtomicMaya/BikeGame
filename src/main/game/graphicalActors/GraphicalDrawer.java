package main.game.graphicalActors;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.graphics.Graphics;
import main.game.graphics.ShapeGraphics;
import main.io.Saveable;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;

/** Use to draw "naked" {@linkplain Graphics}, {@linkplain Graphics} without parent */
public class GraphicalDrawer implements Actor, Saveable {

	/** Storage for all the graphics */
	private transient ArrayList<Graphics> graphics = new ArrayList<>();

	@Override
	public void draw(Canvas canvas) {
		for (Graphics graphics : this.graphics)
			graphics.draw(canvas);
	}

	/** @param graphics {@linkplain ArrayList} of {@linkplain Graphics} to add to the game */
	public void addGraphics(ArrayList<Graphics> graphics) {
		for (Graphics graphic : graphics)
			this.addGraphics(graphic);
	}

	/** @param graphics {@linkplain Graphics} to add to the game */
	public void addGraphics(Graphics graphics) {
		
		if (!this.graphics.contains(graphics))
			this.graphics.add(graphics);
	}
	
	/** @param graphics {@linkplain ShapeGraphics} to add to the game.
     *  @param anchor The anchor position {@linkplain Vector}.   */
	public void addGraphics(ShapeGraphics graphics, Vector anchor) {
		graphics.setRelativeTransform(Transform.I.translated(anchor));
		if (!this.graphics.contains(graphics))
			this.graphics.add(graphics);
	}

	/** @return null, no sense of having a transform */
	@Override
	public Transform getTransform() {
		return null;
	}

	/** @return null, no sense of having a velocity */
	@Override
	public Vector getVelocity() {
		return null;
	}

	@Override
	public void reCreate(ActorGame game) {
		
	}

}

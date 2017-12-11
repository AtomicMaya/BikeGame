package main.game.graphics;

import main.game.actor.Actor;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;

/** Use to draw "naked" {@linkplain Graphics}, {@linkplain Graphics} without parent */
public class GraphicalDrawer implements Actor {

	/** Storage for all the graphics */
	private ArrayList<Graphics> graphics = new ArrayList<>();

	@Override
	public void draw(Canvas canvas) {
		for (Graphics g : graphics) 
			g.draw(canvas);
	}

	/** @param graphics {@linkplain ArrayList} of {@linkplain Graphics} to add to the game */
	public void addGraphics(ArrayList<Graphics> graphics) {
		for (Graphics g : graphics) 
			addGraphics(g);
	}

	/** @param graphics {@linkplain Graphics} to add to the game */
	public void addGraphics(Graphics graphic) {
		if (!graphics.contains(graphic))
			this.graphics.add(graphic);
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

}

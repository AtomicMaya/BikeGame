/**
 *	Author: Clément Jeannet
 *	Date: 	12 déc. 2017
 */
package main.game.GUI.actorBuilder;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import main.game.ActorGame;
import main.game.GUI.menu.LevelEditor;
import main.game.actor.Actor;
import main.game.actor.entities.Laser;
import main.game.actor.entities.Obstacle;
import main.game.actor.entities.Terrain;
import main.math.Polygon;
import main.math.Polyline;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

/** Use in the {@linkplain LevelEditor} to build and add a new {@linkplain Obstacle} */
public class ObstacleBuilder extends ActorBuilder {


	/** {@linkplain Obstacle} created and returned by {@link #getActor()} */
	private Obstacle obstacle;
	
	/** {@linkplain ArrayList} of {@linkplain Vector}, points of the {@linkplain Obstacle} */
	private ArrayList<Vector> points = new ArrayList<>();
	
	/** Position to give to the {@linkplain Obstacle} */
	private Vector position;
	
	/**
	 * Whether this {@linkplain LaserBuilder} has finished building its
	 * {@linkplain Obstacle}
	 */
	private boolean isDone = false;
	
	/** Whether the {@linkplain Obstacle} is placed */
	private boolean placed = false;
	
	/**
	 * Create a new {@linkplain ObstacleBuilder}
	 * @param game The master {@linkplain ActorGame}
	 */
	public ObstacleBuilder(ActorGame game) {
		super(game);
		
		position = getHalfFlooredMousePosition();
	}

	
	@Override
	public void destroy() {
		obstacle.destroy();
	}

	public void update(float deltaTime) {
		if (!placed) {
			position = getHalfFlooredMousePosition();
			if (isLeftPressed())
				placed = true;
		}
		
		if (!isDone()) {
			if (placed && isLeftPressed()) {
				Vector tempPos = getHalfFlooredMousePosition();
				if (!points.contains(tempPos))
					points.add(tempPos);
				else 
				points.remove(tempPos);
			}
			if (getOwner().getKeyboard().get(KeyEvent.VK_ENTER).isPressed()) {
				isDone = true;
				
			}
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (points.size() > 3)
			canvas.drawShape(new Polygon(points), Transform.I.translated(position), new Color(196, 196, 188),
					Color.BLACK, .05f, 1, 2);
		else {
			if (!points.isEmpty())
				canvas.drawShape(new Polyline(position, points.get(0)), Transform.I, Color.green, Color.green, .1f, .8f, 2);
//			if (!points.size() > 1)
//				canvas.drawShape(new Polyline(position, points.get(0)), Transform.I, Color.green, Color.green, .1f, .8f, 2);
		}

	}

	@Override
	public Actor getActor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		this.obstacle.destroy();
		this.obstacle = new Obstacle(getOwner(), position, new Polygon(points));
	}

	@Override
	public void edit() {
		isDone = false;
		placed = false;

	}

	@Override
	public boolean isHovered() {
		// TODO Auto-generated method stub
		return false;
	}

}

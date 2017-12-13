package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.menu.LevelEditor;
import main.game.actor.Actor;
import main.game.actor.entities.Obstacle;
import main.math.*;
import main.math.Polygon;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Area;
import java.util.ArrayList;

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
		System.out.println("created");
		position = getHalfFlooredMousePosition();
	}

	
	@Override
	public void destroy() {
		if (this.obstacle != null)
			this.obstacle.destroy();
	}

	@Override
	public void update(float deltaTime, float zoom) {
		super.update(deltaTime, zoom);
		
		if (!isDone()) {
			if (placed && isLeftPressed()) {
				Vector tempPos = getHalfFlooredMousePosition().sub(position);
				if (!points.contains(tempPos))
					points.add(tempPos);

				 else if (points.contains(tempPos))
					points.remove(tempPos);
			}
			if (placed && getOwner().getKeyboard().get(KeyEvent.VK_ENTER).isPressed()) {
				isDone = true;			
			}
		}
		if (!placed) {
			points.remove(position);
			position = getHalfFlooredMousePosition();
			if (isLeftPressed()) {
				placed = true;
				points.add(Vector.ZERO);
			}
			
		}
	}
	
	@Override
	public void draw(Canvas canvas) {
		if (points.size() > 2)
			canvas.drawShape(new Polygon(points), Transform.I.translated(position), new Color(196, 196, 188),
					Color.BLACK, .05f, 1, 2);
		else if (points.size() > 1)
				canvas.drawShape(new Polyline(points.get(0).add(position), points.get(1).add(position)), Transform.I, Color.green, Color.green, .1f, .8f, 2);
		
		if (!isDone) {
			for (Vector v : points)
				canvas.drawShape(new Circle(.1f), Transform.I.translated(v.add(position)), new Color(22, 84, 44).brighter(), null, 0, 1, 15);
		}
			

	}

	@Override
	public Actor getActor() {
		reCreate();
		return obstacle;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		if (obstacle != null)
			this.obstacle.destroy();
		this.obstacle = new Obstacle(getOwner(), position, new Polygon(points));
	}

	@Override
	public void edit() {
		isDone = false;
//		placed = false;

	}

	@Override
	public boolean isHovered() {
		if (points.size() < 3) 
			return false;
		Polygon p = new Polygon(points);
		Area a = new Area(p.toPath());
		return 	a.contains(getMousePosition().x, getMousePosition().y);
	}

}

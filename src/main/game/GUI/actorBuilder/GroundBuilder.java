/**
 *	Author: Clément Jeannet
 *	Date: 	5 déc. 2017
 */
package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.GraphicalButton;
import main.game.actor.Actor;
import main.game.actor.entities.Terrain;
import main.game.actor.entities.TerrainType;
import main.math.*;
import main.window.Canvas;
import main.window.Mouse;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Used to create a {@linkplain Terrain}
 * @see ActorBuilder
 */
public class GroundBuilder extends ActorBuilder {

	/** {@linkplain ArrayList} of {@linkplain Vector}, points of the {@linkplain Terrain} */
	private ArrayList<Vector> points = new ArrayList<>();
	
	/** {@linkplain ArrayList} of {@linkplain Vector}, fix start points of the {@linkplain Terrain} */
	private ArrayList<Vector> fixStart = new ArrayList<>();
	
	/** {@linkplain ArrayList} of {@linkplain Vector}, fix end points of the {@linkplain Terrain} */
	private ArrayList<Vector> fixEnd = new ArrayList<>();
	
	/** {@linkplain Vector} bounds of the {@linkplain Terrain} to */
	private Vector start = new Vector(-400, 0), end = new Vector(400, 0);

	/** {@linkplain Polyline} used for the drawing */
	private Polyline groundLine;

	/** Current {@linkplain Mouse} position, used to add a temporary point to the {@link #groundLine}, for showing purpose */
	private Vector currentPoint;

	/** Font size of the {@linkplain GraphicalButton }*/
	private float fontSize = .83f;

	
	/** Drawing mode, 0 for normal, with adding points between other, and 1 for free, where you just add points from left to right,
	 * <b>Warning</b> changing mode will update points as in the normal mode */
	private int drawMode = 0;
	
	/** {@linkplain GraphicalButton} used to change the {@link #drawMode}, won't add a new point if clicked */
	private GraphicalButton drawModeButton;
	
	/** Absolute position of the {@linkplain GraphicalButton} {@link #drawModeButton} */
	private Vector drawModeButtonPosition = new Vector(15, 14);

	
	/** {@linkplain GraphicalButton} used to finish the creation of the 
	 * {@linkplain Terrain}, won't add a new point if clicked */
	private GraphicalButton finish;
	
	/** Absolute position on screen of the {@linkplain GraphicalButton} {@link #finish} */
	private Vector finishPosition = new Vector(0, 11);
	
	/** Text of the {@linkplain GraphicalButton} {@link #finish} */
	private final String finishText = "Finish";

	/**
	 * Whether this {@linkplain GroundBuilder} has finished building its
	 * {@linkplain Terrain}
	 */
	private boolean isDone = false;

	/** {@linkplain Terrain} created and returned by {@link #getActor()} */
	private Terrain terrain;

	/**
	 * Create a new {@linkplain GroundBuilder}
	 * @param game : {@linkplain ActorGame} where this belong
	 */
	public GroundBuilder(ActorGame game) {
		super(game);

		fixStart.add(new Vector(-500, -500));
		fixStart.add(new Vector(-500, 0));
		fixEnd.add(new Vector(500, 0));
		fixEnd.add(new Vector(500, -500));

		points.add(start);
		points.add(end);

		// buttons
		drawModeButton = new GraphicalButton(game, new Vector(18, 14), "Change draw mode to : Free",
				fontSize);
		drawModeButton.addOnClickAction(() -> {
			drawModeButton.setText((drawMode == 1) ? "Change draw mode to : Free" : "Change draw mode to : Normal",
					fontSize);
			drawMode = (drawMode == 0) ? 1 : 0;
		});
		drawModeButton.setAnchor(drawModeButtonPosition);

		finish = new GraphicalButton(game, finishPosition, finishText, fontSize);
		finishPosition = new Vector(-finish.getWidth() / 2, finishPosition.y);
		finish.addOnClickAction(() -> {
			this.isDone = true;
			groundLine = new Polyline(updateGround(null));
		});
		finish.setAnchor(finishPosition);
	}

	@Override
	public void draw(Canvas canvas) {
		if (!isDone) {
			for (Vector v : points) {
				canvas.drawShape(new Circle(.1f), Transform.I.translated(v), Color.ORANGE, null, 0, 1, 12);
			}
			canvas.drawShape(new Circle(.1f), Transform.I.translated(getHalfFlooredMousePosition()),
					new Color(22, 84, 44), null, 0, 1, 15);

			drawModeButton.draw(canvas);
			finish.draw(canvas);
		}
		canvas.drawShape(groundLine, Transform.I, Color.decode("#6D5D49"), Color.decode("#548540"), .1f, 1, -10);

	}

	@Override
	public void update(float deltaTime, float zoom) {

		if (!isDone) {
			currentPoint = getHalfFlooredMousePosition();
			if (isLeftPressed() && !drawModeButton.isHovered() && !finish.isHovered()) {
				addPoint(currentPoint);

			}

			points = updatePoints(points);
			groundLine = new Polyline(updateGround(currentPoint));

			// buttons update
			drawModeButton.update(deltaTime, zoom);

			finish.update(deltaTime, zoom);

			// reset if escape is pressed
			if (getOwner().getKeyboard().get(KeyEvent.VK_ESCAPE).isPressed()) {
				points.clear();
				points.add(start);
				points.add(end);
			}
			if (getOwner().getKeyboard().get(KeyEvent.VK_ENTER).isPressed()) {
				isDone = true;
				groundLine = new Polyline(updateGround(null));
			}
		}
	}

	/**
	 * Update all points already in the points list, given the strategy (normal,
	 * or free)
	 * @param points : points list
	 * @return the updated list
	 */
	private ArrayList<Vector> updatePoints(ArrayList<Vector> points) {
		// normal draw mode, with insertion point in the middle
		if (this.drawMode == 0) {
			points = ExtendedMath.sortVectorByX(points);
			// free draw mode, can be strange
		} else if (drawMode == 1) {
			points.remove(end);
			points.add(end);
		}
		return points;
	}

	/**
	 * Update the {@linkplain Terrain} which will be created
	 * @param mousePosition : {@linkplain main.window.Mouse} position
	 * @return a list with the points updated
	 */
	private ArrayList<Vector> updateGround(Vector mousePosition) {
		ArrayList<Vector> temp = new ArrayList<>();
		temp.addAll(points);

		if (mousePosition != null && !temp.contains(mousePosition))
			temp.add(mousePosition);

		temp = updatePoints(temp);

		ArrayList<Vector> points = new ArrayList<>();
		points.addAll(fixStart);
		points.addAll(temp);
		points.addAll(fixEnd);
		return points;
	}

	/**
	 * Add a point in the list
	 * @param v : point
	 */
	private void addPoint(Vector v) {
		if (points.contains(v))
			points.remove(v);
		else
			points.add(v);
	}

	@Override
	public Actor getActor() {
		if (terrain != null)
			terrain.destroy();
		terrain = new Terrain(getOwner(), Vector.ZERO, new Polyline(updateGround(null)), TerrainType.NORMAL);
		return terrain;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		terrain.destroy();
		terrain = new Terrain(getOwner(), Vector.ZERO, new Polyline(updateGround(null)), TerrainType.NORMAL);
	}

	@Override
	public boolean isHovered() {
		return false;
	}

	public void continueBuilding() {
		this.isDone = false;
	}

	@Override
	public void destroy() {
		this.terrain.destroy();
	}

	@Override
	public void edit() {
		this.isDone = false;
	}

}

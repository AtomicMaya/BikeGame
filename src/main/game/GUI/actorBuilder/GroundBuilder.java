/**
 *	Author: Clément Jeannet
 *	Date: 	5 déc. 2017
 */
package main.game.GUI.actorBuilder;

import main.game.ActorGame;
import main.game.GUI.GraphicalButton;
import main.game.GUI.menu.LevelEditor;
import main.game.actor.Actor;
import main.game.actor.entities.Terrain;
import main.math.*;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * Used to create a {@linkplain Terrain}
 * @see ActorBuilder
 */
public class GroundBuilder extends ActorBuilder {

	// points list and stuff
	private ArrayList<Vector> points = new ArrayList<>();
	private ArrayList<Vector> fixStart = new ArrayList<>();
	private ArrayList<Vector> fixEnd = new ArrayList<>();
	private Vector start = new Vector(-400, 0), end = new Vector(400, 0);
	private ActorGame game;

	private Polyline groundLine;

	// current mousePosition
	private Vector currentPoint;

	// button fontSize
	private float fontSize = .83f;

	// drawing mode
	private int drawMode = 0;
	private GraphicalButton drawModeButton;
	private Vector drawModeButtonPosition = new Vector(15, 14);

	// finish button
	private GraphicalButton finish;
	private Vector finishPosition = new Vector(0, 13);
	private final String finishText = "Finish";

	private LevelEditor lv;

	private boolean isDone = false;

	// actor build
	private Terrain terrain;

	/**
	 * Create a new {@linkplain GroundBuilder}
	 * @param game : {@linkplain ActorGame} where this belong
	 * @param levelEditor : {@linkplain LevelEditor} where this is created
	 */
	public GroundBuilder(ActorGame game, LevelEditor levelEditor) {
		super(game);
		this.game = game;
		this.lv = levelEditor;

		fixStart.add(new Vector(-500, -500));
		fixStart.add(new Vector(-500, 0));
		fixEnd.add(new Vector(500, 0));
		fixEnd.add(new Vector(500, -500));

		points.add(start);
		points.add(end);

		// buttons
		drawModeButton = new GraphicalButton(game, new Vector(18, 14), "Change draw mode to : Free",
				fontSize * levelEditor.getZoom());
		drawModeButton.addOnClickAction(() -> {
			drawModeButton.setText((drawMode == 0) ? "Change draw mode to : Free" : "Change draw mode to : Normal",
					fontSize * levelEditor.getZoom());
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
		// terrain = new Terrain(game, Vector.ZERO, new
		// Polyline(updateGround(null)));
	}

	@Override
	public void draw(Canvas canvas) {
		if (!isDone) {
			for (Vector v : points) {
				canvas.drawShape(new Circle(.1f), Transform.I.translated(v), Color.ORANGE, null, 0, 1, 12);
			}
			canvas.drawShape(new Circle(.1f), Transform.I.translated(ExtendedMath.floor(game.getMouse().getPosition())),
					new Color(22, 84, 44), null, 0, 1, 15);

			drawModeButton.draw(canvas);
			finish.draw(canvas);
		}
		canvas.drawShape(groundLine, Transform.I, Color.decode("#6D5D49"), Color.decode("#548540"), .1f, 1, -10);

	}

	@Override
	public void update(float deltaTime, float zoom) {


		if (!isDone) {
			currentPoint = getFlooredMousePosition();
			if (isLeftPressed() && !drawModeButton.isHovered() && !finish.isHovered()) {
				addPoint(currentPoint);

			}

			points = updatePoints(points);
			groundLine = new Polyline(updateGround(currentPoint));

			// buttons update
			drawModeButton.setText(null, fontSize * lv.getZoom());
//			drawModeButton.setPosition(drawModeButtonPosition.mul(lv.getZoom()).add(lv.getCameraPosition()));
			drawModeButton.update(deltaTime, zoom);

			finish.setText(null, fontSize * lv.getZoom());
//			finish.setPosition(finishPosition.mul(lv.getZoom()).add(lv.getCameraPosition()));
			finish.update(deltaTime, zoom);

			// reset if escape is pressed
			if (game.getKeyboard().get(KeyEvent.VK_ESCAPE).isPressed()) {
				points.clear();
				points.add(start);
				points.add(end);
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
		terrain = new Terrain(game, Vector.ZERO, new Polyline(updateGround(null)), 9);
		return terrain;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		terrain.destroy();
		terrain = new Terrain(game, Vector.ZERO, new Polyline(updateGround(null)), 0);
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

}

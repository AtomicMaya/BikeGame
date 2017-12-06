/**
 *	Author: Clément Jeannet
 *	Date: 	5 déc. 2017
 */
package main.game.actor.actorBuilder;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.QuickMafs;
import main.game.actor.entities.GraphicalButton;
import main.game.actor.entities.Ground;
import main.game.actor.menu.LevelEditor;
import main.math.Circle;
import main.math.Polyline;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

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

	private Ground ground;

	public GroundBuilder(ActorGame game, LevelEditor lv) {
		super(game);
		this.game = game;
		this.lv = lv;

		fixStart.add(new Vector(-500, -500));
		fixStart.add(new Vector(-500, 0));
		fixEnd.add(new Vector(500, 0));
		fixEnd.add(new Vector(500, -500));

		points.add(start);
		points.add(end);

		// buttons
		drawModeButton = new GraphicalButton(game, new Vector(18, 14), "Change draw mode to : Free",
				fontSize * lv.getZoom());
		drawModeButton.addOnClickAction(() -> {
			drawModeButton.setText((drawMode == 0) ? "Change draw mode to : Free" : "Change draw mode to : Normal",
					fontSize * lv.getZoom());
			drawMode = (drawMode == 0) ? 1 : 0;
		});

		finish = new GraphicalButton(game, finishPosition, finishText, fontSize);
		finishPosition = new Vector(-finish.getWidth() / 2, finishPosition.y);
		finish.addOnClickAction(() -> {
			this.isDone = true;
			groundLine = new Polyline(updateGround(null));
		});
		ground = new Ground(game, Vector.ZERO, new Polyline(updateGround(null)));
	}

	@Override
	public void draw(Canvas canvas) {
		if (!isDone) {
			for (Vector v : points) {
				canvas.drawShape(new Circle(.1f), Transform.I.translated(v), Color.ORANGE, null, 0, 1, 12);
			}
			canvas.drawShape(new Circle(.1f), Transform.I.translated(QuickMafs.floor(game.getMouse().getPosition())),
					new Color(22, 84, 44), null, 0, 1, 15);

			drawModeButton.draw(canvas);
			finish.draw(canvas);
		}
		canvas.drawShape(groundLine, Transform.I, Color.decode("#6D5D49"), Color.decode("#548540"), .1f, 1, -10);

	}

	@Override
	public void update(float deltaTime) {

		if (!isDone) {
			currentPoint = QuickMafs.floor(getMousePosition());
			if (isLeftPressed() && !drawModeButton.isHovered() && !finish.isHovered()) {
				addPoint(currentPoint);

			}

			points = updatePoints(points);
			groundLine = new Polyline(updateGround(currentPoint));

			// buttons update
			drawModeButton.setText(null, fontSize * lv.getZoom());
			drawModeButton.setPosition(drawModeButtonPosition.mul(lv.getZoom()).add(lv.getCameraPosition()));
			drawModeButton.update(deltaTime);

			finish.setText(null, fontSize * lv.getZoom());
			finish.setPosition(finishPosition.mul(lv.getZoom()).add(lv.getCameraPosition()));
			finish.update(deltaTime);

			// reset if escape is pressed
			if (game.getKeyboard().get(KeyEvent.VK_ESCAPE).isPressed()) {
				points.clear();
				points.add(start);
				points.add(end);
			}
		}
	}

	private ArrayList<Vector> updatePoints(ArrayList<Vector> points) {
		// normal draw mode, with insertion point in the middle
		if (this.drawMode == 0) {
			points = QuickMafs.sortVectorByX(points);
			// free draw mode, can be strange
		} else if (drawMode == 1) {
			points.remove(end);
			points.add(end);
		}
		return points;
	}

	private ArrayList<Vector> updateGround(Vector mousePos) {
		ArrayList<Vector> temp = new ArrayList<>();
		temp.addAll(points);

		if (mousePos != null && !temp.contains(mousePos))
			temp.add(mousePos);

		temp = updatePoints(temp);

		ArrayList<Vector> points = new ArrayList<>();
		points.addAll(fixStart);
		points.addAll(temp);
		points.addAll(fixEnd);
		return points;
	}

	private void addPoint(Vector v) {
		if (points.contains(v))
			points.remove(v);
		else
			points.add(v);
	}

	@Override
	public Actor getActor() {
		if (ground != null)
			ground.destroy();
		ground = new Ground(game, Vector.ZERO, new Polyline(updateGround(null)));
		return ground;
	}

	@Override
	public boolean isDone() {
		return isDone;
	}

	@Override
	public void reCreate() {
		ground.destroy();
		ground = new Ground(game, Vector.ZERO, new Polyline(updateGround(null)));
	}

}

/**
 *	Author: Clément Jeannet
 *	Date: 	4 déc. 2017
 */
package main.game.actor.menu;

import main.game.ActorGame;
import main.game.actor.actorBuilder.CrateBuilder;
import main.game.actor.entities.GraphicalButton;
import main.math.Polygon;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;
import main.window.Mouse;
import main.window.Window;

import java.awt.*;
import java.util.ArrayList;

public class ActorMenu extends Menu {

	private Mouse mouse;

	private ArrayList<GraphicalButton> boutons = new ArrayList<>();
	private ArrayList<Vector> boutonsPosition = new ArrayList<>();

	private Vector maxPosition = Vector.ZERO, minPosition = Vector.ZERO;

	float width, height;

	public ActorMenu(ActorGame game, LevelEditor levelEditor, Window window, Color backgroundColor) {
		super(game, window, false, backgroundColor);
		mouse = window.getMouse();

		Polygon shape = new Polygon(0f, 0f, 0f, .8f, 1.8f, .8f, 1.8f, 0f);
		boutons.add(new GraphicalButton(game, Vector.ZERO, "", 1));
		boutons.get(0).setNewGraphics("res/images/box.4.png", "res/images/box.4.png");
		boutons.get(0).addOnClickAction(() -> levelEditor.addActor(new CrateBuilder(game)), .1f);
		boutonsPosition.add(new Vector(.1f, -.9f));

		boutons.add(new GraphicalButton(game, Vector.ZERO, "", 1));
		boutonsPosition.add(new Vector(2.1f, -.9f));

		boutons.add(new GraphicalButton(game, Vector.ZERO, "", 1));
		boutonsPosition.add(new Vector(.1f, -1.9f));

		boutons.add(new GraphicalButton(game, Vector.ZERO, "", 1));
		boutonsPosition.add(new Vector(2.1f, -1.9f));

		float maxX = Float.MAX_VALUE;
		float maxY = Float.MAX_VALUE;
		for (Vector v : boutonsPosition) {
			maxX = (maxX < v.x) ? v.x : maxY;
			maxY = Math.min(maxY, v.y);
		}

		// System.out.println(maxPosition);
		maxPosition = new Vector(maxX, maxY).add(.4f, -.4f).add(2, 0);

	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (mouse.getRightButton().isPressed()) {
			this.changeStatut();
			if (isOpen()) {
				minPosition = mouse.getPosition().add(-.2f, .2f);

				for (int i = 0; i < boutons.size(); i++) {
					boutons.get(i).setPosition(mouse.getPosition().add(boutonsPosition.get(i)));
				}
			}

		}

		if (isOpen())
			for (GraphicalButton gb : boutons) {
				gb.update(deltaTime);
			}
	}

	@Override
	public void draw(Canvas canvas) {
		if (isOpen()) {
			canvas.drawShape(new Polygon(getRectangle(minPosition, minPosition.add(maxPosition))), Transform.I,
					Color.LIGHT_GRAY, Color.LIGHT_GRAY, .1f, 1, -1);
			for (GraphicalButton gb : boutons) {
				gb.draw(canvas);
			}
		}
	}

	private ArrayList<Vector> getRectangle(Vector un, Vector deux) {

		ArrayList<Vector> points = new ArrayList<>();

		points.add(un);
		points.add(new Vector(deux.x, un.y));
		points.add(deux);
		points.add(new Vector(un.x, deux.y));
		return points;
	}

}

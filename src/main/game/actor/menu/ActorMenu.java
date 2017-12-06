/**
 *	Author: Clément Jeannet
 *	Date: 	4 déc. 2017
 */
package main.game.actor.menu;

import main.game.ActorGame;
import main.game.actor.QuickMafs;
import main.game.actor.actorBuilder.BikeBuilder;
import main.game.actor.actorBuilder.CrateBuilder;
import main.game.actor.actorBuilder.GroundBuilder;
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
	private ArrayList<String> description = new ArrayList<>();
	private Vector maxPosition = Vector.ZERO, minPosition = Vector.ZERO;

	float width, height;

	private LevelEditor lv;

	public ActorMenu(ActorGame game, LevelEditor levelEditor, Window window, Color backgroundColor) {
		super(game, window, false, backgroundColor, false);
		this.mouse = game.getMouse();
		this.lv = levelEditor;
		Polygon shape = new Polygon(0f, 0f, 0f, .8f, 1.8f, .8f, 1.8f, 0f);

		float butonSizeX = .8f, butonSizeY = .8f;
		// crate pos 0,0
		boutons.add(new GraphicalButton(game, Vector.ZERO, butonSizeX, butonSizeY));
		boutons.get(0).setNewGraphics("res/images/box.4.png", "res/images/box.4.png");
		boutons.get(0).addOnClickAction(() -> {
			levelEditor.addActor(new CrateBuilder(game));
			changeStatut();
		});
		boutonsPosition.add(new Vector(0f, 0f));

		// ground pos 1, 0
		boutons.add(new GraphicalButton(game, Vector.ZERO, butonSizeX, butonSizeY));
		boutons.get(1).addOnClickAction(() -> {
			levelEditor.addGround(new GroundBuilder(game, levelEditor));
			changeStatut();
		});
		boutonsPosition.add(new Vector(1f, 0f));

		// bike pos 3, 0
		boutons.add(new GraphicalButton(game, Vector.ZERO, butonSizeX, butonSizeY));
		boutons.get(2).addOnClickAction(() -> {
			levelEditor.addBike(new BikeBuilder(game));
			changeStatut();
		});
		boutonsPosition.add(new Vector(2f, 0f));

		boutons.add(new GraphicalButton(game, Vector.ZERO, butonSizeX, butonSizeY));
		boutonsPosition.add(new Vector(1f, -1f));

		for (int i = 0; i < boutonsPosition.size(); i++) {
			boutonsPosition.set(i, boutonsPosition.get(i).add(.1f, -.9f));
		}
		float maxX = 0;
		float maxY = 0;
		for (Vector v : boutonsPosition) {
			maxX = (maxX < v.x) ? v.x : maxX;
			maxY = Math.min(maxY, v.y);
		}

		// System.out.println(maxPosition);
		maxPosition = new Vector(maxX, maxY).add(.4f, -.4f).add(1, 0);

	}

	@Override
	public void update(float deltaTime) {

		if (mouse.getRightButton().isPressed()) {
			this.setStatut(true);
			if (isOpen()) {
				minPosition = mouse.getPosition().add(-.2f, .2f);

				for (int i = 0; i < boutons.size(); i++) {
					boutons.get(i).setPosition(mouse.getPosition().add(boutonsPosition.get(i)));
				}
			}

		} else if (mouse.getLeftButton().isPressed()) {
			Vector mousePos = mouse.getPosition();

			if (!QuickMafs.isInRectangle(minPosition, minPosition.add(maxPosition), mousePos)) {
				this.setStatut(false);
			}

		}
		if (isOpen()) {
			for (GraphicalButton gb : boutons) {
				gb.update(deltaTime);
			}
		}

	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		// canvas.drawShape(new Polygon(0, 0, 0, 1, 1, 1, 1, 0), Transform.I,
		// Color.BLACK, null, 0, 1, 2);
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

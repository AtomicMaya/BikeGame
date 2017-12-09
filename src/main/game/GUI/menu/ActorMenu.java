/**
 *	Author: Clément Jeannet
 *	Date: 	4 déc. 2017
 */
package main.game.GUI.menu;

import main.game.ActorGame;
import main.game.GUI.GraphicalButton;
import main.game.GUI.actorBuilder.BikeBuilder;
import main.game.GUI.actorBuilder.CrateBuilder;
import main.game.GUI.actorBuilder.ParticleEmitterBuilder;
import main.game.GUI.actorBuilder.PlatformBuilder;
import main.math.ExtendedMath;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;
import main.window.Window;

import java.awt.*;
import java.util.ArrayList;

public class ActorMenu extends Menu {

	private ArrayList<GraphicalButton> boutons = new ArrayList<>();

	private float sizeX = 1.5f, sizeY = 1.5f;
	private float betweenButton = .2f;
	private ArrayList<String> description = new ArrayList<>();
	private Vector maxPosition = Vector.ZERO, minPosition = Vector.ZERO;

	float width, height;

	private int nbButonLine = 3;

	public ActorMenu(ActorGame game, LevelEditor levelEditor, Window window, Color backgroundColor) {
		super(game, Vector.ZERO, false);

		// this.setParent(window);
		// crate pos 0,0
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(0).setNewGraphics("res/images/box.4.png", "res/images/box.4.png");
		boutons.get(0).addOnClickAction(() -> {
			levelEditor.addActor(new CrateBuilder(game, levelEditor));
			changeStatus();
		});

		// ground pos 1, 0
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(1).addOnClickAction(() -> {
			levelEditor.addGround();
			changeStatus();
		});

		// bike pos 3, 0
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(2).addOnClickAction(() -> {
			levelEditor.addBike(new BikeBuilder(game));
			changeStatus();
		});

		// mouving platform pos 1, -1
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(3).addOnClickAction(() -> {
			levelEditor.addActor(new PlatformBuilder(game));
			changeStatus();
		});

		// particle Emitter pos 2 -1
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(3).addOnClickAction(() -> {
			levelEditor.addActor(new ParticleEmitterBuilder(game));
			changeStatus();
		});

		for (GraphicalButton gb : boutons) {
			gb.setParent(this);
			gb.forceShape(width - betweenButton, height - betweenButton);
		}

		for (int i = 0; i < boutons.size(); i++) {
			boutons.get(i).setAnchor(new Vector((i % nbButonLine) * (sizeX + betweenButton),
					-sizeY - i / nbButonLine * (sizeY + betweenButton)));// .add(-betweenButton,
																			// -betweenButton));
		}
		minPosition = new Vector(-betweenButton, betweenButton);

		maxPosition = new Vector(nbButonLine * (sizeX + betweenButton),
				-(sizeY + betweenButton) * ((float) Math.ceil(boutons.size() / nbButonLine) + 1)).add(betweenButton,
						-betweenButton);
	}

	@Override
	public void update(float deltaTime, float zoom) {
		super.update(deltaTime, zoom);
		if (isRightPressed() && !isHovered()) {
			this.setStatus(true);
			this.setAnchor(getMousePosition());

		} else if (isLeftPressed() & !isHovered())
			this.setStatus(false);

		if (isOpen()) {
			for (GraphicalButton gb : boutons) {
				gb.update(deltaTime, zoom);
			}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (isOpen()) {

			canvas.drawShape(new Polygon(getRectangle(minPosition, minPosition.add(maxPosition))), getTransform(),
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

	@Override
	public void destroy() {
		for (GraphicalButton gb : this.boutons)
			gb.destroy();
	}

	@Override
	public boolean isHovered() {
		return ExtendedMath.isInRectangle(getPosition().add(minPosition), getPosition().add(maxPosition),
				getMousePosition());
	}

}

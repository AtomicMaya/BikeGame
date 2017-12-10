/**
 *	Author: Clément Jeannet
 *	Date: 	4 déc. 2017
 */
package main.game.GUI.menu;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.GUI.GraphicalButton;
import main.game.GUI.actorBuilder.*;
import main.math.ExtendedMath;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;
import main.window.Window;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class ActorMenu extends Menu {

	private ArrayList<GraphicalButton> boutons = new ArrayList<>();

	private float sizeX = 3f, sizeY = 3f;
	private float betweenButton = .2f;
	private ArrayList<Comment> description = new ArrayList<>();
	private Vector maxPosition = Vector.ZERO, minPosition = Vector.ZERO;

	private Vector position = new Vector(20, 0);
	float width, height;

	private final String text = "Actors menu";
	private final float fontSize = 1;

	private int nbButonLine = 3;

	public ActorMenu(ActorGame game, LevelEditor levelEditor, Window window, Color backgroundColor) {
		super(game, Vector.ZERO, false);

		this.setAnchor(position);

		// crate pos 0,0
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(0).setNewGraphics("res/images/box.4.png", "res/images/box.4.png");
		boutons.get(0).addOnClickAction(() -> {
			levelEditor.addActorBuilder(new CrateBuilder(game));
			changeStatus();
		});
		description.add(new Comment(game, "Add a crate"));

		// ground pos 1, 0
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(1).addOnClickAction(() -> {
			levelEditor.addGround();
			changeStatus();
		});
		description.add(new Comment(game, "Add or edit the Ground"));

		// bike pos 3, 0
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(2).addOnClickAction(() -> {
			levelEditor.addBike(new BikeBuilder(game));
			changeStatus();
		});
		description.add(new Comment(game, "Add or edit the bike"));

		// mouving platform pos 1, -1
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(3).addOnClickAction(() -> {
			levelEditor.addActorBuilder(new PlatformBuilder(game));
			changeStatus();
		});
		description.add(new Comment(game, "Add a platform"));

		// trampoline builder pos 2 -1
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(4).addOnClickAction(() -> {
			levelEditor.addActorBuilder(new TrampolineBuilder(game));
			changeStatus();
		});
		description.add(new Comment(game, "Add a trampoline"));

		// fluid
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(5).addOnClickAction(() -> {
			levelEditor.addActorBuilder(new LiquidBuilder(game));
			changeStatus();
		});
		description.add(new Comment(game, "Add a fluid"));

		// laser
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(6).addOnClickAction(() -> {
			levelEditor.addActorBuilder(new LaserBuilder(game));
			changeStatus();
		});
		description.add(new Comment(game, "Create a laser"));

		// mine
		boutons.add(new GraphicalButton(game, Vector.ZERO, sizeX, sizeY));
		boutons.get(boutons.size() - 1).addOnClickAction(() -> {
			levelEditor.addActorBuilder(new MineBuilder(game));
			changeStatus();
		});
		description.add(new Comment(game, "Create a mine"));
		
		for (GraphicalButton gb : boutons) {
			gb.forceShape(width - betweenButton, height - betweenButton);
			gb.setDepth(1337);
		}

		for (int i = 0; i < boutons.size(); i++) {
			boutons.get(i).setAnchor(new Vector((i % nbButonLine) * (sizeX + betweenButton),
					-sizeY - i / nbButonLine * (sizeY + betweenButton)).add(this.getAnchor()));
		}
		for (int i = 0; i < description.size(); i++) {
			description.get(i).setParent(boutons.get(i));
			description.get(i).setAnchor(new Vector(-10, 0));

		}
		minPosition = new Vector(-betweenButton, betweenButton);

		maxPosition = new Vector(nbButonLine * (sizeX + betweenButton),
				-(sizeY + betweenButton) * ((float) Math.ceil(boutons.size() / nbButonLine) + 1)).add(betweenButton,
						-betweenButton);
	}

	@Override
	public void update(float deltaTime, float zoom) {
		super.update(deltaTime, zoom);
		if (getOwner().getKeyboard().get(KeyEvent.VK_M).isPressed())
			changeStatus();
		else if (isRightPressed() && !isHovered())
			this.setStatus(true);

		else if ((isLeftPressed() & !isHovered()) || getOwner().getKeyboard().get(KeyEvent.VK_ENTER).isPressed())
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

			canvas.drawShape(new Polygon(createRectangle(minPosition.sub(0, -3), minPosition.add(maxPosition))),
					getTransform(), Color.LIGHT_GRAY, Color.LIGHT_GRAY, .1f * getZoom(), 1, 1335);

			for (int i = 0; i < boutons.size(); i++) {
				boutons.get(i).draw(canvas);
				if (boutons.get(i).isHovered())
					description.get(i).draw(canvas);
			}

			canvas.drawText(text, fontSize, getTransform().translated(new Vector(maxPosition.x / 2, 2).mul(getZoom())),
					Color.BLACK, Color.BLACK, .02f, false, false, new Vector(.5f, 0), 1, 1336);
		}
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

	private ArrayList<Vector> createRectangle(Vector un, Vector deux) {

		ArrayList<Vector> points = new ArrayList<>();

		points.add(un);
		points.add(new Vector(deux.x, un.y));
		points.add(deux);
		points.add(new Vector(un.x, deux.y));
		return points;

	}
}

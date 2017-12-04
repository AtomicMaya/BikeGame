/**
 *	Author: Clément Jeannet
 *	Date: 	4 déc. 2017
 */
package main.game.actor.menu;

import java.awt.Color;
import java.util.ArrayList;

import main.game.ActorGame;
import main.game.actor.entities.GraphicalButton;
import main.math.Vector;
import main.window.Canvas;
import main.window.Mouse;
import main.window.Window;

public class ActorMenu extends Menu {

	private Mouse mouse;

	private ArrayList<GraphicalButton> boutons = new ArrayList<>();

	public ActorMenu(ActorGame game, Window window, Color backgroundColor) {
		super(game, window, false, backgroundColor);
		mouse = window.getMouse();
		boutons.add(new GraphicalButton(game, Vector.ZERO, null, "", 2));
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (mouse.getRightButton().isPressed())
			this.changeStatut();
	}

	@Override
	public void draw(Canvas canvas) {

	}

	@Override
	public void changeStatut() {
		super.setStatut(!isOpen());
	}

}

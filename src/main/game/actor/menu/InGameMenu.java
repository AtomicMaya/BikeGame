/**
 *	Author: Clément Jeannet
 *	Date: 	3 déc. 2017
 */
package main.game.actor.menu;

import main.game.ActorGame;
import main.game.GameWithLevelAndMenu;
import main.game.actor.entities.BetterTextGraphics;
import main.game.actor.entities.GraphicalButton;
import main.math.Polygon;
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;
import main.window.Window;

import java.awt.*;
import java.awt.event.KeyEvent;

public class InGameMenu extends Menu {

	private ActorGame game;

	private GraphicalButton close, backToMainMenu;
	private Shape shape;

	private BetterTextGraphics menuText;

	
	public InGameMenu(GameWithLevelAndMenu game, Window window) {
		super(game, window, false, Color.GRAY);
		this.game = game;
		shape = new Polygon(-10, -10, -10, 10, 10, 10, 10, -10);

		close = new GraphicalButton(game, new Vector(6, -3), new Polygon(0, 0, 1, 0, 1, 1, 1, 0), "Close", 1);

		close.setNewGraphics("./res/images/button.white.1.png",
				"./res/images/button.white.2.png");

		close.addOnClickAction(() -> changeStatut(), .1f);

		backToMainMenu = new GraphicalButton(game, new Vector(-6, -3), new Polygon(0, 0, 0, 1, 1, 1, 1, 0), "Back to Menu", .5f);
		backToMainMenu.addOnClickAction(() -> game.goToMainMenu(), 0f);
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (game.getKeyboard().get(KeyEvent.VK_M).isPressed()
				|| game.getKeyboard().get(KeyEvent.VK_ESCAPE).isPressed()) {
			changeStatut();
		}
		if (isOpen()) {
			close.update(deltaTime);
			backToMainMenu.update(deltaTime);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		if (isOpen()) {
			
			close.draw(canvas);
			backToMainMenu.draw(canvas);
		}
	}

	@Override
	public void changeStatut() {
		super.changeStatut();
		game.setGameFreezeStatus(isOpen());
	}

}

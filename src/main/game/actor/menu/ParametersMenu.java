/**
 *	Author: Clément Jeannet
 *	Date: 	4 déc. 2017
 */
package main.game.actor.menu;

import main.game.ActorGame;
import main.game.actor.actorBuilder.ActorBuilder;
import main.window.Window;

import java.awt.*;

public class ParametersMenu extends Menu {

	public ParametersMenu(ActorGame game, Window window, Color backgroundColor, ActorBuilder ab) {
		super(game, window, false, backgroundColor, false);
	}

}

/**
 *	Author: Clément Jeannet
 *	Date: 	4 déc. 2017
 */
package main.game.actor.actorBuilder;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.QuickMafs;
import main.game.actor.crate.Crate;
import main.game.actor.menu.ParametersMenu;
import main.math.Vector;
import main.window.Canvas;

public class CrateBuilder extends ActorBuilder {

	private Crate crate;
	private Vector position;

	boolean placed = false;

	private ParametersMenu pm;
	private ActorGame game;

	public CrateBuilder(ActorGame game) {
		super(game);
		this.game = game;
		crate = new Crate(game, getMousePosition(), null, false, 1);
	}

	@Override
	public void update(float deltaTime) {
		// crate.update(deltaTime);
		if (!placed) {
			position = QuickMafs.floor(getMousePosition());
			if (isLeftPressed()) {
				placed = true;
			}
			crate.setPosition(position);
		}

	}

	@Override
	public void draw(Canvas canvas) {
		crate.draw(canvas);
	}

	@Override
	public Actor getActor() {
		return crate;
	}

	@Override
	public boolean isDone() {
		return placed;
	}

	@Override
	public void reCreate() {
		crate.destroy();
		crate = new Crate(game, position, null, false, 1);
	}

}

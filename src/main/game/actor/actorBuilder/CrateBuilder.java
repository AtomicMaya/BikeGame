/**
 *	Author: Clément Jeannet
 *	Date: 	4 déc. 2017
 */
package main.game.actor.actorBuilder;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.QuickMafs;
import main.game.actor.crate.Crate;
import main.window.Canvas;

public class CrateBuilder extends ActorBuilder {

	private Crate crate;

	private ActorGame game;

	boolean placed = false;

	public CrateBuilder(ActorGame game) {
		super(game);
		this.game = game;
		crate = new Crate(game, game.getMouse().getPosition(), null, false, 1);
	}

	@Override
	public void update(float deltaTime) {
		crate.update(deltaTime);
		if (!placed && game.getMouse().getLeftButton().isPressed()) {
			placed = true;
			crate.setPosition(QuickMafs.floor(game.getMouse().getPosition()));
		}
		if (!placed)
			crate.setPosition(game.getMouse().getPosition());

	}

	@Override
	public void draw(Canvas canvas) {
		crate.draw(canvas);
	}

	@Override
	public Actor getActor() {
		return crate;
	}

}

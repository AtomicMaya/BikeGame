package main.game.actor.entities;

import main.game.actor.Actor;
import main.window.Canvas;


public interface Lever extends Actor {
	default void update(float deltaTime) {

	}

	default void destroy() {

	}

	@Override
	default void draw(Canvas canvas) {

	}
}

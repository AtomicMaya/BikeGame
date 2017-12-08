package main.game.actor.entities.switchers;

import main.game.actor.Actor;
import main.window.Canvas;


public interface Switcher extends Actor {
	default void update(float deltaTime) {

	}

	default void destroy() {

	}

	@Override
	default void draw(Canvas canvas) {

	}
}

package main.game.actor.entities.collectable;

import main.game.actor.Actor;
import main.window.Canvas;

public interface Collectable extends Actor {
	default void update(float deltaTime) {
	}

	default void destroy() {
	}

	@Override
	default void draw(Canvas canvas) {
	}

	void setCollected(boolean collected);
}

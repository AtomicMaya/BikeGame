package main.game.actor.entities.collectable;

import main.game.actor.Actor;
import main.window.Canvas;

/** Contains all the collectible objects, such as {@linkplain Coin}'s. */
public interface Collectable extends Actor {
    /**
     * Simulates a single time step.
     * @param deltaTime elapsed time since last update, in seconds, non-negative
     */
    default void update(float deltaTime) { }

    default void destroy() { }

    @Override
    default void draw(Canvas canvas) { }
}

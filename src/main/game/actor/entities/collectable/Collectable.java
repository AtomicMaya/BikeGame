package main.game.actor.entities.collectable;

import main.game.actor.Actor;
import main.window.Canvas;

/**
 * Created on 12/6/2017 at 1:54 PM.
 */
public interface Collectable extends Actor {
    default void update(float deltaTime) { }

    default void destroy() { }

    @Override
    default void draw(Canvas canvas) { }

    void setCollected(boolean collected);
}

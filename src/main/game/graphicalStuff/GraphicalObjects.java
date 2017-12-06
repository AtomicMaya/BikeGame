package main.game.graphicalStuff;

import main.game.actor.Graphics;
import main.math.Vector;
import main.window.Canvas;

/**
 * Created on 12/4/2017 at 5:15 PM.
 */
public interface GraphicalObjects extends Graphics {
    @Override
    default void draw(Canvas canvas) {

    }

    void update(float deltaTime);

    Vector getSpeed();
    Vector getPosition();
    void setPosition(Vector position);
}

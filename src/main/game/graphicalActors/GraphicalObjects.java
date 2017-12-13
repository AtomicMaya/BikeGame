package main.game.graphicalActors;

import main.game.graphics.Graphics;
import main.math.Vector;
import main.window.Canvas;

/**
 * Created on 12/4/2017 at 5:15 PM.
 */
public interface GraphicalObjects extends Graphics {
    @Override
    default void draw(Canvas canvas) { }

    /**
     * Allows for the Container Actor (Typically Scenery.java) to update this on the game's update loop.
     * @param deltaTime : the loop time
     */
    void update(float deltaTime);

    /**
     * Retrieves the object's speed.
     * @return a Speed Vector (horizontal speed, vertical speed).
     */
    Vector getSpeed();

    /**
     * Retrieves the object's position.
     * @return a position Vector (x-axis position, y-axis position).
     */
    Vector getPosition();

    /**
     * Allows the container to give the object a new position.
     * @param position : a position Vector (x-axis position, y-axis position).
     */
    void setPosition(Vector position);

    /** @return whether the {@linkplain GraphicalObjects} should reset. */
    boolean getIfResets();

}

package main.game.actor;

import main.window.Canvas;

/**
 * Represents a drawable element.
 */
public interface Graphics {
    
    /**
     * Renders itself on specified canvas.
     * @param canvas target, not null
     */
    public void draw(Canvas canvas);
    
}

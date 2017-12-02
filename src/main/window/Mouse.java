package main.window;

import main.math.Positionable;
import main.math.Transform;
import main.math.Vector;

/**
 * Represents the mouse pointer.
 */
public interface Mouse extends Positionable {
        
    @Override
    default Transform getTransform() {
        Vector position = getPosition();
        return new Transform(1.0f, 0.0f, position.x, 0.0f, 1.0f, position.y);
    }

    Button getButton(int index);
    
    default Button getLeftButton() {
        return getButton(0);
    }
    
    default Button getMiddleButton() {
        return getButton(1);
    }
    
    default Button getRightButton() {
        return getButton(2);
    }
    
    // TODO wheel/scroll if needed
    
}

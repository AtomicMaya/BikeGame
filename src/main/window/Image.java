package main.window;

/**
 * Context-agnostic immutable image.
 */
public interface Image {

    /** @return width, in pixels */
    public int getWidth();
    
    /** @return height, in pixels */
    public int getHeight();
    
}

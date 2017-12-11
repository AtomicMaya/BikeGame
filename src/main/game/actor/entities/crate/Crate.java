/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.entities.crate;

import main.game.ActorGame;
import main.game.actor.entities.GameEntity;
import main.game.graphics.ImageGraphics;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

/** Represents a simple {@linkplain Crate} */
public class Crate extends GameEntity {
    /** Used for save purposes. */
    private static final long serialVersionUID = -677571549932797093L;

    /** Reference the {@linkplain Crate}'s graphical representation. */
    private transient ImageGraphics graphic;

    /** Reference used by the saving process. */
    private String imagePath;

    /** The size of this {@linkplain Crate}. */
    private float width, height;

    /**
     * Create a new {@linkplain Crate}.
     * @param game {@linkplain ActorGame} where the {@linkplain Crate} evolve.
     * @param position initial position {@linkplain Vector} of the {@linkplain Crate}.
     * @param imagePath path to the image to give to the {@linkplain Crate}, if null, set the image.
     * @param fixed Whether the {@linkplain Crate} is fixed.
     * @param width of the {@linkplain Crate}.
     * @param height of the {@linkplain Crate}.
     */
    public Crate(ActorGame game, Vector position, String imagePath, boolean fixed, float width, float height) {
        super(game, fixed, position);
        this.imagePath = (imagePath == null || imagePath.equals("")) ? "res/images/crate.1.png" : imagePath;
        this.width = width;
        this.height = height;
        this.create();
    }

    /**
     * @see #Crate(ActorGame, Vector, String, boolean, float, float)
     * Size replaced by width and height.
     */
    public Crate(ActorGame game, Vector position, String imagePath, boolean fixed, float size) {
        this(game, position, imagePath, fixed, size, size);
    }

    /**
     * Actual creation of the parameters of the {@linkplain GameEntity}, not in the
     * constructor to avoid duplication with the method {@linkplain #reCreate(ActorGame)}
     */
    private void create() {
        this.imagePath = (this.imagePath == null || !this.imagePath.equals("")) ? "res/images/crate.1.png" : this.imagePath;
        this.build(new Polygon(0, 0, 0, this.height, this.width, this.height, this.width, 0));
        this.graphic = this.addGraphics(this.imagePath, this.width, this.height);
    }

    /**
     * Override this {@linkplain Crate}'s size properties.
     * @param width of the {@linkplain Crate}.
     * @param height if the {@linkplain Crate}.
     */
    public void setSize(float width, float height) {
        this.width = width;
        this.height = height;
        this.create();
    }

    @Override
    public void reCreate(ActorGame game) {
        super.reCreate(game);
        this.create();
    }

    @Override
    public void draw(Canvas canvas) {
        this.graphic.draw(canvas);
    }



    @Override
    public void destroy() {
        super.destroy();
        super.getOwner().destroyActor(this);
    }

}

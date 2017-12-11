package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.graphics.ImageGraphics;
import main.math.Constraint;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

/** A Platform that can be used as a sub-object. */
public class GenericPlatform extends GameEntity {
    /** Used for save purposes. */
    private static final long serialVersionUID = 4001144688952819865L;

    /** Reference the {@linkplain GenericPlatform}'s graphical representation. */
    private transient ImageGraphics graphics;

    /** Dimensions of this {@linkplain GenericPlatform}. */
    private float width, height;

    /** The associated {@linkplain Constraint}. */
    private transient Constraint constraint;

    /**
     * Creates a new {@linkplain GenericPlatform}.
     * @param game The master {@linkplain ActorGame}.
     * @param anchorPosition The position {@linkplain Vector} of this {@linkplain GenericPlatform}.
     * @param width The width of the {@linkplain GenericPlatform}.
     * @param height The height of {@linkplain GenericPlatform}.
     */
    public GenericPlatform(ActorGame game, Vector anchorPosition, float width, float height) {
        super(game, false, anchorPosition);
        this.width = width;
        this.height = height;
        this.create();
    }

    /**
     * Actual creation of the parameters of the {@linkplain GameEntity}, not in
     * the constructor to avoid duplication with the method {@linkplain #reCreate(ActorGame)}
     */
    private void create() {
        Polygon shape = new Polygon(0, 0, this.width, 0, this.width, this.height, 0, this.height);
        this.build(shape, 10f, 1f, false, ObjectGroup.OBSTACLE.group);
        this.graphics = this.addGraphics("/res/images/wood.3.png", this.width, this.height, Vector.ZERO, .9f, 1);
    }

    @Override
    public void reCreate(ActorGame game) {
        super.reCreate(game);
        this.create();
    }

	@Override
	public void draw(Canvas canvas) {
		this.graphics.draw(canvas);
	}

    @Override
    public void destroy() {
        if (this.constraint != null)
            this.constraint.destroy();
        super.destroy();
        super.getOwner().destroyActor(this);
    }

    /**
     * Sets a new {@linkplain Constraint} to this {@linkplain GenericPlatform}.
     * @param constraint The new {@linkplain Constraint}.
     */
    public void setConstraint(Constraint constraint) {
        this.constraint = constraint;
    }

    /**
     * @return this {@linkplain GenericPlatform}'s associated {@linkplain Constraint}.
     */
    public Constraint getConstraint() {
        return this.constraint;
    }

    /**
     * Set a new size to this {@linkplain GenericPlatform}
     * @param width the new width.
     * @param height the new height.
     */
	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;
		this.create();
	}
}

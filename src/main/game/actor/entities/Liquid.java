package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.DepthValue;
import main.game.actor.ObjectGroup;
import main.game.graphics.ImageGraphics;
import main.io.Saveable;
import main.math.BasicContactListener;
import main.math.Entity;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;
import java.util.Random;

/** Random Liquids such as Lava and Acid */
public class Liquid extends GameEntity implements Saveable {
    
	/** Used for save purpose */
	private static final long serialVersionUID = 9050325927914605731L;

	/** An {@linkplain ArrayList} of {@linkplain ImageGraphics} containing the graphical components of this liquid. */
    private transient ArrayList<ImageGraphics> graphics;

    /** The {@linkplain Liquid}'s associated {@linkplain BasicContactListener}. */
    private BasicContactListener listener;

    /** The elapsed animation time. */
    private float elapsedAnimationTime;

    /** Dimensions of the {@linkplain Liquid}. */
    private float length, height;

    /** Whether the {@linkplain Liquid} is lava. */
    private boolean isLava;

    /** Whether or not the displayed {@linkplain ImageGraphics} should be replaced by it's inverted. */
    private boolean switched;

    /** A new {@linkplain Random} generator. */
    private transient Random random;

    /**
     * Creates a new {@linkplain Liquid}.
     * @param game The master {@linkplain ActorGame}.
     * @param position The initial position {@linkplain Vector}.
     * @param shape The initial {@linkplain main.math.Shape}
     * @param isLava Whether the {@linkplain Liquid} is lava.
     */
    public Liquid(ActorGame game, Vector position, Polygon shape, boolean isLava) {
        super(game, true, position);
        this.build(shape, -1, 1, true, ObjectGroup.OBSTACLE.group);
        this.isLava = isLava;
        this.switched = false;

        this.elapsedAnimationTime = 0;
        this.length = shape.getPoints().get(2).x;
        this.height = shape.getPoints().get(2).y;

        this.listener = new BasicContactListener();
        this.addContactListener(this.listener);
        this.create();
    }

    /**
     * Actual creation of the parameters of the {@linkplain GameEntity}, not in the
     * constructor to avoid duplication with the method {@linkplain #reCreate(ActorGame)}
     */
    private void create() {
        this.random = new Random();
        this.graphics = new ArrayList<>();
        for (int l = 0; l < this.length; l++) {
            for (int h = 0; h < this.height; h++) {
                if (h == this.height - 1 && !this.switched)
                    this.graphics.add(this.addGraphics(this.isLava ? "./res/images/lava.0.png" : "./res/images/acid.0.png", 1, 1, new Vector(-l, -h), 1, DepthValue.FRONT_OBSTACLE_LOW.value));
                else if (!this.switched)
                    this.graphics.add(this.addGraphics(this.isLava ? "./res/images/lava.2.png" : "./res/images/acid.2.png", 1, 1, new Vector(-l, -h), 1, DepthValue.FRONT_OBSTACLE_LOW.value));
            }
        }
    }

    @Override
    public void reCreate(ActorGame game) {
        super.reCreate(game);
        this.create();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        this.elapsedAnimationTime += deltaTime;
        if (this.elapsedAnimationTime > this.random.nextFloat()) {
            this.switched = !this.switched;
            this.graphics = new ArrayList<>();
            for (int l = 0; l < this.length; l++) {
                for (int h = 0; h < this.height; h++) {
                    if(h == this.height - 1 && !this.switched)
                        this.graphics.add(this.addGraphics(this.isLava ? "./res/images/lava.0.png" : "./res/images/acid.0.png", 1, 1, new Vector(-l, -h), 1, 2));
                    else if (h == this.height - 1 && this.switched)
                        this.graphics.add(this.addGraphics(this.isLava ?"./res/images/lava.1.png" : "./res/images/acid.1.png", 1, 1, new Vector(-l, -h), 1, 2));
                    else if (!this.switched)
                        this.graphics.add(this.addGraphics(this.isLava ?"./res/images/lava.2.png" : "./res/images/acid.2.png", 1, 1, new Vector(-l, -h), 1, 2));
                    else if (this.switched)
                        this.graphics.add(this.addGraphics(this.isLava ?"./res/images/lava.3.png" : "./res/images/acid.3.png", 1, 1, new Vector(-l, -h), 1, 2));
                }
            }
            this.elapsedAnimationTime = 0;
        }

        if(this.listener.getEntities().size() > 0) {
            for (Entity entity : this.listener.getEntities()) {
                if(entity.getCollisionGroup() == ObjectGroup.PLAYER.group || entity.getCollisionGroup() == ObjectGroup.WHEEL.group)
                    this.getOwner().getPayload().triggerDeath(false);
                //todo see if this works.
//                else
//                    entity.destroy();
            }
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        this.getOwner().destroyActor(this);
    }

    @Override
    public void draw(Canvas canvas) {
        for (ImageGraphics graphics : this.graphics)
            graphics.draw(canvas);
    }
}

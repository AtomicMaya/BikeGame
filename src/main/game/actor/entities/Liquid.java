package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.sensors.ProximitySensor;
import main.game.graphics.ImageGraphics;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;

/**
 * Created on 12/9/2017 at 1:06 PM.
 */
public class Liquid extends GameEntity {
    private ArrayList<ImageGraphics> graphics;
    private ProximitySensor sensor;
    private final float animationTime;
    private float elapsedAnimationTime, length, height;
    private boolean isLava, switched;

    public Liquid(ActorGame game, Vector position, Polygon shape, boolean isLava) {
        super(game, true, position);
        this.build(shape, -1, 1, true);
        this.isLava = isLava;
        this.switched = false;
        this.sensor = new ProximitySensor(game, position, shape);
        this.animationTime = .5f;
        this.elapsedAnimationTime = 0;
        this.length = shape.getPoints().get(2).x;
        this.height = shape.getPoints().get(2).y;

        this.graphics = new ArrayList<>();
        for (int l = 0; l < this.length; l++) {
            for (int h = 0; h < this.height; h++) {
                if (h == this.height - 1 && !this.switched)
                    this.graphics.add(this.addGraphics(this.isLava ? "./res/images/lava.0.png" : "./res/images/acid.0.png", 1, 1, new Vector(-l, -h), 1, 2));
                else if (!this.switched)
                    this.graphics.add(this.addGraphics(this.isLava ? "./res/images/lava.2.png" : "./res/images/acid.2.png", 1, 1, new Vector(-l, -h), 1, 2));
            }
        }

        game.addActor(this.sensor);
    }

    @Override
    public void update(float deltaTime) {
        this.elapsedAnimationTime += deltaTime;
        if (this.elapsedAnimationTime > animationTime) {
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
        if(this.sensor.getSensorDetectionStatus()) {
            this.getOwner().getPayload().triggerDeath(false);
        }
    }

    @Override
    public void destroy() {
        this.sensor.destroy();
        super.destroy();
        this.getOwner().destroyActor(this);
    }

    @Override
    public void draw(Canvas canvas) {
        for (ImageGraphics graphics : this.graphics)
            graphics.draw(canvas);
    }
}

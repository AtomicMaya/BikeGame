package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.sensors.KeyboardProximitySensor;
import main.game.graphics.ImageGraphics;
import main.math.*;
import main.window.Canvas;

import static java.awt.event.KeyEvent.VK_E;

/**
 * Created on 12/10/2017 at 12:27 AM.
 */
public class Shotgun extends GameEntity implements Weapon {
    private ImageGraphics graphics;
    private KeyboardProximitySensor sensor;
    private ActorGame game;
    private int ammoCount;
    private boolean shooting;
    private float elapsedTime, shotgunOutTime;
    private Vector position;
    private PrismaticConstraint constraint;

    public Shotgun(ActorGame game, Vector position, int initialAmmoCount) {
        super(game, false, position);
        this.position = position;
        this.game = game;
        this.graphics = this.addGraphics("./res/images/shotgun.png", 2, .5f, Vector.ZERO, 1, 10.2f);
        //this.sensor = new ProximitySensor(game, position.add(-2, 0), new Polygon(0, -0.25f, 2, -1.5f, 2, 1.5f, 0, 0.25f), new ArrayList<>(Arrays.asList(ObjectGroup.ENEMY.group)));
        this.sensor = new KeyboardProximitySensor(game, position.add(-2, 0), new Polygon(0, -0.25f, 2, -1.5f, 2, 1.5f, 0, 0.25f), VK_E);
        this.ammoCount = initialAmmoCount;
        this.shooting = false;
        this.elapsedTime = 0;
        this.shotgunOutTime = .5f;
        this.build(new Circle(.1f));
    }


    @Override
    public void fireWeapon() {
        if (this.ammoCount > 0) {
            this.ammoCount -= 1;
            this.shooting = true;
        }

    }

    @Override
    public void addAmmo(int quantity) {
        this.ammoCount += quantity;
    }

    @Override
    public Transform getTransform() {
        return null;
    }

    @Override
    public Vector getPosition() {
        return this.position;
    }

    @Override
    public Vector getVelocity() {
        return null;
    }

    @Override
    public void update(float deltaTime) {
        this.sensor.setPosition(this.getPosition().add(2, 0));
        this.sensor.update(deltaTime);
        if(this.sensor.getSensorDetectionStatus() && this.shooting) {
            //((Entity) this.sensor.getCollidingEntity()).destroy();
        }



        if(this.shooting)
            this.elapsedTime += deltaTime;
        if (this.elapsedTime > this.shotgunOutTime) {
            this.elapsedTime = 0;
            this.shooting = false;
        }
    }

    @Override
    public void destroy() {
        this.sensor.destroy();
        super.destroy();
        this.game.destroyActor(this);
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.shooting)
            this.graphics.draw(canvas);
        this.sensor.draw(canvas);
    }

    public void setConstraint(PrismaticConstraint constraint) {
        this.constraint = constraint;
    }

    public void detach() {
        if (this.constraint != null) this.constraint.destroy();
    }
}

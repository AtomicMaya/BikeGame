package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.graphics.ShapeGraphics;
import main.math.*;
import main.math.Polygon;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

import static main.math.ExtendedMath.invertXCoordinates;
import static main.math.ExtendedMath.xInverted;

public class Bike extends GameEntity implements PlayableEntity {
    /**
     * Because its asked
     */
    private static final long serialVersionUID = -5894386848192144642L;

    // Game where the Bike evolves.
    private transient ActorGame game;

    private final float MAX_WHEEL_SPEED = 20f;

    // Whether or not the bike is looking towards the rightEmitter.
    private boolean lookRight = true;
    private boolean wonTheGame, wasKilledByGravity;
    private int jumpCount;
    private float timeTillRejump, elapsedRejumpTime;

    private BasicContactListener contactListener;

    // Physical shape of the Bike.
    private transient Polygon hitbox;
    private transient Polyline bikeFrame;


    // Graphics representing the Bike.
    private transient ShapeGraphics bikeFrameGraphic;

    // Entities associated to the Bike.
    private transient Wheel leftWheel, rightWheel;
    private transient CharacterBike character;
    private float angle;

    private boolean isDead, wasTriggered = false;

    /**
     * Create a Bike, the BikeGame's main actor.
     * @param game : The actorGame in which this object exists.
     * @param position : The initial position of the Bike.
     */
    public Bike(ActorGame game, Vector position) {
        super(game, false, position);
        this.game = game;
        this.isDead = false;
        this.timeTillRejump = 3;

        this.create();
    }

    /**
     * Actual creation of the parameters of the GameEntity, not in the constructor to
     * avoid duplication with the method reCreate
     */
    private void create() {
        hitbox = new Polygon(0.0f, 0.5f, 0.5f, 1.0f, 0.0f, 2.0f, -0.5f, 1.0f);
        this.build(hitbox, -1, 5, false, ObjectGroup.PLAYER.group);

        this.contactListener = new BasicContactListener();
        this.addContactListener(this.contactListener);

        this.bikeFrame = new Polyline(-1.3f, .8f, -1.f, .9f,
                -1.f, .9f, -.7f, .8f, -.3f, .8f, // rear mud guard
                -.4f, 1.1f, -.3f, .7f, // ass holder
                -1.f, 0.1f, -.25f, .2f,
                -1.f, 0.1f, -.3f, .7f,
                -.25f, .2f, .8f, .85f,
                -.3f, .7f, .8f, .85f, // Frame
                1.f, .85f, 1.f, 1.25f,
                0.9f, 1.3f, 1.f, 1.25f,
                1.f, .8f, 1.f, 0.1f,
                1.f, .8f, 1.3f, .75f, 1.4f, .7f);
        this.bikeFrameGraphic = this.addGraphics(bikeFrame, null,  Color.decode("#58355e"), .1f, 1, 10);

        this.leftWheel = new Wheel(this.game, new Vector(-1, 0).add(this.getPosition()), .5f);
        this.rightWheel = new Wheel(this.game, this.getPosition().add(new Vector(1, 0)), .5f);
        this.character = new CharacterBike(this.game, this.getPosition());

        this.leftWheel.attach(this.getEntity(), new Vector(-1.0f, 0.0f), new Vector(-0.5f, -1.0f));
        this.rightWheel.attach(this.getEntity(), new Vector(1.0f, 0.0f), new Vector(0.5f, -1.0f));
        this.character.attach(this.getEntity(), new Vector(0.f, 0.5f));
        this.angle = 0;
    }

    @Override
    public void reCreate(ActorGame game) {
        super.reCreate(game);
        this.game = game;
        create();
    }

    @Override
    public void update(float deltaTime) {
        if(this.contactListener.getEntities().size() > 0) {
            for(Entity entity : this.contactListener.getEntities()) {
                if(entity.getCollisionGroup() == ObjectGroup.TERRAIN.group) {
                    this.isDead = true;
                    if(!this.wasTriggered) {
                        this.triggerDeath(true);
                        this.wasTriggered = true;
                    }
                }
            }
        }

        this.leftWheel.relax();
        this.rightWheel.relax();

        if (this.game.getKeyboard().get(KeyEvent.VK_SPACE).isPressed()) {
            this.lookRight = !this.lookRight;
            this.character.invertX();
            this.bikeFrame = new Polyline(invertXCoordinates(this.bikeFrame.getPoints(), xInverted));
            this.bikeFrameGraphic = addGraphics(this.bikeFrame, null, Color.decode("#58355e"), .1f, 1.f, .0f);
        }

        if (this.game.getKeyboard().get(KeyEvent.VK_Q).isPressed() && this.jumpCount < 2) {
            this.getEntity().setVelocity(this.getVelocity().add(0, 10));
            this.jumpCount += 1;
        }
        if (this.jumpCount == 2)
            this.elapsedRejumpTime += deltaTime;
        if (this.elapsedRejumpTime > this.timeTillRejump) {
            this.elapsedRejumpTime = 0;
            this.jumpCount = 0;
        }

        this.leftWheel.relax();
        this.rightWheel.relax();

        if (this.game.getKeyboard().get(KeyEvent.VK_S).isDown()) {
            this.leftWheel.power(0);
            this.rightWheel.power(0);
        } else if (this.game.getKeyboard().get(KeyEvent.VK_W).isDown() && !this.wonTheGame) {
            if (this.game.getKeyboard().get(KeyEvent.VK_A).isDown()) {
                this.getEntity().applyAngularForce(10.0f);
            } else if (this.game.getKeyboard().get(KeyEvent.VK_D).isDown()) {
                this.getEntity().applyAngularForce(-10.f);
            }

            this.angle -= 5f;
            this.character.nextPedal(this.angle);

            if (this.lookRight && this.leftWheel.getSpeed() > -this.MAX_WHEEL_SPEED) {
                this.leftWheel.power(-this.MAX_WHEEL_SPEED);
            } else if (this.rightWheel.getSpeed() < this.MAX_WHEEL_SPEED) {
                this.rightWheel.power(this.MAX_WHEEL_SPEED);
            }

        }

        if (this.wonTheGame && this.lookRight) this.leftWheel.power(0);
        else if (this.wonTheGame && !this.lookRight) this.rightWheel.power(0);

        this.leftWheel.update(deltaTime);
        this.rightWheel.update(deltaTime);
        this.character.update(deltaTime);
    }


    @Override
    public void draw(Canvas canvas) {
        this.bikeFrameGraphic.draw(canvas);
        this.leftWheel.draw(canvas);
        this.rightWheel.draw(canvas);
        this.character.draw(canvas);
    }

    @Override
    public void destroy() {
        this.leftWheel.destroy();
        this.rightWheel.destroy();
        this.character.destroy();
        super.destroy();
        super.getOwner().destroyActor(this);
    }


    @Override
    public void triggerDeath(boolean wasGravity) {
        this.isDead = true;
        this.wasKilledByGravity = wasGravity;

        this.game.addActor(new ParticleEmitter(this.game, this.getPosition().sub(0, .25f), null, 100, (float) -Math.PI / 2f,
                (float) Math.PI, 1.2f, .1f, 1, .3f,
                0xFF830303,  	0x00830303, 1, 20));
        try { Thread.sleep(10); } catch (InterruptedException ignored) {}
        this.destroy();
    }

    @Override
    public void triggerVictory() {

        this.wonTheGame = true;
        this.game.addActor(new ParticleEmitter(this.game, this.getPosition().add(-4, 5), null, 75, (float) Math.PI / 2,
                (float) Math.PI, 1.5f, .1f, 1, .3f,
                0xFFFFFF00, 0xFFFF0000, 2, 10));
        this.game.addActor(new ParticleEmitter(this.game, this.getPosition().add(3, 7), null, 75, (float) Math.PI / 2,
                (float) Math.PI, 1.5f, .1f, 1, .3f,
                0xFFADFF2F,  	0x00551A8B, 2, 10));
    }

    @Override
    public void triggerCheckpoint() {

    }

    @Override
    public boolean getDeathStatus() {
        return this.isDead;
    }

    @Override
    public boolean getVictoryStatus() {
        return this.wonTheGame;
    }

    @Override
    public boolean getIfWasKilledByGravity() {
        return this.wasKilledByGravity;
    }
}


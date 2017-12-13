package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.DepthValue;
import main.game.actor.Linker;
import main.game.actor.ObjectGroup;
import main.game.actor.weapons.Rocket;
import main.game.actor.weapons.Shotgun;
import main.game.actor.weapons.Weapon;
import main.game.graphics.ShapeGraphics;
import main.math.*;
import main.math.Polygon;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Set;

import static main.math.ExtendedMath.invertXCoordinates;
import static main.math.ExtendedMath.xInverted;

/** Main character of the game */
public class Bike extends GameEntity implements PlayableEntity {
    /** Used for save purposes. */
    private static final long serialVersionUID = -5894386848192144642L;

    /** The master {@linkplain ActorGame}.*/
    private transient ActorGame game;

    /** The maximum {@linkplain Wheel} speed. Default {@value} */
    private transient final float MAX_WHEEL_SPEED = 20f;

    /** Whether this {@linkplain Bike} is looking towards the right side of the game. */
    private boolean lookRight = true;

    /** Various parameters representing the player's game state. */
    private transient boolean wonTheGame, wasKilledByGravity;

    /** Counts the number of jumps. */
    private transient int jumpCount = 0;

    /** Represents the quantity of time till the player can double jump again. */
    private transient float timeTillRejump = 20f, elapsedRejumpTime = 0;

    /** A {@linkplain BasicContactListener} associated to this {@linkplain Bike}. */
    private transient BasicContactListener contactListener;

    /** A {@linkplain Polygon} representing the physical shape of this {@linkplain Bike}. */
    private transient Polygon hitbox;

    /** A {@linkplain Polyline} representing the physical shape of this {@linkplain Bike}. */
    private transient Polyline bikeFrame;

    /** The {@linkplain ShapeGraphics} representation of the Bike Frame. */
    private transient ShapeGraphics bikeFrameGraphic;

    /** The {@linkplain Wheel}s associated to this {@linkplain Bike}. */
    private transient Wheel leftWheel, rightWheel;

    /** The {@linkplain CharacterBike} associated to this {@linkplain Bike}. */
    private transient CharacterBike character;

    /** The angle of pedaling. */
    private transient float angle;

    /** Whether this {@linkplain PlayableEntity} is dead. */
    private transient boolean isDead = false;

    /** Whether this {@linkplain PlayableEntity} was triggered. */
    private transient boolean wasTriggered = false;

    /** The {@linkplain ArrayList} of {@linkplain Weapon}s held by this {@linkplain PlayableEntity}. */
    private transient ArrayList<Weapon> weapons = new ArrayList<>();

    /** The current {@linkplain Weapon}. */
    private int activeWeapon = 0;

    /** Whether the {@linkplain Weapon} is deployed. */
    private transient boolean isWeaponDeployed = false;

    /**
     * Create a {@linkplain Bike}, the BikeGame's main actor.
     * @param game The {@linkplain ActorGame} in which this object exists.
     * @param position The initial position {@linkplain Vector} of the {@linkplain Bike}.
     */
    public Bike(ActorGame game, Vector position) {
        super(game, false, position);
        this.game = game;
        this.create();
    }

    /**
     * Actual creation of the parameters of the {@linkplain GameEntity}, not in the
     * constructor to avoid duplication with the method {@linkplain #reCreate(ActorGame)}
     */
    private void create() {
        this.hitbox = new Polygon(0.0f, 0.5f, 0.5f, 1.0f, 0.0f, 2.0f, -0.5f, 1.0f);
        this.build(this.hitbox, -1, 5, false, ObjectGroup.PLAYER.group);

        this.contactListener = new BasicContactListener();
        this.addContactListener(this.contactListener);

        this.bikeFrame = new Polyline(-1.3f, .8f, -1.f, .9f, -1.f, .9f, -.7f, .8f, -.3f, .8f,
                -.4f, 1.1f, -.3f, .7f, -1.f, 0.1f, -.25f, .2f, -1.f, 0.1f, -.3f, .7f, -.25f, .2f, .8f, .85f, -.3f, .7f,
                .8f, .85f, 1.f, .85f, 1.f, 1.25f, 0.9f, 1.3f, 1.f, 1.25f, 1.f, .8f, 1.f, 0.1f, 1.f, .8f, 1.3f, .75f, 1.4f, .7f);
        this.bikeFrameGraphic = this.addGraphics(this.bikeFrame, null, Color.decode("#58355e"), .1f, 1, DepthValue.PLAYER_MEDIUM.value);

        this.leftWheel = new Wheel(this.game, new Vector(-1, 0).add(this.getPosition()), .5f);
        this.rightWheel = new Wheel(this.game, this.getPosition().add(new Vector(1, 0)), .5f);
        this.character = new CharacterBike(this.game, this.getPosition());

        this.weapons = new ArrayList<>();
        this.weapons.add(new Shotgun(this.game, 3, this));
        this.weapons.add(new Rocket(game,1, this));

        this.activeWeapon = 0;

        this.leftWheel.attach(this.getEntity(), new Vector(-1.0f, 0.0f), new Vector(-0.5f, -1.0f));
        this.rightWheel.attach(this.getEntity(), new Vector(1.0f, 0.0f), new Vector(0.5f, -1.0f));
        this.character.setConstraint(Linker.attachPrismatically(this.game, this.getEntity(), this.character.getEntity(),
                new Vector(0.f, 0.5f)));
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
        super.update(deltaTime);

        // Checks whether the Bike has collided with the ground or an obstacle.
        if (this.contactListener.getEntities().size() > 0) {
        	Set<Entity> entities = this.contactListener.getEntities();
            for (Entity entity : entities) {
                if (entity.getCollisionGroup() == ObjectGroup.TERRAIN.group
                        || entity.getCollisionGroup() == ObjectGroup.OBSTACLE.group) {
                    this.isDead = true;
                    if (!this.wasTriggered) {
                        this.triggerDeath(true);
                        this.wasTriggered = true;
                    }
                }
            }
        }

        // Relax both Wheels
        this.leftWheel.relax();
        this.rightWheel.relax();

        // Tilt the Bike
        if (this.game.getKeyboard().get(KeyEvent.VK_A).isDown()) {
            this.getEntity().applyAngularForce(20.0f);
        } else if (this.game.getKeyboard().get(KeyEvent.VK_D).isDown()) {
            this.getEntity().applyAngularForce(-20.f);
        }

        // Switch the orientation of the Bike.
        if (this.game.getKeyboard().get(KeyEvent.VK_SPACE).isPressed()) {
            this.lookRight = !this.lookRight;
            this.character.invertX();
            this.bikeFrame = new Polyline(invertXCoordinates(this.bikeFrame.getPoints(), xInverted));
            this.bikeFrameGraphic = addGraphics(this.bikeFrame, null, Color.decode("#58355e"), .1f, 1.f, DepthValue.PLAYER_MEDIUM.value);
        }

        // Make the Bike jump.
        if (this.game.getKeyboard().get(KeyEvent.VK_Q).isPressed() && this.jumpCount < 1) {
            this.getEntity().setVelocity(this.getVelocity().add(0, 13));
            System.out.println(this.jumpCount);
            this.jumpCount += 1;
        }
        if (this.jumpCount <= 1)
            this.elapsedRejumpTime += deltaTime;
        if (this.elapsedRejumpTime > this.timeTillRejump) {
            this.elapsedRejumpTime = 0;
            this.jumpCount = 0;
        }

        // Make the Bike brake.
        if (this.game.getKeyboard().get(KeyEvent.VK_S).isDown()) {
            this.leftWheel.power(0);
            this.rightWheel.power(0);
        } else if (this.game.getKeyboard().get(KeyEvent.VK_W).isDown() && !this.wonTheGame) { // Make the Bike go forward.
            this.angle -= 5f;
            this.character.nextPedal(this.angle);

            if (this.lookRight && this.leftWheel.getSpeed() > -this.MAX_WHEEL_SPEED) {
                this.leftWheel.power(-this.MAX_WHEEL_SPEED);
            } else if (this.rightWheel.getSpeed() < this.MAX_WHEEL_SPEED) {
                this.rightWheel.power(this.MAX_WHEEL_SPEED);
            }
        }
        if (this.rightWheel.isCollidingWithTerrain() && this.leftWheel.isCollidingWithTerrain()) {
            this.elapsedRejumpTime = this.timeTillRejump - .1f;
        }

        // Handle Weapons
        if (this.isWeaponDeployed
                && (getOwner().getMouse().getMouseScrolledDown() || getOwner().getMouse().getMouseScrolledUp())) {
            this.swapWeapon();
        }
        if (getOwner().getKeyboard().get(KeyEvent.VK_F).isPressed()) {
            this.isWeaponDeployed = !this.isWeaponDeployed;
            this.weapons.get(this.activeWeapon).deploy(this.isWeaponDeployed);
        }

        
        if (this.wonTheGame && this.lookRight)
            this.leftWheel.power(0);
        else if (this.wonTheGame && !this.lookRight)
            this.rightWheel.power(0);

        this.leftWheel.update(deltaTime);
        this.rightWheel.update(deltaTime);
        this.character.update(deltaTime);
        this.weapons.get(this.activeWeapon).update(deltaTime);
    }

    @Override
    public void draw(Canvas canvas) {
        this.bikeFrameGraphic.draw(canvas);
        this.leftWheel.draw(canvas);
        this.rightWheel.draw(canvas);
        this.character.draw(canvas);

        this.weapons.get(this.activeWeapon).draw(canvas);
    }

    @Override
    public void destroy() {
        this.leftWheel.destroy();
        this.rightWheel.destroy();
        this.character.destroy();
        for (Weapon weapon : this.weapons)
            weapon.destroy();
        super.destroy();
        super.getOwner().destroyActor(this);
    }

    @Override
    public void triggerDeath(boolean wasGravity) {
        this.isDead = true;
        this.wasKilledByGravity = wasGravity;

        this.game.addActor(new ParticleEmitter(this.game, this.getPosition().sub(0, .25f), null, 100,
                (float) -Math.PI / 2f, (float) Math.PI, 1.2f, .1f, 1, .3f, 0xFF830303, 0x00830303, 2, 20));
        try {
            Thread.sleep(10);
        } catch (InterruptedException ignored) {}
        this.destroy();
    }

    @Override
    public void triggerVictory() {

        if (!this.wonTheGame) {
            this.game.addActor(new ParticleEmitter(this.game, this.getPosition().add(-7, 6), null, 200,
                    (float) Math.PI / 2, (float) Math.PI, 1.5f, .1f, 1, .3f, 0xFFFFFF00, 0xFFFF0000, 2, 10));
            this.game.addActor(new ParticleEmitter(this.game, this.getPosition().add(3, 8), null, 200,
                    (float) Math.PI / 2, (float) Math.PI, 1.5f, .1f, 1, .3f, 0xFFADFF2F, 0x00551A8B, 2, 10));
            this.character.triggerHappiness();
        }
        this.wonTheGame = true;
    }

    /** Swap between the different {@linkplain Weapon}. */
    public void swapWeapon() {
        this.weapons.get(activeWeapon).deploy(false);
        this.activeWeapon++;
        this.activeWeapon = (this.activeWeapon > this.weapons.size() - 1) ? 0 : this.activeWeapon;
        this.weapons.get(this.activeWeapon).deploy(this.isWeaponDeployed);
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

    @Override
    public boolean isLookingRight() {
        return this.lookRight;
    }
    
    @Override
    public void addAmmos(int quantity, int type) {
    	// TODO
    }
}

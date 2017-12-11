package main.game.actor.entities;

import static main.math.ExtendedMath.invertXCoordinates;
import static main.math.ExtendedMath.xInverted;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import main.game.ActorGame;
import main.game.actor.Linker;
import main.game.actor.ObjectGroup;
import main.game.actor.sensors.Checkpoint;
import main.game.actor.sensors.Trigger;
import main.game.actor.weapons.Rocket;
import main.game.actor.weapons.Shotgun;
import main.game.actor.weapons.Weapon;
import main.game.graphics.ShapeGraphics;
import main.math.BasicContactListener;
import main.math.Entity;
import main.math.Polygon;
import main.math.Polyline;
import main.math.Vector;
import main.window.Canvas;

/** Main character of the game */
public class Bike extends GameEntity implements PlayableEntity {

	private static final long serialVersionUID = -5894386848192144642L;

	// Game where the Bike evolves.
	private transient ActorGame game;

	// wheel max speed
	private transient final float MAX_WHEEL_SPEED = 20f;

	// Whether or not the bike is looking towards the rightEmitter.
	private boolean lookRight = true;
	private transient boolean wonTheGame, wasKilledByGravity;
	private transient int jumpCount;
	private transient float timeTillRejump = 3, elapsedRejumpTime = 0;

	private transient BasicContactListener contactListener;

	// Physical shape of the Bike.
	private transient Polygon hitbox;
	private transient Polyline bikeFrame;

	// Graphics representing the Bike.
	private transient ShapeGraphics bikeFrameGraphic;

	// Entities associated to the Bike.
	private transient Wheel leftWheel, rightWheel;
	private transient CharacterBike character;
	private transient float angle;

	private transient boolean isDead = false, wasTriggered = false;

	// weapons
	private transient ArrayList<Weapon> weapons = new ArrayList<>();
	private int activeWeapon = 0;
	private transient boolean isWeaponDeployed = false;

	/**
	 * Create a Bike, the BikeGame's main actor.
	 * @param game : The actorGame in which this object exists.
	 * @param position : The initial position of the Bike.
	 */
	public Bike(ActorGame game, Vector position) {
		super(game, false, position);
		this.game = game;

		this.create();
	}

	/**
	 * Actual creation of the parameters of the GameEntity, not in the
	 * constructor to avoid duplication with the method reCreate
	 */
	private void create() {
		this.hitbox = new Polygon(0.0f, 0.5f, 0.5f, 1.0f, 0.0f, 2.0f, -0.5f, 1.0f);
		this.build(this.hitbox, -1, 5, false, ObjectGroup.PLAYER.group);

		this.contactListener = new BasicContactListener();
		this.addContactListener(this.contactListener);

		this.bikeFrame = new Polyline(-1.3f, .8f, -1.f, .9f, -1.f, .9f, -.7f, .8f, -.3f, .8f, // rear
																								// mud
																								// guard
				-.4f, 1.1f, -.3f, .7f, // ass holder
				-1.f, 0.1f, -.25f, .2f, -1.f, 0.1f, -.3f, .7f, -.25f, .2f, .8f, .85f, -.3f, .7f, .8f, .85f, // Frame
				1.f, .85f, 1.f, 1.25f, 0.9f, 1.3f, 1.f, 1.25f, 1.f, .8f, 1.f, 0.1f, 1.f, .8f, 1.3f, .75f, 1.4f, .7f);
		this.bikeFrameGraphic = this.addGraphics(bikeFrame, null, Color.decode("#58355e"), .1f, 1, 1);

		this.leftWheel = new Wheel(this.game, new Vector(-1, 0).add(this.getPosition()), .5f);
		this.rightWheel = new Wheel(this.game, this.getPosition().add(new Vector(1, 0)), .5f);
		this.character = new CharacterBike(this.game, this.getPosition());

		this.weapons = new ArrayList<>();
		this.weapons.add(new Shotgun(this.game, 3, this));
		this.weapons.add(new Rocket(game, 3, this));

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
		if (this.contactListener.getEntities().size() > 0) {
			for (Entity entity : this.contactListener.getEntities()) {
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

		this.leftWheel.relax();
		this.rightWheel.relax();

		if (this.game.getKeyboard().get(KeyEvent.VK_SPACE).isPressed()) {
			this.lookRight = !this.lookRight;
			this.character.invertX();
			this.bikeFrame = new Polyline(invertXCoordinates(this.bikeFrame.getPoints(), xInverted));
			this.bikeFrameGraphic = addGraphics(this.bikeFrame, null, Color.decode("#58355e"), .1f, 1.f, .0f);
		}

		if (this.game.getKeyboard().get(KeyEvent.VK_Q).isPressed() && this.jumpCount < 2) {
			this.getEntity().setVelocity(this.getVelocity().add(0, 15));
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

		// weapon part
		if (isWeaponDeployed
				&& (getOwner().getMouse().getMouseScrolledDown() || getOwner().getMouse().getMouseScrolledUp())) {
			this.swapWeapon();
		}
		if (getOwner().getKeyboard().get(KeyEvent.VK_F).isPressed()) {
			isWeaponDeployed = !isWeaponDeployed;
			this.weapons.get(activeWeapon).deploy(isWeaponDeployed);
		}

		if (this.wonTheGame && this.lookRight)
			this.leftWheel.power(0);
		else if (this.wonTheGame && !this.lookRight)
			this.rightWheel.power(0);

		this.leftWheel.update(deltaTime);
		this.rightWheel.update(deltaTime);
		this.character.update(deltaTime);
		this.weapons.get(activeWeapon).update(deltaTime);
	}

	@Override
	public void draw(Canvas canvas) {
		this.bikeFrameGraphic.draw(canvas);
		this.leftWheel.draw(canvas);
		this.rightWheel.draw(canvas);
		this.character.draw(canvas);

		this.weapons.get(activeWeapon).draw(canvas);
	}

	@Override
	public void destroy() {
		this.leftWheel.destroy();
		this.rightWheel.destroy();
		this.character.destroy();
		for (Weapon w : weapons)
			w.destroy();
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

		if (!wonTheGame) {
			this.game.addActor(new ParticleEmitter(this.game, this.getPosition().add(-7, 6), null, 200,
					(float) Math.PI / 2, (float) Math.PI, 1.5f, .1f, 1, .3f, 0xFFFFFF00, 0xFFFF0000, 2, 10));
			this.game.addActor(new ParticleEmitter(this.game, this.getPosition().add(3, 8), null, 200,
					(float) Math.PI / 2, (float) Math.PI, 1.5f, .1f, 1, .3f, 0xFFADFF2F, 0x00551A8B, 2, 10));
		}
		this.wonTheGame = true;
	}

	/** Swap between the different {@linkplain Weapon} */
	public void swapWeapon() {
		weapons.get(activeWeapon).deploy(false);
		activeWeapon++;
		activeWeapon = (activeWeapon > weapons.size() - 1) ? 0 : activeWeapon;
		weapons.get(activeWeapon).deploy(isWeaponDeployed);
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
		return lookRight;
	}
}

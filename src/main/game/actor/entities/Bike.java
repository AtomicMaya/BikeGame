package main.game.actor.entities;

import static main.game.actor.QuickMafs.invertXCoordinates;
import static main.game.actor.QuickMafs.xInverted;

import java.awt.Color;
import java.awt.event.KeyEvent;

import main.game.ActorGame;
import main.game.actor.ShapeGraphics;
import main.math.*;
import main.math.Polygon;
import main.math.Polyline;
import main.math.Vector;
import main.window.Canvas;

public class Bike extends GameEntity {
	/**
	 * Because its asked
	 */
	private static final long serialVersionUID = -5894386848192144642L;

	// game where the Bike evolves
	private transient ActorGame game;

	private final float MAX_WHEEL_SPEED = 20f;

	// Whether or not the bike is looking towards the right
	private boolean lookRight = true;

	private BasicContactListener contactListener;

	// Physical shape of the Bike
	private transient Polygon hitbox;
	private transient Polyline bikeFrame;

	// Graphics to represent the Bike
	private transient ShapeGraphics bikeFrameGraphic;

	// Entities associated to the bike
	private transient Wheel leftWheel, rightWheel;
	private transient CharacterBike character;

	/**
	 * Create a Bike, controllable by the player
	 * @param game : the actorGame in which this object exists
	 * @param position : the initial position of the bike
	 */
	public Bike(ActorGame game, Vector position) {
		super(game, false, position);
		this.game = game;

		this.create();
	}

	/**
	 * Actual creation of the parameters of the GameEntity, not in the constructor to
	 * avoid duplication with the method reCreate
	 */
	private void create() {
		hitbox = new Polygon(0.0f, 0.5f, 0.5f, 1.0f, 0.0f, 2.0f, -0.5f, 1.0f);
		this.build(hitbox);

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
		this.bikeFrameGraphic = this.addGraphics(bikeFrame, null, Color.blue.brighter().brighter().brighter(), .1f, 1, 0);

		this.leftWheel = new Wheel(this.game, new Vector(-1, 0).add(this.getPosition()), .5f);
		this.rightWheel = new Wheel(this.game, this.getPosition().add(new Vector(1, 0)), .5f);
		this.character = new CharacterBike(this.game, this.getPosition());

		this.leftWheel.attach(this.getEntity(), new Vector(-1.0f, 0.0f), new Vector(-0.5f, -1.0f));
		this.rightWheel.attach(this.getEntity(), new Vector(1.0f, 0.0f), new Vector(0.5f, -1.0f));
		this.character.attach(this.getEntity(), new Vector(0.f, 0.5f));
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
	            if(!entity.isGhost()) {
	                //TODO trigger death ->
                }
            }
        }

		this.leftWheel.relax();
		this.rightWheel.relax();

		if (this.game.getKeyboard().get(KeyEvent.VK_SPACE).isPressed()) {
			this.lookRight = !this.lookRight;
			this.character.invertX();
			this.bikeFrame = new Polyline(invertXCoordinates(this.bikeFrame.getPoints(), xInverted));
			this.bikeFrameGraphic = addGraphics(this.bikeFrame, null, Color.BLUE.brighter().brighter().brighter(), .1f, 1.f, .0f);
		}

		if (this.game.getKeyboard().get(KeyEvent.VK_S).isDown()) {
			this.leftWheel.power(0);
			this.rightWheel.power(0);
		} else if (this.game.getKeyboard().get(KeyEvent.VK_W).isDown()) {
			this.character.nextPedal();
			if (this.lookRight && this.leftWheel.getSpeed() > -this.MAX_WHEEL_SPEED) {
				this.leftWheel.power(-this.MAX_WHEEL_SPEED);
			} else if (this.rightWheel.getSpeed() < this.MAX_WHEEL_SPEED) {
				this.rightWheel.power(this.MAX_WHEEL_SPEED);
			}
			if (this.game.getKeyboard().get(KeyEvent.VK_A).isDown()) {
				this.getEntity().applyAngularForce(10.0f);
			} else if (this.game.getKeyboard().get(KeyEvent.VK_D).isDown()) {
				this.getEntity().applyAngularForce(-10.f);
			}
		}

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

}


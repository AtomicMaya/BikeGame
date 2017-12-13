package main.game.actor.weapons;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.actor.Actor;
import main.game.actor.entities.PlayableEntity;
import main.math.ExtendedMath;
import main.math.Polygon;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;

/** Represent a weapon */
public abstract class Weapon implements Actor {

	// general params
	/** {@linkplain PlayableEntity} who own this {@linkplain Weapon} */
	private PlayableEntity player;
	
	/** The master {@linkplain ActorGame} */
	private ActorGame game;

	// weapon params
	/** Whether this weapon is deployed */
	private boolean deployed = false;
	
	/** Actual number of ammo */
	private int ammoCount;
	
	/** Shot time */
	private float elapsedTime = 0, betweenShotTime;
	
	/** Whether it has shot */
	private boolean hasShot = false;

	// GUI stuff
	/** Show ammo number */
	private Comment amoNumber;
	
	/** Text for the {@linkplain Comment} {@link #amoNumber} */
	private String amoText = " shots left";
	
	/** Absolute position on screen */
	private Vector amoNumberPos = new Vector(17, 8);

	/** Loading bar paramenetrs */
	float loadWidth = 5, loadHeight = .2f, loadSizeDif = .05f;
	
	/** Shape of the red loading bar */
	private Polygon loadingBackground = ExtendedMath.createRectangle(loadWidth, loadHeight);
	
	/** Shape of the right loading bar */
	private Polygon loading = ExtendedMath.createRectangle(loadWidth, loadHeight);
	
	/** loading bar position */
	private Vector loadingPos = new Vector(0, 9);

	/** 
	 * Create a new {@linkplain Weapon} 
	 * @param game the master {@linkplain ActorGame}
	 * @param player Owner of this {@linkplain Weapon}
	 * @param initialAmmoNumber Initial number of ammo
	 * @param betweenShotTime Time to wait between the shots
	 * */
	public Weapon(ActorGame game, PlayableEntity player, int initialAmmoNumber, float betweenShotTime) {
		this.game = game;
		this.player = player;
		this.betweenShotTime = betweenShotTime;
		this.ammoCount = initialAmmoNumber;

		this.amoNumber = new Comment(game, initialAmmoNumber + this.amoText);
		this.amoNumber.setAnchor(this.amoNumberPos);

	}

	@Override
	public void update(float deltaTime) {
		if (this.isDeployed() && getOwner().getMouse().getLeftButton().isPressed() && !this.hasShot) {
			fireWeapon();
		}
		if (this.isDeployed()) {
			if (hasShot()) {
				this.elapsedTime += deltaTime;
				if (this.elapsedTime > this.betweenShotTime) {
					this.elapsedTime = 0;
					this.hasShot = false;

				}
			}
			this.amoNumber.update(deltaTime, getOwner().getViewScale() / 20f);
			if (hasShot()) {
				loading = ExtendedMath.createRectangle(getElapsedTime() * this.loadWidth / getBetweenShotTime(), .2f);
			}
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (isDeployed()) {
			amoNumber.draw(canvas);

			Transform t = Transform.I.translated(canvas.getPosition())
					.translated(loadingPos.add(-this.loadWidth / 2, 0));
			canvas.drawShape(loadingBackground, t, Color.RED, Color.RED.darker(), loadSizeDif, .8f, 7331);
			canvas.drawShape(loading, t, Color.GREEN, null, 0, .8f, 7332);

		}
	}

	/** Fire this weapon.
     * @return whether this weapon succesfully fired.
     */
	public boolean fireWeapon() {
		if (this.ammoCount > 0) {
			this.ammoCount -= 1;
			this.hasShot = true;
			this.amoNumber.setText(this.ammoCount + this.amoText);
			this.shoot();
			return true;
		} else
			return false;
	}

	@Override
	public void destroy() {
		this.game.destroyActor(this);
	}

	@Override
	public Transform getTransform() {
		return player.getTransform();
	}

	@Override
	public Vector getVelocity() {
		return player.getVelocity();
	}

	/** @return the {@linkplain ActorGame} where this weapon belong */
	protected ActorGame getOwner() {
		return game;
	}

	/**
	 * @return the {@linkplain PlayableEntity} who is owning this
	 * {@linkplain PortableWeapon}
	 */
	protected PlayableEntity getPlayer() {
		return player;
	}

	/** Whether this weapon has shot */
	protected boolean hasShot() {
		return this.hasShot;
	}

	/** What happen when this weapon shoot */
	public abstract void shoot();

	/**
	 * Add ammo
	 * @param quantity ammo number
	 */
	public void addAmmo(int quantity) {
		this.ammoCount += quantity;
		this.amoNumber.setText(this.amoText + this.ammoCount);
	}

	/** @return whether this weapon is deployed */
	public boolean isDeployed() {
		return deployed;
	}

	public float getElapsedTime() {
		return elapsedTime;
	}

	/** Get the time to wait between the shots */
	protected float getBetweenShotTime() {
		return betweenShotTime;
	}

	/** Set whether this {@linkplain Weapon} is deployed */
	public void deploy(boolean isDeployed) {
		deployed = isDeployed;
	}

}

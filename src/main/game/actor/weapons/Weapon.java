/**
 *	Author: Clément Jeannet
 *	Date: 	10 déc. 2017
 */
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

public abstract class Weapon implements Actor {

	// general params
	private PlayableEntity player;
	private ActorGame game;

	// weapon params
	private boolean deployed = false;
	private int ammoCount;
	private float elapsedTime = 0, betweenShotTime;
	private boolean hasShot = false;

	// GUI stuff
	private Comment amoNumber;
	private String amoText = " shots left";
	private Vector amoNumberPos = new Vector(17, 8);

	float loadWidth = 5, loadHeight = .2f, loadSizeDif = .05f;
	private Polygon loadingBackground = ExtendedMath.createRectangle(loadWidth, loadHeight);
	private Polygon loading = ExtendedMath.createRectangle(loadWidth, loadHeight);
	private Vector loadingPos = new Vector(0, 9);

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

	/** Fire this weapon */
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
	}

	/** @return whether this weapon is deployed */
	public boolean isDeployed() {
		return deployed;
	}

	public float getElapsedTime() {
		return elapsedTime;
	}

	protected float getBetweenShotTime() {
		return betweenShotTime;
	}

	public void deploy(boolean isDeployed) {
		deployed = isDeployed;
	}

}

package main.game.actor.weapons;

import main.game.ActorGame;
import main.game.actor.entities.PlayableEntity;
import main.math.ExtendedMath;
import main.math.Vector;

/** Represent a weapon usable by a {@link PlayableEntity} */
public abstract class PortableWeapon extends Weapon {
    /** The given direction {@linkplain Vector}. */
	private Vector direction;

	/** The position {@linkplain Vector} of the {@linkplain Weapon}. */
	private Vector position;

	/** Whether the {@linkplain PlayableEntity} is looking right. */
	private boolean lookRight;

	/** The firing angle. */
	private float angle = 0;

	/**
	 * Create a new {@linkplain Weapon}.
	 * @param game The master {@linkplain ActorGame}.
	 * @param player The {@linkplain PlayableEntity} that can use this {@linkplain Weapon}.
	 * @param initialAmmoNumber The initial amount of ammunition.
	 * @param betweenShotTime The delay till one can shoot again.
	 */
	public PortableWeapon(ActorGame game, PlayableEntity player, int initialAmmoNumber, float betweenShotTime) {
		super(game, player, initialAmmoNumber, betweenShotTime);
		this.lookRight = player.isLookingRight();
		this.position = player.getPosition().add(this.lookRight ? 1 : -1, 2);

		this.direction = ExtendedMath.direction(this.position, game.getMouse().getPosition());
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (this.isDeployed()) {
			Vector playerPos = getPlayer().getPosition();
			this.position = playerPos.add(this.lookRight ? 1 : -1, 2);

			this.direction = ExtendedMath.direction(playerPos, getOwner().getMouse().getPosition());
			this.lookRight = this.direction.x <= 0;

			this.angle = this.direction.rotated(Math.PI).getAngle();
			// System.out.println(angle);
			if (this.angle > Math.PI / 3 && this.angle <= Math.PI / 2)
                this.angle = (float) (Math.PI / 3);
			else if (this.angle > Math.PI / 2 && this.angle < Math.PI * 2 / 3f)
                this.angle = (float) Math.PI * 2 / 3f;
			else if (this.angle < -Math.PI / 4 && this.angle >= -Math.PI / 2)
                this.angle = (float) (-Math.PI / 4);
			else if (this.angle < -Math.PI / 2 && this.angle > -Math.PI * 3 / 4f)
                this.angle = (float) -Math.PI * 3 / 4f;

			this.direction = new Vector((float) (Math.sin(-this.angle + Math.PI / 2)),
					(float) (Math.cos(-this.angle + Math.PI / 2))).mul(-1);			
		}
	}

    /** @return the direction {@linkplain Vector} of this {@linkplain Weapon}. */
	protected Vector getDirection() {
		return this.direction;
	}

	/** @return the position {@linkplain Vector} of this {@linkplain Weapon}. */
	public Vector getPosition() {
		return this.position;
	}

	/** @return the angle at which the {@linkplain Weapon} is pointed. */
	protected float getAngle() {
		return this.angle;
	}

	/** @return whether the {@linkplain PlayableEntity} is shooting to the right. */
	protected boolean isShootingToTheRight() {
		return this.lookRight;
	}


}

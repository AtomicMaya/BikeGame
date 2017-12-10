package main.game.actor.weapons;

import main.game.ActorGame;
import main.game.actor.entities.PlayableEntity;
import main.math.ExtendedMath;
import main.math.Vector;

/** Represent a weapon usable by a {@link PlayableEntity} */
public abstract class PortableWeapon extends Weapon {

	

	// direction of shout
	private Vector direction;
	// shouting params
	private Vector position;
	private boolean lookRight = false;
	private float angle = 0;



	/**
	 * Create a new weapon
	 * @param game ActorGame where this weapon belong
	 * @param player player who is going to use this weapon
	 * @param initialAmmoNumber initial number of ammos
	 * @param betweenShotTime time to wait between two shot
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

			angle = direction.rotated(Math.PI).getAngle();
			// System.out.println(angle);
			if (angle > Math.PI / 4 && angle <= Math.PI / 2)
				angle = (float) (Math.PI / 4);
			else if (angle > Math.PI / 2 && angle < Math.PI * 3 / 4f)
				angle = (float) Math.PI * 3 / 4f;
			else if (angle < -Math.PI / 4 && angle >= -Math.PI / 2)
				angle = (float) (-Math.PI / 4);
			else if (angle < -Math.PI / 2 && angle > -Math.PI * 3 / 4f)
				angle = (float) -Math.PI * 3 / 4f;

			this.direction = new Vector((float) (Math.sin(-this.angle + Math.PI / 2)),
					(float) (Math.cos(-this.angle + Math.PI / 2))).mul(-1);			
		}
	}


	protected Vector getDirection() {
		return direction;
	}

	public Vector getPosition() {
		return position;
	}

	protected float getAngle() {
		return angle;
	}

	protected boolean isShoutingOnTheRight() {
		return lookRight;
	}


}

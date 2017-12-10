package main.game.actor.entities.weapons;

import main.game.actor.Actor;
import main.game.actor.entities.PlayableEntity;

/** Represent a weapon usable by a {@link PlayableEntity} */
public interface Weapon extends Actor {

	/** Fire this weapon */
	void fireWeapon();

	/**
	 * Add ammo
	 * @param quantity amms number
	 */
	void addAmmo(int quantity);

}

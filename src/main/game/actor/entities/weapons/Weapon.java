package main.game.actor.entities.weapons;

import main.game.actor.Actor;
import main.game.actor.entities.PlayableEntity;

/** Represents a weapon usable by a {@link PlayableEntity}. */
public interface Weapon extends Actor {

	/** Fire the weapon */
	void fireWeapon();

	/**
	 * Add ammunition to the weapon.
	 * @param quantity of ammunition.
	 */
	void addAmmo(int quantity);

}

/**
 *	Author: Clément Jeannet
 *	Date: 	2 déc. 2017
 */
package main.io;

import main.game.actor.Actor;
import main.game.actor.crate.Crate;
import main.game.actor.entities.Ground;
import main.game.actor.entities.Bike;

public enum Classes {

	crate(Crate.class),
	bike(Bike.class),
	ground(Ground.class);
	
	private Class<? extends Actor> c;

	Classes(Class<? extends Actor> o) {
		c = o;
	}

	public boolean isClass(Object o) {
		return c.isInstance(o);
	}

	public Class<? extends Actor> getType() {
		return c;
	}
}

package main.game.actor.myEntities;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created on 11/28/2017 at 8:17 AM.
 */
public enum Positions {
	BIKING (),
	WALKING (),
	FALLING ();

	private ArrayList<Float> positions;

	Positions(Float... vectors) {
		this.positions = new ArrayList<>(Arrays.asList(vectors));
	}
}

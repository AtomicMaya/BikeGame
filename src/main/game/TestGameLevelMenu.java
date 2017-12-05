/**
 *	Author: Clément Jeannet
 *	Date: 	3 déc. 2017
 */
package main.game;

import java.util.Arrays;
import java.util.List;

import main.game.actor.entities.Bike;
import main.game.actor.entities.Ground;
import main.game.levels.Level;
import main.game.levels.Level1;
import main.io.FileSystem;
import main.math.Polyline;
import main.math.Vector;
import main.window.Window;

public class TestGameLevelMenu extends GameWithLevelAndMenu {

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);
		// Polyline p = new Polyline(-1000.0f, -1000.0f, -1000.0f, 0.0f, 0.0f, 0.0f,
		// 3.0f, 1.0f, 8.0f, 1.0f, 15.0f, 3.0f,
		// 20.0f, 3.0f, 20.0f, -5.0f, 35.0f, -5.0f, 50.0f, -5.0f, 55.0f, -4.0f, 65.0f,
		// 0.0f, 6500.0f, -1000.0f);
		//
		// Ground ground = new Ground(this, null, p);
		//
		// Bike player = new Bike(this, new Vector(4, 5));
		//
		// addActor(player);
		// addActor(ground);
		// setPayload(player);
		// setViewCandidate(player);
		this.setViewScaleModifier(20);
		return true;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}

	@Override
	protected List<Level> createLevelList() {
		return Arrays.asList(new Level1(this));
	}

}

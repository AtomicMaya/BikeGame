/**
 *	Author: Clément Jeannet
 *	Date: 	27 nov. 2017
 */
package main.game.actor.myEntities;

import java.awt.event.KeyEvent;

import main.game.actor.ActorGame;
import main.io.FileSystem;
import main.math.Polyline;
import main.math.Vector;
import main.window.Window;

public class Cinematic extends ActorGame {
	
	ActorGame main;
	
	public boolean begin(Window window, FileSystem fileSystem, ActorGame main) {
		super.begin(window, fileSystem);

		this.main = main;
		// TODO creation objects du program
		
		Polyline p = new Polyline(
				-1000.0f, -1000.0f,
				-1000.0f, 0.0f,
				0.0f, 0.0f,
				3.0f, 1.0f,
				8.0f, 1.0f,
				15.0f, 3.0f,
				16.0f, 3.0f,
				25.0f, 0.0f,
				35.0f, -5.0f,
				50.0f, -5.0f,
				55.0f, -4.0f,
				65.0f, 0.0f,
				6500.0f, -1000.0f
				);
		Ground ground = new Ground(this, new Vector(0, -3), p);
		this.addActor(ground);
		return true;
	}
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		if (this.getKeyboard().get(KeyEvent.VK_Q).isPressed()) {
//			test.setEnded(true);
			main.setGameFreezeStatus(false);
		}
	}
}

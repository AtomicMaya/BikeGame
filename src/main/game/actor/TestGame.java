/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor;

import main.game.actor.myEntities.Bike;
import main.game.actor.myEntities.EllipseCinematicEntity;
import main.game.actor.myEntities.Ground;
import main.game.actor.myEntities.RectangleEntity;
import main.io.FileSystem;
import main.math.Polyline;
import main.math.Vector;
import main.window.Window;

import java.awt.*;

public class TestGame extends ActorGame {

	Bike player;
	Ground ground;
	TextGraphics textGraphics;

	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);

		// TODO creation objects du program
		Polyline p = new Polyline(-50f, 0.f, 0.f, -2.f, 50.f, 0.f);

		ground = new Ground(this, null, p);

		player = new Bike(this, new Vector(-0, 5));

		EllipseCinematicEntity ece = new EllipseCinematicEntity(super.newEntity(new Vector(-25,6),false), 1f, .5f, Color.BLUE, Color.BLUE,.1f,1,0);
		RectangleEntity rectangleEntity = new RectangleEntity(super.newEntity(new Vector(-28,4),false),"res/wood.4.png",1,.5f);
		this.addActor(ece);
		this.addActor(rectangleEntity);

		//textGraphics = new TextGraphics("Test", 30, Color.RED, Color.RED, 1f, false, false, new Vector(0, 0));
		//RectangleEntity someRectangle = new RectangleEntity(super.newEntity(new Vector(-28,4),true), textGraphics,5,4);
		//addActor(someRectangle);

		this.setViewCandidate(ece);
		return true;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		player.update(deltaTime);
	}
}

package main;

import main.game.actor.ActorGame;
import main.game.actor.ShapeGraphics;
import main.game.actor.myEntities.Joint;
import main.io.FileSystem;
import main.math.*;
import main.window.Window;

import java.awt.*;

/**
 * Created on 11/27/2017 at 10:25 PM.
 */
public class ConstraintGameTest extends ActorGame {
	private ShapeGraphics anchorFill;
	private Window window;
	private Entity anchor;
	Joint joint1, joint2;
	WheelConstraint c1, c2, constraint;

	public boolean begin(Window window, FileSystem fileSystem) {
		super.begin(window, fileSystem);
		this.window = window;
		this.setGravity(Vector.ZERO);

		anchor = this.newEntity(new Vector(0, 0), true);
		PartBuilder partBuilder = anchor.createPartBuilder();
		partBuilder.setShape(new Circle(4f));
		partBuilder.build();
		anchorFill = new ShapeGraphics(new Circle(4f), null, Color.RED, .1f, 1.f, 0);
		anchorFill.setParent(anchor);

		WheelConstraintBuilder builder = this.createWheelConstraintBuilder();
		joint1 = new Joint(this, new Vector(3f, 3f));
		c1 = joint1.link(anchor, builder, 3f, true, 1.f);
		//joint2 = new Joint(this, new Vector(-.5f, 0));
		//c2 = joint2.link(anchor, builder, -0.5f, true, 2.f);

		return true;

	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		anchorFill.draw(window);
		joint1.draw(window);
//		joint2.draw(window);
		System.out.println(joint1.getPosition());
		anchor.setPosition(Vector.ZERO);
	}

	@Override
	public void end() {

	}

}
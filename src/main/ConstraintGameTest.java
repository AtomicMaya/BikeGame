package main;

import main.game.actor.ActorGame;
import main.game.actor.ShapeGraphics;
import main.game.actor.myEntities.Character;
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
	Character character1, character2;
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

		return true;

	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		anchorFill.draw(window);
		character1.draw(window);
//		character2.draw(window);
		System.out.println(character1.getPosition());
	}

	@Override
	public void end() {

	}

}
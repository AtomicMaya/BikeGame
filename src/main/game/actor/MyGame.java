/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor;

import java.util.ArrayList;

import main.game.Game;
import main.game.actor.myEntities.ComplexObject;
import main.io.FileSystem;
import main.math.Entity;
import main.math.EntityBuilder;
import main.math.Positionable;
import main.math.Transform;
import main.math.Vector;
import main.math.WheelConstraintBuilder;
import main.math.World;
import main.window.Canvas;
import main.window.Keyboard;
import main.window.Window;

public class MyGame implements Game {

	// Viewport properties
	private Vector viewCenter;
	private Vector viewTarget;
	private Positionable viewCandidate;
	private ComplexObject o;
	private static final float VIEW_TARGET_VELOCITY_COMPENSATION = 0.2f;

	private static final float VIEW_INTERPOLATION_RATIO_PER_SECOND = 0.1f;
	private static final float VIEW_SCALE = 10.0f;

	private ArrayList<ComplexObject> actors = new ArrayList<ComplexObject>();

	private World world;

	private Window window;

	private FileSystem fileSystem;

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {
		world = new World();
		world.setGravity(new Vector(0,-9.81f));
		this.window = window;
		this.fileSystem = fileSystem;
		this.viewCenter = Vector.ZERO;
		this.viewTarget = Vector.ZERO;

		return true;
	}

	@Override
	public void update(float deltaTime) {
		world.update(deltaTime);

		for (ComplexObject actor : actors) {
			actor.update(deltaTime);
		}

		// Update expected viewport center
		if (viewCandidate != null) {
			viewTarget = viewCandidate.getPosition()
					.add(viewCandidate.getVelocity().mul(VIEW_TARGET_VELOCITY_COMPENSATION));
		}
		if (o != null) {
			viewTarget = o.getPosition().add(o.getVelocity().mul(VIEW_TARGET_VELOCITY_COMPENSATION));
		}
		// Interpolate with previous location
		float ratio = (float) Math.pow(VIEW_INTERPOLATION_RATIO_PER_SECOND, deltaTime);
		viewCenter = viewCenter.mixed(viewTarget, ratio);
		// Compute new viewport
		Transform viewTransform = Transform.I.scaled(VIEW_SCALE).translated(viewCenter);
		window.setRelativeTransform(viewTransform);

		for (ComplexObject actor : actors) {
			actor.draw(window);
		}

	}

	@Override
	public void end() {

	}

	public Keyboard getKeyboard() {
		return window.getKeyboard();
	}

	public Canvas getCanvas() {
		return window;
	}

//	public void setViewCandidate(Positionable p) {
//		viewCandidate = p;
//	}
	public void setViewCandidate(ComplexObject o) {
		this.o = o;
	}

	public void addActor(ComplexObject actor) {
		this.actors.add(actor);
	}

	public void detroyActor(ComplexObject actor) {
		this.actors.remove(actor);
	}

	public Entity newEntity(Vector position, boolean fixed) {
		EntityBuilder entityBuilder = world.createEntityBuilder();
		entityBuilder.setFixed(fixed);
		entityBuilder.setPosition(position);
		return entityBuilder.build();
	}

	public Entity newEntity(boolean fixed) {
		EntityBuilder entityBuilder = world.createEntityBuilder();
		entityBuilder.setFixed(fixed);
		return entityBuilder.build();
	}

	public WheelConstraintBuilder createWheelConstraintBuilder() {
		return world.createWheelConstraintBuilder();
	}

}

package main.game.actor.myEntities;

import main.game.actor.ActorGame;
import main.game.actor.ShapeGraphics;
import main.math.*;
import main.window.Canvas;

import java.awt.*;

import static main.game.actor.myEntities.EntityBuilder.addGraphics;
import static main.game.actor.myEntities.EntityBuilder.build;


public class Joint extends GameEntity {
	private Vector anchor;
	private float radius = .05f;
	private ShapeGraphics graphics;
	private boolean relative;

	public Joint(ActorGame game, Vector position) {
		super(game, false, position);
		Circle shape = new Circle(radius);
		build(this.getEntity(), shape, -1, -1, true);
		graphics = addGraphics(this.getEntity(), shape, null, Color.BLACK, .1f, 1.f, 0f);
	}

	public WheelConstraint link(Entity entity, WheelConstraintBuilder constraintBuilder, float offset, boolean motorized, float motorSpeed) {
		constraintBuilder.setFirstEntity(entity);
		constraintBuilder.setFirstAnchor(Vector.ZERO);
		constraintBuilder.setSecondEntity(this.getEntity());
		constraintBuilder.setSecondAnchor(new Vector(1f, 1f));
		//constraintBuilder.setAxis(new Vector(1f, 1f));
		constraintBuilder.setInternalCollision(false);
		constraintBuilder.setMotorEnabled(motorized);
		constraintBuilder.setMotorSpeed(motorSpeed);
		constraintBuilder.setMotorMaxTorque(2f);
		constraintBuilder.setDamping(5f);
		constraintBuilder.setFrequency(10f);
		return constraintBuilder.build();
	}

	public Vector getAnchor() {
		return this.anchor;
	}

	@Override
	public void update(float deltaTime) {

	}

	@Override
	public void draw(Canvas canvas) {
		graphics.draw(canvas);
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}

}

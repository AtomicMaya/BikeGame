/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.myEntities;

import main.game.actor.*;
import main.math.*;
import main.window.Canvas;

import static main.game.actor.myEntities.EntityBuilder.*;

public class Wheel extends GameEntity {

	private WheelConstraint constraint = null;

	private ImageGraphics graphics;

	public Wheel(ActorGame game, Vector position, float radius) {
		super(game, false, position);
		Circle circle = new Circle(radius);
		build(getEntity(), circle);
		graphics = addGraphics(this.getEntity(),"res/explosive.11.png",radius * 2,radius*2);
		graphics.setAnchor(new Vector(.5f,.5f));

	}

	public void attach(Entity vehicle, Vector anchor, Vector axis) {
		WheelConstraintBuilder constraintBuilder = super.getOwner().createWheelConstraintBuilder();

		constraintBuilder.setFirstEntity(vehicle);
		// point d'ancrage du véhicule :
		constraintBuilder.setFirstAnchor(anchor);
		// Entity associée à la roue :
		constraintBuilder.setSecondEntity(getEntity());
		// point d'ancrage de la roue (son centre) :
		constraintBuilder.setSecondAnchor(Vector.ZERO);

		// axe le long duquel la roue peut se déplacer :
		constraintBuilder.setAxis(axis);
		// fréquence du ressort associé
		constraintBuilder.setFrequency(3.0f);
		constraintBuilder.setDamping(0.5f);
		// force angulaire maximale pouvant être appliquée
		// à la roue pour la faire tourner :
		constraintBuilder.setMotorMaxTorque(10.0f);
		constraint = constraintBuilder.build();
	}

	public void power(float speed) {
		constraint.setMotorEnabled(true);
		constraint.setMotorSpeed(speed);
	}

	public void relax() {
		constraint.setMotorEnabled(false);
		constraint.setMotorSpeed(0);
	}

	public void detach() {
		constraint.destroy();
	}

	/**
	 * @return relative rotation speed , in radians per second
	 */
	public float getSpeed() {
		return constraint.getMotorSpeed();
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Canvas canvas) {
		graphics.draw(canvas);

	}

}

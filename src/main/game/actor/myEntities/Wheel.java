/**
 *	Author: Clément Jeannet
 *	Date: 	22 nov. 2017
 */
package main.game.actor.myEntities;

import main.game.actor.Actor;
import main.game.actor.ActorGame;
import main.math.Vector;
import main.math.WheelConstraint;
import main.math.WheelConstraintBuilder;
import main.window.Canvas;

public class Wheel extends GameEntity {

	private SimpleEntity wheel;

	private WheelConstraint constraint = null;
	private ActorGame game;

	public Wheel(ActorGame game, Vector position) {
		super(game, false, position);

		this.game = game;
		wheel = new SphereEntity(getEntity(), .5f, "res/explosive.11.png");
		wheel.setFriction(.6f);
	}

	public void attach(SimpleEntity vehicle, Vector anchor, Vector axis) {
		WheelConstraintBuilder constraintBuilder = game.createWheelConstraintBuilder();

		constraintBuilder.setFirstEntity(vehicle.getEntity());
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
		wheel.draw(canvas);
		
	}

}

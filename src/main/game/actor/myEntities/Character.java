package main.game.actor.myEntities;

import main.game.actor.ActorGame;
import main.game.actor.ShapeGraphics;
import main.math.Circle;
import main.math.Polyline;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.util.ArrayList;

import static main.game.actor.myEntities.EntityBuilder.addGraphics;


public class Character extends GameEntity {

	private Vector headPosition, armJointPosition, backPosition;
	private Vector leftElbowPosition, rightElbowPosition, leftHandPosition, rightHandPosition;
	private Vector leftKneePosition, rightKneePosition, leftFootPosition, rightFootPosition;
	private Polyline body;
	private Circle head;

	private boolean pedaling;
	private float angle, angleIncrement;
	private ShapeGraphics graphics;

	public Character(ActorGame game, Vector position, ArrayList<Vector> vectors, float angleIncrement) {
		super(game, true, position);
		this.angleIncrement = angleIncrement;
		setHeadPosition(vectors.get(0));
		setArmJointPosition(vectors.get(1));
		setBackPosition(vectors.get(2));
		setLeftElbowPosition(vectors.get(3));
		setLeftHandPosition(vectors.get(4));
		setRightElbowPosition(vectors.get(5));
		setRightHandPosition(vectors.get(6));
		setLeftKneePosition(vectors.get(7));
		setLeftFootPosition(vectors.get(8));
		setRightKneePosition(vectors.get(9));
		setRightFootPosition(vectors.get(10));
		body = generatePolygon();
		graphics = addGraphics(this.getEntity(), body, null, Color.BLACK, .1f, 1.f, 0);
	}

	private Polyline generatePolygon() {
		return new Polyline(getHeadPosition(), getArmJointPosition(), getLeftElbowPosition(), getLeftHandPosition(), getLeftElbowPosition(), getArmJointPosition(),
				getRightElbowPosition(), getRightHandPosition(), getRightElbowPosition(), getArmJointPosition(), getBackPosition(),
				getLeftKneePosition(), getLeftFootPosition(), getLeftKneePosition(), getBackPosition(), getRightKneePosition(), getRightFootPosition());
	}

	public void nextPedal() {
		angle -= angleIncrement;
		angle %= 360;
		float newLFootX = (float) (getLeftFootPosition().x + Math.cos(toRadians(angle)) * 0.015);
		float newLFootY = (float) (getLeftFootPosition().y + Math.sin(toRadians(angle))* 0.015);
		float newRFootX = (float) (getRightFootPosition().x + Math.cos(toRadians((float) (angle - Math.PI))) * 0.02);
		float newRFootY = (float) (getRightFootPosition().y + Math.sin(toRadians((float) (angle - Math.PI))) * 0.02);
		//System.out.println(newLFootX + "," + newLFootY + "," + newRFootX + "," + newRFootY);
		System.out.println(getLeftFootPosition().x +"," + getLeftFootPosition().y);
		setLeftFootPosition(new Vector(newLFootX, newLFootY));
		setRightFootPosition(new Vector(newRFootX, newRFootY));
	}

	private float toRadians(float angle) {
		return (float) (angle * Math.PI / 180.f);
	}

	@Override
	public void update(float deltaTime) {
		if(isPedaling()) nextPedal();
		body = generatePolygon();
		graphics = addGraphics(this.getEntity(), body, null, Color.BLACK, .1f, 1.f, 0);

	}

	@Override
	public void draw(Canvas canvas) {
		graphics.draw(canvas);
	}
	
	@Override
	public void destroy() {
		super.destroy();
	}


	public void invertX() {
		setHeadPosition(getHeadPosition().mul(new Vector(-1.f, 1.f)));
		setArmJointPosition(getArmJointPosition().mul(new Vector(-1.f, 1.f)));
		setBackPosition(getBackPosition().mul(new Vector(-1.f, 1.f)));
		setLeftElbowPosition(getLeftElbowPosition().mul(new Vector(-1.f, 1.f)));
		setLeftHandPosition(getLeftHandPosition().mul(new Vector(-1.f, 1.f)));
		setRightElbowPosition(getRightElbowPosition().mul(new Vector(-1.f, 1.f)));
		setRightHandPosition(getRightHandPosition().mul(new Vector(-1.f, 1.f)));
		setLeftKneePosition(getLeftKneePosition().mul(new Vector(-1.f, 1.f)));
		setLeftFootPosition(getLeftFootPosition().mul(new Vector(-1.f, 1.f)));
		setRightKneePosition(getRightKneePosition().mul(new Vector(-1.f, 1.f)));
		setRightFootPosition(getRightFootPosition().mul(new Vector(-1.f, 1.f)));
	}

	private Vector getHeadPosition() {
		return headPosition;
	}

	private void setHeadPosition(Vector headPosition) {
		this.headPosition = headPosition;
	}

	private Vector getArmJointPosition() {
		return armJointPosition;
	}

	private void setArmJointPosition(Vector armJointPosition) {
		this.armJointPosition = armJointPosition;
	}

	private Vector getBackPosition() {
		return backPosition;
	}

	private void setBackPosition(Vector backPosition) {
		this.backPosition = backPosition;
	}

	private Vector getLeftElbowPosition() {
		return leftElbowPosition;
	}

	private void setLeftElbowPosition(Vector leftElbowPosition) {
		this.leftElbowPosition = leftElbowPosition;
	}

	private Vector getRightElbowPosition() {
		return rightElbowPosition;
	}

	private void setRightElbowPosition(Vector rightElbowPosition) {
		this.rightElbowPosition = rightElbowPosition;
	}

	private Vector getLeftHandPosition() {
		return leftHandPosition;
	}

	private void setLeftHandPosition(Vector leftHandPosition) {
		this.leftHandPosition = leftHandPosition;
	}

	private Vector getRightHandPosition() {
		return rightHandPosition;
	}

	private void setRightHandPosition(Vector rightHandPosition) {
		this.rightHandPosition = rightHandPosition;
	}

	private Vector getLeftKneePosition() {
		return leftKneePosition;
	}

	private void setLeftKneePosition(Vector leftKneePosition) {
		this.leftKneePosition = leftKneePosition;
	}

	private Vector getRightKneePosition() {
		return rightKneePosition;
	}

	private void setRightKneePosition(Vector rightKneePosition) {
		this.rightKneePosition = rightKneePosition;
	}

	private Vector getLeftFootPosition() {
		return leftFootPosition;
	}

	private void setLeftFootPosition(Vector leftFootPosition) {
		this.leftFootPosition = leftFootPosition;
	}

	private Vector getRightFootPosition() {
		return rightFootPosition;
	}

	private void setRightFootPosition(Vector rightFootPosition) {
		this.rightFootPosition = rightFootPosition;
	}

	public boolean isPedaling() {
		return pedaling;
	}

	public void setPedaling(boolean pedaling) {
		this.pedaling = pedaling;
	}
}

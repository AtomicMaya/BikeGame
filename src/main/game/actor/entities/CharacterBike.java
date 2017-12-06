package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.QuickMafs;
import main.game.actor.ShapeGraphics;
import main.math.*;
import main.math.Polygon;
import main.window.Canvas;

import java.awt.*;
import java.util.ArrayList;

import static main.game.actor.QuickMafs.*;

public class CharacterBike extends GameEntity {
	private Vector headPos, armJointPos, backPos;
	private Vector lElbowPos, rElbowPos, lHandPos, rHandPos;
	private Vector lKneePos, rKneePos, lFootPos, rFootPos;

	private float directionModifier;
	private ArrayList<ShapeGraphics> graphics;
	private PrismaticConstraint constraint;

	private float angle;
	private boolean isYaying;
	private final float timeTillYayEnd = 1.5f;
	private float elapsedYayTime;
    private final float pedalingAngleIncrement = 3.f;

	// Insurance Vectors, so as to reset the various moving limbs to default positions
	private Vector initLElbowPos = new Vector(.5f, 1.f), initLHandPos = new Vector(1.f, .8f);
	private Vector initRElbowPos = new Vector(.5f, 1.f), initRHandPos = new Vector(1.f, .8f);
	private Vector lElbowRaisedPos = new Vector(.1f, 2.2f), lHandRaisedPos = new Vector(.1f, 2.75f);
	private Vector rElbowRaisedPos = new Vector(.2f, 2.2f), rHandRaisedPos = new Vector(.2f, 2.75f);

	/**
	 * Initialize a Character
	 * @param game : the game in which this character exists
	 * @param position : the position the character occupies
	 */
	public CharacterBike(ActorGame game, Vector position) {
		super(game, false, position);
		this.directionModifier = 1.f;

        this.headPos = new Vector(.2f, 1.75f); this.armJointPos = new Vector(.0f, 1.5f); this.backPos = new Vector(-.3f, .5f);
        this.lElbowPos = this.initLElbowPos; this.lHandPos = this.initLHandPos;
        this.rElbowPos = this.initRElbowPos; this.rHandPos = this.initRHandPos;
        this.lKneePos = new Vector(.2f, .3f); this.lFootPos = new Vector(.1f, -.3f);
        this.rKneePos = new Vector(.0f, .2f); this.rFootPos = new Vector(-.1f, -.3f);

        updateGraphics();
		Circle anchor = new Circle(0.1f);
		this.build(anchor);
	}

	private void updateGraphics() {
        Circle head = new Circle(.35f, this.headPos.add(.1f * this.directionModifier, .1f));
        Polygon body = new Polygon(this.headPos, this.armJointPos, backPos);
        this.graphics = new ArrayList<>();
        this.graphics.add(this.addGraphics(head, Color.decode("#f5c396"), Color.BLACK, .02f, 1.f, 10.1f));
        this.graphics.add(this.addGraphics(body, null, Color.BLACK, .15f, 1.f, 10));
        this.graphics.add(this.addGraphics(this.generateLimb(0), null, Color.BLACK, .15f, 1.f, 10));
        this.graphics.add(this.addGraphics(this.generateLimb(1), null, Color.BLACK, .15f, 1.f, 10));
        this.graphics.add(this.addGraphics(this.generateLimb(2), null, Color.BLUE.brighter().brighter(), .15f, 1.f, 10));
        this.graphics.add(this.addGraphics(this.generateLimb(3), null, Color.BLUE.brighter().brighter(), .15f, 1.f, 10));


    }

	@Override
	public void update(float deltaTime) {
		if (isYaying) nextYay(deltaTime);

		this.updateGraphics();
	}

	@Override
	public void draw(Canvas canvas) {
	    for(ShapeGraphics g : this.graphics) {
	        g.draw(canvas);
        }
	}
	
	@Override
	public void destroy() {
		super.destroy();
		super.getOwner().destroyActor(this);
	}

	/**
	 * Generates the shape of the body given all positions.
	 * @return a Polyline
	 */
	private Polyline generateLimb(int limb) {
        switch (limb) {
            case 0:    // Left arm
                return new Polyline(this.armJointPos, this.lElbowPos, this.lHandPos);
            case 1:
                return new Polyline(this.armJointPos, this.rElbowPos, this.rHandPos);
            case 2:
                return new Polyline(this.backPos, this.lKneePos, this.lFootPos, this.lKneePos, this.backPos);
            case 3:
                return new Polyline(this.backPos, this.rKneePos, this.rFootPos);
            default:
                return null;
        }
	}

	/**
	 * Attaches this to the entity, at the anchor point
	 * @param vehicle : the entity on which this will be attached
	 * @param anchor : the point were this should be centered
	 */
	protected void attach(Entity vehicle, Vector anchor) {
		PrismaticConstraintBuilder builder = super.getOwner().createPrismaticConstraintBuilder();
		builder.setFirstEntity(vehicle);
		builder.setSecondEntity(this.getEntity());
		builder.setFirstAnchor(anchor);
		builder.setSecondAnchor(Vector.ZERO);
		builder.setLimitEnabled(true);
		builder.setMotorEnabled(true);
		builder.setMotorSpeed(0);
		builder.setMotorMaxTorque(10f);
		builder.setLowerTranslationLimit(0);
		builder.setUpperTranslationLimit(0);
		builder.setAxis(Vector.Y);
		builder.setInternalCollision(false);
        this.constraint = builder.build();
	}

	/**
	 * Removes the constraint
	 */
	public void detach() {
		if (constraint != null) constraint.destroy();
	}

	/**
	 * Calculate the next position of the feet and the knees.
	 */
	public void nextPedal(float angle) {
        this.lFootPos = new Vector( (float) (Math.cos(toRadians(angle)) * 0.4 + 0.15f) * this.directionModifier,
                (float) (Math.sin(toRadians(angle)) * 0.4 - 0.25f));
        this.rFootPos = new Vector((float) (Math.cos(toRadians(angle) - Math.PI) * 0.4 + 0.15f) * this.directionModifier,
                (float) (Math.sin(toRadians(angle) - Math.PI) * 0.4 - 0.25f));
        this.lKneePos = new Vector(this.lFootPos.x , (this.lFootPos.y + 0.5f) );
        this.rKneePos = new Vector(this.rFootPos.x , (this.rFootPos.y + 0.5f));
    }

	/**
	 * Calculates the coordinates of the next point relative to time
	 * @param anchor : the point to which this point is 'attached', can moved
	 * @param initial : the absolute initial coordinates that this point occupied
	 * @param goal : the point that this point should attain
	 * @return the new coordinates of this point
	 */
	private Vector getNewPosition(Vector anchor, Vector initial, Vector goal) {
		float radius = QuickMafs.getDistance(anchor, initial);
		float angle = QuickMafs.getAngle(anchor, initial, goal);
		angle += angle * this.directionModifier * this.elapsedYayTime / (this.timeTillYayEnd / 2.f);
		return new Vector((float) (anchor.x + radius * Math.cos(angle)), (float) (anchor.y - radius * Math.sin(angle)));
	}

	/**
	 * Calculates all the next positions and assigns them to the elbows and hands
	 * @param deltaTime : the current deltaTime, so that we can use time as a parameter of the animation
	 */
	private void nextYay(float deltaTime) {
		this.elapsedYayTime += deltaTime;
		if (this.elapsedYayTime < this.timeTillYayEnd / 2.f) {
			this.lElbowPos = getNewPosition(this.armJointPos, this.initLElbowPos, this.lElbowRaisedPos);
			this.lHandPos = getNewPosition(this.armJointPos, this.initLHandPos, this.lHandRaisedPos);
			this.rElbowPos = getNewPosition(this.armJointPos, this.initRElbowPos, this.rElbowRaisedPos);
			this.rHandPos = getNewPosition(this.armJointPos, this.initRHandPos, this.rHandRaisedPos);
		} else if (this.elapsedYayTime < this.timeTillYayEnd) {
            this.lElbowPos = getNewPosition(this.armJointPos, this.lElbowRaisedPos, this.initLElbowPos);
            this.lHandPos = getNewPosition(this.armJointPos, this.lHandRaisedPos, this.initLHandPos);
            this.rElbowPos = getNewPosition(this.armJointPos, this.rElbowRaisedPos, this.initRElbowPos);
            this.rHandPos = getNewPosition(this.armJointPos, this.rHandRaisedPos, this.initRHandPos);
		}

		if (this.elapsedYayTime > this.timeTillYayEnd) {
			this.isYaying = false;
			this.elapsedYayTime = 0.f;

			// Insurance for bad math...
			this.lElbowPos = this.initLElbowPos; this.lHandPos = this.initLHandPos;
			this.rElbowPos = this.initRElbowPos; this.rHandPos = this.initRHandPos;
		}
	}

	/**
	 * Trigger the Victory animation
	 */
	public void triggerYayAnimation() {
		this.isYaying = true;
	}

	/**
	 * @return if the Victory animation is active
	 */
	public boolean getIsYaying() {
		return this.isYaying;
	}

	/**
	 * Inverts all the xCoordinates on the x axis
	 */
	public void invertX() {
		directionModifier = directionModifier * -1;
		headPos = headPos.mul(xInverted); armJointPos = armJointPos.mul(xInverted);
		backPos = backPos.mul(xInverted);
		lElbowPos = lElbowPos.mul(xInverted); lHandPos = lHandPos.mul(xInverted);
		rElbowPos = rElbowPos.mul(xInverted); rHandPos = rHandPos.mul(xInverted);

		// Insurance against bad math...
		lKneePos = new Vector(.2f, .3f).mul(directionModifier == 1 ? xyNormal : xInverted);
		lFootPos = new Vector(.1f, -.3f).mul(directionModifier == 1 ? xyNormal : xInverted);
		rKneePos = new Vector(.0f, .2f).mul(directionModifier == 1 ? xyNormal : xInverted);
		rFootPos = new Vector(-.1f, -.3f).mul(directionModifier == 1 ? xyNormal : xInverted);

		lElbowRaisedPos = lElbowRaisedPos.mul(xInverted); lHandRaisedPos = lHandRaisedPos.mul(xInverted);
		rElbowRaisedPos = rElbowRaisedPos.mul(xInverted); rHandRaisedPos = rHandRaisedPos.mul(xInverted);
		initLElbowPos = initLElbowPos.mul(xInverted); initLHandPos = initLHandPos.mul(xInverted);
		initRElbowPos = initRElbowPos.mul(xInverted); initRHandPos = initRHandPos.mul(xInverted);
	}
}
package main.game.actor.entities.weapons;

import main.game.ActorGame;
import main.game.GUI.Comment;
import main.game.actor.Actor;
import main.game.actor.entities.PlayableEntity;
import main.math.Impact;
import main.math.Polyline;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class Shotgun implements Actor, Weapon {

	// private ImageGraphics graphics;
	// private KeyboardProximitySensor sensor;
	private ActorGame game;
	private int ammoCount;
	private boolean shooting;
	private float elapsedTime, shotgunOutTime;
	private Vector position;
//	private PrismaticConstraint constraint;

	private boolean deployed = false;
	private Vector direction;
	private float laserDiatance = 16;
	private String imagePath = "./res/images/shotgun.png";
	private boolean lookRight = false;

	private PlayableEntity player;
	private float angle = 0;

	private Comment amoNumber;
	private String amoText = " amos left";

	public Shotgun(ActorGame game, Vector position, int initialAmmoCount, PlayableEntity player) {
		// super(game, false, position);
		this.position = position;
		this.game = game;
		this.player = player;
		// this.graphics = this.addGraphics("./res/images/shotgun.png", 2, .5f,
		// Vector.ZERO, 1, 10.2f);
		// this.sensor = new ProximitySensor(game, position.add(-2, 0), new
		// Polygon(0, -0.25f, 2, -1.5f, 2, 1.5f, 0, 0.25f), new
		// ArrayList<>(Arrays.asList(ObjectGroup.ENEMY.group)));
		// this.sensor = new KeyboardProximitySensor(game, position.add(-2, 0),
		// new Polygon(0, -0.25f, 2, -1.5f, 2, 1.5f, 0, 0.25f), VK_E);
		this.ammoCount = initialAmmoCount;
		this.shooting = false;
		this.elapsedTime = 0;
		this.shotgunOutTime = .5f;
		// this.build(new Circle(.1f));
		direction = direction(position, game.getMouse().getPosition());
		amoNumber = new Comment(game, ammoCount + amoText);
		amoNumber.setAnchor(new Vector(10, 6));
	}

	private Vector direction(Vector un, Vector deux) {
		return new Vector(un.x - deux.x, un.y - deux.y).normalized();
	}

	@Override
	public void update(float deltaTime) {
		if (deployed && game.getMouse().getLeftButton().isPressed()) {
			fireWeapon();
		}
		if (game.getKeyboard().get(KeyEvent.VK_F).isPressed())
			deployed = !deployed;
		

		Vector playerPos = player.getPosition();
		position = playerPos.add(lookRight ? 1 : -1, 2);

		direction = direction(playerPos, game.getMouse().getPosition());
		lookRight = direction.x <= 0;

		angle = direction.rotated(Math.PI).getAngle();
//		System.out.println(angle);
		if (angle > Math.PI / 4 && angle <= Math.PI / 2)
			angle = (float) (Math.PI / 4);
		else if (angle > Math.PI / 2 && angle < Math.PI * 3 / 4f)
			angle = (float) Math.PI * 3 / 4f;
		else if (angle < -Math.PI / 4 && angle >= -Math.PI / 2)
			angle = (float) (-Math.PI / 4);
		else if (angle < -Math.PI / 2 && angle > -Math.PI * 3 / 4f)
			angle = (float) -Math.PI * 3 / 4f;

		direction = new Vector((float) (Math.sin(-angle + Math.PI / 2)), (float) (Math.cos(-angle + Math.PI / 2)))
				.mul(-1);

		if (this.shooting)
			this.elapsedTime += deltaTime;
		if (this.elapsedTime > this.shotgunOutTime) {
			this.elapsedTime = 0;
			this.shooting = false;
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (deployed) {
			canvas.drawImage(canvas.getImage(imagePath),
					Transform.I.scaled(2, .5f * ((lookRight) ? 1 : -1)).translated(position).rotated(angle, position),
					1, 5);

			Vector corection = new Vector(
					(float) (Math.sin(-angle + Math.PI / 2 + Math.PI / 14 * (lookRight ? -1 : 1))),
					(float) (Math.cos(-angle + Math.PI / 2 + Math.PI / 14 * (lookRight ? -1 : 1)))).mul(-1);
			canvas.drawShape(
					new Polyline(position.add(corection.mul(-2.1f)), position.add(direction.mul(-laserDiatance))),
					Transform.I, Color.GREEN, Color.GREEN, .05f, 1, -12);
			List<Impact> impacts = game.getImpacts(position, position.add(direction.mul(-laserDiatance)));
			amoNumber.draw(canvas);
		}
	}

	@Override
	public void fireWeapon() {
		if (this.ammoCount > 0) {
			this.ammoCount -= 1;
			this.shooting = true;
			amoNumber.setText(ammoCount + amoText);
			System.out.println("shout");
		}

	}

	@Override
	public void addAmmo(int quantity) {
		this.ammoCount += quantity;
		amoNumber.setText(ammoCount + amoText);
	}

	@Override
	public Vector getPosition() {
		return this.position;
	}

	@Override
	public void destroy() {
		// this.sensor.destroy();
		// super.destroy();
		this.game.destroyActor(this);
	}
	//
	// public void setConstraint(PrismaticConstraint constraint) {
	// this.constraint = constraint;
	// }

	@Override
	public Transform getTransform() {
		return player.getTransform();
	}

	@Override
	public Vector getVelocity() {
		return player.getVelocity();
	}

	// public void detach() {
	// if (this.constraint != null)
	// this.constraint.destroy();
	// }
}

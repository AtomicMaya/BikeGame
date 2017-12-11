/**
 *	Author: Clément Jeannet
 *	Date: 	10 déc. 2017
 */
package main.game.actor.weapons;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.actor.entities.GameEntity;
import main.math.*;
import main.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class Missile extends GameEntity {

	private Vector direction;
	private String imagePath = "res/images/missile.red.4.png";
	private BasicContactListener listener;

	// explosion stuff
	private ArrayList<String> boomGraphics;
	private boolean triggered = false;

	private final float animationTime = .5f;
	private float elapsedAnimationTime = 0;

	private int graphicsCounter = 0;
	private float secretProbability = (float) Math.random();

	public Missile(ActorGame game, Vector position, Vector targetPos) {
		super(game, false, position);
		this.direction = ExtendedMath.direction(position, targetPos).mul(-1);
		listener = new BasicContactListener();
		getEntity().addContactListener(listener);
		this.build(ExtendedMath.createRectangle(1, 24 / 96f), 100, -1, false, ObjectGroup.PROJECTILE.group);

		boomGraphics = new ArrayList<>(
				Arrays.asList("./res/images/explosion.bomb.0.png", "./res/images/explosion.bomb.1.png",
						"./res/images/explosion.bomb.2.png", "./res/images/explosion.bomb.3.png",
						"./res/images/explosion.bomb.4.png", "./res/images/explosion.bomb.5.png",
						"./res/images/explosion.bomb.6.png", "./res/images/explosion.bomb.7.png",
						"./res/images/explosion.bomb.8.png", "./res/images/explosion.bomb.9.png",
						"./res/images/explosion.bomb.10.png", "./res/images/explosion.bomb.11.png",
						"./res/images/explosion.bomb.12.png", "./res/images/explosion.bomb.13.png",
						"./res/images/explosion.bomb.14.png", "./res/images/explosion.bomb.15.png",
						"./res/images/explosion.bomb.16.png", "./res/images/explosion.bomb.17.png",
						"./res/images/explosion.bomb.18.png", "./res/images/explosion.bomb.19.png",
						"./res/images/explosion.bomb.20.png", "./res/images/explosion.bomb.21.png",
						"./res/images/explosion.bomb.22.png", "./res/images/explosion.bomb.23.png",
						"./res/images/explosion.bomb.24.png", "./res/images/explosion.bomb.25.png",
						"./res/images/explosion.bomb.26.png", "./res/images/explosion.bomb.27.png",
						"./res/images/explosion.bomb.28.png", "./res/images/explosion.bomb.29.png",
						"./res/images/explosion.bomb.30.png", "./res/images/explosion.bomb.31.png"));

	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
		this.getEntity().setVelocity(direction.mul(19));

		if (!this.triggered) {
			Set<Entity> entities = listener.getEntities();
			for (Entity e : entities) {
				if (e.getCollisionGroup() == ObjectGroup.PLAYER.group
						|| e.getCollisionGroup() == ObjectGroup.WHEEL.group) {
					getOwner().getPayload().triggerDeath(false);
				} else if (e.getCollisionGroup() != ObjectGroup.SENSOR.group
						&& e.getCollisionGroup() != ObjectGroup.CHECKPOINT.group
						&& e.getCollisionGroup() != ObjectGroup.FINISH.group)
					this.triggered = true;
				System.out.println(e.getCollisionGroup());
			}
		}
		if (this.triggered)
			this.elapsedAnimationTime += deltaTime;
		this.graphicsCounter = (int) Math
				.floor(this.elapsedAnimationTime / this.animationTime * this.boomGraphics.size());
		if (this.graphicsCounter > this.boomGraphics.size() - 1) {
			this.destroy();
			this.graphicsCounter = 0;
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (!this.triggered) {
			Vector resize = (secretProbability < 42 / 404f) ? new Vector(1, 47 / 50f) : new Vector(1, 24 / 96f);
			canvas.drawImage(canvas.getImage((secretProbability < 42 / 404f) ? "res/images/roquette.png" : imagePath),
					Transform.I.scaled(resize.x, resize.y).rotated((float) (direction.getAngle() + Math.PI))
							.translated(getPosition()),
					1, 12);
		} else
			this.addGraphics(this.boomGraphics.get(this.graphicsCounter), 4, 4, new Vector(.5f, .5f), 1, 10)
					.draw(canvas);
	}

}

package main.game.actor.entities;

import main.game.ActorGame;
import main.math.Circle;
import main.math.Polygon;
import main.math.Shape;
import main.math.Vector;
import main.window.Canvas;

public class MovingPlatform extends GameEntity {
	private float loopTime, pauseTime;
	private float elapsedTime = 0.f;
	private float speed;
	private Platform platform;
	private Vector evolution;

	public MovingPlatform(ActorGame game, Vector position, Vector evolution, float distance, float advancementTime, float pauseTime) {
		super(game, false, position);
		this.loopTime = advancementTime;
		this.pauseTime = pauseTime;
		this.speed = distance / advancementTime;
		this.evolution = evolution;

		Shape platformShape = new Polygon(.0f, .0f, 5.f, .0f, 5.f, 1.f, .0f, 1.f);
		this.platform = new Platform(game, position, platformShape);

		this.build(new Circle(0.1f), -1f, -1, false);
		this.platform.attach(this.getEntity(), Vector.ZERO);

		game.addActor(this.platform);
	}

	@Override
	public void update(float deltaTime) {
		this.elapsedTime += deltaTime;
		if (0 < this.elapsedTime && this.elapsedTime < this.loopTime) {
			this.platform.setPosition(this.evolution.mul(this.speed * deltaTime, this.speed * deltaTime));
		} else if (this.loopTime + this.pauseTime < this.elapsedTime && this.elapsedTime < 2 * this.loopTime + this.pauseTime) {
            this.platform.setPosition(this.evolution.mul(-this.speed * deltaTime, -this.speed * deltaTime));
		} else if (this.elapsedTime > 2 * (this.loopTime + this.pauseTime)){
            this.elapsedTime = 0.f;
		}
        this.platform.update(deltaTime);
	}

	@Override
	public void destroy() {
		super.destroy();
        super.getOwner().destroyActor(this);
	}

	@Override
	public void draw(Canvas canvas) {
        this.platform.draw(canvas);
	}
}

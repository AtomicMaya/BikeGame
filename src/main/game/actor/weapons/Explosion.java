/**
 * Author: Clément Jeannet Date: 12 déc. 2017
 */
package main.game.actor.weapons;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.DepthValue;
import main.game.actor.entities.GameEntity;
import main.game.audio.Audio;
import main.math.Node;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;
import main.window.Image;

import java.util.ArrayList;
import java.util.Arrays;

/** Draw an explosion graphics in the game, auto removed from the {@linkplain ActorGame} when finished */
public class Explosion extends Node implements Actor {

	/**
	 * An {@linkplain ArrayList} containing the links to where the explosion
	 * animation graphics are stored on disk.
	 */
	private ArrayList<String> boomGraphics;

	/** How far the animation has progressed. */
	private int graphicsCounter = 0;

	/** The time until the explosion has finished animating. */
	private final float animationTime = .5f;

	/** The elapsed animation time. */
	private float elapsedAnimationTime = 0;

	/** Relative position of the explosion to its parent */
	private Vector anchor;

	/** The master {@linkplain ActorGame} */
	private ActorGame game;

	/** Create a new {@linkplain Explosion} graphics
	 * @param game The master {@linkplain ActorGame}
	 * @param anchor Relative position of the explosion to its parent
	 * @param parent {@linkplain GameEntity} which spawn this {@linkplain Explosion} 
	 * @param delay how much time to wait before the explosion
	 * */
	public Explosion(ActorGame game, Vector anchor, GameEntity parent, float delay) {
		this.setParent(parent);
		this.elapsedAnimationTime = -Math.abs(delay);
		this.game = game;
		this.anchor = anchor;
		this.boomGraphics = new ArrayList<>(
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
		this.elapsedAnimationTime += deltaTime;
		this.graphicsCounter = (int) Math
				.floor(this.elapsedAnimationTime / this.animationTime * this.boomGraphics.size());
		if (this.graphicsCounter > this.boomGraphics.size() - 1) {
		    new Audio("./res/audio/explosion.wav");
			this.graphicsCounter = 0;
			this.game.destroyActor(this);
		}
	}

	@Override
	public void draw(Canvas canvas) {
		if (graphicsCounter >= 0) {
			Image i = canvas.getImage(this.boomGraphics.get(this.graphicsCounter));
			Transform t = new Transform(4, 0, getPosition().x + this.anchor.x - 2, 0, 4, getPosition().y + this.anchor.y - 2);
			canvas.drawImage(i, t, 1, DepthValue.BACK_OBSTACLE_LOW.value);
		}

	}
}

package main.game.actor.entities.collectable;

import main.game.ActorGame;
import main.game.actor.DepthValue;
import main.game.actor.ObjectGroup;
import main.game.actor.entities.GameEntity;
import main.game.actor.sensors.ProximitySensor;
import main.game.graphics.ImageGraphics;
import main.math.Polygon;
import main.math.Vector;
import main.window.Canvas;

import java.util.ArrayList;
import java.util.Arrays;

/** A collectible coin, that, when collected, gets added to the score. */
public class Coin extends GameEntity implements Collectable {
   
	/** Used for save purposes. */
	private static final long serialVersionUID = -5512548763495437053L;

	/** The master {@linkplain ActorGame}. */
	private transient ActorGame game;

	/** Contains the graphics. */
	private transient ArrayList<ImageGraphics> graphics;

	/** The affected {@linkplain ProximitySensor}. */
	private transient ProximitySensor sensor;

	/** The {@linkplain Polygon}, the geometric representation of this {@linkplain Coin}. */
	private transient Polygon shape;

	/** The time that has already passed for the {@linkplain Coin}'s animation. */
	private transient float elapsedAnimationTime;

	/** The total animation time. */
	private transient final float animationTime = 1.5f;

	/** The current index for the {@linkplain ArrayList graphics} iteration. */
	private transient int graphicsCounter;

	/** If this {@linkplain Coin} is a special type of coin that gives a higher score bonus. */
	private boolean isBigCoin;

	/**
	 * Creates a {@linkplain Coin}.
	 * @param game The {@linkplain ActorGame} where this {@linkplain Coin} exists.
	 * @param position This {@linkplain Coin}'s position {@linkplain Vector}.
	 * @param isBigCoin Whether this {@linkplain Coin} is a special coin.
	 */
	public Coin(ActorGame game, Vector position, boolean isBigCoin) {
		super(game, true, position);
		this.game = game;
		this.isBigCoin = isBigCoin;
		this.create();
	}

    /**
     * Actual creation of the parameters of the {@linkplain GameEntity}, not in the constructor to
     * avoid duplication with the method {@linkplain #reCreate(ActorGame)}.
     */
	private void create() {
		if (this.isBigCoin) {
			this.graphics = new ArrayList<>(
					Arrays.asList(this.addGraphics("./res/images/coin.gold.big.1.png", 1.5f, 1.5f),
							this.addGraphics("./res/images/coin.gold.big.2.png", 1.5f, 1.5f),
							this.addGraphics("./res/images/coin.gold.big.3.png", 1.5f, 1.5f),
							this.addGraphics("./res/images/coin.gold.big.4.png", 1.5f, 1.5f),
							this.addGraphics("./res/images/coin.gold.big.5.png", 1.5f, 1.5f),
							this.addGraphics("./res/images/coin.gold.big.6.png", 1.5f, 1.5f),
							this.addGraphics("./res/images/coin.gold.big.7.png", 1.5f, 1.5f),
							this.addGraphics("./res/images/coin.gold.big.8.png", 1.5f, 1.5f)));
			this.shape = new Polygon(0, 0, 1.5f, 0, 1.5f, 1.5f, 0, 1.5f);

		} else {
			this.graphics = new ArrayList<>(Arrays.asList(this.addGraphics("./res/images/coin.gold.1.png", .75f, .75f),
					this.addGraphics("./res/images/coin.gold.2.png", .75f, .75f),
					this.addGraphics("./res/images/coin.gold.3.png", .75f, .75f),
					this.addGraphics("./res/images/coin.gold.4.png", .75f, .75f),
					this.addGraphics("./res/images/coin.gold.5.png", .75f, .75f),
					this.addGraphics("./res/images/coin.gold.6.png", .75f, .75f),
					this.addGraphics("./res/images/coin.gold.7.png", .75f, .75f),
					this.addGraphics("./res/images/coin.gold.8.png", .75f, .75f),
					this.addGraphics("./res/images/coin.gold.9.png", .75f, .75f),
					this.addGraphics("./res/images/coin.gold.10.png", .75f, .75f)));
			this.shape = new Polygon(0, 0, .75f, 0, .75f, .75f, 0, .75f);
		}

		for (ImageGraphics graphics : graphics)
			graphics.setDepth(DepthValue.FRONT_OBSTACLE_LOW.value + .5f);
		this.build(this.shape, -1, -1, true, ObjectGroup.SENSOR.group);
		this.sensor = new ProximitySensor(game, getPosition(), this.shape);
		this.graphicsCounter = 0;
		this.elapsedAnimationTime = 0;
	}
	
	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
		this.create();
	}

	@Override
	public void update(float deltaTime) {
		this.sensor.update(deltaTime);

		if (this.sensor.getSensorDetectionStatus()) {
			this.game.getGameManager().addToScore(this.isBigCoin ? 200 : 20);
			this.destroy();
		}

		this.elapsedAnimationTime += deltaTime;
		if (this.elapsedAnimationTime > this.animationTime)
			this.elapsedAnimationTime = 0;
		this.graphicsCounter = (int) (this.elapsedAnimationTime / this.animationTime * this.graphics.size());
		if (this.graphicsCounter == this.graphics.size()) this.graphicsCounter = this.graphics.size() -1;

	}

	@Override
	public void draw(Canvas canvas) {
		this.graphics.get(this.graphicsCounter).draw(canvas);
	}

	@Override
	public void destroy() {
		this.sensor.destroy();
		super.destroy();
		this.game.destroyActor(this);
	}

	@Override
	public void setPosition(Vector newPosition) {
		super.setPosition(newPosition);
		this.sensor.setPosition(newPosition);
	}
}

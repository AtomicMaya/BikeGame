package main.game.actor.entities;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.Linker;
import main.io.Saveable;
import main.math.Transform;
import main.math.Vector;
import main.window.Canvas;

/** A trampoline that can give a Jump boost. */
public class Trampoline implements Actor, Saveable {
    /** The {@linkplain GenericPlatform} that is the jumpable part of the {@linkplain Trampoline}. */
	private transient GenericPlatform genericPlatform;

	/** The physical link, the {@linkplain AnchorPoint}. */
	private transient AnchorPoint anchor;

	/** The master {@linkplain ActorGame}. */
	private transient ActorGame game;

    /** Used for save purposes. */
    private static final long serialVersionUID = 1927654389208717735L;

    /** The initial position {@linkplain Vector}. */
	private Vector position;

	/** The width and the height of the {@linkplain GenericPlatform}. */
	private float width, height;

    /**
     * Creates a new {@linkplain Trampoline}.
     * @param game The master {@linkplain ActorGame}.
     * @param position The initial position {@linkplain Vector}.
     * @param width The width of the {@linkplain GenericPlatform}.
     * @param height The height of the {@linkplain GenericPlatform}.
     */
	public Trampoline(ActorGame game, Vector position, float width, float height) {
		this.game = game;
		this.position = position;
		this.width = width;
		this.height = height;

		this.create();
	}

    /**
     * Actual creation of the parameters of the {@linkplain GameEntity}, not in the constructor to avoid duplication
     * with the method {@linkplain #reCreate(ActorGame)}.
     */
	private void create() {
		this.width = this.width < 0 ? 5 : this.width;
		this.height = this.height < 0 ? 1 : this.height;
		this.anchor = new AnchorPoint(this.game, this.position);

		this.genericPlatform = new GenericPlatform(this.game, this.position, this.width, this.height);

        this.genericPlatform.setConstraint(Linker.attachWeldConstraint(this.game, this.anchor.getEntity(), this.genericPlatform.getEntity(), new Vector(-1, 0),
                0, 2.5f, 0));

    }

	@Override
	public void reCreate(ActorGame game) {
		this.game = game;
		create();
	}

	@Override
	public void draw(Canvas canvas) {
		this.anchor.draw(canvas);
		this.genericPlatform.draw(canvas);
	}

    @Override
    public void update(float deltaTime) {
        this.anchor.update(deltaTime);
        this.genericPlatform.update(deltaTime);
    }

	@Override
	public void destroy() {
		this.anchor.destroy();
		this.genericPlatform.destroy();
		this.game.destroyActor(this);
	}

	@Override
	public Transform getTransform() {
		return Transform.I.translated(this.position);
	}


    @Override
    public Vector getPosition() {
        return this.position;
    }


	@Override
	public Vector getVelocity() {
		return Vector.ZERO;
	}

	public void setPosition(Vector newPosition) {
		this.position = newPosition;
		this.anchor.setPosition(newPosition);
		this.genericPlatform.setPosition(newPosition.add(1,0));
	}

	public void setSize(float width, float height) {
		this.width = width;
		this.height = height;

		this.genericPlatform.setSize(width, height);
	}

}

package main.game.actor.sensors;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.actor.entities.GameEntity;
import main.io.Saveable;
import main.math.BasicContactListener;
import main.math.Entity;
import main.math.ExtendedMath;
import main.math.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/** A {@linkplain GameEntity} that can trigger events. */
public abstract class Trigger extends GameEntity implements Saveable {
    /** Used for save purposes. */
	private static final long serialVersionUID = 5271808823415280590L;

	/** Status of this {@linkplain Trigger}. */
	private boolean trigger = false;

	/** The associated {@linkplain BasicContactListener}. */
	private transient BasicContactListener contactListener;

	/** The associated {@linkplain ObjectGroup} that trigger this {@linkplain Trigger}. */
	private ArrayList<Integer> triggerGroups = new ArrayList<>();

	/** Represent the dimensions of the {@linkplain Sensor}. */
	private float width, height;

	/** The {@linkplain Trigger}'s {@linkplain ObjectGroup}. */
	private int group = ObjectGroup.CHECKPOINT.group;

	/**
	 * Create a new {@linkplain Trigger}.
	 * @param game The master {@linkplain ActorGame}.
	 * @param fixed Whether this {@linkplain Trigger} is fixed.
	 * @param position The position {@linkplain Vector} of this {@linkplain Trigger}.
	 * @param width width of the trigger area.
	 * @param height height of the trigger area.
	 */
	public Trigger(ActorGame game, boolean fixed, Vector position, float width, float height) {
		super(game, fixed, position);
		this.width = width;
		this.height = height;
		this.create();
	}

	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
		this.create();
	}

	private void create() {
		this.build(ExtendedMath.createRectangle(this.width, this.height), -1, -1, true, this.group);
		this.contactListener = new BasicContactListener();
		this.addContactListener(this.contactListener);
	}

	/** Change the group of this {@linkplain Trigger}. */
	public void setGroup(ObjectGroup objectGroup) {
		this.group = objectGroup.group;
	}

	@Override
	public void update(float deltaTime) {
		if (!this.trigger) {
			Set<Entity> entities = this.contactListener.getEntities();
			for (Entity e : entities) {
				if (this.triggerGroups.contains(e.getCollisionGroup())) {
					this.trigger = true;
					this.trigger();
				}
			}
		}
	}

	/** Actions to do once this {@linkplain Trigger} is triggered. */
	abstract void trigger();

	/** Add a list of {@linkplain ObjectGroup} to this {@linkplain Trigger}. */
	protected void addGroupTrigger(List<ObjectGroup> list) {
		for (ObjectGroup objectGroup : list)
			if (!this.triggerGroups.contains(objectGroup.group))
				this.triggerGroups.add(objectGroup.group);
	}

	/** Add an {@linkplain ObjectGroup} to this {@linkplain Trigger}. */
	protected void addGroupTrigger(ObjectGroup objectGroup) {
		if (!this.triggerGroups.contains(objectGroup.group))
			this.triggerGroups.add(objectGroup.group);
	}

	/** @return whether this {@linkplain Trigger} is triggered. */
	public boolean isTriggered() {
		return this.trigger;
	}

	/** Set the trigger status of this {@linkplain Trigger}. */
	public void setTriggerStatus(boolean triggerStatus) {
		this.trigger = triggerStatus;
	}

	/**
	 * Set the size of this {@linkplain Trigger}.
	 * @param width The new width.
	 * @param height The new height.
	 */
	public void setSize(float width, float height) {
		this.width = (width < 0) ? this.width : width;
		this.height = (height < 0) ? this.height : height;
		this.create();
	}

	/** @return the width of the area. */
	protected float getWidth() {
		return this.width;
	}

	/** @return the height of the area. */
	protected float getHeight() {
		return this.height;
	}
}

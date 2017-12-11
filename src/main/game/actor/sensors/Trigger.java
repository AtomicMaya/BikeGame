package main.game.actor.sensors;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import main.game.ActorGame;
import main.game.actor.ObjectGroup;
import main.game.actor.entities.GameEntity;
import main.io.Saveable;
import main.math.BasicContactListener;
import main.math.Entity;
import main.math.ExtendedMath;
import main.math.Vector;

public abstract class Trigger extends GameEntity implements Saveable {

	// for save purpose
	private static final long serialVersionUID = 5271808823415280590L;
	// status
	private boolean trigger = false;
	// trigger params
	private transient BasicContactListener contactListener;
	private ArrayList<Integer> triggerGroups = new ArrayList<>();

	// sensor area
	private float width, height;

	private int group = ObjectGroup.CHECKPOINT.group;

	/**
	 * Create a new {@linkplain Trigger}
	 * @param game {@linkplain ActorGame} where this {@linkplain Trigger} belong
	 * @param fixed whether this {@linkplain Trigger} is fixed
	 * @param position position of this {@linkplain Trigger}
	 * @param width width of the trigger area
	 * @param height height of the area
	 */
	public Trigger(ActorGame game, boolean fixed, Vector position, float width, float height) {
		super(game, fixed, position);
		this.width = width;
		this.height = height;
		create();
	}

	@Override
	public void reCreate(ActorGame game) {
		super.reCreate(game);
		create();
	}

	private void create() {
		this.build(ExtendedMath.createRectangle(width, height), -1, -1, true, group);
		this.contactListener = new BasicContactListener();
		this.addContactListener(contactListener);
	}

	/** Change the group appartenance of this {@linkplain Trigger} */
	public void setGroup(ObjectGroup og) {
		group = og.group;
	}

	@Override
	public void update(float deltaTime) {
		if (!trigger) {
			Set<Entity> entities = contactListener.getEntities();
			for (Entity e : entities) {
				if (triggerGroups.contains(e.getCollisionGroup())) {
					trigger = true;
					trigger();
				}
			}
		}
	}

	/** method called when this is triggered is set to true */
	abstract void trigger();

	/** Add a list of ObjectGroup to this {@linkplain Trigger} */
	protected void addGroupTrigger(List<ObjectGroup> list) {
		for (ObjectGroup o : list)
			if (!this.triggerGroups.contains(o.group))
				this.triggerGroups.add(o.group);
	}

	/** Add an ObjectGroup to this {@linkplain Trigger} */
	protected void addGroupTrigger(ObjectGroup og) {
		if (!triggerGroups.contains(og.group))
			triggerGroups.add(og.group);
	}

	/** @return whether this {@linkplain Trigger} is triggered */
	public boolean isTriggered() {
		return trigger;
	}

	/** Set the trigger status of this {@linkplain Trigger} */
	public void setTriggerStatus(boolean triggerStatus) {
		this.trigger = triggerStatus;
	}

	/**
	 * Set the size of this {@linkplain Trigger}
	 * @param width new wisth
	 * @param height new height
	 */
	public void setSize(float width, float height) {
		this.width = (width < 0) ? this.width : width;
		this.height = (height < 0) ? this.height : height;
		create();
	}

	/** @return the width of the area */
	protected float getWidth() {
		return width;
	}

	/** @return the height of the area */
	protected float getHeight() {
		return height;
	}
}

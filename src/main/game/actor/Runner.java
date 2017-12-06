package main.game.actor;

import main.game.actor.entities.ParallelAction;

/**
 * Created on 12/6/2017 at 2:10 PM.
 */
public interface Runner {
    /**
     * Runs a runnable action in parallel to this thread.
     * @param action : the action to run
     * @param expirationTime : When the action should expire.
     */
    default void runAction(Runnable action, float expirationTime) {
        ParallelAction.generateWorker(action).execute();
    }

    /**
     * Adds runnable actions to this lever
     * @param action : the action to run
     * @param expirationTime : When this button shouldn't be considered busy anymore.
     */
    void addAction(Runnable action, float expirationTime);
}

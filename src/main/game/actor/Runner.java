package main.game.actor;

/** Can run actions. */
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

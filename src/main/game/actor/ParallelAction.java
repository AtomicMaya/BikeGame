package main.game.actor;

import javax.swing.*;

/** Allows the running of {@linkplain Runnable}s without breaking the main Swing thread. */
public abstract class ParallelAction {
    public static SwingWorker<Void, Void> generateWorker(Runnable action) {
        return new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                action.run();
                return null;
            }
        };
    }
}
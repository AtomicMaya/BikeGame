package main.game.actor.entities;

import javax.swing.*;

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
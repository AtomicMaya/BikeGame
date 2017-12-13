package main.io;

import main.game.ActorGame;
import main.game.actor.Actor;

import java.io.Serializable;

/** Represent a saveable actor */
public interface Saveable extends Serializable, Actor {

    /**
     * Method used recreate an actor when loaded from a file, ActorGame can't be
     * save and would anyway not be the same in a new game so this method create the
     * Actor using its parameters and an ActorGame given in parameters.
     * @param game The master {@linkplain ActorGame}.
     */
    void reCreate(ActorGame game);
}
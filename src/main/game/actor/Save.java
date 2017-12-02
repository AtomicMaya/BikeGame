/**
 *	Author: Clément Jeannet
 *	Date: 	2 déc. 2017
 */
package main.game.actor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import main.game.ActorGame;

public class Save {

	public static void newSave(Actor actor, File file) throws IOException {
		if (!file.exists())
			file.createNewFile();

		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(actor);
			oos.close();
		} catch (IOException exception) {
			System.out.println("Erreur lors de l'écriture : " + exception.getMessage());
			exception.printStackTrace();
		}
	}

	public static Actor readSavedActor(ActorGame game, File file) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			Object o = ois.readObject();
			ois.close();

			//System.out.println(p2);
			Actor actor = (Actor)o;
			actor.reCreate(game);
			return actor;
		} catch (ClassNotFoundException exception) {
			System.out.println("Impossible de lire l'objet : " + exception.getMessage());
			exception.printStackTrace();

		} catch (IOException exception) {
			System.out.println("Erreur lors de l'écriture : " + exception.getMessage());
			exception.printStackTrace();
		}
		return null;
	}
}

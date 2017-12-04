/**
 *	Author: Clément Jeannet
 *	Date: 	2 déc. 2017
 */
package main.io;

import main.game.ActorGame;
import main.game.actor.Actor;
import main.game.actor.Savable;

import java.io.*;

public class Save {

	/**
	 * Save an actor in a file
	 * 
	 * @param actor the actor to save
	 * @param file where to save the actor
	 */
	public static void saveActor(Actor actor, File file) {
		try {
			if (!file.exists())
				// create a file the file does not exist yet
				file.createNewFile();
			// write the actor in the file
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
			oos.writeObject(actor);
			oos.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Read and load the saved Actor in the file
	 * 
	 * @param game the game in which the actor will evolve
	 * @param file the file where is stored the actors
	 * @return the actor, null if something went wrong
	 */
	public static Actor readSavedActor(ActorGame game, File file) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
			Object o = ois.readObject();
			ois.close();

			// transform the object into an actor
			try {
				Savable actor = (Savable) o;
				actor.reCreate(game);
				return (Actor) actor;
			} catch (ClassCastException cce) {
				cce.printStackTrace();
			}

		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void saveParameters(int actorCameraNumber, FileSystem fileSystem, File file) {
		try {
			if (!file.exists())
				file.createNewFile();
			OutputStream oos = fileSystem.write(file.getPath());
			String save = "viewCandidateNumber : <" + actorCameraNumber + ">";
			System.out.println(file);
			oos.write(save.getBytes());
			oos.flush();
			oos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static int viewCandidateNumberInFile(FileSystem fileSystem, File file) {
		try {
			InputStream is = fileSystem.read(file.getPath());
			byte[] b = new byte[is.available()];
			is.read(b);
			is.close();

			String s = new String(b);
			int start = s.indexOf('<') + 1;
			int stop = s.indexOf('>', start);
			return Integer.parseInt(s.substring(start, stop));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;

	}

	/**
	 * @return the files in a folder
	 */
	public static File[] availableSaves(File folder) {

		if (folder.isDirectory()) {
			return folder.listFiles();
		}
		return new File[] {};
	}
}

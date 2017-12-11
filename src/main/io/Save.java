/**
 *	Author: Clément Jeannet
 *	Date: 	2 déc. 2017
 */
package main.io;

import main.game.ActorGame;
import main.game.actor.Actor;

import java.io.*;

public class Save {

	/**
	 * Save an actor in a file
	 * 
	 * @param actor the actor to save
	 * @param file where to save the actor
	 */
	public static boolean saveActor(Actor actor, File file) {
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new FileOutputStream(file));
			// write the actor in the file
			oos.writeObject(actor);

			oos.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			if (oos != null)
				try {
					oos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return false;
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
				Saveable actor = (Saveable) o;
				System.out.println(o + " got");
				actor.reCreate(game);
				System.out.println(actor + " recreated");
				return (Actor) actor;
			} catch (ClassCastException cce) {
				cce.printStackTrace();
				System.out.println("casting error");
			}

		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			System.out.println("loading error");
		}
		return null;
	}

//	public static void saveParameters(int viewCandidateNumber, int playerNumber, FileSystem fileSystem, File file) {
//		try {
//			if (!file.exists())
//				file.createNewFile();
//			OutputStream oos = fileSystem.write(file.getPath());
//			String save = "viewCandidateNumber : <" + viewCandidateNumber + ">\n";
//			save += "playerNumber : <" + playerNumber + ">";
//
//			oos.write(save.getBytes());
//			oos.flush();
//			oos.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

//	public static int[] getParams(FileSystem fileSystem, File file) {
//		int[] r = new int[2];
//		if (file.exists())
//			try {
//				InputStream is = fileSystem.read(file.getPath());
//				byte[] b = new byte[is.available()];
//				is.read(b);
//				is.close();
//
//				String s = new String(b);
//				int start1 = s.indexOf('<') + 1;
//				int stop1 = s.indexOf('>', start1);
//				r[0] = Integer.parseInt(s.substring(start1, stop1));
//
//				int start2 = s.indexOf('<') + 1;
//				int stop2 = s.indexOf('>', start1);
//				r[1] = Integer.parseInt(s.substring(start2, stop2));
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		return r;
//
//	}

	/**
	 * @return the files in a folder
	 */
	public static File[] availableSaves(ActorGame game) {

		File folder = new File(game.getSaveDirectory());
		if (folder.isDirectory()) {
			return folder.listFiles();
		}
		return new File[] {};
	}

	/**
	 * Delete a directory and its content
	 * @param directory directory to delete
	 * @return whether the specified directory and its content has been deleted
	 */
	public static boolean deleteDirectory(File directory) {
		File[] toDelete = directory.listFiles();
		if (toDelete != null) {
			for (File f : toDelete) {
				deleteDirectory(f);
			}
		}
		return directory.delete();
	}
}

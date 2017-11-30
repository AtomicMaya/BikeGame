package main.game.actor;

import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

import java.io.FileInputStream;
import java.io.IOException;

public class Audio {
	private AudioPlayer player = AudioPlayer.player;
	private AudioStream stream;
	private AudioData data;
	private ContinuousAudioDataStream loop = null;

	public void playSound(String fileLocation, boolean loops) {
		try {
			stream = new AudioStream(new FileInputStream(fileLocation));
			if(loops) {
				data = stream.getData();
				loop = new ContinuousAudioDataStream(data);
			}
			player.start(loops ? loop : stream);
		} catch (IOException e) { e.printStackTrace(); }

	}
}

package main.game.actor;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Audio implements LineListener {
	private boolean isPlaying;
	private AudioInputStream stream;
	private AudioFormat format;
	private DataLine.Info info;
	private Clip audioClip;
	private FloatControl soundControl;

	public Audio(String fileLocation, int loops, float volumeDecrease) {
		File audioFile = new File(fileLocation);
		try {
			this.stream = AudioSystem.getAudioInputStream(audioFile);
			this.format = stream.getFormat();
			this.info = new DataLine.Info(Clip.class, format);
			this.audioClip = (Clip) AudioSystem.getLine(info);
			this.audioClip.addLineListener(this);
			this.audioClip.open(stream);
			this.audioClip.loop(loops);
			this.soundControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
			this.soundControl.setValue(-volumeDecrease);

			this.audioClip.start();

			while (isPlaying) {
				try { Thread.sleep(1000); }
				catch (InterruptedException ignored) {  }
			}
		} catch (UnsupportedAudioFileException|LineUnavailableException|IOException ignored) { }

	}

	public Audio(String fileLocation) {
		new Audio(fileLocation, 0, 0.f);
	}

	@Override
	public void update(LineEvent event) {
		LineEvent.Type type = event.getType();
		if (type == LineEvent.Type.STOP) isPlaying = false;
	}

	public void destroy() {
		this.audioClip.close();
		try { this.stream.close(); }
		catch (IOException ignored) { }
	}
}

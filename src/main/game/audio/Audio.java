package main.game.audio;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/** A sound loader, allowing us to play sounds and music in the game. */
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
			this.format = this.stream.getFormat();
			this.info = new DataLine.Info(Clip.class, this.format);
			this.audioClip = (Clip) AudioSystem.getLine(this.info);
			this.audioClip.addLineListener(this);
			this.audioClip.open(this.stream);
			this.audioClip.loop(loops);
			this.soundControl = (FloatControl) this.audioClip.getControl(FloatControl.Type.MASTER_GAIN);
			this.soundControl.setValue(-volumeDecrease);

			this.audioClip.start();

			while (this.isPlaying) {
				try { Thread.sleep(1000); }
				catch (InterruptedException ignored) {  }
			}
		} catch (UnsupportedAudioFileException|LineUnavailableException|IOException ignored) { }

	}

	public Audio(String fileLocation, float volumeDecrease) {
		new Audio(fileLocation, -1, volumeDecrease);
	}

	public Audio(String fileLocation) {
		new Audio(fileLocation, 0, 0.f);
	}

	@Override
	public void update(LineEvent event) {
		LineEvent.Type type = event.getType();
		if (type == LineEvent.Type.STOP) this.isPlaying = false;
	}

	public void destroy() {
		try {
            this.audioClip.close();
            this.stream.close(); }
		catch (IOException|NullPointerException ignored) { }
	}
}

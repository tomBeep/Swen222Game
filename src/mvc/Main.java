package mvc;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Runs the game through the gui.
 * 
 * @author Thomas Edwards
 *
 */
public class Main {

	private static Clip clip;

	public Main() {
		loadSoundClip();
		new MainMenu();
	}

	/**
	 * Plays a noise.
	 */
	public static void playSoundClip() {
		clip.loop(1);
	}

	private void loadSoundClip() {
		try {
			clip = AudioSystem.getClip();
			AudioInputStream inputStream = AudioSystem
					.getAudioInputStream(Main.class.getResourceAsStream("scifi012.wav"));
			clip.open(inputStream);
			clip.start();
		} catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		new Main();
	}

}

package animations;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;

public class TransitionAnimation {

	private Image image;
	private int animationPercent = 0;
	private int alpha = 220;
	private boolean yellow;

	public TransitionAnimation(boolean yellow) {
		this.yellow = yellow;
	}

	public void drawAnimation(Graphics2D g, int width, int height) {
		g.drawImage(image, 0, 0, null);
		g.setColor(new Color(0, 0, 0, alpha));
		g.fillRect(0, 0, width, height);
	}

	public void update() {
		animationPercent += 5;
		alpha -= 10;
	}

	public void setImage(Image i) {
		this.image = i;
	}

	public Image getImage() {
		return image;
	}

	public boolean isYellow() {
		return yellow;
	}

	public boolean isDone() {
		if (animationPercent >= 100)
			return true;
		return false;
	}

}

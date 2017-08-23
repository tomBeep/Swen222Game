package animations;

import java.awt.Color;
import java.awt.Graphics2D;

import piece.Piece;

public class DeathAnimation extends MovingAnimation {
	private Piece p;

	public DeathAnimation(Piece deadPiece) {
		super(null);
		this.p = deadPiece;
	}

	/**
	 * Draws all the pieces part of this animation.
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param selected
	 * @param greyOut
	 */
	@Override
	public void drawAnimation(Graphics2D g, int x, int y, int width, int height, boolean selected, boolean greyOut) {
		x = x * width;
		y = y * height;

		// draw the piece in its current spot.
		p.drawPiece(g, x, y, width, height, selected, greyOut);

		// draw a translucent circle over the piece, to show the piece is dying.
		int alpha = (int) (animationPercent * 2.5);
		g.setColor(new Color(0, 0, 0, alpha));
		g.fillOval(x, y, width, height);
		animationPercent += 10;
	}

	@Override
	public boolean containsOldPoint(int x, int y) {
		return x == p.getX() && y == p.getY();
	}

	@Override
	public boolean containsPiece(Piece p) {
		return false;
	}

}

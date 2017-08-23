package animations;

import java.awt.Graphics2D;

import piece.Direction;
import piece.Piece;

public class FallingAnimation extends MovingAnimation {
	boolean moving = true;
	Piece deadPiece;
	int fallingPercent = 100;

	public FallingAnimation(Direction d, Piece deadPiece) {
		super(d);
		this.deadPiece = deadPiece;
	}

	@Override
	public void drawAnimation(Graphics2D g, int x, int y, int width, int height, boolean selected, boolean greyOut) {
		if (moving) {
			// do the move, then fall.
			super.drawAnimation(g, x, y, width, height, selected, greyOut);
			if (super.animationPercent >= 100) {
				animationPercent = 0;
				fallingPercent = 95;
				moving = false;
			}
		} else if (fallingPercent != 100) {
			if (fallingPercent <= 20) {
				animationPercent = 100;
				fallingPercent = 100;
				return;
			}
			x = deadPiece.getX() + 1;
			y = deadPiece.getY() + 1;
			if (super.d == Direction.NORTH) {
				x = x * width;
				y = y * height;
			} else if (super.d == Direction.SOUTH) {
				x = x * width;
				y = y * height;
			} else if (super.d == Direction.EAST) {
				y = y * height;
				x = x * width;
			} else if (super.d == Direction.WEST) {
				y = y * height;
				x = x * width;
			}
			int finalWidth = width * fallingPercent / 100;
			int finalHeight = height * fallingPercent / 100;
			x += (width - finalWidth) / 2;
			y += (height - finalHeight) / 2;
			deadPiece.drawPiece(g, x, y, finalWidth, finalHeight, selected, greyOut);
			fallingPercent -= 5;
		}

	}
}

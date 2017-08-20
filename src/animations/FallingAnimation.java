package animations;

import java.awt.Graphics2D;

import piece.Direction;
import piece.Piece;

public class FallingAnimation extends Animation {
	boolean moving = true;
	Piece deadPiece;

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
				super.animationPercent = 95;
				moving = false;
			}
		} else if (super.animationPercent != 100) {
			if (super.animationPercent <= 20) {
				super.animationPercent = 100;
				return;
			}
			if (super.d == Direction.NORTH) {
				x = x * width;
				y = (y - 1) * height;
			} else if (super.d == Direction.SOUTH) {
				x = x * width;
				y = (y + 1) * height;
			} else if (super.d == Direction.EAST) {
				y = y * width;
				x = (x + 1) * width;
			} else if (super.d == Direction.WEST) {
				y = y * width;
				x = (x - 1) * width;
			}
			int finalWidth = width * animationPercent / 100;
			int finalHeight = height * animationPercent / 100;
			x += (width - finalWidth) / 2;
			y += (height - finalHeight) / 2;
			deadPiece.drawPiece(g, x, y, finalWidth, finalHeight, selected, greyOut);
			animationPercent -= 5;
		}

	}
}

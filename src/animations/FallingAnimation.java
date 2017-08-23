package animations;

import java.awt.Graphics2D;

import piece.Direction;
import piece.Piece;

public class FallingAnimation extends MovingAnimation {
	private boolean moving = true;// whether or not the animation is in it's moving or falling state.
	private Piece deadPiece;
	private int fallingPercent = 100;

	public FallingAnimation(Direction d, Piece deadPiece) {
		super(d);
		this.deadPiece = deadPiece;
	}

	@Override
	public void drawAnimation(Graphics2D g, int x, int y, int width, int height, boolean selected, boolean greyOut) {
		if (moving) {// draw the piece moving
			super.drawAnimation(g, x, y, width, height, selected, greyOut);
			if (super.animationPercent >= 100) {// once moving is done, start falling
				moving = false;
				if (chain) {// if a chain movement
					piecesToMove.remove(0);// deletes the falling piece from the chain moving animation.
				}
			}
		} else {// draw the piece falling
			if (chain) {
				animationPercent = 100;// draws non-falling pieces. (If part of a chain movement)
				super.drawAnimation(g, x, y, width, height, selected, greyOut);
			}

			x = deadPiece.getX() + 1;
			y = deadPiece.getY() + 1;
			x = x * width;
			y = y * height;

			// makes the piece 'shrink' as part of the falling animation.
			int finalWidth = width * fallingPercent / 100;
			int finalHeight = height * fallingPercent / 100;
			x += (width - finalWidth) / 2;
			y += (height - finalHeight) / 2;
			deadPiece.drawPiece(g, x, y, finalWidth, finalHeight, selected, greyOut);
		}

	}

	@Override
	public boolean isDone() {
		return fallingPercent <= 20;
	}

	@Override
	public void update() {
		if (moving)
			animationPercent += 10;
		else
			fallingPercent -= 5;
	}
}

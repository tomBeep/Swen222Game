package animations;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import piece.Direction;
import piece.Piece;

public class MovingAnimation {

	protected List<Entry> list = new ArrayList<Entry>();
	protected int animationPercent = 0;
	protected Direction d;
	protected boolean chain = false;// whether or not the falling animation is part of a chain movement.

	/**
	 * Creates a Moving animation, all pieces involved in this moving animation should be added vis the addPiece method.
	 * 
	 * @param d
	 *            the direction of the animation
	 */
	public MovingAnimation(Direction d) {
		this.d = d;
	}

	public void addPiece(Piece piece, int oldX, int oldY) {
		list.add(new Entry(piece, oldX, oldY));
		if (list.size() > 1)
			chain = true;
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
	public void drawAnimation(Graphics2D g, int x, int y, int width, int height, boolean selected, boolean greyOut) {
		x = x * width;
		y = y * height;

		// move a percentage of the animation...
		if (d == Direction.EAST) {
			x = x + width * animationPercent / 100;
		} else if (d == Direction.WEST) {
			x = x - width * animationPercent / 100;
		} else if (d == Direction.NORTH) {
			y = y - height * animationPercent / 100;
		} else if (d == Direction.SOUTH) {
			y = y + height * animationPercent / 100;
		}

		// draws the chain of pieces in their current state of movement.
		drawPieces(g, x, y, width, height, selected, greyOut, list.size() - 1);
	}

	protected void drawPieces(Graphics2D g, int x, int y, int width, int height, boolean selected, boolean greyOut,
			int i) {
		if (i < 0)// stopping condition.
			return;
		list.get(i).piece.drawPiece(g, x, y, width, height, selected, greyOut);// draws the piece

		// recurssivly moves to the next piece in the chain.
		if (d == Direction.EAST) {
			drawPieces(g, x + width, y, width, height, selected, greyOut, i - 1);
		} else if (d == Direction.WEST) {
			drawPieces(g, x - width, y, width, height, selected, greyOut, i - 1);
		} else if (d == Direction.NORTH) {
			drawPieces(g, x, y - height, width, height, selected, greyOut, i - 1);
		} else if (d == Direction.SOUTH) {
			drawPieces(g, x, y + height, width, height, selected, greyOut, i - 1);
		}
	}

	public boolean containsOldPoint(int x, int y) {
		for (int i = 0; i < list.size(); i++) {
			Entry e = list.get(i);
			if (e.oldX == x && e.oldY == y)
				return true;
		}
		return false;
	}

	public boolean containsPiece(Piece p) {
		for (int i = 0; i < list.size(); i++) {
			Entry e = list.get(i);
			if (e.piece == p)
				return true;
		}
		return false;
	}

	/**
	 * @return true if the animation is finished.
	 */
	public boolean isDone() {
		return animationPercent >= 100;
	}

	/**
	 * updates the current state of the animation.
	 */
	public void update() {
		animationPercent += 10;
	}
}

class Entry {

	Piece piece;// the piece, at it's new location
	int oldX, oldY;// the old location of the piece.

	public Entry(Piece p1, int oldX, int oldY) {
		super();
		this.piece = p1;
		this.oldX = oldX;
		this.oldY = oldY;
	}
}

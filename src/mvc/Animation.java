package mvc;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import piece.Direction;
import piece.Piece;

public class Animation {

	private List<Entry> list = new ArrayList<Entry>();
	int animationPercent = 10;
	Direction d;

	public Animation(Direction d) {
		this.d = d;
	}

	public void addPiece(Piece piece, int oldX, int oldY) {
		list.add(new Entry(piece, oldX, oldY));
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
		y = y * width;

		// move percentage of animation...
		if (d == Direction.EAST) {
			// for drawing background of pieces
			// g.setColor(new Color(129, 129, 129, 255));// gray
			// g.fillRect(x, y, width * (2 + list.size()), height + 5);
			x = x + width * animationPercent / 100;
		} else if (d == Direction.WEST) {
			x = x - width * animationPercent / 100;
		} else if (d == Direction.NORTH) {
			y = y - height * animationPercent / 100;
		} else if (d == Direction.SOUTH) {
			y = y + height * animationPercent / 100;
		}

		drawPieces(g, x, y, width, height, selected, greyOut, 0);
		animationPercent += 10;

	}

	private void drawPieces(Graphics2D g, int x, int y, int width, int height, boolean selected, boolean greyOut,
			int i) {
		if (i >= list.size())// stopping condition.
			return;
		list.get(i).p1.drawPiece(g, x, y, width, height, selected, greyOut);
		if (d == Direction.EAST) {
			drawPieces(g, x + width, y, width, height, selected, greyOut, i + 1);
		} else if (d == Direction.WEST) {
			drawPieces(g, x - width, y, width, height, selected, greyOut, i + 1);
		} else if (d == Direction.NORTH) {
			drawPieces(g, x, y - height, width, height, selected, greyOut, i + 1);
		} else if (d == Direction.SOUTH) {
			drawPieces(g, x, y + height, width, height, selected, greyOut, i + 1);
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
			if (e.p1 == p)
				return true;
		}
		return false;
	}

}

class Entry {

	Piece p1;// the piece, at it's new location
	int oldX, oldY;// the old location of the piece.

	public Entry(Piece p1, int oldX, int oldY) {
		super();
		this.p1 = p1;
		this.oldX = oldX;
		this.oldY = oldY;
	}
}

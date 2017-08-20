package mvc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import piece.Direction;
import piece.Piece;

public class Animation {
	Piece p1;
	int oldX, oldY;
	int animationPercent = 10;
	Direction d;

	public Animation(Piece piece, int oldX, int oldY, Direction d) {
		p1 = piece;
		this.oldX = oldX;
		this.oldY = oldY;
		this.d = d;
	}

	public void drawAnimation(Graphics2D g, int x, int y, int width, int height, boolean selected, boolean greyOut) {
		x = x * width;
		y = y * width;

		g.setStroke(new BasicStroke(2));
		// move percentage of animation...
		if (d == Direction.EAST) {
			g.setColor(new Color(129, 129, 129, 255));// gray
			g.fillRect(x, y, width * 2, height);
			g.setColor(new Color(0, 0, 0, 255));// gray
			g.drawRect(x + 1, y + 1, width * 2, height);
			x = x + width * animationPercent / 100;
		} else if (d == Direction.WEST) {
			g.setColor(new Color(129, 129, 129, 255));// gray
			g.fillRect(x - width, y, width * 2, height);
			g.setColor(new Color(0, 0, 0, 255));// gray
			g.drawRect(x - width + 1, y + 1, width * 2, height);
			x = x - width * animationPercent / 100;
		} else if (d == Direction.NORTH) {
			g.setColor(new Color(129, 129, 129, 255));// gray
			g.fillRect(x, y - width, width, height * 2);
			g.setColor(new Color(0, 0, 0, 255));// gray
			g.drawRect(x + 1, y - width + 1, width, height * 2);
			y = y - height * animationPercent / 100;
		} else if (d == Direction.SOUTH) {
			g.setColor(new Color(129, 129, 129, 255));// gray
			g.fillRect(x, y, width, height * 2);
			g.setColor(new Color(0, 0, 0, 255));// gray
			g.drawRect(x + 1, y + 1, width, height * 2);
			y = y + height * animationPercent / 100;
		}

		g.setStroke(new BasicStroke(4));
		if (animationPercent == 100) {
			p1.drawPiece(g, x, y, width, height, selected, greyOut);
		} else {
			p1.drawPiece(g, x, y, width, height, selected, greyOut);
			animationPercent += 10;
		}

	}

	public boolean oldPoint(int x, int y) {
		if (x == oldX && y == oldY)
			return true;
		return false;
	}

	public Piece getPiece() {
		return p1;
	}

}

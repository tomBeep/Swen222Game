package sides;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

import piece.Direction;
import piece.Reaction;

public class SwordSide extends AbstractSide {

	public SwordSide(Direction d) {
		super(d);
	}

	@Override
	public Reaction getReaction(AbstractSide other) {
		if (other instanceof ShieldSide) {
			// returns a moveReaction
			return Reaction.getReactionFromDir(other.getDirection());
		} else if (other instanceof SwordSide) {
			return Reaction.DIE;
		} else if (other instanceof FaceSide) {
			return Reaction.VICTORY;
		}

		return Reaction.NO_REACTION;
	}

	@Override
	public String toString() {
		if (super.getDirection() == Direction.NORTH || super.getDirection() == Direction.SOUTH)
			return "|";
		return "-";
	}

	@Override
	public AbstractSide clone() {
		return new SwordSide(direction);
	}

	@Override
	public void drawSide(Graphics2D g, int x, int y, int width, int height) {
		// Color and line thickness should be set before calling this method.

		// move x to the middle of the piece.
		x += width / 2;
		y += height / 2;
		int endx = x;
		int endy = y;

		// calculates where the endX and endY should be
		if (direction == Direction.EAST) {
			endx = x + width / 2;// for East, it should be middle of right edge
		} else if (direction == Direction.WEST)
			endx = x - width / 2;
		else if (direction == Direction.NORTH)
			endy = y - height / 2;
		else if (direction == Direction.SOUTH)
			endy = y + height / 2;

		// finnally, draw the line
		g.drawLine(x, y, endx, endy);
	}

}

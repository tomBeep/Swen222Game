package sides;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Stroke;

import piece.Direction;
import piece.Reaction;

public class ShieldSide extends AbstractSide {

	public ShieldSide(Direction d) {
		super(d);
	}

	@Override
	public Reaction getReaction(AbstractSide other) {
		// Shield sides will never cause any reactions to their piece.
		return Reaction.NO_REACTION;
	}

	@Override
	public String toString() {

		return "#";
	}

	@Override
	public AbstractSide clone() {
		return new ShieldSide(direction);
	}

	@Override
	public void drawSide(Graphics2D g, int x, int y, int width,int height) {
		if (direction == Direction.NORTH) {
			g.drawLine(x, y, x+width, y);
		} else if (direction == Direction.WEST)
			g.drawLine(x, y, x, y+height);
		else if (direction == Direction.EAST)
			g.drawLine(x+width, y, x+width, y+height);
		else if (direction == Direction.SOUTH)
			g.drawLine(x, y+height, x+width, y+height);
	}
}

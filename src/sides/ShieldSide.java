package sides;

import java.awt.Graphics2D;

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
	public void drawSide(Graphics2D g, int x, int y, int width, int height) {
		// Color and line thickness should be set before calling this method.

		// simply draw along an edge depending on what Direction the shield is facing.
		if (direction == Direction.NORTH) {
			g.drawLine(x, y, x + width, y);
		} else if (direction == Direction.WEST)
			g.drawLine(x, y, x, y + height);
		else if (direction == Direction.EAST)
			g.drawLine(x + width, y, x + width, y + height);
		else if (direction == Direction.SOUTH)
			g.drawLine(x, y + height, x + width, y + height);
	}
}

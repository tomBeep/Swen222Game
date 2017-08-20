package sides;

import java.awt.Graphics2D;

import piece.Direction;
import piece.Reaction;

public class NothingSide extends AbstractSide {

	public NothingSide(Direction d) {
		super(d);
	}

	@Override
	public Reaction getReaction(AbstractSide other) {
		if (other instanceof SwordSide)
			return Reaction.DIE;
		return Reaction.NO_REACTION;
	}

	@Override
	public String toString() {
		return " ";
	}

	@Override
	public AbstractSide clone() {
		return new NothingSide(direction);
	}

	@Override
	public void drawSide(Graphics2D g, int x, int y, int width,int height) {
	}
}

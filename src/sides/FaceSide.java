package sides;

import java.awt.Graphics2D;

import piece.Direction;
import piece.Reaction;

public class FaceSide extends AbstractSide {
	private int playerNumber;

	public FaceSide(int playerNumber) {
		super(Direction.NORTH);// a dud direction.
		this.playerNumber = playerNumber;
	}

	@Override
	public Reaction getReaction(AbstractSide other) {
		if (other instanceof SwordSide)
			return Reaction.DEFEAT;
		return Reaction.NO_REACTION;
	}

	@Override
	public String toString() {
		if (playerNumber == 1)
			return "0";
		return "1";
	}

	@Override
	public AbstractSide clone() {
		return new FaceSide(playerNumber);
	}

	@Override
	public void drawSide(Graphics2D g, int x, int y, int width,int height) {
		
	}
	

}

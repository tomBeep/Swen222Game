package sides;

import java.awt.Graphics2D;

import piece.Direction;
import piece.Reaction;

/**
 * Each Abstract Side contains the direction that it is currently at.
 * 
 * @author Thomas Edwards
 *
 */
public abstract class AbstractSide {

	protected Direction direction;

	public AbstractSide(Direction d) {
		this.direction = d;
	}

	/**
	 * @param other
	 *            the other side.
	 * @return the reaction which occurs to this Piece when this side, and the other side are next to each other.
	 */
	public abstract Reaction getReaction(AbstractSide other);

	@Override
	public abstract String toString();

	@Override
	public abstract AbstractSide clone();
	
	public abstract void drawSide(Graphics2D g,int x, int y, int width,int height);

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction newDir) {
		this.direction = newDir;
	}

}

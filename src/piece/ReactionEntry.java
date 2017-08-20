package piece;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.InvalidMoveException;

/**
 * A reaction Entry contains two pieces (p) and two reactions (r), each piece is associated with exactly one of the
 * reactions. A
 * reaction entry is considered equal to another entry if both pieces -> reactions are the same.
 * 
 * For example {p1->r1, p2->r2} is considered equal to {p2->r2, p1->r1}.
 * 
 * @author Thomas Edwards
 *
 */
public class ReactionEntry {
	private Piece piece1, piece2;// the two pieces involved in this reaction
	private Reaction reaction1, reaction2;// reaction which each piece has in this two way reaction.

	private static BufferedImage reactionImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_RGB);

	static {
		Graphics g = reactionImg.createGraphics();
		g.setColor(Color.magenta);
		g.fillRect(0, 0, 16, 16);
	}

	/**
	 * @param piece1
	 * @param piece2
	 * @param reaction1
	 *            the reaction associated with piece 1
	 * @param reaction2
	 *            the reaction associated with piece 2
	 */
	public ReactionEntry(Piece piece1, Piece piece2, Reaction reaction1, Reaction reaction2) {
		this.piece1 = piece1;
		this.piece2 = piece2;
		this.reaction1 = reaction1;
		this.reaction2 = reaction2;
	}

	/**
	 * Applies the reaction, this means each piece involved in the reaction will have an effect applied to them.
	 */
	public void apply() throws InvalidMoveException {
		try {
			piece1.doReaction(reaction1);
			piece2.doReaction(reaction2);
		} catch (InvalidHeadMoveException e) {
			throw new InvalidMoveException("Cannot this reaction as it will cause the head Piece to be moved");
		}
	}

	/**
	 * Draws a small square on the graphics board pane to represent the reaction.
	 * 
	 * @param g
	 * @param panelWidth
	 * @param panelHeight
	 * @return the reactionRectangle which should be clicked on to do the reaction.
	 */
	public Rectangle drawReaction(Graphics2D g, int panelWidth, int panelHeight) {
		int x;
		int y;
		if (leftRightReaction()) {
			x = (1 + piece2.getX()) * panelWidth / 12 - 8;
			y = (1 + piece2.getY()) * panelHeight / 12 + panelWidth / 24 - 8;
		} else {
			x = (1 + piece2.getX()) * panelWidth / 12 + panelWidth / 24 - 8;
			y = (1 + piece2.getY()) * panelHeight / 12 - 8;
		}
		g.drawImage(ReactionEntry.reactionImg, x, y, null);
		return new Rectangle(x, y, 16, 16);
	}

	private boolean leftRightReaction() {
		if (piece1.getX() != piece2.getX())
			return true;
		return false;
	}

	/**
	 * @return true if the reactionEntry contains a reaction that does literally nothing. False if the reaction does
	 *         something.
	 */
	public boolean uselessReaction() {
		return reaction1 == Reaction.NO_REACTION && reaction2 == Reaction.NO_REACTION;
	}

	@Override
	public boolean equals(Object other) {
		// Reaction a->b WILL equal Reaction b->a
		if (!(other instanceof ReactionEntry))
			return false;
		ReactionEntry re = (ReactionEntry) other;
		if (re.piece1 == this.piece1 && re.piece2 == this.piece2 && reaction1 == re.reaction1
				&& re.reaction2 == reaction2)
			return true;
		if (re.piece1 == this.piece2 && re.piece2 == this.piece1 && reaction1 == re.reaction2
				&& re.reaction1 == reaction2)
			return true;
		return false;
	}

	@Override
	public String toString() {
		if (piece1.toString().startsWith("\'%\'") || piece2.toString().startsWith("\'%\'")) {
			return "End Game, (this reaction will cause a VICTORY effect)";
		}
		return piece1.toString() + " reacts with " + piece2.toString() + ", '" + piece1.getName() + "' will "
				+ reaction1 + ", '" + piece2.getName() + "' will " + reaction2;
	}

	@Override
	public ReactionEntry clone() {
		return this;
	}
}

package piece;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import animations.MovingAnimation;
import animations.DeathAnimation;
import animations.FallingAnimation;
import mvc.Main;
import mvc.Model;
import player.Board;
import player.Graveyard;
import player.Player;
import sides.AbstractSide;

public class Piece {
	private AbstractSide northSide, eastSide, southSide, westSide;
	protected Board board;
	protected int x, y;// location of piece on the board
	private char name;
	protected Graveyard grave;
	protected int playerNumber;

	/**
	 * @param northSide
	 * @param southSide
	 * @param eastSide
	 * @param westSide
	 * @param name
	 * @param player
	 */
	public Piece(AbstractSide northSide, AbstractSide southSide, AbstractSide eastSide, AbstractSide westSide,
			char name, Graveyard g, int playerNumber) {
		this.northSide = northSide;
		this.eastSide = eastSide;
		this.southSide = southSide;
		this.westSide = westSide;
		this.name = name;
		this.grave = g;
		this.playerNumber = playerNumber;
	}

	public void moveToBoard(Board board, int x, int y) {
		this.board = board;
		this.x = x;
		this.y = y;
	}

	/**
	 * Draws this piece at the given x, y exact pixel locations with the given width and height.
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @param selected
	 * @param greyOut
	 */
	public void drawPiece(Graphics2D g, int x, int y, int width, int height, boolean selected, boolean greyOut) {
		int alpha = (greyOut && !selected) ? 127 : 255;
		// draws the background
		if (!selected)
			g.setColor(new Color(129, 129, 129, alpha));// gray
		else
			g.setColor(Color.BLUE);
		g.fillRect(x, y, width, height);

		// draws the border
		g.setColor(new Color(0, 0, 0, alpha));// black
		g.drawRect(x, y, width, height);

		// draws the circle
		x = x + 10;
		y = y + 10;
		width = width - 20;
		height = height - 20;
		if (playerNumber == 1)
			g.setColor(new Color(255, 255, 0, alpha));// yellow
		else
			g.setColor(new Color(0, 255, 0, alpha));// green
		g.fillOval(x, y, width, height);

		// draws each side
		g.setColor(new Color(255, 0, 0, alpha));// red
		northSide.drawSide(g, x, y, width, height);
		southSide.drawSide(g, x, y, width, height);
		westSide.drawSide(g, x, y, width, height);
		eastSide.drawSide(g, x, y, width, height);
	}

	/**
	 * Returns a list of all reactions associated with this piece.
	 * Wont return any useless reactions, but will potentially return duplicate reactions. (Reactions which other pieces
	 * will return as well).
	 * 
	 * @param b
	 * @return a list of all reaction entrys that this piece can cause.
	 */
	public List<ReactionEntry> getReactions(Board b) {
		List<ReactionEntry> reactions = new ArrayList<>();
		Piece otherPiece = null;
		// thisEast <-> otherWest
		if (x + 1 < Board.BOARD_WIDTH && (otherPiece = b.getPiece(x + 1, y)) != null) {
			Reaction thisReaction = eastSide.getReaction(otherPiece.westSide);
			Reaction otherReaction = otherPiece.westSide.getReaction(eastSide);
			ReactionEntry r = new ReactionEntry(this, otherPiece, thisReaction, otherReaction);
			if (!r.uselessReaction())
				reactions.add(r);
		}
		// thisWest <-> otherEast
		if (x - 1 >= 0 && (otherPiece = b.getPiece(x - 1, y)) != null) {
			Reaction thisReaction = westSide.getReaction(otherPiece.eastSide);
			Reaction otherReaction = otherPiece.eastSide.getReaction(westSide);
			ReactionEntry r = new ReactionEntry(this, otherPiece, thisReaction, otherReaction);
			if (!r.uselessReaction())
				reactions.add(r);
		}
		// thisSouth <-> otherNorth
		if (y + 1 < Board.BOARD_HEIGHT && (otherPiece = b.getPiece(x, y + 1)) != null) {
			Reaction thisReaction = southSide.getReaction(otherPiece.northSide);
			Reaction otherReaction = otherPiece.northSide.getReaction(southSide);
			ReactionEntry r = new ReactionEntry(this, otherPiece, thisReaction, otherReaction);
			if (!r.uselessReaction())
				reactions.add(r);
		}
		// thisNorth <-> otherSouth
		if (y - 1 >= 0 && (otherPiece = b.getPiece(x, y - 1)) != null) {
			Reaction thisReaction = northSide.getReaction(otherPiece.southSide);
			Reaction otherReaction = otherPiece.southSide.getReaction(northSide);
			ReactionEntry r = new ReactionEntry(this, otherPiece, thisReaction, otherReaction);
			if (!r.uselessReaction())
				reactions.add(r);
		}

		return reactions;
	}

	/**
	 * Moves the pieces, if the piece is moved out of bounds then it is killed. This method is caused by the doReaction
	 * method.
	 * 
	 * @param dx
	 * @param dy
	 */
	private void move(int dx, int dy) {
		board.setPiece(x, y, null);
		this.x += dx;
		this.y += dy;
		try {
			board.setPiece(x, y, this);
		} catch (ArrayIndexOutOfBoundsException e) {
			grave.add(this);
			Main.playSoundClip();
		}
	}

	/**
	 * Kills the piece, moving it off the board and into the graveyard.
	 */
	public void die() {
		board.setPiece(x, y, null);
		grave.add(this);
		Main.playSoundClip();
	}

	/**
	 * Gets the reaction between this piece and another, mainly used for testing purposes.
	 * 
	 * @param other
	 * @return the reaction between this piece and the other piece
	 */
	public Reaction getReaction(Piece other) {
		Reaction r = null;
		if (other.x - 1 == x) {
			r = eastSide.getReaction(other.westSide);
		} else if (other.x + 1 == x) {
			r = westSide.getReaction(other.eastSide);
		} else if (other.y - 1 == y) {
			r = southSide.getReaction(other.northSide);
		} else if (other.y + 1 == y) {
			r = northSide.getReaction(other.southSide);
		} else
			throw new Error("Pieces arent next to each other");
		return r;
	}

	@SuppressWarnings("incomplete-switch")
	public void propogateMovement(Reaction r) {
		switch (r) {
		case MOVEUP:
			if (y - 1 >= 0 && board.getPiece(x, y - 1) != null)
				board.getPiece(x, y - 1).doReaction(r);
			break;
		case MOVEDOWN:
			if (y + 1 < Board.BOARD_HEIGHT && board.getPiece(x, y + 1) != null)
				board.getPiece(x, y + 1).doReaction(r);
			break;
		case MOVERIGHT:
			if (x + 1 < Board.BOARD_WIDTH && board.getPiece(x + 1, y) != null)
				board.getPiece(x + 1, y).doReaction(r);
			break;
		case MOVELEFT:
			if (x - 1 >= 0 && board.getPiece(x - 1, y) != null)
				board.getPiece(x - 1, y).doReaction(r);
			break;
		}
	}

	/**
	 * Does the given reaction. propogates the movement if reaction is a movement reaction.
	 * 
	 * @param r
	 *            the reaction to do
	 */
	@SuppressWarnings("incomplete-switch")
	public void doReaction(Reaction r) {
		switch (r) {
		case MOVEUP:
			propogateMovement(r);
			addAnimation(Direction.NORTH);
			this.move(0, -1);
			break;
		case MOVEDOWN:
			propogateMovement(r);
			addAnimation(Direction.SOUTH);
			this.move(0, 1);
			break;
		case MOVERIGHT:
			propogateMovement(r);
			addAnimation(Direction.EAST);
			this.move(1, 0);
			break;
		case MOVELEFT:
			propogateMovement(r);
			addAnimation(Direction.WEST);
			this.move(-1, 0);
			break;
		case DIE:
			addDeathAnimation();
			this.die();
			break;
		case DEFEAT:
			System.out.println("not implemented in piece");
			break;
		}
	}

	private boolean willFallOff(Direction d) {
		int tempX = x;
		int tempY = y;
		if (d == Direction.NORTH) {
			tempY--;
		} else if (d == Direction.SOUTH) {
			tempY++;
		} else if (d == Direction.EAST) {
			tempX++;
		} else if (d == Direction.WEST) {
			tempX--;
		}
		if (tempX == 0 && tempY == 0 || tempX == 0 && tempY == 1 || tempX == 1 && tempY == 0)
			return true;
		if (tempX == 9 && tempY == 9 || tempX == 9 && tempY == 8 || tempX == 8 && tempY == 9)
			return true;
		if (tempX < 0 || tempX > 9 || tempY < 0 || tempY > 9)
			return true;
		return false;
	}

	private void addAnimation(Direction d) {
		if (Model.animation == null) {
			if (this.willFallOff(d)) {
				Model.animation = new FallingAnimation(d, this);
			} else
				Model.animation = new MovingAnimation(d);
		}
		Model.animation.addPiece(this, x, y);
	}

	private void addDeathAnimation() {
		if (Model.animation == null)
			Model.animation = new DeathAnimation(this);
	}

	/**
	 * Rotates one spot clockwise
	 */
	public void rotatePiece() {
		AbstractSide temp = northSide;
		westSide.setDirection(Direction.NORTH);
		northSide = westSide;

		southSide.setDirection(Direction.WEST);
		westSide = southSide;

		eastSide.setDirection(Direction.SOUTH);
		southSide = eastSide;

		temp.setDirection(Direction.EAST);
		eastSide = temp;
	}

	public Piece clone(Board newBoard, Graveyard newGrave) {
		Piece p = new Piece(northSide.clone(), southSide.clone(), eastSide.clone(), westSide.clone(), name, null,
				this.playerNumber);
		p.grave = newGrave;
		p.x = this.x;
		p.y = this.y;
		p.board = newBoard;
		return p;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setX(int newX) {
		this.x = newX;
	}

	public void setY(int newY) {
		this.y = newY;
	}

	public AbstractSide getNorth() {
		return northSide;
	}

	public AbstractSide getSouth() {
		return southSide;
	}

	public AbstractSide getEast() {
		return eastSide;
	}

	public AbstractSide getWest() {
		return westSide;
	}

	public char getName() {
		return name;
	}

	public int getPlayerNumber() {
		return this.playerNumber;
	}

	/**
	 * @return number between 1-24 of the piece number.
	 */
	public int getPieceNumber() {
		if (this.playerNumber == 1) {
			return name - 96;
		}
		return name - 64;
	}

	public boolean belongsToPlayer(Player p) {
		return this.playerNumber == p.getPlayerNumber();
	}

	@Override
	public String toString() {
		return String.format("\'%c\':(%d,%d)", name, x, y);
	}

}

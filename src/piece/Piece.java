package piece;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.Board;
import main.Game;
import main.Player;
import mvc.Animation;
import mvc.Model;
import sides.AbstractSide;

public class Piece {
	private AbstractSide northSide, eastSide, southSide, westSide;
	protected Board board;
	protected int x, y;// location of piece on the board
	private char name;
	private Player player;
	protected int playerNumber;

	private int dx = 50, dy = 50;

	/**
	 * @param northSide
	 * @param southSide
	 * @param eastSide
	 * @param westSide
	 * @param name
	 * @param player
	 */
	public Piece(AbstractSide northSide, AbstractSide southSide, AbstractSide eastSide, AbstractSide westSide,
			char name, Player player) {
		this.northSide = northSide;
		this.eastSide = eastSide;
		this.southSide = southSide;
		this.westSide = westSide;
		this.name = name;
		if (player != null) {
			this.playerNumber = player.getPlayerNumber();
		}
		this.player = player;
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
			player.getGraveyard().add(this);
		}
	}

	/**
	 * Kills the piece, moving it off the board and into the graveyard.
	 */
	public void die() {
		board.setPiece(x, y, null);
		player.getGraveyard().add(this);
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
			addAnimation(Direction.NORTH);
			propogateMovement(r);
			this.move(0, -1);
			break;
		case MOVEDOWN:
			addAnimation(Direction.SOUTH);
			propogateMovement(r);
			this.move(0, 1);
			break;
		case MOVERIGHT:
			addAnimation(Direction.EAST);
			propogateMovement(r);
			this.move(1, 0);
			break;
		case MOVELEFT:
			addAnimation(Direction.WEST);
			propogateMovement(r);
			this.move(-1, 0);
			break;
		case DIE:
			this.die();
			break;
		case DEFEAT:
			Game.gameOver = true;
			break;
		}
	}

	private void addAnimation(Direction d) {
		if (Model.animation == null)
			Model.animation = new Animation(d);
		Model.animation.addPiece(this, x, y);
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

	public Piece clone(Board newBoard, Player newPlayer) {
		Piece p = new Piece(northSide.clone(), southSide.clone(), eastSide.clone(), westSide.clone(), name, newPlayer);
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

	public Player getPlayer() {
		return this.player;
	}

	public int getPlayerNumber() {
		return this.playerNumber;
	}

	public boolean belongsToPlayer(Player p) {
		return this.playerNumber == p.getPlayerNumber();
	}

	@Override
	public String toString() {
		return String.format("\'%c\':(%d,%d)", name, x, y);
	}

}

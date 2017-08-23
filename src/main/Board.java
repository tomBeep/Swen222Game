package main;

import java.util.ArrayList;
import java.util.List;

import piece.Piece;
import piece.ReactionEntry;

public class Board {
	public static final int BOARD_WIDTH = 10, BOARD_HEIGHT = 10;

	private Piece[][] board;

	/**
	 * Creates a new Board of BOARD_WIDTH width and BOARD_HEIGHT height.
	 */
	public Board() {
		board = new Piece[BOARD_WIDTH][BOARD_HEIGHT];
	}

	/**
	 * @param x
	 * @param y
	 * @return the piece at position x,y
	 */
	public Piece getPiece(int x, int y) {
		if (x < 0 || y < 0 || x >= BOARD_WIDTH || y >= BOARD_HEIGHT)
			throw new IndexOutOfBoundsException("Trying to get a piece out of board range");
		return board[x][y];
	}

	/**
	 * @param letter
	 * @return the piece with the letter name, or null if there was no piece of this name.
	 */
	public Piece getPiece(char letter) {
		for (int x = 0; x < BOARD_WIDTH; x++) {
			for (int y = 0; y < BOARD_HEIGHT; y++) {
				Piece p = board[x][y];
				if (p != null && p.getName() == letter)
					return p;
			}
		}
		return null;
	}

	/**
	 * Sets the position x,y, to the given piece.
	 * 
	 * @param x
	 * @param y
	 * @param p
	 */
	public void setPiece(int x, int y, Piece p) {
		// invalidates the corners of the board
		if (x == 0 && y == 0 || x == 0 && y == 1 || x == 1 && y == 0)
			throw new ArrayIndexOutOfBoundsException();
		if (x == 9 && y == 9 || x == 9 && y == 8 || x == 8 && y == 9)
			throw new ArrayIndexOutOfBoundsException();
		board[x][y] = p;
	}

	/**
	 * Works like setPiece(), except it also updates the pieces x,y position.
	 */
	public void placePiece(Piece p, int x, int y) {
		// invalidates the corners of the board
		if (x == 0 && y == 0 || x == 0 && y == 1 || x == 1 && y == 0)
			throw new ArrayIndexOutOfBoundsException();
		if (x == 9 && y == 9 || x == 9 && y == 8 || x == 8 && y == 9)
			throw new ArrayIndexOutOfBoundsException();
		board[x][y] = p;
		p.moveToBoard(this, x, y);
	}

	/**
	 * Iterates through each piece on the board getting it's reactions and combining them into a single list. Duplicate
	 * reactions are removed.
	 * 
	 * @return a list of all reactionEntrys possible for this board State.
	 */
	public List<ReactionEntry> getReactions() {
		List<ReactionEntry> reactions = new ArrayList<>();

		for (int x = 0; x < BOARD_WIDTH; x++) {
			for (int y = 0; y < BOARD_HEIGHT; y++) {
				// skips over null spots.
				if (board[x][y] == null)
					continue;
				// Adds non-duplicate reactions to the list
				for (ReactionEntry r : board[x][y].getReactions(this)) {
					if (!reactions.contains(r))
						reactions.add(r);
				}
			}
		}

		return reactions;
	}

	public Board clone(Player newPlayer, Graveyard newGrave) {
		Board b = new Board();
		for (int i = 0; i < BOARD_WIDTH; i++) {
			for (int j = 0; j < BOARD_HEIGHT; j++) {
				if (board[i][j] != null) {
					Piece clonedPiece = board[i][j].clone(b, newGrave);
					b.setPiece(i, j, clonedPiece);
				}
			}
		}
		return b;
	}

}

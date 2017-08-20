package main;

import piece.Direction;
import piece.HeadPiece;
import piece.Piece;
import sides.FaceSide;
import sides.NothingSide;
import sides.ShieldSide;
import sides.SwordSide;

public class UnplayedPieces {

	private Piece[] unplayedPieces;

	public UnplayedPieces() {
		unplayedPieces = new Piece[25];// the last piece is the 'head' piece.
	}

	/**
	 * Sets up the starting, 24 unique pieces.
	 * 
	 * @param p
	 *            the player whom the piece belongs to.
	 * @param player1
	 *            true means that all the pieces will be named a-z, false means all named A-Z
	 */
	public void setup(Player p, int playerNumber) {
		int firstChar = playerNumber == 1 ? 97 : 65;// player 2 characters are all capital letters.

		// north-south-east-west
		unplayedPieces[0] = new Piece(new SwordSide(Direction.NORTH), new SwordSide(Direction.SOUTH),
				new ShieldSide(Direction.EAST), new SwordSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[1] = new Piece(new SwordSide(Direction.NORTH), new SwordSide(Direction.SOUTH),
				new NothingSide(Direction.EAST), new SwordSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[2] = new Piece(new ShieldSide(Direction.NORTH), new ShieldSide(Direction.SOUTH),
				new ShieldSide(Direction.EAST), new ShieldSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[3] = new Piece(new SwordSide(Direction.NORTH), new ShieldSide(Direction.SOUTH),
				new NothingSide(Direction.EAST), new NothingSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[4] = new Piece(new NothingSide(Direction.NORTH), new NothingSide(Direction.SOUTH),
				new NothingSide(Direction.EAST), new NothingSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[5] = new Piece(new SwordSide(Direction.NORTH), new ShieldSide(Direction.SOUTH),
				new ShieldSide(Direction.EAST), new SwordSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[6] = new Piece(new SwordSide(Direction.NORTH), new SwordSide(Direction.SOUTH),
				new SwordSide(Direction.EAST), new SwordSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[7] = new Piece(new SwordSide(Direction.NORTH), new ShieldSide(Direction.SOUTH),
				new NothingSide(Direction.EAST), new ShieldSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[8] = new Piece(new NothingSide(Direction.NORTH), new NothingSide(Direction.SOUTH),
				new ShieldSide(Direction.EAST), new NothingSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[9] = new Piece(new SwordSide(Direction.NORTH), new SwordSide(Direction.SOUTH),
				new ShieldSide(Direction.EAST), new ShieldSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[10] = new Piece(new SwordSide(Direction.NORTH), new NothingSide(Direction.SOUTH),
				new ShieldSide(Direction.EAST), new SwordSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[11] = new Piece(new SwordSide(Direction.NORTH), new NothingSide(Direction.SOUTH),
				new NothingSide(Direction.EAST), new NothingSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[12] = new Piece(new SwordSide(Direction.NORTH), new ShieldSide(Direction.SOUTH),
				new ShieldSide(Direction.EAST), new NothingSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[13] = new Piece(new NothingSide(Direction.NORTH), new ShieldSide(Direction.SOUTH),
				new ShieldSide(Direction.EAST), new NothingSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[14] = new Piece(new SwordSide(Direction.NORTH), new SwordSide(Direction.SOUTH),
				new NothingSide(Direction.EAST), new ShieldSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[15] = new Piece(new SwordSide(Direction.NORTH), new ShieldSide(Direction.SOUTH),
				new NothingSide(Direction.EAST), new SwordSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[16] = new Piece(new SwordSide(Direction.NORTH), new NothingSide(Direction.SOUTH),
				new NothingSide(Direction.EAST), new ShieldSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[17] = new Piece(new SwordSide(Direction.NORTH), new NothingSide(Direction.SOUTH),
				new ShieldSide(Direction.EAST), new ShieldSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[18] = new Piece(new NothingSide(Direction.NORTH), new NothingSide(Direction.SOUTH),
				new ShieldSide(Direction.EAST), new ShieldSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[19] = new Piece(new SwordSide(Direction.NORTH), new SwordSide(Direction.SOUTH),
				new NothingSide(Direction.EAST), new NothingSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[20] = new Piece(new SwordSide(Direction.NORTH), new NothingSide(Direction.SOUTH),
				new NothingSide(Direction.EAST), new SwordSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[21] = new Piece(new SwordSide(Direction.NORTH), new NothingSide(Direction.SOUTH),
				new ShieldSide(Direction.EAST), new NothingSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[22] = new Piece(new SwordSide(Direction.NORTH), new ShieldSide(Direction.SOUTH),
				new ShieldSide(Direction.EAST), new ShieldSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[23] = new Piece(new NothingSide(Direction.NORTH), new ShieldSide(Direction.SOUTH),
				new ShieldSide(Direction.EAST), new ShieldSide(Direction.WEST), (char) firstChar++, p);

		unplayedPieces[24] = new HeadPiece(new FaceSide(playerNumber), new FaceSide(playerNumber),
				new FaceSide(playerNumber), new FaceSide(playerNumber), '%', p);

	}

	/**
	 * @return the array, size 25 (24 not including head piece), of unplayed pieces.
	 */
	public Piece[] getUnplayedPieces() {
		return unplayedPieces;
	}

	public Piece getPiece(int index) {
		return unplayedPieces[index];
	}

	/**
	 * @param letter
	 * @return the index that this letter is found at.
	 */
	public int getIndex(char letter) {
		for (int i = 0; i < 24; i++) {
			if (this.unplayedPieces[i] != null && this.unplayedPieces[i].getName() == letter) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public UnplayedPieces clone() {
		UnplayedPieces clone = new UnplayedPieces();
		for (int i = 0; i < 25; i++) {
			clone.unplayedPieces[i] = this.unplayedPieces[i];
		}
		return clone;
	}
}

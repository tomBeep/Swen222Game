package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import piece.Direction;
import piece.InvalidHeadMoveException;
import piece.Piece;
import piece.Reaction;
import piece.ReactionEntry;

/**
 * This class is the main interface for the game, all methods should be called off the player when running the game.
 * 
 * @author Thomas Edwards
 *
 */
public class Player {

	private static Stack<Player> undoStack = new Stack<>();
	private Board board;
	private UnplayedPieces unplayed;
	private Graveyard graveyard;
	/**
	 * Location of the player's creation block
	 */
	private final int x, y;
	/**
	 * Location of the player's head block
	 */
	private final int headX, headY;
	private final int playerNumber;
	private List<ReactionEntry> reactions;
	private List<Piece> movedPieces;

	/**
	 * Essentially this controls whether or not the player can move/rotate pieces or can only create a piece.
	 */
	private boolean hasPlacedPiece = false;

	/**
	 * Creaters a new Player with a starting set of pieces.
	 * 
	 * @param creationSpotX
	 * @param creationSpotY
	 * @param headSpotX
	 * @param headSpotY
	 * @param board
	 * @param playerNumber
	 */
	public Player(int creationSpotX, int creationSpotY, int headSpotX, int headSpotY, Board board, int playerNumber) {
		this.x = creationSpotX;
		this.y = creationSpotY;
		this.headX = headSpotX;
		this.headY = headSpotY;
		this.playerNumber = playerNumber;
		this.board = board;

		graveyard = new Graveyard();

		(unplayed = new UnplayedPieces()).setup(playerNumber, graveyard);// setups all the starting pieces.
		board.placePiece(unplayed.getPiece(24), headSpotX, headSpotY);// places the head position
		movedPieces = new ArrayList<>();
	}

	/**
	 * Takes a piece at the given index of the unplaced-pieces-array and attempts to add it to the board.
	 * 
	 * @param pieceIndex
	 * @throws InvalidMoveException
	 *             if either the creation spot is full, you are attempting to add a piece that has already been
	 *             added, the rotation amount isnt a correct number or you have already added a piece to the board
	 */
	public void playPiece(int pieceIndex, int rotationAmount) throws InvalidMoveException {
		if (pieceIndex < 0 || pieceIndex > 24)
			throw new Error("Trying to select a piece outside of the alphabet range");
		if (rotationAmount != 0 && rotationAmount != 90 && rotationAmount != 180 && rotationAmount != 270)
			throw new InvalidMoveException("Can only play a piece at 0, 90, 180 or 270 orientation");
		if (board.getPiece(x, y) != null)
			throw new InvalidMoveException("Can't add units to board if the creation spot is full");
		if (hasPlacedPiece)
			throw new InvalidMoveException("Can only add one piece to the board per turn.");
		Piece p = unplayed.getPiece(pieceIndex);// pickup piece
		if (p == null)
			throw new InvalidMoveException("This unit doesn't exist, or it has already been added to the board");

		undoStack.push(this.clone());// records the game state
		// rotates the piece to the correct orientation.
		while (rotationAmount > 0) {
			p.rotatePiece();
			rotationAmount -= 90;
		}

		unplayed.getUnplayedPieces()[pieceIndex] = null;// remove piece from unplayedPile
		board.placePiece(p, x, y);// place piece on board
		this.hasPlacedPiece = true;
		reactions = board.getReactions();// updatesReactions.
	}

	/**
	 * Takes a piece with the given letter name and attempts to add it to the board.
	 * 
	 * @param pieceIndex
	 * @throws InvalidMoveException
	 *             if either the creation spot is full, you are attempting to add a piece that has already been
	 *             added, the rotation amount isnt a correct number or you have already added a piece to the board
	 */
	public void playPiece(char pieceLetter, int rotationAmount) throws InvalidMoveException {
		int index = unplayed.getIndex(pieceLetter);
		if (index == -1)
			throw new InvalidMoveException("This unit has already been added to the board or doesnt exist.");
		playPiece(index, rotationAmount);// pickup piece
	}

	/**
	 * Moves the piece at the given location, in the given direction and updates the possible reactions
	 * 
	 * @throws InvalidMoveException
	 *             If there is no piece at the given location or you try to move a piece twice in a turn, or you try to
	 *             move a piece that isn't yours.
	 */
	public void movePiece(int x, int y, Direction d) throws InvalidMoveException {
		if (!hasPlacedPiece)
			throw new InvalidMoveException("You must place a piece, or pass before moving any pieces");
		if (reactions != null && reactions.size() != 0) {
			throw new InvalidMoveException("You have reactions which have not taken place yet.");
		}
		Piece p = board.getPiece(x, y);
		if (p == null)
			throw new InvalidMoveException("Square selected to move contains no Piece");
		if (!p.belongsToPlayer(this))
			throw new InvalidMoveException("Cannot move a Piece that isnt yours");
		if (movedPieces.contains(p))
			throw new InvalidMoveException("Can't move/rotate a Piece more than once per turn");

		undoStack.push(this.clone());// records the game state

		// moves the piece by giving it a move Reaction
		Reaction r = Reaction.getReactionFromDir(d);
		try {
			p.doReaction(r);
		} catch (InvalidHeadMoveException e) {
			throw new InvalidMoveException("Can't move the Head-Piece in any shape or form");
		}
		movedPieces.add(p);// ensures that piece can't be moved or rotated anymore.
		reactions = board.getReactions();// updates potential reactions.
	}

	/**
	 * Moves the piece of the given name, in the given direction and updates the possible reactions
	 * 
	 * @throws InvalidMoveException
	 *             If there is no piece found or you try to move a piece twice in a turn, or you try to
	 *             move a piece that isn't yours.
	 */
	public void movePiece(char letter, Direction d) throws InvalidMoveException {
		Piece p = board.getPiece(letter);
		if (p == null)
			throw new InvalidMoveException("Specified Piece could not be found on the board");
		movePiece(p.getX(), p.getY(), d);
	}

	/**
	 * @param x
	 * @param y
	 * @param amount
	 *            in degrees (so either 90,180 or 270)
	 * @throws InvalidMoveException
	 *             if you havent created a piece yet, or there was no piece at the board location or you try rotate a
	 *             piece that isnt yours
	 */
	public void rotatePiece(int x, int y, int rotationAmount) throws InvalidMoveException {
		if (rotationAmount != 0 && rotationAmount != 90 && rotationAmount != 180 && rotationAmount != 270)
			throw new InvalidMoveException("Can only rotate a piece at 0, 90, 180 or 270 degrees");
		if (!hasPlacedPiece)
			throw new InvalidMoveException("You must place a piece, or pass before rotating any pieces");
		if (reactions != null && reactions.size() != 0)
			throw new InvalidMoveException("You have reactions which have not taken place yet.");

		Piece p = board.getPiece(x, y);
		if (p == null)
			throw new InvalidMoveException("There was no piece on this location of the board");
		if (!p.belongsToPlayer(this))
			throw new InvalidMoveException("Cannot rotate a piece that isnt yours");
		if (movedPieces.contains(p))
			throw new InvalidMoveException("Can't rotate/move a Piece more than once per turn");

		undoStack.push(this.clone());// records the game state
		// rather crude method of rotating, but it works fine.
		while (rotationAmount > 0) {
			p.rotatePiece();
			rotationAmount -= 90;
		}
		movedPieces.add(p);// ensures that piece can't be moved or rotated anymore.
		reactions = board.getReactions();// gets the reactions available after the move/rotation
	}

	/**
	 * @param amount
	 *            in degrees (so either 90,180 or 270)
	 * @throws InvalidMoveException
	 *             if you havent created a piece yet, or there was no piece at the board location or you try rotate a
	 *             piece that isnt yours
	 */
	public void rotatePiece(char letter, int amount) throws InvalidMoveException {
		Piece p = board.getPiece(letter);
		if (p == null)
			throw new InvalidMoveException("Specified Piece could not be found on the board");
		rotatePiece(p.getX(), p.getY(), amount);
	}

	/**
	 * Does the desired Reaction and then updates the list of possible reactions.
	 * 
	 * @param reactionIndex
	 *            the index of the reaction you would like to do.
	 * @throws InvalidMoveException
	 *             if reaction index is invalid.
	 */
	public void doReaction(int reactionIndex) throws InvalidMoveException {
		if (reactionIndex >= reactions.size() || reactionIndex < 0)
			throw new InvalidMoveException("The reaction index chosen was invalid");

		undoStack.push(this.clone());// records the game state
		ReactionEntry r = reactions.get(reactionIndex);
		r.apply();
		reactions = board.getReactions();// re-calculates the possible reactions.
	}

	/**
	 * Ends the players turn and resets all lists/flags. (Use pass() to end the turn.)
	 * 
	 */
	private void endTurn() {
		movedPieces = new ArrayList<>();
		reactions = null;
		this.hasPlacedPiece = false;
		undoStack = new Stack<>();// prevents a player from undoing parts of the previous player's turn.
	}

	/**
	 * @return the Player containing all the information about the previous state to be undone too.
	 * @throws InvalidMoveException
	 *             if there is nothing to undo (the undoStack is empty)
	 */
	public Player undoAction() throws InvalidMoveException {
		if (undoStack.isEmpty())
			throw new InvalidMoveException("Nothing to undo");
		Player p = undoStack.pop();
		p.reactions = p.getBoard().getReactions();
		return p;
	}

	public Player clone() {
		Player clone = new Player(x, y, headX, headY, this.board, playerNumber);
		clone.graveyard = graveyard.clone(null);
		Board b = board.clone(clone, clone.graveyard);
		clone.board = b;
		if (this.reactions != null) {
			clone.reactions = new ArrayList<ReactionEntry>();
			for (int i = 0; i < reactions.size(); i++) {
				ReactionEntry r = this.reactions.get(i);
				clone.reactions.add(r.clone());
			}
		}
		if (this.movedPieces != null) {
			clone.movedPieces = new ArrayList<Piece>();
			for (int i = 0; i < movedPieces.size(); i++) {
				Piece p = this.movedPieces.get(i);
				clone.movedPieces.add(p.clone(b, clone.graveyard));
			}
		}
		clone.hasPlacedPiece = this.hasPlacedPiece;
		clone.unplayed = this.unplayed.clone(clone.board, clone.graveyard);
		return clone;
	}

	/**
	 * Either Ends the players turn, or moves the player's turn to stage 2.
	 * 
	 * @return true if the turn was ended, false if it was moved to stage 2.
	 * @throws InvalidMoveException
	 *             If there are still reactions to be carried out.
	 */
	public boolean pass() throws InvalidMoveException {
		if (reactions != null && reactions.size() > 0)
			throw new InvalidMoveException("Can't end the turn or pass when there are reactions remaining");
		// if you havent placed anything this turn, then move to stage 2
		if (!this.hasPlacedPiece) {
			this.hasPlacedPiece = true;
			return false;
		} else {// end the turn.
			endTurn();
			return true;
		}
	}

	public List<ReactionEntry> getReactions() {
		return reactions;
	}

	public Graveyard getGraveyard() {
		return this.graveyard;
	}

	public UnplayedPieces getUnplayedPieces() {
		return this.unplayed;
	}

	public Board getBoard() {
		return board;
	}

	/**
	 * @return the x co-ordinate of this player's creation spot
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y co-ordinate of this player's creation spot
	 */
	public int getY() {
		return y;
	}

	public int getPlayerNumber() {
		return this.playerNumber;
	}

	public List<Piece> getMovedPieces() {
		return this.movedPieces;
	}

	public void setBoard(Board b) {
		this.board = b;
	}

	public boolean getHasPlacedPiece() {
		return this.hasPlacedPiece;
	}

	public void setHasPlacedPiece(boolean b) {
		this.hasPlacedPiece = b;
	}
}

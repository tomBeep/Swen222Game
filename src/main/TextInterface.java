package main;

import java.io.InputStream;
import java.util.Scanner;

import piece.Direction;
import piece.Piece;
import piece.ReactionEntry;

/**
 * Text interface to play the game.
 * 
 * @author Thomas Edwards
 *
 */
public class TextInterface {
	private Player p1, p2;
	private Scanner sc;

	public TextInterface(Player p1, Player p2, InputStream s) {
		this.p1 = p1;
		this.p2 = p2;
		Game.currentTurnPlayer = p1;
		System.out.println("Welcome to Swords and Shields!");
		System.out.println("Developed By Thomas Edwards 26/07/2017\n");
		sc = new Scanner(s);
		startScreen();
	}

	/**
	 * The main loop keeping the game going.
	 */
	public void mainControl() {
		displayBoard();
		System.out.println("Welcome Players, type \'info\' for game information");
		System.out.println("Player 1's turn:");
		String line = null;

		while (!Game.gameOver) {// mainLoop
			if ((line = sc.nextLine()) != null)
				parseCommand(line);
		}

		this.gameOver();
		sc.close();
	}

	/**
	 * Parses the given line and calls a doCommand on the desired Command
	 * 
	 * @param line
	 *            the line to parse into a command
	 */
	public void parseCommand(String line) {
		try {
			String[] cmd = line.split(" ");
			if (line.equalsIgnoreCase("pass")) {
				doCommand("pass", '~', -1);
			} else if (cmd.length == 3 && line.startsWith("create")) {
				doCommand(cmd[0], cmd[1].charAt(0), Integer.parseInt(cmd[2]));
			} else if (cmd.length == 3 && line.startsWith("move")) {
				doCommand(cmd[0], cmd[1].charAt(0), Direction.numFromWord(cmd[2]));
			} else if (cmd.length == 3 && line.startsWith("rotate")) {
				doCommand(cmd[0], cmd[1].charAt(0), Integer.parseInt(cmd[2]));
			} else if (line.equalsIgnoreCase("undo")) {
				doCommand("undo", 'a', 1);
			}
			// this logic is done immediately, from this method without using doCommand().
			else if (line.equalsIgnoreCase("info")) {
				this.printInfo();
			} else if (line.equalsIgnoreCase("graveyard")) {
				this.displayGraveyard();
			} else if (line.equalsIgnoreCase("unplayed")) {
				this.displayUnplayedPieces();
			} else {
				System.out.println("Error: Unrecognisable Command, type \'info\' for command list");
			}
		} catch (NumberFormatException e) {
			System.out.println("Error: Unrecognisable Command, type \'info\' for command list");
		}
	}

	/**
	 * Each command can be expected to call the corresponding player command, redraw the board and then print a
	 * confirmation message
	 * 
	 * @param command
	 *            the command do do
	 * @param letter
	 *            the letter to do ('~' if no letter)
	 * @param amount
	 *            measure of the command(
	 */
	public void doCommand(String command, char letter, int amount) {
		try {
			if (command.equals("move")) {
				Game.currentTurnPlayer.movePiece(letter, Direction.dirFromNum(amount));
				displayBoard();
				System.out.println("Piece " + letter + " was moved");
			} else if (command.equals("rotate")) {
				Game.currentTurnPlayer.rotatePiece(letter, amount);
				displayBoard();
				System.out.println("Piece was rotated by " + amount + " degrees");
			} else if (command.equals("create")) {
				Game.currentTurnPlayer.playPiece(letter, amount);
				displayBoard();
				System.out.println("Piece " + letter + " was created!");
			} else if (command.equals("pass")) {
				boolean turnEnd = Game.currentTurnPlayer.pass();
				if (!turnEnd)// if you havent created a piece, simply move to phase 2
					System.out.println("Passed ~~~ No Pieces were created this turn");
				else {// if you have created a piece, then your turn is ended...
					if (Game.currentTurnPlayer == p1) {
						Game.currentTurnPlayer = p2;
						System.out.println("Player 2's turn:");
					} else {
						Game.currentTurnPlayer = p1;
						System.out.println("Player 1's turn:");
					}
				}

			} else if (command.equals("undo")) {
				if (Game.currentTurnPlayer == p1) {
					p1 = Game.currentTurnPlayer = p1.undoAction();
					p2.setBoard(p1.getBoard());
				} else {
					p2 = Game.currentTurnPlayer = p2.undoAction();
					p1.setBoard(p2.getBoard());
				}
				displayBoard();
				System.out.println("Previous action was undone.");
			} else {
				System.out.println("Error: Unrecognised Command, type \'info\' for command list");
				return;
			}

			if (Game.currentTurnPlayer.getReactions() != null) {
				reactionHandler();
			}
		} catch (InvalidMoveException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Handles the "waiting for a reaction to be chosen" state.
	 */
	public void reactionHandler() {
		try {
			while (!Game.gameOver && Game.currentTurnPlayer.getReactions().size() > 0) {

				// keep listing and doing reactions...
				listReactions();
				int index = getReactionIndex();// index is the number the user inputs.

				// check if undo was called.
				if (index == -7) {// -7 is the special 'undo' index
					undoHandler();
					return;
				}

				Game.currentTurnPlayer.doReaction(index);
				displayBoard();
				System.out.println("Reaction Done");
			}
		} catch (InvalidMoveException e) {
			System.out.println(e.getMessage());
			reactionHandler();
		}
	}

	private void undoHandler() throws InvalidMoveException {
		//undoes the action.
		if (Game.currentTurnPlayer == p1) {
			p1 = Game.currentTurnPlayer = p1.undoAction();
			p2.setBoard(p1.getBoard());
		} else {
			p2 = Game.currentTurnPlayer = p2.undoAction();
			p1.setBoard(p2.getBoard());
		}
		displayBoard();
		System.out.println("Previous action was undone.");
		
		//re-check the board for reactions.
		if (Game.currentTurnPlayer.getReactions() != null && Game.currentTurnPlayer.getReactions().size() > 0) {
			reactionHandler();
		}
	}

	/**
	 * @return the index of the reaction to be done. or '-7' if undo was desired.
	 */
	private int getReactionIndex() {
		String line = null;
		while ((line = sc.nextLine()) != null) {
			if (line.equalsIgnoreCase("undo")) {
				return -7;// returns -7 if undo was selected.
			} else {
				try {
					return Integer.parseInt(line) - 1;
				} catch (NumberFormatException e) {
					System.out.println("Not a number....");
				}
			}
		}
		return -1;
	}

	/**
	 * Lists the reactions available to the player
	 */
	public void listReactions() {
		System.out.println(
				"You have reactions to choose from, type the number of the reaction you would like to carry out. ");
		System.out.println("Type undo to undo the last move");
		int i = 1;
		for (ReactionEntry r : Game.currentTurnPlayer.getReactions()) {
			System.out.println(i + ") " + r.toString());
			i++;
		}
	}

	/**
	 * Displays the board in its current state.
	 */
	public void displayBoard() {
		System.out.println("-------------BOARD--------------");
		Board b = Game.currentTurnPlayer.getBoard();
		for (int y = 0; y < Board.BOARD_HEIGHT; y++) {
			for (int z = 0; z < 3; z++) {// loops three times per row
				for (int x = 0; x < Board.BOARD_WIDTH; x++) {
					Piece current = b.getPiece(x, y);

					// deals with empty spot
					if (current == null) {
						// if the empty spot is a creation spot...
						if (p1.getX() == x && p1.getY() == y) {
							System.out.print("***");// print stars
						} else if (p2.getX() == x && p2.getY() == y) {
							System.out.print("***");
						} else
							System.out.print("   ");// else prints empty square
					}

					// deals with a spot occupied by a piece.
					else if (z == 0)
						printTopLine(current);
					else if (z == 1)
						printMiddleLine(current);
					else
						printBottomLine(current);

					// prints RHS border
					if (x == Board.BOARD_WIDTH - 1)
						System.out.print("  |");
				}
				System.out.println("\n");
			}
		}
		System.out.println("-------------BOARD--------------\n");
	}

	/**
	 * Prints unplayedPieces
	 */
	public void displayUnplayedPieces() {
		System.out.println("------------------UNPLAYED-PIECES----------------------");
		for (int i = 0; i < 24; i++) {
			Piece p = Game.currentTurnPlayer.getUnplayedPieces().getPiece(i);
			if (p != null)
				printPiece(p);
			System.out.println("********************");
		}
		System.out.println("------------------UNPLAYED-PIECES----------------------");
	}

	/**
	 * Prints graveyard
	 */
	public void displayGraveyard() {
		System.out.println("---------------------Graveyard-------------------------");
		for (int i = 0; i < Game.currentTurnPlayer.getGraveyard().size(); i++) {
			Piece p = Game.currentTurnPlayer.getGraveyard().get(i);
			printPiece(p);
			System.out.println("********************");
		}
		System.out.println("---------------------Graveyard-------------------------");
	}

	/**
	 * Prints the three lines which make up a piece.
	 * 
	 * @param p
	 *            the piece to print
	 */
	public void printPiece(Piece p) {
		printTopLine(p);
		System.out.print("\n");
		printMiddleLine(p);
		System.out.print("\n");
		printBottomLine(p);
		System.out.print("\n");
	}

	private void printTopLine(Piece p) {
		System.out.print(" " + p.getNorth() + " ");
	}

	private void printMiddleLine(Piece p) {
		System.out.print(p.getWest().toString() + p.getName() + p.getEast());
	}

	private void printBottomLine(Piece p) {
		System.out.print(" " + p.getSouth() + " ");
	}

	/**
	 * Prints the gameOver information.
	 */
	public void gameOver() {
		if (Game.currentTurnPlayer.getPlayerNumber() == p1.getPlayerNumber())
			System.out.println("Game Over Player 2 Won!");
		else
			System.out.println("Game Over Player 1 Won!");
	}

	/**
	 * Prints the startScreen info
	 */
	public void startScreen() {
		System.out.println("Type \'Start\' + enter to start the game");
		while (!sc.nextLine().equalsIgnoreCase("start")) {
			System.out.println("Unrecognisable Command, please type \'Start\'");
		}
		mainControl();
	}

	/**
	 * Prints the info box which gives a brief summary of the rules and ways of interacting.
	 */
	public void printInfo() {

		System.out.println(
				"--------------------------------------------Game Controls--------------------------------------------");
		System.out.println(
				"The objective of this game is to destroy the other player's head. Players take alternating turns, each turn consists of two phases\n"
						+ "The Creation Phase, where you can create a new piece if your CreationSpot is free\n"
						+ "The Moving Phase where you can move every piece on your board 0 or 1 times");
		System.out.println(
				"------------------------------------------------------------------------------------------------------");
		System.out.println("\t\t\tType\t\t\t\t\tWhat it does");
		System.out.println(
				"------------------------------------------------------------------------------------------------------");
		System.out.println("info						:	Displays this box with all the commands in it");
		System.out.println(
				"create <letter> <0,90,180,270>			:	Plays the specified piece from the Unplayed area, to the creationSpot at the given rotation. 1 = no rotation");
		System.out.println(
				"rotate <letter> <0,90,180,270>			:	Rotates the specified once twice or three times clockwise");
		System.out.println(
				"move <letter> <up/down/right/left>		:	Moves the specified piece in the specified direction");
		System.out.println("pass						:	Ends this phase of your turn");
		System.out.println("graveyard					:	Displays all your pieces that have been killed");
		System.out.println("unplayed					:	Displays all your pieces that are not played yet");
		System.out.println(
				"undo						:	Undoes the  last action, this can be typed at any time as long as you have done actions to undo.");
		System.out.println(
				"------------------------------------------------------------------------------------------------------");
	}

	public static void setTurn(Player p) {
		if (Game.currentTurnPlayer != p)
			Game.currentTurnPlayer = p;
	}

}

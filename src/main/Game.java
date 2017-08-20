package main;

public class Game {

	/**
	 * Whether or not the game is over.
	 */
	public static boolean gameOver = false;
	/**
	 * The player who's current turn it is.
	 */
	public static Player currentTurnPlayer;

	public Game() {
		Board board = new Board();
		Player p1 = new Player(2, 2, 1, 1, board, 1);
		Player p2 = new Player(7, 7, 8, 8, board, 2);
		new TextInterface(p1, p2, System.in);
	}

	public static void main(String[] args) {
		new Game();
	}
}

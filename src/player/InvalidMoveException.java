package player;

/**
 * Invalid Move exceptions are thrown when you try to make a move that is not valid, these exceptions should never end
 * the turn, or stop the game. Instead a player should be told to re-attempt his last action, if this exception is
 * thrown.
 * 
 * @author Thomas Edwards
 *
 */
@SuppressWarnings("serial")
public class InvalidMoveException extends Exception {

	public InvalidMoveException(String arg0) {
		super(arg0);
	}

}

package piece;

/**
 * An exception which is thrown when an invalid move is attempted, this exception indicates that the player made a
 * mistake and didnt type in a valid command, this might be for multiple reasons which should be stated in the message
 * parameter.
 * 
 * @author Thomas Edwards
 *
 */
@SuppressWarnings("serial")
public class InvalidHeadMoveException extends RuntimeException {

	public InvalidHeadMoveException(String message) {
		super(message);
	}
}

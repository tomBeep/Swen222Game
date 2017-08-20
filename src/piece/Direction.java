package piece;

public enum Direction {
	NORTH, SOUTH, EAST, WEST;

	/**
	 * @param d
	 * @return the opposite Direction of the given Direction
	 */
	public static Direction getOpposite(Direction d) {
		if (d == NORTH)
			return SOUTH;
		else if (d == SOUTH)
			return NORTH;
		else if (d == EAST)
			return WEST;
		else
			return EAST;
	}

	/**
	 * 1 = NORTH
	 * 2 = EAST
	 * 3 = SOUTH
	 * 4 = WEST
	 * 
	 * @param number
	 * @return the corresponding direction from a number
	 */
	public static Direction dirFromNum(int number) {
		if (number == 1)
			return Direction.NORTH;
		if (number == 2)
			return Direction.EAST;
		if (number == 3)
			return Direction.SOUTH;
		if (number == 4)
			return Direction.WEST;
		return null;
	}

	/**
	 * @param word
	 *            up,down,left,right ONLY
	 * @return Direction associated with the word
	 * @throws NumberFormatException
	 *             if the word is not one of the 4 above words
	 */
	public static int numFromWord(String word) throws NumberFormatException {
		if (word.equalsIgnoreCase("up"))
			return 1;
		if (word.equalsIgnoreCase("right"))
			return 2;
		if (word.equalsIgnoreCase("down"))
			return 3;
		if (word.equalsIgnoreCase("left"))
			return 4;
		throw new NumberFormatException("Not correct format");
	}

	/**
	 * @param current
	 * @return the next Direction in a clockwise rotation.
	 */
	public static Direction getNextClockwise(Direction current) {
		if (current == NORTH)
			return EAST;
		else if (current == EAST)
			return SOUTH;
		else if (current == SOUTH)
			return WEST;
		else
			return NORTH;
	}
}

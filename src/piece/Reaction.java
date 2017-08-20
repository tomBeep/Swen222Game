package piece;

public enum Reaction {
	MOVEUP, MOVEDOWN, MOVELEFT, MOVERIGHT, DIE, VICTORY, DEFEAT, NO_REACTION;

	/**
	 * Returns the correct Reaction for the given direction eg. south will return a Direction.DOWN
	 * 
	 * @param d
	 *            the direction
	 * @return the reaction based off the Direciton
	 */
	public static Reaction getReactionFromDir(Direction d) {
		if (d == Direction.NORTH)
			return MOVEUP;
		else if (d == Direction.SOUTH)
			return MOVEDOWN;
		else if (d == Direction.EAST)
			return MOVERIGHT;
		else
			return MOVELEFT;
	}

}

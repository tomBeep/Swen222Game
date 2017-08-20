package main;

import java.util.ArrayList;

import piece.Piece;

/**
 * Graveyard. (ArrayList but with a nicer name).
 * 
 * @author Thomas Edwards
 *
 */
public class Graveyard extends ArrayList<Piece> {

	public Graveyard() {
		super();
	}

	@Override
	public Graveyard clone() {
		Graveyard clone = new Graveyard();
		for (int i = 0; i < this.size(); i++) {
			clone.add(this.get(i));
		}
		return clone;
	}
}

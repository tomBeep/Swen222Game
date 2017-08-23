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

	public Graveyard clone(Board newBoard) {
		Graveyard clone = new Graveyard();
		for (int i = 0; i < this.size(); i++) {
			clone.add(this.get(i).clone(newBoard, clone));
		}
		return clone;
	}
}

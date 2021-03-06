package mvc;

import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import javax.swing.Timer;

import animations.TransitionAnimation;
import piece.Direction;
import piece.Piece;

public class Controller implements ActionListener, MouseListener, KeyListener {

	private Model model;
	/**
	 * the currently selected pieces for moving/rotating and creating
	 */
	Piece selectedCreatePiece, selectedMovePiece;

	public Controller(Model m) {
		model = m;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource().toString().equals("board")) {
			boardHandler(e);// when you click on the board panel
		} else if (e.getSource().toString().equals("yellowPieces")) {
			piecesHandler(true, e);// when you click on the yellow pieces panel
		} else if (e.getSource().toString().equals("greenPieces")) {
			piecesHandler(false, e);// click on the green pieces
		} else {
			reset();// if you click anywhere else
		}
		model.notifyObservers();
	}

	/**
	 * Resets all the 'selection' variables so that nothing is 'selected'.
	 */
	private void reset() {
		selectedCreatePiece = null;
		selectedMovePiece = null;
		model.reactionRects = null;
		model.greyOut = false;
	}

	/**
	 * Handles the clicks on the board.
	 * 
	 * @param e
	 */
	private void boardHandler(MouseEvent e) {
		if (selectedCreatePiece != null) {// if in creation stage, clear the piece.
			selectedCreatePiece = null;
		} else if (model.reactionRects != null) {// if there are reactions, do them first.
			handleReactions(e);
		} else if (selectedMovePiece == null) {// handle first click
			handleFirstClick(e);
		} else if (!model.greyOut) {// handle second (movement) click
			handleSecondClick(e);
		} else {// handle the third (rotation) clicks.
			handleThirdClick(e);
		}
	}

	/**
	 * If reactions are present, handlethem first
	 * 
	 * @param e
	 */
	private void handleReactions(MouseEvent e) {
		boolean reactionDone = false;
		// checks which reaction Rectangle the click was in.
		for (int i = 0; i < model.reactionRects.length; i++) {
			if (model.reactionRects[i].contains(e.getX(), e.getY())) {
				model.doReaction(i);
				model.reactionRects = null;
				reactionDone = true;
				break;
			}
		}
		if (!reactionDone)// if you didn't click on the reaction square, throw error
			model.diplayTempMessage("You must do Reactions (Click the purple square)");
	}

	private void handleFirstClick(MouseEvent e) {
		int width = e.getComponent().getWidth() / 12;
		int height = e.getComponent().getHeight() / 12;
		int x = (int) (e.getX() / width);
		int y = (int) (e.getY() / height);
		selectedMovePiece = model.getBoardPiece(x - 1, y - 1);

		// creates the clickable areas for moving and rotating.
		// if you click in these rectangles, then a movement is caused.
		model.movingRects = new Shape[4];// north-east-south-west
		model.movingRects[0] = new Rectangle2D.Double((x * width), ((y - 1) * height), width, height);
		model.movingRects[1] = new Rectangle2D.Double((x + 1) * width, (y * height), width, height);
		model.movingRects[2] = new Rectangle2D.Double((x * width), (y + 1) * height, width, height);
		model.movingRects[3] = new Rectangle2D.Double((x - 1) * width, (y * height), width, height);
		model.rotationRect = new Rectangle2D.Double((x) * width, (y * height), width, height);
	}

	private void handleSecondClick(MouseEvent e) {
		for (int i = 0; i < 4; i++) {
			// if you clicked in a move rectangle, move
			if (model.movingRects[i].contains(e.getX(), e.getY())) {
				model.movePiece(selectedMovePiece, Direction.dirFromNum(i + 1));
				break;
			}
		}
		// else if you clicked in the rotationRect, rotate
		if (model.rotationRect.contains(e.getX(), e.getY())) {
			model.greyOut = true;
		} else {// if you didn't click on any rectangles, unselect the piece.
			selectedMovePiece = null;
		}
	}

	private void handleThirdClick(MouseEvent e) {
		if (model.rotationRect.contains(e.getX(), e.getY())) {
			if (model.pieceCanBeMoved(selectedMovePiece))// checks that piece hasn't been moved/rotated before.
				selectedMovePiece.rotatePiece();
		} else {
			model.rotatePiece(selectedMovePiece, 0);// lock in rotation.
			model.greyOut = false;
			selectedMovePiece = null;
		}
	}

	private void piecesHandler(boolean yellow, MouseEvent e) {
		// first click, find the piece clicked on and remember it.
		int playerNumber = yellow ? 1 : 2;
		if (selectedCreatePiece == null && model.currentPlayer.getPlayerNumber() == playerNumber) {
			int x = (int) (e.getX() / (e.getComponent().getWidth() / 5));
			int y = (int) (e.getY() / (e.getComponent().getHeight() / 5));
			selectedCreatePiece = model.getUnplayedPiece(yellow, x, y);

			Model.tranimation = new TransitionAnimation(yellow);// do a transition animation.
			model.startTimer(3);
		}
		// second click, create the selected piece in the selected orientation.
		else if (model.currentPlayer.getPlayerNumber() == playerNumber) {
			int x = (int) (e.getX() / (e.getComponent().getWidth() / 2));
			int y = (int) (e.getY() / (e.getComponent().getHeight() / 2));
			model.createPiece(selectedCreatePiece, x * 90 + y * 180);
			selectedCreatePiece = null;

			Model.tranimation = new TransitionAnimation(yellow);// do a transition animation.
			model.startTimer(3);
		} else {// if you get here, then you are clicking on the wrong area. resets the selected piece.
			if (selectedCreatePiece == null)// if you are deselecting a piece don't state the error.
				if (yellow)
					model.diplayTempMessage("You can only create Green pieces on Green's turn");
				else
					model.diplayTempMessage("You can only create Yellow pieces on Yellow's turn");
			selectedCreatePiece = null;
			Model.tranimation = null;// ends the animation, if there was one.
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		reset();
		if (e.getActionCommand().equals("Pass")) {
			model.pass();
		} else if (e.getActionCommand().equals("Surrender")) {
			model.surrender();
		} else if (e.getActionCommand().equals("Undo")) {
			model.undo();
			reset();
		}
		model.notifyObservers();
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		int i = arg0.getKeyCode() - 37;
		// move's the piece if it was an arrow key.
		// HOPEFULLY this works on linux/mac, havent tested it.
		if (!model.greyOut && selectedMovePiece != null && i > 0 && i < 4) {
			model.movePiece(selectedMovePiece, Direction.dirFromNum(i));
			selectedMovePiece = null;
			model.notifyObservers();
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}

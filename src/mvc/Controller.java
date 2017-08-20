package mvc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.List;

import main.Board;
import main.Graveyard;
import piece.Direction;
import piece.Piece;
import piece.ReactionEntry;

public class Controller implements ActionListener, MouseListener, KeyListener {

	private Model model;
	/**
	 * the currently selected pieces for moving/rotating and creating
	 */
	private Piece selectedCreatePiece, selectedMovePiece;
	private boolean greyOut = false;// whether or not the bored is 'greyed out' and thus in a rotation state

	// the clickable movement areas.
	private Shape[] movingRects;
	private Shape rotationRect;
	private Shape[] reactionRects;

	public Controller(Model m) {
		model = m;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource().toString().equals("board")) {
			boardHandler(e);
		} else if (e.getSource().toString().equals("yellowPieces")) {
			piecesHandler(true, e);
		} else if (e.getSource().toString().equals("greenPieces")) {
			piecesHandler(false, e);
		} else {
			reset();
		}
		model.notifyObservers();
	}

	/**
	 * Resets all the 'selection' variables so that nothing is 'selected'
	 */
	private void reset() {
		selectedCreatePiece = null;
		selectedMovePiece = null;
		reactionRects = null;
	}

	private void boardHandler(MouseEvent e) {
		if (selectedCreatePiece != null) {// if in creation stage, clear the piece.
			selectedCreatePiece = null;
		} else if (reactionRects != null) {// if there are reactions, do them first.
			handleReactions(e);
		} else if (selectedMovePiece == null) {// handle first click
			handleFirstClick(e);
		} else if (!greyOut) {// handle second click
			handleSecondClick(e);
		} else {// handle the third (rotation) clicks.
			handleThirdClick(e);
		}
	}

	private void handleReactions(MouseEvent e) {
		boolean reactionDone = false;
		for (int i = 0; i < reactionRects.length; i++) {
			if (reactionRects[i].contains(e.getX(), e.getY())) {
				model.doReaction(i);
				reactionRects = null;
				reactionDone = true;
				break;
			}
		}
		if (!reactionDone)// if you didn't click on the reaction square, throw error
			model.infoPanel.diplayTempMessage("You must do Reactions (Click the purple square)");
	}

	private void handleFirstClick(MouseEvent e) {
		int width = e.getComponent().getWidth() / 12;
		int x = (int) (e.getX() / width);
		int y = (int) (e.getY() / width);
		selectedMovePiece = model.getBoardPiece(x - 1, y - 1);

		// creates the clickable areas for moving and rotating.
		movingRects = new Shape[4];// north-east-south-west
		movingRects[0] = new Rectangle2D.Double((x * width), ((y - 1) * width), width, width);
		movingRects[1] = new Rectangle2D.Double((x + 1) * width, (y * width), width, width);
		movingRects[2] = new Rectangle2D.Double((x * width), (y + 1) * width, width, width);
		movingRects[3] = new Rectangle2D.Double((x - 1) * width, (y * width), width, width);
		rotationRect = new Rectangle2D.Double((x) * width, (y * width), width, width);
	}

	private void handleThirdClick(MouseEvent e) {
		if (rotationRect.contains(e.getX(), e.getY())) {
			if (model.pieceCanBeMoved(selectedMovePiece))
				selectedMovePiece.rotatePiece();
		} else {
			model.rotatePiece(selectedMovePiece, 0);// lock in rotation.
			greyOut = false;
			selectedMovePiece = null;
		}
	}

	private void handleSecondClick(MouseEvent e) {
		for (int i = 0; i < 4; i++) {
			if (movingRects[i].contains(e.getX(), e.getY())) {
				model.movePiece(selectedMovePiece, Direction.dirFromNum(i + 1));
				break;
			}
		}
		if (rotationRect.contains(e.getX(), e.getY())) {
			greyOut = true;
		} else {
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
		}
		// second click, create the selected piece in the selected orientation.
		else if (model.currentPlayer.getPlayerNumber() == playerNumber) {
			int x = (int) (e.getX() / (e.getComponent().getWidth() / 2));
			int y = (int) (e.getY() / (e.getComponent().getHeight() / 2));
			model.createPiece(selectedCreatePiece.getName(), x * 90 + y * 180);
			selectedCreatePiece = null;
		} else {// if you get here, then you are clicking on the wrong area. resets the selected piece.
			if (selectedCreatePiece == null)// if you are clicking out of a selected piece, don't state the error.
				if (yellow)
					model.infoPanel.diplayTempMessage("You can only create Green pieces on Green's turn");
				else
					model.infoPanel.diplayTempMessage("You can only create Yellow pieces on Yellow's turn");
			selectedCreatePiece = null;
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	public void drawBoard(Graphics2D g, int panelWidth, int panelHeight) {
		Board b = model.getBoard();
		boolean aniDrawn = false;
		int width = panelWidth / 12;
		int height = panelHeight / 12;

		// draws the background of the board.
		g.setColor(Color.GRAY);
		g.fillRect(width, height, width * 10, height * 10);

		g.setStroke(new BasicStroke(4));
		for (int y = 1; y < 11; y++) {
			for (int x = 1; x < 11; x++) {
				Piece p = b.getPiece(x - 1, y - 1);
				if (p != null && p == selectedMovePiece) {// draw the selected piece
					p.drawPiece(g, x * width, y * height, width, height, true, greyOut);
				} else if (model.animation != null && model.animation.containsPiece(p)) {
					// animation not drawn on 'where' the current piece is, this area is left blank
				} else if (model.animation != null && model.animation.containsOldPoint(x - 1, y - 1) && !aniDrawn) {
					// animation is drawn from the old piece's position.
					model.animation.drawAnimation(g, x, y, width, height, false, greyOut);
					aniDrawn = true;
				} else if (p != null)// draw the normal piece

					p.drawPiece(g, x * width, y * height, width, height, false, greyOut);
				else {// draw empty board square
					drawEmptySquare(g, width, height, y, x);
				}
			}
		}

		// draws the graphical prompts for the reactions.
		drawReactions(g, panelWidth, panelHeight);

		// draws a slightly bigger piece for rotating.
		drawRotationPiece(g, width, height);
	}

	private void drawRotationPiece(Graphics2D g, int width, int height) {
		if (greyOut) {// grey out means that a piece has been selected for rotating.
			Piece p = selectedMovePiece;
			p.drawPiece(g, (p.getX() + 1) * width - 5, (p.getY() + 1) * height - 5, width + 10, height + 10, true,
					greyOut);
		}
	}

	private void drawReactions(Graphics2D g, int panelWidth, int panelHeight) {
		List<ReactionEntry> reactions = model.currentPlayer.getReactions();
		if (reactions != null && !reactions.isEmpty()) {
			reactionRects = new Rectangle[reactions.size()];
			for (int i = 0; i < reactions.size(); i++) {
				ReactionEntry re = reactions.get(i);
				reactionRects[i] = re.drawReaction(g, panelWidth, panelHeight);
			}
		}
	}

	private void drawEmptySquare(Graphics2D g, int width, int height, int y, int x) {
		int alpha = greyOut ? 127 : 255;// the transparency
		if (x == 3 && y == 3) {// draw the yellow creation spot
			g.setColor(new Color(255, 255, 0, alpha));// yellow
			g.fillRect(x * width, y * height, width, height);
		} else if (x == 8 && y == 8) {// draw the green creation spot
			g.setColor(new Color(0, 255, 0, alpha));// green
			g.fillRect(x * width, y * height, width, height);
		}
		// draw empty square with border unless it's one of the square behind the board
		else if (!(x == 1 && y == 1 || x == 2 && y == 1 || x == 1 && y == 2)
				&& !(x == 9 && y == 10 || x == 10 && y == 9 || x == 10 && y == 10)) {
			g.setColor(new Color(0, 0, 0, alpha));// black
			g.drawRect(x * width - 1, y * height - 1, width, height);
			g.setColor(new Color(129, 129, 129, alpha));// grey
			g.fillRect(x * width, y * height, width, height);
		}
	}

	public void drawGraveyard(boolean yellow, Graphics2D g, int panelWidth, int panelHeight) {
		g.clearRect(0, 0, panelWidth, panelWidth);
		g.setStroke(new BasicStroke(4));
		int width = panelWidth / 5;
		int height = panelHeight / 5;
		Graveyard grave = model.getGraveyard(yellow);
		if (grave == null)
			return;
		int x = 0, y = 0;
		for (Piece p : grave) {
			p.drawPiece(g, x * width, y * height, width, height, false, greyOut);
			x++;
			if (x >= 5) {
				x = 0;
				y++;
			}
		}
	}

	/**
	 * @param yellow
	 * @param g
	 * @param panelWidth
	 * @param panelHeight
	 * @return true if it drew the single piece in 4 orientation, flase if it drew all pieces.
	 */
	public void drawPieces(boolean yellow, Graphics2D g, int panelWidth, int panelHeight) {
		// draw the 4 orientations of a single piece
		if (yellow && selectedCreatePiece != null && selectedCreatePiece.getPlayerNumber() == 1
				|| !yellow && selectedCreatePiece != null && selectedCreatePiece.getPlayerNumber() == 2) {
			int width = panelWidth / 2;
			int height = panelHeight / 2;
			Piece p = selectedCreatePiece;
			g.setStroke(new BasicStroke(10));
			for (int y = 0; y < 2; y++) {
				for (int x = 0; x < 2; x++) {
					p.drawPiece(g, x * width, y * height, width, height, false, greyOut);
					selectedCreatePiece.rotatePiece();
				}
			}
		} else {// draw all pieces
			g.setStroke(new BasicStroke(4));
			int width = panelWidth / 5;
			int height = panelHeight / 5;
			for (int y = 0; y < 5; y++) {
				for (int x = 0; x < 5; x++) {
					Piece p = model.getUnplayedPiece(yellow, x, y);
					if (p != null)
						p.drawPiece(g, x * width, y * height, width, height, false, greyOut);

				}
			}
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
		if (!greyOut && selectedMovePiece != null && i > 0 && i < 4) {
			model.movePiece(selectedMovePiece, Direction.dirFromNum(i));
			selectedMovePiece = null;
			model.notifyObservers();
		}
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

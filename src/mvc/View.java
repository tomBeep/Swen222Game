package mvc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import animations.CreationAnimation;
import gui.Factory;
import gui.InfoPanel;
import gui.TomPanel;
import main.Board;
import main.Graveyard;
import piece.Piece;
import piece.ReactionEntry;

@SuppressWarnings("serial")
public class View extends JFrame implements Observer {
	private TomPanel mainBoard, yPieces, gPieces, yGrave, gGrave;
	private Controller controller;
	private Model model;
	private int splitPaneWidth;

	public View(Model m) {
		super();
		this.model = m;
		model.addObserver(this);
		CreationAnimation.v = this;// makes sure that creation Animation knows about this view.

		// creates a new controller.
		controller = new Controller(model);
		addKeyListener(controller);
		// sets up the infoPanel
		model.infoPanel = Factory.createInfoPanel();
		// sets up the panes...
		JSplitPane split1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, model.infoPanel, createPanes());
		JSplitPane split2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, split1, Factory.createToolBar(controller));
		this.add(split2);
		splitPaneWidth = split1.getDividerSize();// records the divider width

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);// sets the frame in the middle of the screen.
		setVisible(true);
		requestFocus();// makes the keyboard focus on this
	}

	/**
	 * @return a JPanel with 3 sub Panels containing the pieces, board and graveyard
	 */
	public JSplitPane createPanes() {
		JSplitPane backPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, setupPieces(), setupMainBoard());
		JSplitPane temp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, backPanel, setupGraveyards());
		return temp;
	}

	public JPanel setupMainBoard() {
		mainBoard = Factory.createBoard(this, controller);
		return mainBoard;
	}

	public JSplitPane setupGraveyards() {
		yGrave = Factory.createYellowGrave(this, controller);
		gGrave = Factory.createGreenGrave(this, controller);

		JSplitPane bothPieces = new JSplitPane(JSplitPane.VERTICAL_SPLIT, yGrave, gGrave);
		return bothPieces;
	}

	public JSplitPane setupPieces() {
		yPieces = Factory.createYellowPieces(this, controller);
		gPieces = Factory.createGreenPieces(this, controller);

		JSplitPane bothPieces = new JSplitPane(JSplitPane.VERTICAL_SPLIT, yPieces, gPieces);
		return bothPieces;
	}

	@Override
	public void update(Observable o, Object arg) {
		if (model.gameOver) {// handles the game Over state.
			model.gameOver = false;// prevents further messageDialogues being opened.
			String message;
			if (model.currentPlayer.getPlayerNumber() == 1)
				message = "The game is over. Player 2 won!";
			else
				message = "The game is over. Player 1 won!";
			JOptionPane.showMessageDialog(this, message, "Game Over", JOptionPane.PLAIN_MESSAGE);
			this.dispose();
			new MainMenu();
			return;
		}
		// redraws all the componenents of the board.
		mainBoard.repaint();
		yPieces.repaint();
		gPieces.repaint();
		yGrave.repaint();
		gGrave.repaint();
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
			p.drawPiece(g, x * width, y * height, width, height, false, model.greyOut);
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
		if (yellow && controller.selectedCreatePiece != null && controller.selectedCreatePiece.getPlayerNumber() == 1
				|| !yellow && controller.selectedCreatePiece != null
						&& controller.selectedCreatePiece.getPlayerNumber() == 2) {
			int width = panelWidth / 2;
			int height = panelHeight / 2;
			Piece p = controller.selectedCreatePiece;
			g.setStroke(new BasicStroke(10));
			for (int y = 0; y < 2; y++) {
				for (int x = 0; x < 2; x++) {
					p.drawPiece(g, x * width, y * height, width, height, false, model.greyOut);
					controller.selectedCreatePiece.rotatePiece();
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
						p.drawPiece(g, x * width, y * height, width, height, false, model.greyOut);

				}
			}
		}
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
				if (p != null && p == controller.selectedMovePiece) {// draw the selected piece
					p.drawPiece(g, x * width, y * height, width, height, true, model.greyOut);
				} else if (Model.animation != null && Model.animation.containsPiece(p)) {
					// don't draw the piece, because the animation is responsible for drawing it.

				} else if (Model.animation != null && Model.animation.containsOldPoint(x - 1, y - 1) && !aniDrawn) {
					// animation is drawn from the old piece's position.
					Model.animation.drawAnimation(g, x, y, width, height, false, model.greyOut);
					aniDrawn = true;
				} else if (Model.cranimation != null && (x == 3 && y == 3 && model.currentPlayer.getPlayerNumber() == 1
						|| x == 8 && y == 8 && model.currentPlayer.getPlayerNumber() == 2)) {
					drawEmptySquare(g, width, height, y, x);
				} else if (p != null)// draw the normal piece
					p.drawPiece(g, x * width, y * height, width, height, false, model.greyOut);
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
		if (model.greyOut) {// grey out means that a piece has been selected for rotating.
			Piece p = controller.selectedMovePiece;
			p.drawPiece(g, (p.getX() + 1) * width - 5, (p.getY() + 1) * height - 5, width + 10, height + 10, true,
					model.greyOut);
		}
	}

	private void drawReactions(Graphics2D g, int panelWidth, int panelHeight) {
		List<ReactionEntry> reactions = model.currentPlayer.getReactions();
		if (reactions != null && !reactions.isEmpty()) {
			model.reactionRects = new Rectangle[reactions.size()];
			for (int i = 0; i < reactions.size(); i++) {
				ReactionEntry re = reactions.get(i);
				model.reactionRects[i] = re.drawReaction(g, panelWidth, panelHeight);
			}
		}
	}

	private void drawEmptySquare(Graphics2D g, int width, int height, int y, int x) {
		int alpha = model.greyOut ? 127 : 255;// the transparency
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

	public TomPanel getMainBoard() {
		return mainBoard;
	}

	public TomPanel getyPieces() {
		return yPieces;
	}

	public InfoPanel getinfoPanel() {
		return model.infoPanel;
	}

	public TomPanel getgPieces() {
		return gPieces;
	}

	public int getSplitPaneWidth() {
		return splitPaneWidth;
	}

}

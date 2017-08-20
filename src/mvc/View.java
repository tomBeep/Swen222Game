package mvc;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

import animations.CreationAnimation;

@SuppressWarnings("serial")
public class View extends JFrame implements Observer {
	private TomPanel mainBoard, yPieces, gPieces, yGrave, gGrave;
	public static int boardWidth = 640;
	private Controller controller;
	private Model model;
	private InfoPanel infoPanel;

	public View(Model m) {
		super();
		this.model = m;
		m.addObserver(this);

		// creates a new controller.
		controller = new Controller(model);
		addKeyListener(controller);
		//setFocusable(true);

		// creates the main back-JPanel
		infoPanel = new InfoPanel();
		m.infoPanel = infoPanel;
		JSplitPane split1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, infoPanel.getPanel(), createPanes());
		JSplitPane split2 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, split1, createToolBar());
		this.add(split2);

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * @return a JPanel with 3 sub Panels containing the pieces, board and graveyard
	 */
	public JSplitPane createPanes() {
		JSplitPane backPanel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, setupPieces(), setupMainBoard());
		JSplitPane temp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, backPanel, setupGraveyards());
		return temp;
	}

	public JToolBar createToolBar() {
		JToolBar bar = new JToolBar();
		bar.setPreferredSize(new Dimension(100, 70));
		bar.setFloatable(false);
		bar.setLayout(new GridLayout(1, 3));
		bar.add(makeButton("Undo"));
		bar.add(makeButton("Pass"));
		bar.add(makeButton("Surrender"));
		return bar;
	}

	private JButton makeButton(String name) {
		JButton b = new TomButton(name);
		b.addActionListener(controller);
		return b;
	}

	public JPanel setupMainBoard() {
		mainBoard = new TomPanel("board") {
			@Override
			protected void paintComponent(Graphics g) {
				Image offscreen = createImage(this.getWidth(), this.getHeight());
				controller.drawBoard((Graphics2D) offscreen.getGraphics(), this.getWidth(), this.getHeight());
				g.drawImage(offscreen, 0, 0, null);
			}
		};
		mainBoard.addKeyListener(controller);
		mainBoard.addMouseListener(controller);
		mainBoard.setPreferredSize(new Dimension(boardWidth, boardWidth));
		return mainBoard;
	}

	public JSplitPane setupGraveyards() {

		yGrave = new TomPanel("yellowGrave") {
			@Override
			protected void paintComponent(Graphics g) {
				Image offscreen = createImage(this.getWidth(), this.getHeight());
				controller.drawGraveyard(true, (Graphics2D) offscreen.getGraphics(), this.getWidth(), this.getHeight());
				g.drawImage(offscreen, 0, 0, null);
			}
		};
		yGrave.setPreferredSize(new Dimension(boardWidth / 2, boardWidth / 2));
		yGrave.addMouseListener(controller);

		gGrave = new TomPanel("greenGrave") {
			@Override
			protected void paintComponent(Graphics g) {
				Image offscreen = createImage(this.getWidth(), this.getHeight());
				controller.drawGraveyard(false, (Graphics2D) offscreen.getGraphics(), this.getWidth(),
						this.getHeight());
				g.drawImage(offscreen, 0, 0, null);
			}
		};
		gGrave.addMouseListener(controller);
		gGrave.setPreferredSize(new Dimension(boardWidth / 2, boardWidth / 2));
		JSplitPane bothPieces = new JSplitPane(JSplitPane.VERTICAL_SPLIT, yGrave, gGrave);
		return bothPieces;
	}

	public JSplitPane setupPieces() {

		yPieces = new TomPanel("yellowPieces") {
			@Override
			protected void paintComponent(Graphics g) {
				Image offscreen = createImage(this.getWidth(), this.getHeight());
				controller.drawPieces(true, (Graphics2D) offscreen.getGraphics(), this.getWidth(), this.getHeight());
				g.drawImage(offscreen, 0, 0, null);
			}
		};
		yPieces.setPreferredSize(new Dimension(boardWidth / 2, boardWidth / 2));
		yPieces.addMouseListener(controller);

		gPieces = new TomPanel("greenPieces") {
			@Override
			protected void paintComponent(Graphics g) {
				Image offscreen = createImage(this.getWidth(), this.getHeight());
				controller.drawPieces(false, (Graphics2D) offscreen.getGraphics(), this.getWidth(), this.getHeight());
				g.drawImage(offscreen, 0, 0, null);
			}
		};
		gPieces.setPreferredSize(new Dimension(boardWidth / 2, boardWidth / 2));
		gPieces.addMouseListener(controller);

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

}

package mvc;

import java.awt.Shape;
import java.io.IOException;
import java.util.Observable;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Timer;

import animations.CreationAnimation;
import animations.MovingAnimation;
import animations.TransitionAnimation;
import gui.Factory;
import gui.InfoPanel;
import main.Board;
import main.Graveyard;
import main.InvalidMoveException;
import main.Player;
import main.UnplayedPieces;
import piece.Direction;
import piece.Piece;

public class Model extends Observable {
	public static final int animationSpeed = 30;

	public static MovingAnimation animation;
	public static CreationAnimation cranimation;
	public static TransitionAnimation tranimation;

	public static boolean gameOver = false;
	public static int winner;

	Player p1, p2, currentPlayer;
	View v;
	// the clickable movement areas.
	Shape[] movingRects;
	Shape rotationRect;
	Shape[] reactionRects;
	boolean greyOut = false;// whether or not the bored is 'greyed out' and thus in a rotation state

	private InfoPanel infoPanel;
	private Timer timer1;
	private Timer timer2;
	private Timer timer3;

	public Model(Player p1, Player p2) {
		this.p1 = p1;
		this.p2 = p2;
		currentPlayer = p1;
		infoPanel = Factory.createInfoPanel();

		// sets up timer 1 which is the movementAnimationTimer
		timer1 = new Timer(animationSpeed, (e) -> {
			if (animation == null || animation.isDone()) {
				animation = null;
				timer1.stop();
				notifyObservers();
			} else {
				animation.update();
				notifyObservers();
			}
		});

		// sets up timer 2 which is the creationAnimationTimer
		timer2 = new Timer(animationSpeed, (e) -> {
			if (cranimation == null || cranimation.isDone()) {
				cranimation = null;
				timer2.stop();
				notifyObservers();
			} else {
				cranimation.update();
				notifyObservers();
			}
		});

		// sets up timer 3 which is the transitionAnimationTimer
		timer3 = new Timer(Model.animationSpeed, (e) -> {
			if (tranimation == null || tranimation.isDone()) {
				tranimation = null;
				timer3.stop();
				notifyObservers();
			} else {
				tranimation.update();
				notifyObservers();
			}
		});
	}

	public void createPiece(Piece p, int rotation) {
		char pieceName = p.getName();
		try {
			currentPlayer.playPiece(pieceName, rotation);
			cranimation = new CreationAnimation(p);
			timer2.start();
		} catch (InvalidMoveException e) {
			infoPanel.diplayTempMessage(e.getMessage());
		}
	}

	public void doReaction(int reactionIndex) {
		try {
			currentPlayer.doReaction(reactionIndex);
			if (animation != null)
				timer1.start();
		} catch (InvalidMoveException e) {
			animation = null;
			infoPanel.diplayTempMessage(e.getMessage());
		}
	}

	public void movePiece(Piece toMove, Direction d) {
		try {
			currentPlayer.movePiece(toMove.getName(), d);
			if (animation != null)
				timer1.start();
		} catch (InvalidMoveException e) {
			animation = null;
			infoPanel.diplayTempMessage(e.getMessage());
		}
	}

	public void rotatePiece(Piece toRotate, int amount) {
		try {
			currentPlayer.rotatePiece(toRotate.getName(), amount);
		} catch (InvalidMoveException e) {
			infoPanel.diplayTempMessage(e.getMessage());
		}
	}

	public void pass() {
		try {
			boolean ended = currentPlayer.pass();
			if (ended) {
				infoPanel.switchTurns();
				if (p1 == currentPlayer)
					currentPlayer = p2;
				else
					currentPlayer = p1;
			}
		} catch (InvalidMoveException e) {
			infoPanel.diplayTempMessage(e.getMessage());
		}

	}

	public Board getBoard() {
		return currentPlayer.getBoard();
	}

	public Piece getBoardPiece(int x, int y) {
		try {
			return this.getBoard().getPiece(x, y);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	public Graveyard getGraveyard(boolean yellow) {
		if (yellow)
			return p1.getGraveyard();
		return p2.getGraveyard();
	}

	public Piece getUnplayedPiece(boolean yellow, int x, int y) {
		int index = x + y * 5;
		if (index >= 24)
			return null;
		if (yellow)
			return p1.getUnplayedPieces().getPiece(index);
		return p2.getUnplayedPieces().getPiece(index);
	}

	public void undo() {
		try {
			Player p = currentPlayer.undoAction();
			if (currentPlayer == p1) {
				currentPlayer = p;
				p1 = p;
				p2.setBoard(p.getBoard());
			} else {
				currentPlayer = p;
				p2 = p;
				p1.setBoard(p.getBoard());
			}
		} catch (InvalidMoveException e) {
			infoPanel.diplayTempMessage(e.getMessage());
		}
	}

	public void diplayTempMessage(String message) {
		this.infoPanel.diplayTempMessage(message);
	}

	public UnplayedPieces getUnplayed(boolean yellow) {
		if (yellow)
			return p1.getUnplayedPieces();
		return p2.getUnplayedPieces();
	}

	public void surrender() {
		gameOver = true;
		if (currentPlayer.getPlayerNumber() == 1)
			winner = 2;
		else
			winner = 1;
		notifyObservers();
	}

	public InfoPanel getInfoPanel() {
		return this.infoPanel;
	}

	/**
	 * @param p
	 * @return true if the piece can be moved and rotated
	 */
	public boolean pieceCanBeMoved(Piece p) {
		if (currentPlayer.getMovedPieces().contains(p) || !currentPlayer.getHasPlacedPiece()) {
			return false;
		}
		return true;
	}

	public void startTimer(int timer) {
		if (timer == 1)
			timer1.start();
		else if (timer == 2) {
			timer2.start();
		} else if (timer == 3) {
			timer3.start();
		}
	}

	public boolean isTurn(boolean yellow) {
		if (yellow && currentPlayer == p1)
			return true;
		if (!yellow && currentPlayer == p2)
			return true;
		return false;
	}

	@Override
	public void notifyObservers() {
		setChanged();
		super.notifyObservers();
	}

}

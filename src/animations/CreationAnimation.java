
package animations;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Line2D;

import javax.swing.JComponent;
import javax.swing.JPanel;

import mvc.Controller;
import mvc.View;
import piece.Piece;

public class CreationAnimation extends JComponent {

	public static View v;

	private double currentX, currentY, endX, endY;
	private int width, height;
	private Piece createdPiece;
	private int animationPercent = 0;
	private int updateAmount = 4;
	private double updateDX, updateDY;

	public CreationAnimation(Piece createdPiece) {
		v.setGlassPane(this);
		v.getGlassPane().setVisible(true);
		this.createdPiece = createdPiece;
		if (createdPiece.getPlayerNumber() == 1) {
			width = v.getMainBoard().getWidth() / 12;
			height = v.getMainBoard().getHeight() / 12;
			setStart(createdPiece, true);
			setEnd(createdPiece, true);
		} else {
			width = v.getMainBoard().getWidth() / 12;
			height = v.getMainBoard().getHeight() / 12;
			setStart(createdPiece, false);
			setEnd(createdPiece, false);
		}
		updateDX = ((endX - currentX) * updateAmount / 100);
		updateDY = ((endY - currentY) * updateAmount / 100);
	}

	private void setStart(Piece p, boolean yellow) {
		if (yellow) {
			int crossWidth = 4 + ((p.getPieceNumber() - 1) % 5) * v.getyPieces().getWidth() / 5;
			int crossHeight = 4 + ((int) (p.getPieceNumber() / 5)) * v.getyPieces().getHeight() / 5;
			currentX = crossWidth;
			currentY = crossHeight + v.getinfoPanel().getHeight() + v.getSplitPaneWidth();
		} else {
			int crossWidth = 4 + ((p.getPieceNumber() - 1) % 5) * v.getyPieces().getWidth() / 5;
			int crossHeight = 4 + ((int) (p.getPieceNumber() / 5)) * v.getyPieces().getHeight() / 5;
			currentX = crossWidth;
			currentY = crossHeight + v.getinfoPanel().getHeight() + v.getSplitPaneWidth() * 2
					+ v.getyPieces().getHeight();
		}
	}

	private void setEnd(Piece p, boolean yellow) {
		if (yellow) {
			endX = v.getyPieces().getWidth() + v.getMainBoard().getWidth() * 3 / 12 + v.getSplitPaneWidth();
			endY = v.getMainBoard().getHeight() * 3 / 12 + v.getinfoPanel().getHeight() + v.getSplitPaneWidth();
		} else {
			endX = v.getyPieces().getWidth() + v.getMainBoard().getWidth() * 8 / 12 + v.getSplitPaneWidth();
			endY = v.getMainBoard().getHeight() * 8 / 12 + v.getinfoPanel().getHeight() + v.getSplitPaneWidth();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setStroke(new BasicStroke(4));
		createdPiece.drawPiece(g2d, (int) currentX, (int) currentY, width, height, false, false);
	}

	/**
	 * @return true if the animation is finished.
	 */
	public boolean isDone() {
		boolean done = animationPercent >= 100;
		if (done)
			v.getGlassPane().setVisible(false);
		return done;
	}

	public Piece getPiece() {
		return createdPiece;
	}

	/**
	 * updates the current state of the animation.
	 */
	public void update() {
		animationPercent += updateAmount;
		currentX += updateDX;
		currentY += updateDY;
	}

}

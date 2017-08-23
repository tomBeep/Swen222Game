
package animations;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JComponent;
import javax.swing.JPanel;

import mvc.View;
import piece.Piece;

public class CreationAnimation extends JComponent {
	double currentX, currentY, endX, endY;
	int width, height;
	View v;
	Piece createdPiece;
	int animationPercent = 0;
	int updateAmount = 10;
	double updateDX, updateDY;

	public CreationAnimation(View v, Piece createdPiece) {
		v.setGlassPane(this);
		v.getGlassPane().setVisible(true);
		this.v = v;
		this.createdPiece = createdPiece;
		if (createdPiece.getPlayerNumber() == 1) {
			width = v.getyPieces().getWidth() / 5;
			height = v.getyPieces().getHeight() / 5;
			setStart(createdPiece, true);
			setEnd(createdPiece, true);
		} else {
			width = v.getgPieces().getWidth() / 5;
			height = v.getgPieces().getHeight() / 5;
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
		return animationPercent >= 100;
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

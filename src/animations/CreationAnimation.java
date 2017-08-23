
package animations;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import mvc.View;
import piece.Piece;

@SuppressWarnings("serial")
public class CreationAnimation extends JComponent {

	public static View v;// creation Animation REALLY needs to know about the view, to decide distances.

	private Piece createdPiece;
	private int animationPercent = 0;// how far through the animation it is.
	private int updatePercent = 5;// amount to update percent by each update method.
	private double updateDX, updateDY;// amount to update x and y by each time update() is called
	private double currentX, currentY;

	private double endX, endY;
	private int width, height;

	/**
	 * Creates a new creation animation from the given piece. to move through the animation steps use the update method.
	 * 
	 * @param createdPiece
	 */
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
		updateDX = ((endX - currentX) * updatePercent / 100);
		updateDY = ((endY - currentY) * updatePercent / 100);

	}

	/**
	 * Calculates the start point of the animation.
	 * 
	 * @param p
	 * @param yellow
	 */
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

	/**
	 * Calculates the end point for the animation.
	 * 
	 * @param p
	 * @param yellow
	 */
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
	 * @return true if the animation is finished. False if it isnt.
	 */
	public boolean isDone() {
		boolean done = animationPercent >= 100;
		if (done)// also removes the frame when done.
			v.getGlassPane().setVisible(false);
		return done;
	}

	/**
	 * Updates the current state of the animation.
	 */
	public void update() {
		animationPercent += updatePercent;
		currentX += updateDX;
		currentY += updateDY;
	}

	public Piece getPiece() {
		return createdPiece;
	}
}

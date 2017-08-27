
package animations;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JComponent;
import mvc.View;
import piece.Piece;

/**
 * A creation animation, works a bit differently to other animations. It calculates it's start and end points
 * and then interpolates across the line between them drawing the piece at each stage in the line.
 * 
 * @author Thomas Edwards
 *
 */
@SuppressWarnings("serial")
public class CreationAnimation extends JComponent {

	public static View v;// creation Animation REALLY needs to know about the view, to calculate cross-pane distances.

	private Piece createdPiece;

	private int animationPercent = 0;// how far through the animation it is.
	private int updatePercent = 5;// amount to update percent by each update method.

	private double updateDX, updateDY;// amount to update x and y by each time update() is called
	private double currentX, currentY;// current position of animation.

	private double endX, endY;
	private int width, height;

	/**
	 * Creates a new creation animation from the given piece. to move through the animation steps use the update method.
	 * 
	 * @param createdPiece
	 */
	public CreationAnimation(Piece createdPiece) {
		v.setGlassPane(this);// makes the glass pane visible.
		v.getGlassPane().setVisible(true);
		this.createdPiece = createdPiece;
		width = v.getMainBoard().getWidth() / 12;// width and height of 1 board square.
		height = v.getMainBoard().getHeight() / 12;

		// calculates start and end points
		if (createdPiece.getPlayerNumber() == 1) {
			getStart(createdPiece, true);
			getEnd(createdPiece, true);
		} else {
			getStart(createdPiece, false);
			getEnd(createdPiece, false);
		}

		// calcualtes how much each 'update()' should move the animation along.
		updateDX = ((endX - currentX) * updatePercent / 100);
		updateDY = ((endY - currentY) * updatePercent / 100);
	}

	/**
	 * Calculates the start point of the animation.
	 * 
	 * @param p
	 * @param yellow
	 */
	private void getStart(Piece p, boolean yellow) {
		if (yellow) {
			int crossWidth = 4 + ((p.getPieceNumber() - 1) % 5) * v.getyPieces().getWidth() / 5;
			int crossHeight = 4 + ((int) (p.getPieceNumber() / 5)) * v.getyPieces().getHeight() / 5;
			currentX = crossWidth;
			currentY = crossHeight + v.getinfoPanel().getHeight() + 10;
		} else {
			int crossWidth = 4 + ((p.getPieceNumber() - 1) % 5) * v.getyPieces().getWidth() / 5;
			int crossHeight = 4 + ((int) (p.getPieceNumber() / 5)) * v.getyPieces().getHeight() / 5;
			currentX = crossWidth;
			currentY = crossHeight + v.getinfoPanel().getHeight() + 10 * 2 + v.getyPieces().getHeight();
		}
	}

	/**
	 * Calculates the end point for the animation.
	 * 
	 * @param p
	 * @param yellow
	 */
	private void getEnd(Piece p, boolean yellow) {
		if (yellow) {
			endX = v.getyPieces().getWidth() + v.getMainBoard().getWidth() * 3 / 12 + 10;
			endY = v.getMainBoard().getHeight() * 3 / 12 + v.getinfoPanel().getHeight() + 10;
		} else {
			endX = v.getyPieces().getWidth() + v.getMainBoard().getWidth() * 8 / 12 + 10;
			endY = v.getMainBoard().getHeight() * 8 / 12 + v.getinfoPanel().getHeight() + 10;
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

package animations;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

import mvc.View;
import piece.Piece;

public class CreationAnimation extends JComponent {

	public CreationAnimation(View v, Piece createdPiece) {
		v.setGlassPane(this);
		v.getGlassPane().setVisible(true);
	}

	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.RED);
		g.fillOval(50, 50, 500, 500);
	}

}

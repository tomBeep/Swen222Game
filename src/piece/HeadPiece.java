package piece;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import mvc.Model;
import player.Board;
import player.Graveyard;
import sides.AbstractSide;

public class HeadPiece extends Piece {
	private BufferedImage smileyFace = new BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB);

	public HeadPiece(AbstractSide northSide, AbstractSide southSide, AbstractSide eastSide, AbstractSide westSide,
			char name, Graveyard g, int playerNumber) {
		super(northSide, southSide, eastSide, westSide, name, g, playerNumber);
		makeSmileyFace(playerNumber);
	}

	private void makeSmileyFace(int player) {
		Graphics2D g = (Graphics2D) smileyFace.getGraphics();
		if (player == 1)
			g.setColor(Color.yellow);
		else
			g.setColor(Color.green);
		g.fillOval(32, 32, 446, 446);

		g.setColor(Color.black);
		g.fillOval(125, 125, 100, 100);
		g.fillOval(290, 125, 100, 100);
		g.fillOval(125, 300, 250, 100);
	}

	@SuppressWarnings("incomplete-switch")
	@Override
	public void doReaction(Reaction r) {
		switch (r) {
		case DEFEAT:
			Model.gameOver = true;
			Model.winner = playerNumber == 1 ? 2 : 1;
			break;
		case MOVELEFT:
		case MOVEDOWN:
		case MOVERIGHT:
		case MOVEUP:
			throw new InvalidHeadMoveException("Can't move the head piece");
		}
	}

	@Override
	public void drawPiece(Graphics2D g, int x, int y, int width, int height, boolean selected, boolean greyOut) {
		int alpha = (greyOut && !selected) ? 127 : 255;
		// draws the background
		if (!selected)
			g.setColor(new Color(0, 0, 0, alpha));// black
		else
			g.setColor(Color.BLUE);
		g.fillRect(x, y, width, height);

		// draws the border
		g.setColor(new Color(0, 0, 0, alpha));// black
		g.drawRect(x, y, width, height);

		// draws the smiley face
		g.drawImage(smileyFace, x + width / 8, y + width / 8, width * 3 / 4, height * 3 / 4, null);
	}

	@Override
	public Piece clone(Board newBoard, Graveyard newGrave) {
		Piece p = new HeadPiece(null, null, null, null, 'a', null, playerNumber);
		p.grave = newGrave;
		p.x = this.x;
		p.y = this.y;
		p.board = newBoard;
		return p;
	}

}

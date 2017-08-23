package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JButton;
import javax.swing.JToolBar;

import mvc.Controller;
import mvc.View;

public class Factory {

	public final int baseBoardWidth = 640;

	public Factory() {
	}

	public static TomPanel createYellowGrave(View v, Controller controller) {
		TomPanel panel = new TomPanel("yellowGrave") {
			@Override
			protected void paintComponent(Graphics g) {
				Image offscreen = createImage(this.getWidth(), this.getHeight());
				v.drawGraveyard(true, (Graphics2D) offscreen.getGraphics(), this.getWidth(), this.getHeight());
				g.drawImage(offscreen, 0, 0, null);
			}
		};
		panel.setPreferredSize(new Dimension(View.baseBoardWidth / 2, View.baseBoardWidth / 2));
		panel.addMouseListener(controller);
		return panel;
	}

	public static TomPanel createGreenGrave(View v, Controller controller) {
		TomPanel panel = new TomPanel("greenGrave") {
			@Override
			protected void paintComponent(Graphics g) {
				Image offscreen = createImage(this.getWidth(), this.getHeight());
				v.drawGraveyard(false, (Graphics2D) offscreen.getGraphics(), this.getWidth(), this.getHeight());
				g.drawImage(offscreen, 0, 0, null);
			}
		};
		panel.setPreferredSize(new Dimension(View.baseBoardWidth / 2, View.baseBoardWidth / 2));
		panel.addMouseListener(controller);
		return panel;
	}

	public static TomPanel createYellowPieces(View v, Controller controller) {
		TomPanel p = new TomPanel("yellowPieces") {
			@Override
			protected void paintComponent(Graphics g) {
				Image offscreen = createImage(this.getWidth(), this.getHeight());
				v.drawPieces(true, (Graphics2D) offscreen.getGraphics(), this.getWidth(), this.getHeight());
				g.drawImage(offscreen, 0, 0, null);
			}
		};
		p.setPreferredSize(new Dimension(View.baseBoardWidth / 2, View.baseBoardWidth / 2));
		p.addMouseListener(controller);
		return p;
	}

	public static TomPanel createBoard(View v, Controller controller) {
		TomPanel tp = new TomPanel("board") {
			@Override
			protected void paintComponent(Graphics g) {
				Image offscreen = createImage(this.getWidth(), this.getHeight());
				v.drawBoard((Graphics2D) offscreen.getGraphics(), this.getWidth(), this.getHeight());
				g.drawImage(offscreen, 0, 0, null);
			}
		};
		tp.addKeyListener(controller);
		tp.addMouseListener(controller);
		tp.setPreferredSize(new Dimension(View.baseBoardWidth, View.baseBoardWidth));
		return tp;
	}

	public static TomPanel createGreenPieces(View v, Controller controller) {
		TomPanel p = new TomPanel("greenPieces") {
			@Override
			protected void paintComponent(Graphics g) {
				Image offscreen = createImage(this.getWidth(), this.getHeight());
				v.drawPieces(false, (Graphics2D) offscreen.getGraphics(), this.getWidth(), this.getHeight());
				g.drawImage(offscreen, 0, 0, null);
			}
		};
		p.setPreferredSize(new Dimension(View.baseBoardWidth / 2, View.baseBoardWidth / 2));
		p.addMouseListener(controller);
		return p;
	}

	public static InfoPanel createInfoPanel() {
		return new InfoPanel();
	}

	public static JToolBar createToolBar(Controller controller) {
		JToolBar bar = new JToolBar();
		bar.setPreferredSize(new Dimension(100, 70));
		bar.setFloatable(false);
		bar.setLayout(new GridLayout(1, 3));
		bar.add(makeButton(controller, "Undo"));
		bar.add(makeButton(controller, "Pass"));
		bar.add(makeButton(controller, "Surrender"));
		return bar;
	}

	private static JButton makeButton(Controller ctrler, String name) {
		JButton b = new TomButton(name);
		b.addActionListener(ctrler);
		return b;
	}
}

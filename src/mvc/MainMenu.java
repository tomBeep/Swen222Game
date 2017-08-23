package mvc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import gui.TomButton;
import main.Board;
import main.Player;

@SuppressWarnings("serial")
public class MainMenu extends JFrame {
	private int width = 400, height = 300;

	public MainMenu() {
		super();
		setFocusable(false);
		add(makeButtonPanel());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setUndecorated(true);
		pack();
		setLocationRelativeTo(null);// sets the frame in the middle of the screen.
		setVisible(true);
	}

	private void startGame() {
		Board board = new Board();
		Player p1 = new Player(2, 2, 1, 1, board, 1);
		Player p2 = new Player(7, 7, 8, 8, board, 2);
		new View(new Model(p1, p2));
		dispose();
	}

	private void doOptions() {
		JOptionPane.showMessageDialog(this, "Sword and Shield Game\nDeveloped by Thomas Edwards\nAugust 2017", "Info",
				JOptionPane.INFORMATION_MESSAGE);
	}

	private JPanel makeButtonPanel() {
		JPanel panel = new JPanel();
		panel.setBackground(Color.BLACK);
		panel.setLayout(new GridLayout(4, 1));
		panel.setBorder(new LineBorder(Color.gray));

		JLabel label = new JLabel("Sword and Shield", SwingConstants.CENTER);
		label.setForeground(Color.red);
		label.setFont(new Font("Garamond", Font.PLAIN, 30));
		panel.add(label);

		panel.add(makeButton("Start Game", (e) -> startGame()));
		panel.add(makeButton("Info", (e) -> doOptions()));
		panel.add(makeButton("Quit", (e) -> this.dispose()));

		return panel;
	}

	private JButton makeButton(String name, ActionListener a) {
		JButton button = new TomButton(name);
		button.addActionListener(a);
		button.setPreferredSize(new Dimension(width, height / 4));
		return button;
	}
}

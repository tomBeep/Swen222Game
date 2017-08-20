package mvc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class InfoPanel {

	private JPanel panel;
	private JLabel label;
	private Color turn = Color.YELLOW;
	private JTextArea text;
	private Timer timer;

	public InfoPanel() {
		panel = new JPanel();
		panel.setPreferredSize(new Dimension(600, 50));
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(textPanel());
		panel.add(turnImage());
	}

	private JPanel turnImage() {
		JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				g.setColor(Color.black);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				g.setColor(turn);
				g.fillRect(4, 4, this.getWidth() - 8, this.getHeight() - 8);
			}
		};
		label = new JLabel("Yellow Turn");
		panel.add(label);
		panel.setPreferredSize(new Dimension(200, 50));
		return panel;
	}

	private JTextArea textPanel() {
		text = new JTextArea();
		text.setPreferredSize(new Dimension(400, 200));
		text.setEditable(false);
		text.setLineWrap(true);
		text.setBackground(Color.black);
		text.setForeground(Color.yellow);
		text.setFont(new Font("Arial", 1, 20));
		return text;
	}

	/**
	 * Displays the message on the info panel for 5 seconds.
	 * 
	 * @param message
	 *            to display
	 */
	public void diplayTempMessage(String message) {
		text.setText("Error: " + message);
		if (timer != null)
			timer.cancel();
		timer = new Timer();
		TimerTask t = new TimerTask() {
			public void run() {
				text.setText("");
				timer.cancel();
			}
		};
		timer.schedule(t, 3000);// 5 second messages
	}

	public JPanel getPanel() {
		return panel;
	}

	public void switchTurns() {
		if (turn == Color.YELLOW) {
			turn = Color.green;
			label.setText("Green Turn");
		} else {
			turn = Color.yellow;
			label.setText("Yellow Turn");
		}
		panel.repaint();
	}

}

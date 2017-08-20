package mvc;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;

public class TomButton extends JButton {

	public TomButton(String text) {
		super(text);
		
		//customisation here
		setBackground(Color.BLACK);
		setForeground(Color.yellow);
		setFocusable(false);
		setFont(new Font("Arial", 1, 20));
	}

}

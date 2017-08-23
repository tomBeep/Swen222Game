package gui;
import java.awt.Color;
import java.awt.LayoutManager;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

@SuppressWarnings("serial")
public class TomPanel extends JPanel {
	private String panelName;

	public TomPanel(String name) {
		super();
		panelName = name;
		
		//customise Panel here
		super.setBorder(new LineBorder(Color.BLACK,2,true));
	}

	public TomPanel(LayoutManager arg0, String name) {
		super(arg0);
		panelName = name;
	}

	@Override
	public String toString() {
		return panelName;
	}

	public String getName() {
		return panelName;
	}

}

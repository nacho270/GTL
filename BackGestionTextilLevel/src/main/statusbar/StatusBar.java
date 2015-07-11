package main.statusbar;

import java.awt.Frame;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

public class StatusBar extends JPanel{

	private static final long serialVersionUID = -4025707201173772238L;

	public StatusBar(Frame padre) {
		setBorder(new BevelBorder(BevelBorder.LOWERED));
//		setPreferredSize(new Dimension(padre.getWidth(), 16));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}
	
	public void addComponent(ComponenteStatusBar<?> comp) {
		add(comp);
	}
}

package main.statusbar;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CompoTestRenderer extends ComponenteStatusBarRenderer<CompoTest>{

	private JLabel contLabel;
	
	public CompoTestRenderer(CompoTest c) {
		super(c);
		JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JLabel statusLabel = new JLabel("status");
		statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
		contLabel = new JLabel();
		p.add(statusLabel);
		p.add(contLabel);
		
		getComponente().add(p);
	}

	@Override
	protected void render() {
		contLabel.setText(""+getComponente().getCon());
	}

}

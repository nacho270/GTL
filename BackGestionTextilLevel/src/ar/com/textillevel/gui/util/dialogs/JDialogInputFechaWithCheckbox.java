package ar.com.textillevel.gui.util.dialogs;

import java.awt.Frame;
import javax.swing.JCheckBox;

public class JDialogInputFechaWithCheckbox extends JDialogInputFecha {

	private static final long serialVersionUID = 1L;

	private JCheckBox chk;

	public JDialogInputFechaWithCheckbox(Frame padre, String title, String checkBokTitle, boolean checkSelected, boolean checkEnabled) {
		super(padre, title);
		setSize(250, 120);
		setUpComponentes(checkBokTitle, checkSelected, checkEnabled);
	}

	private void setUpComponentes(String checkBokTitle, boolean checkSelected, boolean checkEnabled) {
		this.chk  = new JCheckBox(checkBokTitle);
		this.chk.setSelected(checkSelected);
		this.chk.setEnabled(checkEnabled);
		getPanelCentral().add(chk);
	}

	public boolean isChkSelected() {
		return chk.isSelected();
	}

}
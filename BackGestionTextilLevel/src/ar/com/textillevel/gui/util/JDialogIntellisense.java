package ar.com.textillevel.gui.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;

import ar.clarin.fwjava.componentes.VerticalFlowLayout;

public class JDialogIntellisense extends JDialog {

	private static final long serialVersionUID = 4155800509212523237L;

	private boolean acepto = false;
	private String selectedValue = null;

	public JDialogIntellisense(Dialog owner) {
		super(owner);
		setUpComponentes();
	}

	public JDialogIntellisense(Frame owner) {
		super(owner);
		setUpComponentes();
	}

	private void setUpComponentes() {
		setUndecorated(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		setLayout(new BorderLayout());
	}

	public void displaySugerencias(List<String> valores) {
		this.acepto = false;
		JPanel panelLabels = new JPanel(new VerticalFlowLayout());
		for (String valor : valores) {
			final JLabel lbl = new JLabel(valor);
			lbl.setOpaque(true);
			lbl.setSize(JDialogIntellisense.this.getWidth(), 25);
			lbl.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					JDialogIntellisense.this.selectedValue = lbl.getText();
					JDialogIntellisense.this.acepto = true;
					salir();
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					lbl.setBackground(Color.red.darker());
				}

				@Override
				public void mouseExited(MouseEvent e) {
					lbl.setBackground(null);
				}
			});
			panelLabels.add(lbl);
		}
		JScrollPane jsp = new JScrollPane(panelLabels);
		add(jsp, BorderLayout.CENTER);
	}

	public String getSelectedValue() {
		return selectedValue;
	}

	public boolean isAcepto() {
		return acepto;
	}

	private void salir() {
		dispose();
	}

}

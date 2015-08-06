package ar.com.textillevel.gui.util.controles.intellisense;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.EventListenerList;

import ar.clarin.fwjava.componentes.VerticalFlowLayout;

public class JDialogIntellisense extends JDialog {

	private static final long serialVersionUID = 4155800509212523237L;

	private boolean acepto = false;
	private String selectedValue = null;
	private final EventListenerList listeners = new EventListenerList();

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
					if (e.getClickCount() == 2) {
						final ValorSeleccionadoListener[] l = listeners.getListeners(ValorSeleccionadoListener.class);
						final ValorSeleccionadoData valorSeleccionadoData = new ValorSeleccionadoData(lbl.getText());
						SwingUtilities.invokeLater(new Runnable() {
							public void run() {
								for (int i = 0; i < l.length; i++) {
									l[i].onSelectedValue(valorSeleccionadoData);
								}
							}
						});
					}
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

	public void ubicar(JFormattedTextField txtCUIT) {
		Point l = txtCUIT.getLocationOnScreen();
		setLocation(l.x, l.y + txtCUIT.getHeight());
		setSize(txtCUIT.getWidth(), 200);		
	}
	
	public void addValorSeleccionadoActionListener(ValorSeleccionadoListener listener) {
		listeners.add(ValorSeleccionadoListener.class, listener);
	}
}

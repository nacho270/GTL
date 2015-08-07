package ar.com.textillevel.gui.util.controles.intellisense;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
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
	private JPanel panelLabels;
	private List<IntellisenseLabel> labels = new ArrayList<IntellisenseLabel>();
	private int selectedLabel = 0;
	
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
		JScrollPane jsp = new JScrollPane(getPanelLabels());
		add(jsp, BorderLayout.CENTER);
		// ESTO LO HACE ANDAR... NO SE PORQUE
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					System.out.println("abajo");
				} else if (e.getKeyCode() == KeyEvent.VK_UP) {
					System.out.println("arriba");
				}
			}
		});
	}

	public void displaySugerencias(final List<String> valores) {
		this.acepto = false;
		getPanelLabels().removeAll();
		getLabels().clear();
		for (String valor : valores) {
			IntellisenseLabel lbl = new IntellisenseLabel(valor);
			lbl.addTeclaArribaAbajoListener(new TeclaArribaAbajoEventListener() {
				public void onTeclaArriba(TeclaArribaAbajoEventData data) {
					if (selectedLabel > 0) {
						getLabels().get(selectedLabel).deseleccionar();
						selectedLabel--;
					}
					updateSelectedLabel();
				}
				
				public void onTeclaAbajo(TeclaArribaAbajoEventData data) {
					if (selectedLabel < valores.size() - 1) {
						getLabels().get(selectedLabel).deseleccionar();
						selectedLabel++;
					}
					updateSelectedLabel();
				}
			});
			getLabels().add(lbl);
			getPanelLabels().add(lbl);
		}
	}

	private JPanel getPanelLabels() {
		if(panelLabels == null) {
			panelLabels = new JPanel(new VerticalFlowLayout());		
		}
		return panelLabels;
	}

	public String getSelectedValue() {
		return selectedValue;
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void ubicar(JFormattedTextField txtCUIT, int cantidad) {
		setVisible(false);
		Point l = txtCUIT.getLocationOnScreen();
		setLocation(l.x, l.y + txtCUIT.getHeight());
		setSize(txtCUIT.getWidth(), Math.min(200,30*cantidad));
	}

	public void addValorSeleccionadoActionListener(ValorSeleccionadoListener listener) {
		listeners.add(ValorSeleccionadoListener.class, listener);
	}
	
	private void fireValorSeleccionadoEvent(String text) {
		final ValorSeleccionadoListener[] l = listeners.getListeners(ValorSeleccionadoListener.class);
		final ValorSeleccionadoData valorSeleccionadoData = new ValorSeleccionadoData(text);
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				for (int i = 0; i < l.length; i++) {
					l[i].onSelectedValue(valorSeleccionadoData);
				}
			}
		});
	}

	private class IntellisenseLabel extends JLabel {

		private static final long serialVersionUID = -8220670640752095837L;
		private final EventListenerList labelListeners = new EventListenerList();

		public IntellisenseLabel(String valor) {
			super(valor);
			setOpaque(true);
			setSize(JDialogIntellisense.this.getWidth(), 25);
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						fireValorSeleccionadoEvent(getText());
					}
				}
				
				@Override
				public void mouseEntered(MouseEvent e) {
					seleccionar();
				}
				
				@Override
				public void mouseExited(MouseEvent e) {
					deseleccionar();
				}
			});
			addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_DOWN) {
						fireTeclaArribaAbajoEvent(false);
					} else if (e.getKeyCode() == KeyEvent.VK_UP) {
						fireTeclaArribaAbajoEvent(true);
					} else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						fireValorSeleccionadoEvent(getText());
					}
				}
			});
		}
		
		public void addTeclaArribaAbajoListener(TeclaArribaAbajoEventListener listener) {
			labelListeners.add(TeclaArribaAbajoEventListener.class, listener);
		}
		
		private void fireTeclaArribaAbajoEvent(final boolean arriba) {
			final TeclaArribaAbajoEventListener[] l = labelListeners.getListeners(TeclaArribaAbajoEventListener.class);
			final TeclaArribaAbajoEventData teclaArribaABajoData = new TeclaArribaAbajoEventData();
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					for (int i = 0; i < l.length; i++) {
						if (arriba) {
							l[i].onTeclaArriba(teclaArribaABajoData);
						}else{
							l[i].onTeclaAbajo(teclaArribaABajoData);
						}
					}
				}
			});
		}
		
		private void seleccionar() {
			setBackground(Color.red.darker());
			setForeground(Color.WHITE);
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		
		private void deseleccionar() {
			setBackground(null);
			setForeground(Color.BLACK);
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	public void updateSelectedLabel() {
		getLabels().get(selectedLabel).requestFocus();
		getLabels().get(selectedLabel).seleccionar();
	}

	public List<IntellisenseLabel> getLabels() {
		return labels;
	}
}

package ar.com.textillevel.gui.modulos.abm.listaprecios;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FocusTraversalPolicy;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import ar.com.fwcommon.componentes.FWCheckBoxList;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.ventas.cotizacion.DefinicionPrecio;

public class JDialogSeleccionarDefinicionesAImprimir extends JDialog {

	private static final long serialVersionUID = -2280735973919993515L;

	private FWCheckBoxList<DefinicionPrecio> chkListDefiniciones;
	private JCheckBox chkSeleccionarTodos;
	private JButton btnAceptar;
	private JButton btnSalir;
	
	private boolean acepto;
	private List<DefinicionPrecio> definicionesAImprimir;
	
	public JDialogSeleccionarDefinicionesAImprimir(Frame padre, List<DefinicionPrecio> definiciones) {
		super(padre);
		this.definicionesAImprimir = definiciones;
		setUpComponentes();
		setUpScreen();
		getChkListDefiniciones().setValues(definiciones.toArray());
		getChkSeleccionarTodos().setSelected(true);
		getChkListDefiniciones().setAllSelectedItems(true);
		getBtnAceptar().requestFocus();
	}

	private void setUpScreen() {
		setTitle("Productos a imprimir");
		setResizable(false);
		setModal(true);
		setSize(300, 200);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		GuiUtil.centrar(this);
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}
		});
		JScrollPane jsp = new JScrollPane(getChkListDefiniciones(), JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		jsp.setPreferredSize(new Dimension(380, 200));
		
		JPanel pnlSur = new JPanel();
		pnlSur.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		pnlSur.add(getChkSeleccionarTodos());
		pnlSur.add(getBtnAceptar());
		pnlSur.add(getBtnSalir());
		
		add(jsp, BorderLayout.CENTER);
		add(pnlSur, BorderLayout.SOUTH);
		
		final Component[] comps = new Component[] { getBtnAceptar(), getBtnSalir()};
		FocusTraversalPolicy policy = new FocusTraversalPolicy() {

			List<Component> textList = Arrays.asList(comps);

			@Override
			public Component getFirstComponent(Container focusCycleRoot) {
				return comps[0];
			}

			@Override
			public Component getLastComponent(Container focusCycleRoot) {
				return comps[comps.length - 1];
			}

			@Override
			public Component getComponentAfter(Container focusCycleRoot, Component aComponent) {
				int index = textList.indexOf(aComponent);
				return comps[(index + 1) % comps.length];
			}

			@Override
			public Component getComponentBefore(Container focusCycleRoot, Component aComponent) {
				int index = textList.indexOf(aComponent);
				return comps[(index - 1 + comps.length) % comps.length];
			}

			@Override
			public Component getDefaultComponent(Container focusCycleRoot) {
				return comps[0];
			}
		};

		setFocusTraversalPolicy(policy);
	}

	private FWCheckBoxList<DefinicionPrecio> getChkListDefiniciones() {
		if (chkListDefiniciones == null) {
			chkListDefiniciones = new FWCheckBoxList<DefinicionPrecio>(){

				private static final long serialVersionUID = -3864123866954080421L;

				@Override
				public void itemListaSeleccionado(Object item, boolean seleccionado) {
					if (seleccionado) {
						DefinicionPrecio def = (DefinicionPrecio) item;
						if (!getDefinicionesAImprimir().contains(def)) {
							getDefinicionesAImprimir().add(def);
						}
					} else {
						getDefinicionesAImprimir().remove(item);
						getChkSeleccionarTodos().setSelected(false);
					}
				}
			};
		}
		return chkListDefiniciones;
	}

	private JCheckBox getChkSeleccionarTodos() {
		if(chkSeleccionarTodos == null){
			chkSeleccionarTodos = new JCheckBox("Seleccionar Todos");
			chkSeleccionarTodos.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					getChkListDefiniciones().setAllSelectedItems(true);
					for(int i = 0;i<getChkListDefiniciones().getSelectedCount();i++){
						DefinicionPrecio def =(DefinicionPrecio) getChkListDefiniciones().getItemAt(i);
						if(!getDefinicionesAImprimir().contains(def)){
							getDefinicionesAImprimir().add(def);
						}
					}
				}
			});
		}
		return chkSeleccionarTodos;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (getDefinicionesAImprimir().isEmpty()) {
						FWJOptionPane.showErrorMessage(JDialogSeleccionarDefinicionesAImprimir.this, "Debe elegir al menos un tipo de producto a imprimir", "Error");
						return;
					}
					setAcepto(true);
					dispose();
				}
			});
		}
		return btnAceptar;
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Cancelar");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					salir();
				}
				
			});
		}
		return btnSalir;
	}
	
	private void salir() {
		setAcepto(false);
		dispose();
	}
	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public List<DefinicionPrecio> getDefinicionesAImprimir() {
		return definicionesAImprimir;
	}

	public void setDefinicionesAImprimir(List<DefinicionPrecio> definicionesAImprimir) {
		this.definicionesAImprimir = definicionesAImprimir;
	}
}

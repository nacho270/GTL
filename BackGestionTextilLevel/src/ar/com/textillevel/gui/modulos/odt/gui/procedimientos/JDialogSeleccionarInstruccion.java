package ar.com.textillevel.gui.modulos.odt.gui.procedimientos;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.entidades.ventas.articulos.TipoArticulo;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.panels.PanComboConElementoOtro;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.ProcesoTipoMaquina;
import ar.com.textillevel.modulos.odt.enums.ESectorMaquina;
import ar.com.textillevel.modulos.odt.enums.ETipoInstruccionProcedimiento;

public class JDialogSeleccionarInstruccion extends JDialog {

	private static final long serialVersionUID = 6219289170260989663L;

	private JButton btnAceptar;
	private JButton btnCancelar;

	private JComboBox cmbTipoInstruccion;
	private PanComboTipoInstruccion panComboInstruccion;
	private JLabel lblDescripcionDetalladaInstruccion;

	private boolean acepto;

	private InstruccionProcedimiento instruccionActual;
	private TipoArticulo tipoArticulo;
	private final ESectorMaquina sectorMaquina;
	private ProcesoTipoMaquina proceso;

	public JDialogSeleccionarInstruccion(Dialog padre, TipoArticulo tipoArticulo, ESectorMaquina sectorMaquina, ProcesoTipoMaquina proceso) {
		super(padre);
		this.sectorMaquina = sectorMaquina;
		this.proceso = proceso;
		setTipoArticulo(tipoArticulo);
		setUpScreen();
		setUpComponentes();
	}

	public JDialogSeleccionarInstruccion(Dialog padre, TipoArticulo tipoArticulo, InstruccionProcedimiento instruccion, ESectorMaquina sectorMaquina, ProcesoTipoMaquina proceso) {
		super(padre);
		this.proceso = proceso;
		this.sectorMaquina = sectorMaquina;
		setTipoArticulo(tipoArticulo);
		setInstruccionActual(instruccion);
		setUpScreen();
		setUpComponentes();
		loadData();
	}

	private void loadData() {
		getCmbTipoInstruccion().setSelectedItem(getInstruccionActual().getTipo());
		getPanComboInstruccion().setSelectedItem(new InstruccionProcedimientoWrapper(getInstruccionActual()));
	}

	private void setUpComponentes() {
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				salir();
			}

		});
		add(getPanelNorte(), BorderLayout.CENTER);
		add(getPanelSur(), BorderLayout.SOUTH);
	}

	private JPanel getPanelNorte() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.add(new JLabel("Tipo instrucción: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
		panel.add(getCmbTipoInstruccion(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
		panel.add(getPanComboInstruccion(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 2, 1, 1, 0));
		panel.add(getLblDescripcionDetalladaInstruccion(), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 2, 1, 0, 0));
		return panel;
	}

	public JComboBox getCmbTipoInstruccion() {
		if (cmbTipoInstruccion == null) {
			cmbTipoInstruccion = new JComboBox();
			List<ETipoInstruccionProcedimiento> instruccionesList = new ArrayList<ETipoInstruccionProcedimiento>(Arrays.asList(ETipoInstruccionProcedimiento.values()));
			if(sectorMaquina == ESectorMaquina.SECTOR_TERMINADO && instruccionesList != null) {//El sector terminado no lleva instrucciones de tipo "PASADA"
				 instruccionesList.remove(ETipoInstruccionProcedimiento.PASADA);
			}
			GuiUtil.llenarCombo(cmbTipoInstruccion, instruccionesList, true);
			cmbTipoInstruccion.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						refreshPanelComboInstrucciones();
					}
				}

			});

			refreshPanelComboInstrucciones();
		}
		return cmbTipoInstruccion;
	}

	private void refreshPanelComboInstrucciones() {
		ETipoInstruccionProcedimiento tipoInstruccion = (ETipoInstruccionProcedimiento) getCmbTipoInstruccion().getSelectedItem();
		List<InstruccionProcedimiento> instruccionesByTipo = new ArrayList<InstruccionProcedimiento>();
		for(InstruccionProcedimiento ip : proceso.getInstrucciones()) {
			if(ip.getTipo() == tipoInstruccion) {
				instruccionesByTipo.add(ip);
			}
		}
		List<InstruccionProcedimientoWrapper> itemsTOs = armarTOs(instruccionesByTipo);
		itemsTOs.add(new InstruccionProcedimientoWrapper(null));
		getPanComboInstruccion().setItems(itemsTOs);
		getPanComboInstruccion().setTipoInstruccion(tipoInstruccion);
		getPanComboInstruccion().setSelectedItem(null);
	}

	private List<InstruccionProcedimientoWrapper> armarTOs(List<InstruccionProcedimiento> instruccionesBySectorAndTipo) {
		List<InstruccionProcedimientoWrapper> items = new ArrayList<JDialogSeleccionarInstruccion.InstruccionProcedimientoWrapper>();
		for(InstruccionProcedimiento i : instruccionesBySectorAndTipo) {
			items.add(new InstruccionProcedimientoWrapper(i));
		}
		return items;
	}

	private PanComboTipoInstruccion getPanComboInstruccion() {
		if(panComboInstruccion == null) {
			InstruccionProcedimientoWrapper itemOtro = new InstruccionProcedimientoWrapper(null);
			panComboInstruccion = new PanComboTipoInstruccion("Instrucciones:", new ArrayList<InstruccionProcedimientoWrapper>(), itemOtro);
		}
		return panComboInstruccion;
	}

	private JPanel getPanelSur() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel.add(getBtnAceptar());
		panel.add(getBtnCancelar());
		return panel;
	}

	private void setUpScreen() {
		setTitle("Alta/modificación de instrucciones");
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setModal(true);
		setSize(new Dimension(450, 250));
		GuiUtil.centrar(this);
	}

	public JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if (validar()) {
						setAcepto(true);
						dispose();
					}
				}

			});
		}
		return btnAceptar;
	}

	private boolean validar() {
		if(getCmbTipoInstruccion().getSelectedItem() == null) {
			return false;
		} else {
			return true;
		}
	}

	private JButton getBtnCancelar() {
		if (btnCancelar == null) {
			btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					salir();
				}

			});
		}
		return btnCancelar;
	}

	private void salir() {
		if (FWJOptionPane.showQuestionMessage(JDialogSeleccionarInstruccion.this, "Va a salir sin grabar los cambios. Esta seguro?", "Pregunta") == FWJOptionPane.YES_OPTION) {
			setAcepto(false);
			dispose();
		}
	}

	public boolean isAcepto() {
		return acepto;
	}

	public void setAcepto(boolean acepto) {
		this.acepto = acepto;
	}

	public InstruccionProcedimiento getInstruccionFinal(){
		return ((InstruccionProcedimientoWrapper)getPanComboInstruccion().getSelectedItem()).instruccion;
	}

	private InstruccionProcedimiento getInstruccionActual() {
		return instruccionActual;
	}

	private void setInstruccionActual(InstruccionProcedimiento instruccionActual) {
		this.instruccionActual = instruccionActual;
	}

	private class InstruccionProcedimientoWrapper {

		private InstruccionProcedimiento instruccion;
		
		public InstruccionProcedimientoWrapper(InstruccionProcedimiento instruccion) {
			this.instruccion = instruccion;
		}

		public String toString() {
			if(instruccion == null) {
				return "OTRO";
			} else {
				return InstruccionProcedimientoRenderer.getDescripcionInstruccion(instruccion);
			}
		}

		public String getDescrDetalladaInstruccion() {
			if(instruccion == null) {
				return "";
			} else {
				return InstruccionProcedimientoRenderer.renderInstruccionASHTML(instruccion);
			}
		}
		
		public boolean equals(Object o) {
			if(o == null) {
				return false;
			}
			if(instruccion == null) {
				return ((InstruccionProcedimientoWrapper)o).instruccion == null;
			} else {
				return instruccion.equals(((InstruccionProcedimientoWrapper)o).instruccion);
			}
		}
	
	}

	private class PanComboTipoInstruccion extends PanComboConElementoOtro<InstruccionProcedimientoWrapper> {

		private static final long serialVersionUID = 3940798857955497236L;
		private ETipoInstruccionProcedimiento tipoInstruccion;

		public PanComboTipoInstruccion(String lblCombo, List<InstruccionProcedimientoWrapper> items, InstruccionProcedimientoWrapper itemOtro) {
			super(lblCombo, items, itemOtro, true);
			addBotonModificarEventListener(new BotonModificarEventListener<InstruccionProcedimientoWrapper>() {

				@Override
				public void botonModificarPresionado(BotonModificarData<InstruccionProcedimientoWrapper> data) {
					JDialogAltaInstruccionProcedimiento dialogo = new JDialogAltaInstruccionProcedimiento(JDialogSeleccionarInstruccion.this, data.getItemAModificar().instruccion, getTipoArticulo());
					dialogo.setVisible(true);
					if(dialogo.isAcepto()) {
						InstruccionProcedimiento instruccion = dialogo.getInstruccion();
						proceso.getInstrucciones().set(data.getComboIndex(), instruccion);
						itemDistintoOtroSelected(new InstruccionProcedimientoWrapper(instruccion));
					}
				}
			});
		}

		@Override
		public InstruccionProcedimientoWrapper itemOtroSelected() {
			JDialogAltaInstruccionProcedimiento dialogo = new JDialogAltaInstruccionProcedimiento(JDialogSeleccionarInstruccion.this, sectorMaquina, tipoInstruccion, getTipoArticulo());
			dialogo.setVisible(true);
			if(dialogo.isAcepto()) {
				InstruccionProcedimiento ip = dialogo.getInstruccion();
				if(ip.getId() == null) {
					//un parche feo para permitir que funcione el equals
					ip.setId(-1*(proceso.getInstrucciones().size() + 1)); 
				}
				proceso.getInstrucciones().add(ip);
				return new InstruccionProcedimientoWrapper(ip);
			}
			return null;
		}

		public void setTipoInstruccion(ETipoInstruccionProcedimiento tipoInstruccion) {
			this.tipoInstruccion = tipoInstruccion;
		}

		@Override
		protected void itemDistintoOtroSelected(InstruccionProcedimientoWrapper itemSelected) {
			getLblDescripcionDetalladaInstruccion().setText(itemSelected.getDescrDetalladaInstruccion());
		}

	}

	private TipoArticulo getTipoArticulo() {
		return tipoArticulo;
	}

	public void setTipoArticulo(TipoArticulo tipoArticulo) {
		this.tipoArticulo = tipoArticulo;
	}

	private JLabel getLblDescripcionDetalladaInstruccion() {
		if(lblDescripcionDetalladaInstruccion == null) {
			lblDescripcionDetalladaInstruccion = new JLabel("", SwingConstants.CENTER);
			lblDescripcionDetalladaInstruccion.setBorder(BorderFactory.createTitledBorder("Detalle de la instrucción"));
			
		}
		return lblDescripcionDetalladaInstruccion;
	}

}
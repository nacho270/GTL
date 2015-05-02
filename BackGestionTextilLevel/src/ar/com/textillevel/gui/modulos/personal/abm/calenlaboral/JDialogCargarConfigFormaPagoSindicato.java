package ar.com.textillevel.gui.modulos.personal.abm.calenlaboral;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.ConfigFormaPagoSindicato;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.TotalHorasPagoDia;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.facade.api.remote.SindicatoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class JDialogCargarConfigFormaPagoSindicato extends JDialog {

	private static final long serialVersionUID = 8877955399644325580L;

	private Frame owner;
	private JButton btnAceptar;
	private JButton btnSalir;

	private JPanel panelCentral;
	private JPanel panelBotones;
	private JComboBox cmbSindicato;
	private PanTablaTotalHorasPagoDia panTablaTotalHorasPagoDia;
	
	private SindicatoFacadeRemote sindicatoFacade;
	private ConfigFormaPagoSindicato configFormaPagoSindicato;
	
	private boolean acepto;
	
	public JDialogCargarConfigFormaPagoSindicato(Frame owner, ConfigFormaPagoSindicato configFormaPagoSindicato) {
		super(owner);
		this.owner = owner;
		this.configFormaPagoSindicato = configFormaPagoSindicato;
		this.sindicatoFacade = GTLPersonalBeanFactory.getInstance().getBean2(SindicatoFacadeRemote.class);
		setUpComponentes();
		setUpScreen();
		GuiUtil.centrarEnFramePadre(this);
		setDatos();
	}

	private void setDatos() {
		getCmbSindicato().setSelectedItem(configFormaPagoSindicato.getSindicato());
		getPanTablaTotalHorasPagoDia().agregarElementos(configFormaPagoSindicato.getTotalHorasPagoPorDias());
	}

	private void setUpScreen() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Configuración de total de horas por sindicato/días");
		setResizable(false);
		setSize(new Dimension(500, 300));
		setModal(true);
	}

	private void setUpComponentes() {
		add(getPanelCentral(),BorderLayout.CENTER);
		add(getPanelBotones(),BorderLayout.SOUTH);
	}

	private JPanel getPanelCentral() {
		if(panelCentral == null){
			panelCentral = new JPanel();
			panelCentral.setLayout(new GridBagLayout());
			panelCentral.add(new JLabel("Sindicato: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getCmbSindicato(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.WEST, GridBagConstraints.NONE,new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getPanTablaTotalHorasPagoDia(),  GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 10, 10), 2, 1, 1, 0));
		}
		return panelCentral;
	}

	private JComboBox getCmbSindicato() {
		if(cmbSindicato == null) {
			cmbSindicato = new JComboBox();
			List<Sindicato> allSindicatos = sindicatoFacade.getAllOrderByName();
			GuiUtil.llenarCombo(cmbSindicato, allSindicatos, true);
			cmbSindicato.setSelectedIndex(allSindicatos.size() - 1);
		}
		return cmbSindicato;
	}

	private JPanel getPanelBotones() {
		if(panelBotones == null){
			panelBotones = new JPanel();
			panelBotones.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelBotones.add(getBtnAceptar());
			panelBotones.add(getBtnSalir());
		}
		return panelBotones;
	}

	private JButton getBtnAceptar() {
		if (btnAceptar == null) {
			btnAceptar = new JButton("Aceptar");
			btnAceptar.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					if(validar()) {
						acepto = true;
						configFormaPagoSindicato.setSindicato((Sindicato)getCmbSindicato().getSelectedItem());
						configFormaPagoSindicato.getTotalHorasPagoPorDias().clear();
						configFormaPagoSindicato.getTotalHorasPagoPorDias().addAll(getPanTablaTotalHorasPagoDia().getElementos());
						dispose();
					}
				}

			});
		}
		return btnAceptar;
	}

	private boolean validar() {
		if(getCmbSindicato().getSelectedItem() == null) {
			CLJOptionPane.showErrorMessage(owner, "Debe seleccionar un sindicato.", "Error");
			return false;
		}
		String txtValidacionTotalHorasDia = getPanTablaTotalHorasPagoDia().validar();
		if(txtValidacionTotalHorasDia != null) {
			CLJOptionPane.showErrorMessage(owner, txtValidacionTotalHorasDia, "Error");
			return false;
		}
		return true;
	}

	private JButton getBtnSalir() {
		if (btnSalir == null) {
			btnSalir = new JButton("Cancelar");
			btnSalir.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					acepto = false;
					dispose();
				}
			});
		}
		return btnSalir;
	}
	
	private PanTablaTotalHorasPagoDia getPanTablaTotalHorasPagoDia() {
		if(panTablaTotalHorasPagoDia == null) {
			panTablaTotalHorasPagoDia = new PanTablaTotalHorasPagoDia();
		}
		return panTablaTotalHorasPagoDia;
	}

	public ConfigFormaPagoSindicato getConfigFormaPagoSindicato() {
		return configFormaPagoSindicato;
	}

	private class PanTablaTotalHorasPagoDia extends PanelTabla<TotalHorasPagoDia> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 4;
		private static final int COL_DIA = 0;
		private static final int COL_TOTAL_HORAS = 1;
		private static final int COL_DISCRIMINA_EN_RS = 2;
		private static final int COL_OBJ = 3;

		public PanTablaTotalHorasPagoDia() {
			agregarBotonModificar();
		}
		
		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_DIA, "DIA", 50, 50, true);
			tabla.setMultilineColumn(COL_TOTAL_HORAS, "TOTAL DE HORAS A PAGAR", 150, true);
			tabla.setStringColumn(COL_DISCRIMINA_EN_RS, "DISCRIMINA EN EL RECIBO DE SUELDO", 200, 200, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			return tabla;
		}

		@Override
		protected void agregarElemento(TotalHorasPagoDia elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_DIA] = elemento.getDia();
			row[COL_TOTAL_HORAS] = elemento.getTotalHoras();
			row[COL_DISCRIMINA_EN_RS] = elemento.isDiscriminaEnRS() ? "SI" : "NO";
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected TotalHorasPagoDia getElemento(int fila) {
			return (TotalHorasPagoDia)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		public String validar() {
			if(getTabla().getRowCount() <= 0) {
				return "Debe definir al menos una configuración por día";
			} else {
				return null;
			}
		}

		@Override
		public boolean validarAgregar() {
			TotalHorasPagoDia totalHorasPagoDia = new TotalHorasPagoDia();
			JDialogCargarTotalHorasPagoDia dialogo = new JDialogCargarTotalHorasPagoDia(owner, totalHorasPagoDia);
			dialogo.setVisible(true);
			if(dialogo.isAcepto()) {
				agregarElemento(totalHorasPagoDia);
			}
			return false;
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			TotalHorasPagoDia totalHorasPagoDia = getElemento(filaSeleccionada);
			JDialogCargarTotalHorasPagoDia dialogo = new JDialogCargarTotalHorasPagoDia(owner, totalHorasPagoDia);
			dialogo.setVisible(true);
			if(dialogo.isAcepto()) {
				getTabla().setValueAt(totalHorasPagoDia.getDia(), filaSeleccionada, COL_DIA);
				getTabla().setValueAt(totalHorasPagoDia.getTotalHoras(), filaSeleccionada, COL_TOTAL_HORAS);
				getTabla().setValueAt(totalHorasPagoDia.isDiscriminaEnRS() ? "SI" : "NO", filaSeleccionada, COL_DISCRIMINA_EN_RS);
			}
		}

	}

	public boolean isAcepto() {
		return acepto;
	}

}
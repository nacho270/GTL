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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.ConfigFormaPagoSindicato;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.RangoDiasFeriado;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.TotalHorasPagoDia;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.facade.api.remote.SindicatoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class JDialogCargarFeriado extends JDialog {

	private static final long serialVersionUID = 8877955399644325580L;

	private JButton btnAceptar;
	private JButton btnSalir;

	private JPanel panelCentral;
	private JPanel panelBotones;

	private PanelDatePicker panFechaDesde;
	private PanelDatePicker panFechaHasta;
	private CLJTextField txtDescripcion;
	private PanDatosConfigSindicatoFeriado panDatosConfigSindicatoFeriado;
	private JCheckBox chkRedefinirHorasPorSindicato;

	private RangoDiasFeriado feriado;
	private Integer anio;
	private boolean acepto;
	private SindicatoFacadeRemote sindicatoFacade;

	public JDialogCargarFeriado(Frame owner, Integer anio, RangoDiasFeriado feriado) {
		super(owner);
		this.feriado = feriado;
		this.anio = anio;
		this.sindicatoFacade = GTLPersonalBeanFactory.getInstance().getBean2(SindicatoFacadeRemote.class);
		setUpComponentes();
		setUpScreen();
		GuiUtil.centrarEnFramePadre(this);
		setDatos();
	}

	private void setDatos() {
		getTxtDescripcion().setText(feriado.getDescripcion());
		getPanelFechaDesde().setSelectedDate(feriado.getDesde());
		getPanelFechaHasta().setSelectedDate(feriado.getHasta());
		//Preparo la lista de configuracion default
		List<ConfigFormaPagoSindicato> configDefaultList = new ArrayList<ConfigFormaPagoSindicato>();
		for(Sindicato s : sindicatoFacade.getAllOrderByName()) {
			ConfigFormaPagoSindicato config = new ConfigFormaPagoSindicato();
			config.setSindicato(s);
			TotalHorasPagoDia totalHorasPagoDia = new TotalHorasPagoDia();
			config.getTotalHorasPagoPorDias().add(totalHorasPagoDia);
			configDefaultList.add(config);
		}
		getPanDatosConfigSindicatoFeriado().setConfigDefault(configDefaultList);
		boolean noEstaRedefinidoPorSindicato = feriado.getConfigsFormasPagoSindicatos().isEmpty();
		if(noEstaRedefinidoPorSindicato) {
			getPanDatosConfigSindicatoFeriado().agregarElementos(configDefaultList);
		} else {
			getPanDatosConfigSindicatoFeriado().agregarElementos(feriado.getConfigsFormasPagoSindicatos());
		}
		getPanDatosConfigSindicatoFeriado().setModoConsulta(noEstaRedefinidoPorSindicato);
		getChkRedefinirHorasPorSindicato().setSelected(!noEstaRedefinidoPorSindicato);
	}

	private void setUpScreen() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Datos del Feriado");
		setResizable(false);
		setSize(new Dimension(520, 350));
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
			panelCentral.add(new JLabel("Descripción: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 10, 10), 1, 1, 0, 0));
			panelCentral.add(getTxtDescripcion(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL,new Insets(10, 10, 10, 10), 1, 1, 1, 0));
			panelCentral.add(getPanelFechaDesde(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 2, 1, 0, 0));
			panelCentral.add(getPanelFechaHasta(),  GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 2, 1, 0, 0));
			panelCentral.add(getChkRedefinirHorasPorSindicato(),  GenericUtils.createGridBagConstraints(0, 3,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 1, 1, 0, 0));
			panelCentral.add(getPanDatosConfigSindicatoFeriado(),  GenericUtils.createGridBagConstraints(0, 4,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 2, 1, 0, 1));
		}
		return panelCentral;
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
						capturarSetearDatos();
						acepto = true;
						dispose();
					}
				}

			});
		}
		return btnAceptar;
	}

	private void capturarSetearDatos() {
		feriado.setDescripcion(getTxtDescripcion().getText().trim());
		feriado.setDesde(DateUtil.getManiana(new Date(getPanelFechaDesde().getDate().getTime())));
		feriado.setHasta(DateUtil.getManiana(new Date(getPanelFechaHasta().getDate().getTime())));
		feriado.getConfigsFormasPagoSindicatos().clear();
		if(getChkRedefinirHorasPorSindicato().isSelected()) {
			feriado.getConfigsFormasPagoSindicatos().addAll(getPanDatosConfigSindicatoFeriado().capturarSetearDatos());
		}
	}

	private boolean validar() {
		if(StringUtil.isNullOrEmpty(getTxtDescripcion().getText())) {
			CLJOptionPane.showErrorMessage(JDialogCargarFeriado.this, "Debe completar la descripción del feriado.", "Error");
			getTxtDescripcion().requestFocus();
			return false;
		}
		java.util.Date desde = getPanelFechaDesde().getDate();
		if(desde == null) {
			CLJOptionPane.showErrorMessage(JDialogCargarFeriado.this, "Debe ingresar una fecha desde.", "Error");
			return false;
		}
		int anioDesde = DateUtil.getAnio(new Date(getPanelFechaDesde().getDate().getTime()));
		if(anioDesde != anio) {
			CLJOptionPane.showErrorMessage(JDialogCargarFeriado.this, "El año de la fecha desde es diferente a " + anio + " .", "Error");
			return false;
		}
		java.util.Date hasta = getPanelFechaHasta().getDate();
		if(hasta == null) {
			CLJOptionPane.showErrorMessage(JDialogCargarFeriado.this, "Debe ingresar una fecha hasta.", "Error");
			return false;
		}
		int anioHasta = DateUtil.getAnio(new Date(getPanelFechaHasta().getDate().getTime()));
		if(anioHasta != anio) {
			CLJOptionPane.showErrorMessage(JDialogCargarFeriado.this, "El año de la fecha hasta es diferente a " + anio + " .", "Error");
			return false;
		}
		if(desde.after(hasta)) {
			CLJOptionPane.showErrorMessage(JDialogCargarFeriado.this, "La fecha desde debe ser menor o igual a la fecha hasta.", "Error");
			return false;
		}
		String txtValidar = getPanDatosConfigSindicatoFeriado().validarElementos();
		if(getChkRedefinirHorasPorSindicato().isSelected() && !StringUtil.isNullOrEmpty(txtValidar)) {
			CLJOptionPane.showErrorMessage(JDialogCargarFeriado.this, txtValidar, "Error");
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

	private CLJTextField getTxtDescripcion() {
		if(txtDescripcion == null) {
			txtDescripcion = new CLJTextField();
		}
		return txtDescripcion;
	}

	private PanelDatePicker getPanelFechaDesde() {
		if(panFechaDesde == null){
			panFechaDesde = new PanelDatePicker();
			panFechaDesde.setCaption("Desde: ");
		}
		return panFechaDesde;
	}
	
	private PanelDatePicker getPanelFechaHasta() {
		if(panFechaHasta == null){
			panFechaHasta = new PanelDatePicker();
			panFechaHasta.setCaption("Hasta: ");
		}
		return panFechaHasta;
	}

	private JCheckBox getChkRedefinirHorasPorSindicato() {
		if(chkRedefinirHorasPorSindicato == null) {
			chkRedefinirHorasPorSindicato = new JCheckBox("Redefinir horas por sindicato");
			chkRedefinirHorasPorSindicato.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED) {
						getPanDatosConfigSindicatoFeriado().setModoConsulta(false);
					} else {
						getPanDatosConfigSindicatoFeriado().setModoConsulta(true);
						getPanDatosConfigSindicatoFeriado().restart();
					}
				}

			});
		}
		return chkRedefinirHorasPorSindicato;
	}


	
	private PanDatosConfigSindicatoFeriado getPanDatosConfigSindicatoFeriado() {
		if(panDatosConfigSindicatoFeriado == null) {
			panDatosConfigSindicatoFeriado = new PanDatosConfigSindicatoFeriado();
		}
		return panDatosConfigSindicatoFeriado;
	}

	public boolean isAcepto() {
		return acepto;
	}

	private class PanDatosConfigSindicatoFeriado extends PanelTabla<ConfigFormaPagoSindicato> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 4;
		private static final int COL_SINDICATO = 0;
		private static final int COL_CANT_HORAS = 1;
		private static final int COL_DISCRIMINA_RS = 2;
		private static final int COL_OBJ = 3;

		private List<ConfigFormaPagoSindicato> configDefault;
		
		public PanDatosConfigSindicatoFeriado() {
			setBorder(BorderFactory.createTitledBorder("Horas a Pagar por Sindicato"));
			getBotonAgregar().setVisible(false);
			getBotonEliminar().setVisible(false);
		}

		public List<ConfigFormaPagoSindicato> capturarSetearDatos() {
			List<ConfigFormaPagoSindicato> result = new ArrayList<ConfigFormaPagoSindicato>();
			for(int i = 0; i < getTabla().getRowCount(); i++) {
				ConfigFormaPagoSindicato elemento = getElemento(i);
				Integer cantHoras = (Integer)getTabla().getTypedValueAt(i, COL_CANT_HORAS);
				Boolean discriminaEnRS = (Boolean)getTabla().getValueAt(i, COL_DISCRIMINA_RS);
				TotalHorasPagoDia totalHorasPagoPorDias = elemento.getTotalHorasPagoPorDias().get(0);
				totalHorasPagoPorDias.setDiscriminaEnRS(discriminaEnRS);
				totalHorasPagoPorDias.setTotalHoras(cantHoras);
				result.add(elemento);
			}
			return result;
		}

		public void restart() {
			getTabla().removeAllRows();
			agregarElementos(getConfigDefault());
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_SINDICATO, "SINDICATO", 100, 100, true);
			tabla.setIntColumn(COL_CANT_HORAS, "CANTIDAD DE HORAS A PAGAR", 170, false);
			tabla.setCheckColumn(COL_DISCRIMINA_RS, "DISCRIMINA EN EL REC. SUELDO", 170, false);
			tabla.setStringColumn(COL_OBJ, "", 0);
			return tabla;
		}

//		public String validar() {
//			List<ConfigFormaPagoSindicato> elementos = getElementos();
//			List<Sindicato> sindicatos = new ArrayList<Sindicato>();
//			for(ConfigFormaPagoSindicato c : elementos) {
//				sindicatos.add(c.Sindicato());
//			}
//			if(new HashSet<Sindicato>(sindicatos).size() != sindicatos.size()) {
//				return "Sólo puede haber una configuración por sindicato";
//			} else {
//				return null;
//			}
			//TODO:
//			return null;
//		}

		@Override
		protected void agregarElemento(ConfigFormaPagoSindicato elemento) {
			Object[] row = new Object[CANT_COLS];
			TotalHorasPagoDia totalHorasPagoDia = elemento.getTotalHorasPagoPorDias().get(0);
			row[COL_SINDICATO] = elemento.getSindicato();
			row[COL_CANT_HORAS] = totalHorasPagoDia.getTotalHoras();
			row[COL_DISCRIMINA_RS] = totalHorasPagoDia.isDiscriminaEnRS();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected ConfigFormaPagoSindicato getElemento(int fila) {
			return (ConfigFormaPagoSindicato)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			Integer cantHoras = (Integer)getTabla().getTypedValueAt(fila, COL_CANT_HORAS);
			if(cantHoras == null || cantHoras <= 0) {
				return "La cantidad de horas debe ser mayor a cero.";
			}
			return null;
		}

		private List<ConfigFormaPagoSindicato> getConfigDefault() {
			return configDefault;
		}

		public void setConfigDefault(List<ConfigFormaPagoSindicato> configDefault) {
			this.configDefault = configDefault;
		}

	}

}
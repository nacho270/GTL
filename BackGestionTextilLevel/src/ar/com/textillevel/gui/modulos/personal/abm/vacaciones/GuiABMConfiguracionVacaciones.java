package ar.com.textillevel.gui.modulos.personal.abm.vacaciones;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJNumericTextField;
import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.vacaciones.ConfiguracionVacaciones;
import ar.com.textillevel.modulos.personal.entidades.vacaciones.PeriodoVacaciones;
import ar.com.textillevel.modulos.personal.facade.api.remote.ConfiguracionVacacionesFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class GuiABMConfiguracionVacaciones extends GuiABMListaTemplate {

	private static final long serialVersionUID = 7155215670634215242L;

	private JPanel tabDetalle;
	private JPanel panDetalle;
	private JPanel panelCentro;
	
	private PanelTablaPeriodosVacaciones panelTabla;
	private FWJNumericTextField txtMesesMinimos;
	
	private PanelDatePicker panelFechaDesde;
	
	private ConfiguracionVacacionesFacadeRemote confFacade;
	
	private ConfiguracionVacaciones configuracionActual;
	
	public GuiABMConfiguracionVacaciones(Integer idModulo) {
		super();
		setHijoCreado(true);
		setTitle("Configuración de vacaciones");
		constructGui();
		setEstadoInicial();
	}
	
	private void constructGui() {
		panTabs.addTab("Información", getTabDetalle());
	}

	private JPanel getTabDetalle() {
		if (tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new BorderLayout());
			tabDetalle.add(getPanDetalle(), BorderLayout.NORTH);
		}
		return tabDetalle;
	}

	private JPanel getPanDetalle() {
		if (panDetalle == null) {
			panDetalle = new JPanel();

			JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
			panelNorte.add(getPanelFechaDesde());

			panDetalle.add(panelNorte, BorderLayout.NORTH);
			panDetalle.add(getPanelCentro(), BorderLayout.CENTER);
		}
		return panDetalle;
	}
	
	public JPanel getPanelCentro() {
		if(panelCentro == null){
			panelCentro = new JPanel(new GridBagLayout());
			panelCentro.add(new JLabel("Meses minimos: "),GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			panelCentro.add(getTxtMesesMinimos(),GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 1, 0));
			panelCentro.add(getPanelTabla(),GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 2, 2, 1, 1));
		}
		return panelCentro;
	}
	
	public PanelDatePicker getPanelFechaDesde() {
		if (panelFechaDesde == null) {
			panelFechaDesde = new PanelDatePicker();
			panelFechaDesde.setCaption("Desde: ");
			panelFechaDesde.setSelectedDate(DateUtil.getHoy());
		}
		return panelFechaDesde;
	}
	
	private class PanelTablaPeriodosVacaciones extends PanelTabla<PeriodoVacaciones> {

		private static final long serialVersionUID = 3686822389075743217L;

		private static final int CANT_COLS = 3;
		private static final int COL_ANIOS = 0;
		private static final int COL_DIAS = 1;
		private static final int COL_OBJ = 2;
		
		public PanelTablaPeriodosVacaciones() {
			agregarBotonModificar();
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setIntColumn(COL_ANIOS, "Antigüedad", 70, true);
			tabla.setIntColumn(COL_DIAS, "Días", 70, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			return tabla;
		}

		@Override
		protected void agregarElemento(PeriodoVacaciones elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_ANIOS] = elemento.getAntiguedadAniosRequerida();
			row[COL_DIAS] = elemento.getCantidadDias();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected PeriodoVacaciones getElemento(int fila) {
			return (PeriodoVacaciones)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}
		
		@Override
		public boolean validarAgregar() {
			JDialogInputDiasYAnios jDialogInputDiasYAnios = new JDialogInputDiasYAnios(GuiABMConfiguracionVacaciones.this.getFrame(),getConfiguracionActual().getPeriodos());
			jDialogInputDiasYAnios.setVisible(true);
			if(jDialogInputDiasYAnios.isAcepto()){
				getConfiguracionActual().getPeriodos().add(jDialogInputDiasYAnios.getPeriodoVacacionesActual());
				refreshTable();
			}
			return false;
		}
		
		@Override
		public boolean validarQuitar() {
			int filaSeleccionada = getTabla().getSelectedRow();
			if(filaSeleccionada!=-1){
				getConfiguracionActual().getPeriodos().remove(filaSeleccionada);
				refreshTable();
			}
			return false;
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			if(filaSeleccionada!=-1){
				PeriodoVacaciones p = getElemento(filaSeleccionada);
				JDialogInputDiasYAnios jDialogInputDiasYAnios = new JDialogInputDiasYAnios(GuiABMConfiguracionVacaciones.this.getFrame(),p,getConfiguracionActual().getPeriodos());
				jDialogInputDiasYAnios.setVisible(true);
				if(jDialogInputDiasYAnios.isAcepto()){
					getConfiguracionActual().getPeriodos().set(filaSeleccionada, jDialogInputDiasYAnios.getPeriodoVacacionesActual());
					refreshTable();
				}
			}
		}
	}
	
	private void refreshTable(){
		getPanelTabla().limpiar();
		Collections.sort(getConfiguracionActual().getPeriodos(), new Comparator<PeriodoVacaciones>() {
			public int compare(PeriodoVacaciones o1, PeriodoVacaciones o2) {
				return o1.getAntiguedadAniosRequerida().compareTo(o2.getAntiguedadAniosRequerida());
			}
		});
		for(PeriodoVacaciones p : getConfiguracionActual().getPeriodos()){
			getPanelTabla().agregarElemento(p);
		}
	}

	public ConfiguracionVacaciones getConfiguracionActual() {
		return configuracionActual;
	}

	public void setConfiguracionActual(ConfiguracionVacaciones configuracionActual) {
		if(configuracionActual==null){
			configuracionActual = new ConfiguracionVacaciones();
		}
		this.configuracionActual = configuracionActual;
	}
	
	public PanelTablaPeriodosVacaciones getPanelTabla() {
		if(panelTabla == null){
			panelTabla = new PanelTablaPeriodosVacaciones();
		}
		return panelTabla;
	}

	public FWJNumericTextField getTxtMesesMinimos() {
		if(txtMesesMinimos==null){
			txtMesesMinimos = new FWJNumericTextField(0, 99);
			if(getConfiguracionActual()!=null && getConfiguracionActual().getMesesMinimosParaEntrar()!=null){
				txtMesesMinimos.setValue(getConfiguracionActual().getMesesMinimosParaEntrar().longValue());
			}
		}
		return txtMesesMinimos;
	}
	
	@Override
	public void cargarLista() {
		List<ConfiguracionVacaciones> confs = getConfFacade().getAll();
		lista.clear();
		if (confs != null) {
			for (ConfiguracionVacaciones c : confs) {
				lista.addItem(c);
			}
		}
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		cargarLista();
		if (lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setConfiguracionActual(new ConfiguracionVacaciones());		
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar la configuración seleccionada?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				getConfFacade().remove(getConfiguracionActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if (nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getPanelFechaDesde().requestFocus();
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()){
			capturarDatos();
			ConfiguracionVacaciones conf = getConfFacade().save(getConfiguracionActual());
			FWJOptionPane.showInformationMessage(GuiABMConfiguracionVacaciones.this.getFrame(), "La configuración se ha guardado exitosamente", "Información");
			lista.setSelectedValue(conf, true);
			return true;
		}
		return false;
	}
	
	private void capturarDatos() {
		getConfiguracionActual().setMesesMinimosParaEntrar(getTxtMesesMinimos().getValue());
		getConfiguracionActual().setFechaVigencia(new java.sql.Date(getPanelFechaDesde().getDate().getTime()));
		Collections.sort(getConfiguracionActual().getPeriodos(), new Comparator<PeriodoVacaciones>() {
			public int compare(PeriodoVacaciones o1, PeriodoVacaciones o2) {
				return o1.getAntiguedadAniosRequerida().compareTo(o2.getAntiguedadAniosRequerida());
			}
		});
	}
	
	private boolean validar() {
		if(getTxtMesesMinimos().getValueWithNull()==null){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar la cantidad de meses mínima", "Error");
			getTxtMesesMinimos().requestFocus();
			return false;
		}
		if(getConfiguracionActual().getPeriodos().isEmpty()){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar al menos un periodo", "Error");
			return false;
		}
		return true;
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
	}

	@Override
	public void limpiarDatos() {
		getTxtMesesMinimos().setText("");
		getPanelTabla().limpiar();
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		setConfiguracionActual((ConfiguracionVacaciones) lista.getSelectedValue());
		limpiarDatos();
		if (getConfiguracionActual() != null) {
			llenarDatos();
		}
	}

	private void llenarDatos() {
		getTxtMesesMinimos().setValue(getConfiguracionActual().getMesesMinimosParaEntrar().longValue());
		refreshTable();
		getPanelFechaDesde().setSelectedDate(getConfiguracionActual().getFechaVigencia());
	}

	@Override
	public void itemComboMaestroSeleccionado() {
		
	}
	
	public ConfiguracionVacacionesFacadeRemote getConfFacade() {
		if(confFacade == null){
			confFacade = GTLPersonalBeanFactory.getInstance().getBean2(ConfiguracionVacacionesFacadeRemote.class);
		}
		return confFacade;
	}
}

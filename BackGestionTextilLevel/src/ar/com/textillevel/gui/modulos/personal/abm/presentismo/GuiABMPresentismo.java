package ar.com.textillevel.gui.modulos.personal.abm.presentismo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.presentismo.ConfiguracionPresentismo;
import ar.com.textillevel.modulos.personal.entidades.presentismo.DescuentoPresentismo;
import ar.com.textillevel.modulos.personal.entidades.presentismo.DescuentoPresentismoPorAusencia;
import ar.com.textillevel.modulos.personal.entidades.presentismo.DescuentoPresentismoRangoMinutos;
import ar.com.textillevel.modulos.personal.facade.api.remote.ConfiguracionPresentismoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.SindicatoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class GuiABMPresentismo extends GuiABMListaTemplate {

	private static final long serialVersionUID = 7319311006695546888L;

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private PanelDatePicker panelFechaDesde;
	private PanelTablaDescuentoPresentismoAusencia tablaDescuentoAusencia;
	private PanelTablaDescuentoPresentismoRangoMinutos tablaDescuentoMinutos;
	private FWJTextField txtPorcentajePremio;

	private ConfiguracionPresentismo configuracionPresentismoActual;
	private List<DescuentoPresentismoRangoMinutos> descuentosPorMinutosCargados;
	private List<DescuentoPresentismoPorAusencia> descuentosPorAusenciaCargados;
	private ConfiguracionPresentismoFacadeRemote confPresentismoFacade;

	public GuiABMPresentismo(Integer idModulo) {
		super();
		setHijoCreado(true);
		setTitle("Configuración de Antigüedades");
		setDescuentosPorAusenciaCargados(new ArrayList<DescuentoPresentismoPorAusencia>());
		setDescuentosPorMinutosCargados(new ArrayList<DescuentoPresentismoRangoMinutos>());
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
			JPanel pnlTablas = new JPanel(new GridBagLayout());
			pnlTablas.add(getPanelFechaDesde(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			pnlTablas.add(new JLabel("Premio por presentismo: "), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 1, 1, 0, 0));
			pnlTablas.add(getTxtPorcentajePremio(), GenericUtils.createGridBagConstraints(2, 0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 1, 1, 0.3, 0));
			pnlTablas.add(getTablaDescuentoAusencia(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 3, 1, 1, 0));
			pnlTablas.add(getTablaDescuentoMinutos(), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(5, 5, 5, 5), 3, 1, 1, 0));

			panDetalle.add(pnlTablas, BorderLayout.CENTER);
		}
		return panDetalle;
	}

	@Override
	public void cargarLista() {
		Sindicato sindicatoSeleccionado = (Sindicato) getItemComboMaestroSeleccionado();
		if (sindicatoSeleccionado != null) {
			List<ConfiguracionPresentismo> confs = getConfPresentismoFacade().getAllBySindicato(sindicatoSeleccionado);
			lista.clear();
			if (confs != null) {
				for (ConfiguracionPresentismo c : confs) {
					lista.addItem(c);
				}
			}
		}
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicionTemplate(false);
		setContenidoComboMaestro(GTLPersonalBeanFactory.getInstance().getBean2(SindicatoFacadeRemote.class).getAllOrderByName(), "Sindicato: ");
		cargarLista();
		if (lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		Sindicato sindicato = (Sindicato) getItemComboMaestroSeleccionado();
		ConfiguracionPresentismo conf = new ConfiguracionPresentismo();
		conf.setSindicato(sindicato);
		setConfiguracionPresentismoActual(conf);
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if (lista.getSelectedIndex() >= 0) {
			if (FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar la configuración para el sindicato seleccionado?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				getConfPresentismoFacade().remove(getConfiguracionPresentismoActual());
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
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar un sindicato", "Error");
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
		if (validar()) {
			capturarDatos();
			ConfiguracionPresentismo conf = getConfPresentismoFacade().save(getConfiguracionPresentismoActual());
			FWJOptionPane.showInformationMessage(getFrame(), "La configuración se ha grabado con éxito", "Información");
			lista.setSelectedValue(conf, true);
			return true;
		}
		return false;
	}

	private void capturarDatos() {
		getConfiguracionPresentismoActual().getDescuentos().clear();
		getConfiguracionPresentismoActual().getDescuentos().addAll(getDescuentosPorAusenciaCargados());
		getConfiguracionPresentismoActual().getDescuentos().addAll(getDescuentosPorMinutosCargados());
		getConfiguracionPresentismoActual().setFecha(new java.sql.Date(getPanelFechaDesde().getDate().getTime()));
		getConfiguracionPresentismoActual().setPorcentajeTotal(new BigDecimal(GenericUtils.getDoubleValue(getTxtPorcentajePremio().getText())));
	}

	private boolean validar() {
		if(getTxtPorcentajePremio().getText().trim().length() == 0){
			FWJOptionPane.showErrorMessage(getFrame(), "Debe ingresar el porcentaje de premio por defecto", "Error");
			getTxtPorcentajePremio().requestFocus();
			return false;
		}
		if (getDescuentosPorAusenciaCargados().isEmpty()) {
			if (FWJOptionPane.showQuestionMessage(getFrame(), "Advertencia: No ha cargado descuentos por ausencia. Desea continuar", "Pregunta") == FWJOptionPane.NO_OPTION) {
				return false;
			}
		}
		if (getDescuentosPorMinutosCargados().isEmpty()) {
			if (FWJOptionPane.showQuestionMessage(getFrame(), "Advertencia: No ha cargado descuentos por rango de minutos. Desea continuar", "Pregunta") == FWJOptionPane.NO_OPTION) {
				return false;
			}
		}
		if(getConfiguracionPresentismoActual().getId()!=null && !getConfiguracionPresentismoActual().getFecha().equals(new java.sql.Date(getPanelFechaDesde().getDate().getTime()))){
			ConfiguracionPresentismo confPost = getConfPresentismoFacade().getConfiguracionPosteriorBySindicato(getConfiguracionPresentismoActual().getSindicato(), new java.sql.Date(getPanelFechaDesde().getDate().getTime()));
			if (confPost != null) {
				FWJOptionPane.showErrorMessage(getFrame(), "Existe una configuración con la fecha " + DateUtil.dateToString(confPost.getFecha()) + ". Por favor, elija una fecha posterior.", "Error");
				return false;
			}
		}
		return true;
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
	}

	@Override
	public void limpiarDatos() {
		getDescuentosPorAusenciaCargados().clear();
		getDescuentosPorMinutosCargados().clear();
		getTablaDescuentoMinutos().refreshTable();
		getTablaDescuentoAusencia().refreshTable();
		getTxtPorcentajePremio().setText("");
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		setConfiguracionPresentismoActual((ConfiguracionPresentismo) lista.getSelectedValue());
		limpiarDatos();
		if (getConfiguracionPresentismoActual() != null) {
			llenarDatos();
		}
	}

	private void llenarDatos() {
		if (getConfiguracionPresentismoActual().getDescuentos() != null && !getConfiguracionPresentismoActual().getDescuentos().isEmpty()) {
			for (DescuentoPresentismo d : getConfiguracionPresentismoActual().getDescuentos()) {
				if (d instanceof DescuentoPresentismoPorAusencia) {
					getDescuentosPorAusenciaCargados().add((DescuentoPresentismoPorAusencia) d);
				} else {
					getDescuentosPorMinutosCargados().add((DescuentoPresentismoRangoMinutos) d);
				}
			}
		}
		getPanelFechaDesde().setSelectedDate(getConfiguracionPresentismoActual().getFecha());
		getTablaDescuentoAusencia().refreshTable();
		getTablaDescuentoMinutos().refreshTable();
		getTxtPorcentajePremio().setText(GenericUtils.getDecimalFormat().format(getConfiguracionPresentismoActual().getPorcentajeTotal()));
	}

	@Override
	public void itemComboMaestroSeleccionado() {
		cargarLista();
	}

	public PanelDatePicker getPanelFechaDesde() {
		if (panelFechaDesde == null) {
			panelFechaDesde = new PanelDatePicker();
			panelFechaDesde.setCaption("Válido desde: ");
		}
		return panelFechaDesde;
	}

	public ConfiguracionPresentismoFacadeRemote getConfPresentismoFacade() {
		if (confPresentismoFacade == null) {
			confPresentismoFacade = GTLPersonalBeanFactory.getInstance().getBean2(ConfiguracionPresentismoFacadeRemote.class);
		}
		return confPresentismoFacade;
	}

	public ConfiguracionPresentismo getConfiguracionPresentismoActual() {
		return configuracionPresentismoActual;
	}

	public void setConfiguracionPresentismoActual(ConfiguracionPresentismo configuracionPresentismoActual) {
		this.configuracionPresentismoActual = configuracionPresentismoActual;
	}

	private class PanelTablaDescuentoPresentismoAusencia extends PanelTabla<DescuentoPresentismoPorAusencia> {

		private static final long serialVersionUID = 4955456353927859667L;

		private static final int CANT_COLS = 3;
		private static final int COL_CANT_FALTAS = 0;
		private static final int COL_PORC_DESCUENTO = 1;
		private static final int COL_OBJ = 2;

		public PanelTablaDescuentoPresentismoAusencia() {
			super();
			setBorder(BorderFactory.createTitledBorder("Descuentos por ausencia"));
			setPreferredSize(new Dimension(600, 200));
			agregarBotonModificar();
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setIntColumn(COL_CANT_FALTAS, "Faltas", 100, true);
			tabla.setStringColumn(COL_PORC_DESCUENTO, "Descuento", 100, 100, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			return tabla;
		}

		@Override
		protected void agregarElemento(DescuentoPresentismoPorAusencia elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_CANT_FALTAS] = elemento.getCantidadFaltas();
			row[COL_PORC_DESCUENTO] = GenericUtils.getDecimalFormat().format(elemento.getPorcentajeDescuento()) + "%";
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected DescuentoPresentismoPorAusencia getElemento(int fila) {
			return (DescuentoPresentismoPorAusencia) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarAgregar() {
			JDialogAgregarModificarDescuentoPorFalta dialog = new JDialogAgregarModificarDescuentoPorFalta(getFrame(), getDescuentosPorAusenciaCargados());
			dialog.setVisible(true);
			if (dialog.isAcepto()) {
				getDescuentosPorAusenciaCargados().add(dialog.getDescuentoActual());
				refreshTable();
			}
			return false;
		}

		private void refreshTable() {
			limpiar();
			for (DescuentoPresentismoPorAusencia d : getDescuentosPorAusenciaCargados()) {
				agregarElemento(d);
			}
		}

		@Override
		public boolean validarQuitar() {
			int filaSeleccionada = getTabla().getSelectedRow();
			getDescuentosPorAusenciaCargados().remove(filaSeleccionada);
			refreshTable();
			return true;
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			if (filaSeleccionada > -1) {
				JDialogAgregarModificarDescuentoPorFalta dialog = new JDialogAgregarModificarDescuentoPorFalta(getFrame(), getDescuentosPorAusenciaCargados(), getElemento(filaSeleccionada));
				dialog.setVisible(true);
				if (dialog.isAcepto()) {
					getDescuentosPorAusenciaCargados().set(filaSeleccionada, dialog.getDescuentoActual());
					refreshTable();
				}
			}
		}
	}

	private class PanelTablaDescuentoPresentismoRangoMinutos extends PanelTabla<DescuentoPresentismoRangoMinutos> {

		private static final long serialVersionUID = -3599770123031945237L;

		private static final int CANT_COLS = 3;
		private static final int COL_RANGO_MINUTOS = 0;
		private static final int COL_PORC_DESCUENTO = 1;
		private static final int COL_OBJ = 2;

		public PanelTablaDescuentoPresentismoRangoMinutos() {
			super();
			setBorder(BorderFactory.createTitledBorder("Descuentos por llegadas tarde"));
			setPreferredSize(new Dimension(600, 200));
			agregarBotonModificar();
		}

		private void refreshTable() {
			limpiar();
			for (DescuentoPresentismoRangoMinutos d : getDescuentosPorMinutosCargados()) {
				agregarElemento(d);
			}
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_RANGO_MINUTOS, "Rango", 250, 250, true);
			tabla.setStringColumn(COL_PORC_DESCUENTO, "Descuento", 100, 100, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			tabla.setReorderingAllowed(false);
			return tabla;
		}

		@Override
		protected void agregarElemento(DescuentoPresentismoRangoMinutos elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_RANGO_MINUTOS] = "De " + elemento.getMinutosDesde() + " a " + elemento.getMinutosHasta() + " minutos.";
			row[COL_PORC_DESCUENTO] = GenericUtils.getDecimalFormat().format(elemento.getPorcentajeDescuento()) + "%";
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected DescuentoPresentismoRangoMinutos getElemento(int fila) {
			return (DescuentoPresentismoRangoMinutos) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarAgregar() {
			JDialogAgregarModificarDescuentoPorRangoMinutos dialog = new JDialogAgregarModificarDescuentoPorRangoMinutos(getFrame(), getDescuentosPorMinutosCargados());
			dialog.setVisible(true);
			if (dialog.isAcepto()) {
				getDescuentosPorMinutosCargados().add(dialog.getDescuentoActual());
				refreshTable();
			}
			return false;
		}

		@Override
		public boolean validarQuitar() {
			int filaSeleccionada = getTabla().getSelectedRow();
			getDescuentosPorAusenciaCargados().remove(filaSeleccionada);
			refreshTable();
			return true;
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			if (filaSeleccionada > -1) {
				JDialogAgregarModificarDescuentoPorRangoMinutos dialog = new JDialogAgregarModificarDescuentoPorRangoMinutos(getFrame(), getDescuentosPorMinutosCargados(),
						getElemento(filaSeleccionada));
				dialog.setVisible(true);
				if (dialog.isAcepto()) {
					getDescuentosPorMinutosCargados().set(filaSeleccionada, dialog.getDescuentoActual());
					refreshTable();
				}
			}
		}
	}

	public PanelTablaDescuentoPresentismoAusencia getTablaDescuentoAusencia() {
		if (tablaDescuentoAusencia == null) {
			tablaDescuentoAusencia = new PanelTablaDescuentoPresentismoAusencia();
		}
		return tablaDescuentoAusencia;
	}

	public PanelTablaDescuentoPresentismoRangoMinutos getTablaDescuentoMinutos() {
		if (tablaDescuentoMinutos == null) {
			tablaDescuentoMinutos = new PanelTablaDescuentoPresentismoRangoMinutos();
		}
		return tablaDescuentoMinutos;
	}

	public List<DescuentoPresentismoRangoMinutos> getDescuentosPorMinutosCargados() {
		return descuentosPorMinutosCargados;
	}

	public void setDescuentosPorMinutosCargados(List<DescuentoPresentismoRangoMinutos> descuentosPorMinutosCargados) {
		this.descuentosPorMinutosCargados = descuentosPorMinutosCargados;
	}

	public List<DescuentoPresentismoPorAusencia> getDescuentosPorAusenciaCargados() {
		return descuentosPorAusenciaCargados;
	}

	public void setDescuentosPorAusenciaCargados(List<DescuentoPresentismoPorAusencia> descuentosPorAusenciaCargados) {
		this.descuentosPorAusenciaCargados = descuentosPorAusenciaCargados;
	}

	public FWJTextField getTxtPorcentajePremio() {
		if(txtPorcentajePremio == null){
			txtPorcentajePremio = new FWJTextField();
		}
		return txtPorcentajePremio;
	}

}

package ar.com.textillevel.gui.modulos.personal.abm.tareas;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.GuiUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Categoria;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.CategoriaValorPuesto;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ConfiguracionValorHoraCategoria;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ValorPuesto;
import ar.com.textillevel.modulos.personal.facade.api.remote.ConfiguracionVHCategoriaFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.SindicatoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class GuiABMConfigValorHoraCategoria extends GuiABMListaTemplate {

	private static final long serialVersionUID = 5490390741310236258L;

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private PanelDatePicker panFechaDesde;
	private PanelDatePicker panFechaHasta;
	
	private PanelTablaCategoriaValorPuesto panTablaCategoriaValorPuesto;

	private SindicatoFacadeRemote sindicatoFacade;
	private ConfiguracionVHCategoriaFacadeRemote configFacade;
	
	private ConfiguracionValorHoraCategoria configActual;

	public GuiABMConfigValorHoraCategoria(Integer idModulo){
		super();
		setHijoCreado(true);
		setTitle("Configurar Valor Hora por Puesto");
		constructGui();
		setEstadoInicial();
	}
	
	private void constructGui() {
		panTabs.addTab("Información de la Configuración", getTabDetalle());		
	}
	
	private JPanel getTabDetalle() {
		if(tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new BorderLayout());
			tabDetalle.add(getPanDetalle(), BorderLayout.NORTH);
		}
		return tabDetalle;
	}
	
	private JPanel getPanDetalle() {
		if(panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(getPanFechaDesde(), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getPanFechaHasta(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getPanTablaCategoriaValorPuesto(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
		}
		return panDetalle;
	}
	
	private PanelTablaCategoriaValorPuesto getPanTablaCategoriaValorPuesto() {
		if(panTablaCategoriaValorPuesto == null) {
			panTablaCategoriaValorPuesto = new PanelTablaCategoriaValorPuesto();
		}
		return panTablaCategoriaValorPuesto;
	}

	@Override
	public void cargarLista() {
		Sindicato sindicatoSeleccionado = (Sindicato)getItemComboMaestroSeleccionado();
		if(sindicatoSeleccionado != null) {
			getPanTablaCategoriaValorPuesto().setSindicato(sindicatoSeleccionado);
			List<ConfiguracionValorHoraCategoria> configList = getConfigFacade().getAllByIdSindicato(sindicatoSeleccionado.getId());
			lista.clear();
			for(ConfiguracionValorHoraCategoria c : configList) {
				lista.addItem(c);
			}
		}
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		setContenidoComboMaestro(getSindicatoFacade().getAllOrderByName(), "Sindicato: ");
		cargarLista();
		if(lista.getModel().getSize() > 0) {
			lista.setSelectedIndex(0);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		Sindicato sindicato = (Sindicato)getItemComboMaestroSeleccionado();
		ConfiguracionValorHoraCategoria config = new ConfiguracionValorHoraCategoria();
		config.setSindicato(sindicato);
		setConfigActual(config);
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(CLJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar la Configuración seleccionada?", "Confirmación") == CLJOptionPane.YES_OPTION) {
				getConfigFacade().remove(getConfigActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if(nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			return true;
		} else {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar una Configuración", "Error");
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
		if(validar()) {
			capturarSetearDatos();
			ConfiguracionValorHoraCategoria config = getConfigFacade().save(getConfigActual());
			lista.setSelectedValue(config, true);
			return true;
		}
		return false;
	}

	private boolean validar() {
		if(getPanFechaDesde().getDate() == null){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar la fecha desde.", "Error");
			return false;
		}
		if(getPanFechaHasta().getDate() == null){
			CLJOptionPane.showErrorMessage(this, "Debe ingresar la fecha hasta.", "Error");
			return false;
		}
		if(getPanTablaCategoriaValorPuesto().getElementos().isEmpty()) {
			CLJOptionPane.showErrorMessage(this, "Debe definir al menos un valor de hora para un puesto.", "Error");
			return false;
		}
		Sindicato s = (Sindicato)getItemComboMaestroSeleccionado();
		ConfiguracionValorHoraCategoria config = getConfigFacade().getConfigByFechaAndSindicato(new Date(getPanFechaDesde().getDate().getTime()), s.getId());
		if(config!= null && !config.getId().equals(configActual.getId())) {
			CLJOptionPane.showErrorMessage(this, StringW.wordWrap("Ya existe una configuración que se solapa con la fecha desde ingresada."), "Error");
			return false;
		}
		config = getConfigFacade().getConfigByFechaAndSindicato(new Date(getPanFechaHasta().getDate().getTime()), s.getId());
		if(config!= null && !config.getId().equals(configActual.getId())) {
			CLJOptionPane.showErrorMessage(this, StringW.wordWrap("Ya existe una configuración que se solapa con la fecha hasta ingresada."), "Error");
			return false;
		}
		return true;
	}

	private void capturarSetearDatos() {
		getConfigActual().setFechaDesde(new Date(getPanFechaDesde().getDate().getTime()));
		getConfigActual().setFechaHasta(new Date(getPanFechaHasta().getDate().getTime()));
		getConfigActual().getCategoriasValoresPuesto().clear();
		getConfigActual().getCategoriasValoresPuesto().addAll(getPanTablaCategoriaValorPuesto().getElementos());
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
		getPanTablaCategoriaValorPuesto().setModoConsulta(!estado);
	}

	@Override
	public void limpiarDatos() {
		getPanFechaDesde().clearFecha();
		getPanFechaHasta().clearFecha();
		getPanTablaCategoriaValorPuesto().limpiar();
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		if(nivelItemSelector != -1) {
			ConfiguracionValorHoraCategoria config = (ConfiguracionValorHoraCategoria)lista.getSelectedValue();
			setConfigActual(getConfigFacade().getByIdEager(config.getId()));
			limpiarDatos();
			if(getConfigActual() != null) {
				getPanFechaDesde().setSelectedDate(getConfigActual().getFechaDesde());
				getPanFechaHasta().setSelectedDate(getConfigActual().getFechaHasta());
				getPanTablaCategoriaValorPuesto().agregarElementos(configActual.getCategoriasValoresPuesto());
			}
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
		cargarLista();
	}

	public ConfiguracionValorHoraCategoria getConfigActual() {
		return configActual;
	}

	public void setConfigActual(ConfiguracionValorHoraCategoria configActual) {
		this.configActual = configActual;
	}

	private PanelDatePicker getPanFechaDesde() {
		if(panFechaDesde == null){
			panFechaDesde = new PanelDatePicker();
			panFechaDesde.setCaption("Fecha Desde: ");
		}
		return panFechaDesde;
	}

	private PanelDatePicker getPanFechaHasta() {
		if(panFechaHasta == null){
			panFechaHasta = new PanelDatePicker();
			panFechaHasta.setCaption("Fecha Hasta: ");
		}
		return panFechaHasta;
	}
	
	private SindicatoFacadeRemote getSindicatoFacade() {
		if(sindicatoFacade == null){
			sindicatoFacade = GTLPersonalBeanFactory.getInstance().getBean2(SindicatoFacadeRemote.class);
		}
		return sindicatoFacade;
	}

	private ConfiguracionVHCategoriaFacadeRemote getConfigFacade() {
		if(configFacade == null){
			configFacade = GTLPersonalBeanFactory.getInstance().getBean2(ConfiguracionVHCategoriaFacadeRemote.class);
		}
		return configFacade;
	}

	private class PanelTablaCategoriaValorPuesto extends PanelTabla<CategoriaValorPuesto> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 4;
		private static final int COL_CATEGORIA = 0;
		private static final int COL_VALOR_HORA_X_DEFECTO = 1;
		private static final int COL_DETALLE_VALOR_POR_PUESTO = 2;
		private static final int COL_OBJ = 3;

		private Sindicato sindicato;

		public PanelTablaCategoriaValorPuesto() {
			agregarBotonModificar();
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_CATEGORIA, "CATEGORIA", 100, 100, true);
			tabla.setMultilineColumn(COL_DETALLE_VALOR_POR_PUESTO, "VALOR HORA POR PUESTOS", 200, true);
			tabla.setStringColumn(COL_VALOR_HORA_X_DEFECTO, "VALOR HORA POR DEFECTO", 150, 150, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);

			tabla.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {
					if(e.getClickCount() == 2 && !isModoConsulta() && getTabla().getSelectedRow() != -1) {
						handleActionModificar(getTabla().getSelectedRow());
					}
				}

			});

			return tabla;
		}

		private void handleActionModificar(int filaSeleccionada) {
			CategoriaValorPuesto categoriaValorPuesto = getElemento(filaSeleccionada);
			JDialogCargarCategoriaValorPuesto dialogo = new JDialogCargarCategoriaValorPuesto(GuiABMConfigValorHoraCategoria.this.getFrame(), categoriaValorPuesto, sindicato, getCategoriasUtilizadas());
			dialogo.setVisible(true);
			if(dialogo.isAcepto()) {
				updateElemento(filaSeleccionada, dialogo.getCatValorPuesto());
			}
		}

		private void updateElemento(int selectedRow, CategoriaValorPuesto catValorPuesto) {
			getTabla().setValueAt(catValorPuesto, selectedRow, COL_OBJ);
			getTabla().setValueAt(catValorPuesto.getCategoria().getNombre(), selectedRow, COL_CATEGORIA);
			getTabla().setValueAt(printValoresHoraPorPuesto(catValorPuesto), selectedRow, COL_DETALLE_VALOR_POR_PUESTO);
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			handleActionModificar(filaSeleccionada);
		}

		@Override
		protected void agregarElemento(CategoriaValorPuesto elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_CATEGORIA] = elemento.getCategoria().getNombre();
			row[COL_OBJ] = elemento;
			row[COL_VALOR_HORA_X_DEFECTO] = GenericUtils.getDecimalFormat().format(elemento.getValorHoraDefault().doubleValue());
			row[COL_DETALLE_VALOR_POR_PUESTO] = printValoresHoraPorPuesto(elemento);
			getTabla().addRow(row);
		}

		@Override
		protected CategoriaValorPuesto getElemento(int fila) {
			return (CategoriaValorPuesto)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarAgregar() {
			CategoriaValorPuesto catValorPuesto = new CategoriaValorPuesto();
			catValorPuesto.setCategoria(catValorPuesto.getCategoria());
			JDialogCargarCategoriaValorPuesto dialogo = new JDialogCargarCategoriaValorPuesto(GuiABMConfigValorHoraCategoria.this.getFrame(), catValorPuesto, sindicato, getCategoriasUtilizadas());
			dialogo.setVisible(true);
			if(dialogo.isAcepto()) {
				catValorPuesto = dialogo.getCatValorPuesto();
				agregarElemento(catValorPuesto);
			}
			return false;
		}

		private List<Categoria> getCategoriasUtilizadas() {
			List<Categoria> categoriasUsadas = new ArrayList<Categoria>();
			for(CategoriaValorPuesto cvp : getElementos()) {
				categoriasUsadas.add(cvp.getCategoria());
			}
			return categoriasUsadas;
		}

		public void setSindicato(Sindicato sindicato) {
			this.sindicato = sindicato;
		}

		private String printValoresHoraPorPuesto(CategoriaValorPuesto catValorPuesto) {
			if(catValorPuesto.getValoresPuesto().isEmpty()) {
				return "";
			} else {
				List<String> strList = new ArrayList<String>();
				for(ValorPuesto vp : catValorPuesto.getValoresPuesto()) {
					strList.add(vp.getPuesto().getNombre() + ": " + GenericUtils.getDecimalFormat().format(vp.getValorHora().doubleValue()));
				}
				return StringUtil.getCadena(strList, "\n");
			}
		}
	
	}

}
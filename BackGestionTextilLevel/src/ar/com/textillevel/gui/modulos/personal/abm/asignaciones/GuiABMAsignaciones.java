package ar.com.textillevel.gui.modulos.personal.abm.asignaciones;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.facade.api.remote.QuincenaFacadeRemote;
import ar.com.textillevel.gui.modulos.personal.abm.tareas.JDialogCargarCategoriaValorPuesto;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.gui.util.controles.PanelDatePicker;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Categoria;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.CategoriaValorPuesto;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ETipoCobro;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.ValorPuesto;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.Quincena;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.Asignacion;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.AsignacionNoRemPorcSueldoBruto;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.AsignacionNoRemSimple;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.asignaciones.enums.ETipoAsignacion;
import ar.com.textillevel.modulos.personal.facade.api.remote.AsignacionFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.SindicatoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class GuiABMAsignaciones extends GuiABMListaTemplate {

	private static final long serialVersionUID = 1L;
	
	private static final String PAN_ASIG_SIMPLE = "PAN_ASIG_SIMPLE";
	private static final String PAN_ASIG_PORC_SUELDO_BRUTO = "PAN_ASIG_PORC_SUELDO_BRUTO";

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private JPanel panAsigSimple;
	private JPanel panAsigPorcSueldoBruto;
	private JPanel panCardLayout;
	
	private JPanel panCmbQuincena;
	private JComboBox cmbQuincena;

	private CardLayout cardLayout;
	private JComboBox cmbTipoAsignacion;

	private PanelDatePicker panFechaDesde;
	private PanelDatePicker panFechaHasta;
	private JTextField txtImporte;
	private JTextField txtPorcSueldoBruto;
	private JTextField txtVisualizacionEnRS;

	private SindicatoFacadeRemote sindicatoFacade;
	private AsignacionFacadeRemote asignacionFacade;
	private QuincenaFacadeRemote quincenaFacade;

	private Asignacion asignacionActual;

	public GuiABMAsignaciones(Integer idModulo){
		super();
		setHijoCreado(true);
		setTitle("Administrar Asignaciones");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Información de la Asignación", getTabDetalle());		
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
			panDetalle.add(getPanFechaHasta(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));

			panDetalle.add(getPanCmbQuincena(),  GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 2, 1, 0, 0));

			panDetalle.add(new JLabel("Tipo de Asignación:"),  GenericUtils.createGridBagConstraints(0, 2,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbTipoAsignacion(),  GenericUtils.createGridBagConstraints(1, 2,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));

			cardLayout = new CardLayout();
			panCardLayout = new JPanel(cardLayout);
			panCardLayout.setBorder(BorderFactory.createTitledBorder("Datos de la asignación"));
			panCardLayout.add(PAN_ASIG_SIMPLE, getPanAsigSimple());
			panCardLayout.add(PAN_ASIG_PORC_SUELDO_BRUTO, getPanAsigPorcSueldoBruto());
			panDetalle.add(panCardLayout,  GenericUtils.createGridBagConstraints(0, 3,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 1, 1));
			
		}
		return panDetalle;
	}

	private JPanel getPanAsigSimple() {
		if(panAsigSimple == null) {
			panAsigSimple = new JPanel();
			panAsigSimple.setLayout(new GridBagLayout());
			panAsigSimple.add(new JLabel("Importe: "),  GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 10), 1, 1, 0, 0));
			panAsigSimple.add(getTxtImporte(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 5), 1, 1, 1, 0));
		}
		return panAsigSimple;
	}

	private JPanel getPanCmbQuincena() {
		if(panCmbQuincena == null) {
			panCmbQuincena = new JPanel();
			panCmbQuincena.setLayout(new GridBagLayout());
			panCmbQuincena.add(new JLabel("Quincena: "),  GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 10), 1, 1, 0, 0));
			panCmbQuincena.add(getCmbQuincena(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(10, 0, 5, 5), 1, 1, 0, 0));
		}
		return panCmbQuincena;
	}

	private JComboBox getCmbQuincena() {
		if(cmbQuincena == null) {
			cmbQuincena = new JComboBox();
			List<Quincena> allQuincenas = getQuincenaFacade().getAllOrderByName();
			List<Object> items = new ArrayList<Object>();
			items.add("TODOS");
			for(Quincena q : allQuincenas) {
				items.add(q);
			}
			GuiUtil.llenarCombo(cmbQuincena, items, true);
			cmbQuincena.setSelectedIndex(-1);
		}
		return cmbQuincena;
 	}

	private JPanel getPanAsigPorcSueldoBruto() {
		if(panAsigPorcSueldoBruto == null) {
			panAsigPorcSueldoBruto = new JPanel();
			panAsigPorcSueldoBruto.setLayout(new GridBagLayout());
			panAsigPorcSueldoBruto.add(new JLabel("Porcentaje: "),  GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 10), 1, 1, 0, 0));
			panAsigPorcSueldoBruto.add(getTxtPorcSueldoBruto(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 5), 1, 1, 1, 0));
			panAsigPorcSueldoBruto.add(new JLabel("Texto a Mostrar en el Recibo de Sueldo: "),  GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 10), 1, 1, 0, 0));
			panAsigPorcSueldoBruto.add(getTxtVisualizacionEnRS(),  GenericUtils.createGridBagConstraints(1, 1,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 0, 5, 5), 1, 1, 0, 0));
		}
		return panAsigPorcSueldoBruto;
	}

	@Override
	public void cargarLista() {
		Sindicato sindicatoSeleccionado = (Sindicato)getItemComboMaestroSeleccionado();
		if(sindicatoSeleccionado != null) {
			List<Asignacion> asignacionList = getAsignacionFacade().getAllByIdSindicato(sindicatoSeleccionado.getId());
			lista.clear();
			for(Asignacion c : asignacionList) {
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
		this.asignacionActual = null;
		limpiarDatos();
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar la Configuración seleccionada?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				getAsignacionFacade().remove(getAsignacionActual());
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
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar una Configuración", "Error");
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
			Asignacion asignacion = getAsignacionFacade().save(getAsignacionActual());
			lista.setSelectedValue(asignacion, true);
			return true;
		}
		return false;
	}

	private boolean validar() {
		if(getPanFechaDesde().getDate() == null){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar la fecha desde.", "Error");
			return false;
		}
		if(getPanFechaHasta().getDate() == null){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar la fecha hasta.", "Error");
			return false;
		}
		if(getTipoAsignacionSel() == ETipoAsignacion.SIMPLE) {
			String importeStr = getTxtImporte().getText();
			if(StringUtil.isNullOrEmpty(importeStr)) {
				FWJOptionPane.showErrorMessage(this, "Debe ingresar el importe.", "Error");
				getTxtImporte().requestFocus();
				return false;
			}
			if(!GenericUtils.esNumerico(importeStr)) {
				FWJOptionPane.showErrorMessage(this, "El importe debe ser numérico.", "Error");
				getTxtImporte().requestFocus();
				return false;
			}
		} else {
			String importeStr = getTxtPorcSueldoBruto().getText();
			if(StringUtil.isNullOrEmpty(importeStr)) {
				FWJOptionPane.showErrorMessage(this, "Debe ingresar el porcentaje.", "Error");
				getTxtPorcSueldoBruto().requestFocus();
				return false;
			}
			if(!GenericUtils.esNumerico(importeStr)) {
				FWJOptionPane.showErrorMessage(this, "El porcentaje debe ser numérico.", "Error");
				getTxtPorcSueldoBruto().requestFocus();
				return false;
			}
			if(StringUtil.isNullOrEmpty(getTxtVisualizacionEnRS().getText())) {
				FWJOptionPane.showErrorMessage(this, "Debe ingresar el texto de visualización en el recibo de sueldo.", "Error");
				getTxtVisualizacionEnRS().requestFocus();
				return false;
			}
		}
		Sindicato sindicato = (Sindicato)getItemComboMaestroSeleccionado();
		if(sindicato.getTipoCobro() == ETipoCobro.QUINCENAL && getCmbQuincena().getSelectedItem() == null) {
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar la quincena.", "Error");
			return false;
		}
		return true;
	}

	private void capturarSetearDatos() {
		Sindicato sindicato = (Sindicato)getItemComboMaestroSeleccionado();
		switch (getTipoAsignacionSel()) {
		case SIMPLE:
			AsignacionNoRemSimple asigSimple = null;
			if(getAsignacionActual() == null) {
				asigSimple = new AsignacionNoRemSimple();
			} else {
				asigSimple = (AsignacionNoRemSimple)asignacionActual;
			}
			asigSimple.setSindicato(sindicato);
			asigSimple.setQuincena(getQuincenaActual());
			asigSimple.setFechaDesde(new Date(getPanFechaDesde().getDate().getTime()));
			asigSimple.setFechaHasta(new Date(getPanFechaHasta().getDate().getTime()));
			asigSimple.setImporte((new BigDecimal(GenericUtils.getDoubleValue(getTxtImporte().getText()))));
			setAsignacionActual(asigSimple);
		break;

		case PORCE_SUELDO_BRUTO:
			AsignacionNoRemPorcSueldoBruto asigPorcSueldoBruto = null;
			if(getAsignacionActual() == null) {
				asigPorcSueldoBruto = new AsignacionNoRemPorcSueldoBruto();
			} else {
				asigPorcSueldoBruto = (AsignacionNoRemPorcSueldoBruto)asignacionActual;
			}
			asigPorcSueldoBruto.setSindicato(sindicato);
			asigPorcSueldoBruto.setQuincena(getQuincenaActual());
			asigPorcSueldoBruto.setFechaDesde(new Date(getPanFechaDesde().getDate().getTime()));
			asigPorcSueldoBruto.setFechaHasta(new Date(getPanFechaHasta().getDate().getTime()));
			asigPorcSueldoBruto.setPorcentaje((new BigDecimal(GenericUtils.getDoubleValue(getTxtPorcSueldoBruto().getText()))));
			asigPorcSueldoBruto.setTextoVisualizacionRS(getTxtVisualizacionEnRS().getText().trim());
			setAsignacionActual(asigPorcSueldoBruto);
		break;
		
		default:
			break;
		}
		
	}

	private Quincena getQuincenaActual() {
		if(getCmbQuincena().getSelectedIndex() == -1 || getCmbQuincena().getSelectedIndex() == 0) {
			return null;
		} else {
			return (Quincena)getCmbQuincena().getSelectedItem();
		}
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
		if(isAgregar()) {
			getCmbTipoAsignacion().setEnabled(true);
		} else {
			getCmbTipoAsignacion().setEnabled(false);//Si es una modificación no se puede cambiar de tipo de asignación!
		}
//		getPanTablaCategoriaValorPuesto().setModoConsulta(!estado);
	}

	@Override
	public void limpiarDatos() {
		getCmbQuincena().setSelectedIndex(-1);
		getPanFechaDesde().clearFecha();
		getPanFechaHasta().clearFecha();
//		getPanTablaCategoriaValorPuesto().limpiar();
		getTxtImporte().setText(null);
		getTxtPorcSueldoBruto().setText(null);
		getTxtVisualizacionEnRS().setText(null);
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		if(nivelItemSelector != -1) {
			Asignacion asignacion = (Asignacion)lista.getSelectedValue();
			setAsignacionActual(getAsignacionFacade().getByIdEager(asignacion.getId()));
			limpiarDatos();
			if(getAsignacionActual() != null) {
				getPanFechaDesde().setSelectedDate(getAsignacionActual().getFechaDesde());
				getPanFechaHasta().setSelectedDate(getAsignacionActual().getFechaHasta());
				Quincena quincena = getAsignacionActual().getQuincena();
				if(quincena == null) {
					getCmbQuincena().setSelectedIndex(0);
				} else {
					getCmbQuincena().setSelectedItem(quincena);
				}
				switch (getAsignacionActual().getTipo()) {
					case SIMPLE:
						getCmbTipoAsignacion().setSelectedItem(ETipoAsignacion.SIMPLE);
						AsignacionNoRemSimple asigSimple = (AsignacionNoRemSimple)getAsignacionActual();
						getTxtImporte().setText(GenericUtils.getDecimalFormat().format(asigSimple.getImporte()));
					break;

					case PORCE_SUELDO_BRUTO:
						getCmbTipoAsignacion().setSelectedItem(ETipoAsignacion.PORCE_SUELDO_BRUTO);
						AsignacionNoRemPorcSueldoBruto asigPorcSueldoBruto = (AsignacionNoRemPorcSueldoBruto)getAsignacionActual();
						getTxtPorcSueldoBruto().setText(GenericUtils.getDecimalFormat().format(asigPorcSueldoBruto.getPorcentaje()));
						getTxtVisualizacionEnRS().setText(asigPorcSueldoBruto.getTextoVisualizacionRS());
					break;

					default:
						break;
				}
				
				
			}
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
		cargarLista();
		Sindicato sindicato = (Sindicato)getItemComboMaestroSeleccionado();
		getPanCmbQuincena().setVisible(sindicato.getTipoCobro() == ETipoCobro.QUINCENAL);
	}

	public Asignacion getAsignacionActual() {
		return asignacionActual;
	}

	public void setAsignacionActual(Asignacion asignacionActual) {
		this.asignacionActual = asignacionActual;
	}

	private JComboBox getCmbTipoAsignacion() {
		if(cmbTipoAsignacion == null) {
			cmbTipoAsignacion = new JComboBox();
			List<ETipoAsignacion> tipoItemList = Arrays.asList(ETipoAsignacion.values());
			GuiUtil.llenarCombo(cmbTipoAsignacion, tipoItemList, true);
			cmbTipoAsignacion.addItemListener(new ItemListener() {

				public void itemStateChanged(ItemEvent evt) {
					if (evt.getStateChange() == ItemEvent.SELECTED) {
						ETipoAsignacion itemSeleccionado = getTipoAsignacionSel();
						switch (itemSeleccionado) {
						case SIMPLE:
							cardLayout.show(panCardLayout, PAN_ASIG_SIMPLE);							
						break;

						case PORCE_SUELDO_BRUTO:
							cardLayout.show(panCardLayout, PAN_ASIG_PORC_SUELDO_BRUTO);
						break;
						
						default:
							break;
						}
					}
				}

			});

		}
		return cmbTipoAsignacion;
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

	private JTextField getTxtImporte() {
		if(txtImporte == null) {
			txtImporte = new JTextField();
			txtImporte.setPreferredSize(new Dimension(40, 20));
			txtImporte.setSize(new Dimension(40, 20));
		}
		return txtImporte;
	}

	private JTextField getTxtPorcSueldoBruto() {
		if(txtPorcSueldoBruto == null) {
			txtPorcSueldoBruto = new JTextField();
		}
		return txtPorcSueldoBruto;
	}

	private JTextField getTxtVisualizacionEnRS() {
		if(txtVisualizacionEnRS == null) {
			txtVisualizacionEnRS = new JTextField();
		}
		return txtVisualizacionEnRS;
	}

	private SindicatoFacadeRemote getSindicatoFacade() {
		if(sindicatoFacade == null){
			sindicatoFacade = GTLPersonalBeanFactory.getInstance().getBean2(SindicatoFacadeRemote.class);
		}
		return sindicatoFacade;
	}

	private AsignacionFacadeRemote getAsignacionFacade() {
		if(asignacionFacade == null){
			asignacionFacade = GTLPersonalBeanFactory.getInstance().getBean2(AsignacionFacadeRemote.class);
		}
		return asignacionFacade;
	}

	private QuincenaFacadeRemote getQuincenaFacade() {
		if(quincenaFacade == null){
			quincenaFacade = GTLPersonalBeanFactory.getInstance().getBean2(QuincenaFacadeRemote.class);
		}
		return quincenaFacade;
	}
	
	private ETipoAsignacion getTipoAsignacionSel() {
		return (ETipoAsignacion) cmbTipoAsignacion.getSelectedItem();
	}

	@SuppressWarnings("unused")
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
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
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
			JDialogCargarCategoriaValorPuesto dialogo = new JDialogCargarCategoriaValorPuesto(GuiABMAsignaciones.this.getFrame(), categoriaValorPuesto, sindicato, getCategoriasUtilizadas());
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
			//TODO: Agregar las validaciones
			CategoriaValorPuesto catValorPuesto = new CategoriaValorPuesto();
			catValorPuesto.setCategoria(catValorPuesto.getCategoria());
			JDialogCargarCategoriaValorPuesto dialogo = new JDialogCargarCategoriaValorPuesto(GuiABMAsignaciones.this.getFrame(), catValorPuesto, sindicato, getCategoriasUtilizadas());
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
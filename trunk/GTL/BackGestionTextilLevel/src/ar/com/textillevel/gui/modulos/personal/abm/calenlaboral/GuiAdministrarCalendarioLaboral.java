package ar.com.textillevel.gui.modulos.personal.abm.calenlaboral;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.DateUtil;
import ar.clarin.fwjava.util.StringUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.CalendarioAnualFeriado;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.ConfigFormaPagoSindicato;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.RangoDiasFeriado;
import ar.com.textillevel.modulos.personal.entidades.calenlaboral.TotalHorasPagoDia;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.facade.api.remote.CalendarioAnualFeriadoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class GuiAdministrarCalendarioLaboral extends GuiABMListaTemplate {

	private static final long serialVersionUID = 1L;

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private CLJNumericTextField txtAnio;
	private PanTablaConfigFormaPagoSindicato panTablaConfigFormaPagoSindicato;
	private PanTablaFeriados panTablaFeriados;

	private CalendarioAnualFeriado calendario;
	private CalendarioAnualFeriadoFacadeRemote calendarioFacade;

	public GuiAdministrarCalendarioLaboral(Integer idModulo) {
		super();
		setHijoCreado(true);
		calendarioFacade = GTLPersonalBeanFactory.getInstance().getBean2(CalendarioAnualFeriadoFacadeRemote.class);
		setTitle("Administrar Calendario Laboral");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Descripción del Feriado", getTabDetalle());
	}

	private JPanel getTabDetalle() {
		if (tabDetalle == null) {
			tabDetalle = new JPanel();
			tabDetalle.setLayout(new GridBagLayout());
			tabDetalle.add(getPanDetalle(), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 1, 1, 1, 1));
		}
		return tabDetalle;
	}

	private JPanel getPanDetalle() {
		if (panDetalle == null) {
			panDetalle = new JPanel();
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel("Año: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtAnio(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 0, 5), 1, 1, 0, 0));
			panDetalle.add(getPanTablaConfigFormaPagoSindicato(), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 0, 0, 0), 2, 1, 0, 0.2));
			panDetalle.add(getPanTablaFeriados(), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(10, 0, 0, 0), 2, 1, 1, 0.8));
		}
		return panDetalle;
	}
	
	private CLJNumericTextField getTxtAnio() {
		if (txtAnio == null) {
			txtAnio = new CLJNumericTextField();
			txtAnio.setPreferredSize(new Dimension(50, 20));
		}
		return txtAnio;
	}

	@Override
	public void cargarLista() {
		lista.clear();
		List<CalendarioAnualFeriado> allCalendarios = calendarioFacade.getAll();
		for(CalendarioAnualFeriado calen : allCalendarios) {
			lista.addItem(calen);
		}
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
		setModoEdicionTemplate(false);
		cargarLista();
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		calendario = new CalendarioAnualFeriado();
		limpiarDatos();
		getTxtAnio().requestFocus();
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(CLJOptionPane.showQuestionMessage(GuiAdministrarCalendarioLaboral.this, "¿Está seguro que desea eliminar calendario seleccionado?", "Confirmación") == CLJOptionPane.YES_OPTION) {
			calendario = (CalendarioAnualFeriado)lista.getSelectedValue();
			calendarioFacade.eliminarCalendario(calendario);
		}
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		this.calendario = (CalendarioAnualFeriado)lista.getSelectedValue();
		this.calendario = calendarioFacade.getByIdEager(calendario.getAnio());
		return true;
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if(validar()) {
			try {
				capturarSetearDatos();
				calendarioFacade.save(calendario);
				cargarLista();
				if(nivelNodoSeleccionado == -1) {
					lista.setSelectedIndex(0);
				} else {
					lista.setSelectedIndex(nivelNodoSeleccionado);
				}
				return true;
			} catch(ValidacionException e) {
				CLJOptionPane.showErrorMessage(GuiAdministrarCalendarioLaboral.this, StringW.wordWrap(e.getMensajeError()), "Error");
			}
		}
		return false;
	}

	private void capturarSetearDatos() {
		calendario.setAnio(getTxtAnio().getValue());
		calendario.getConfigsFormasPagoSindicatos().clear();
		calendario.getConfigsFormasPagoSindicatos().addAll(getPanTablaConfigFormaPagoSindicato().getElementos());
		calendario.getFeriados().clear();
		calendario.getFeriados().addAll(getPanTablaFeriados().getElementos());
	}

	private boolean validar() {
		if(StringUtil.isNullOrEmpty(getTxtAnio().getText())) {
			mostrarMensajeValidacion("Debe ingresar un año", "Error", tabDetalle);
			getTxtAnio().requestFocus();
			return false;
		}
		if(getTxtAnio().getValue() <= 0) {
			mostrarMensajeValidacion("El año debe ser mayor a cero.", "Error", tabDetalle);
			getTxtAnio().requestFocus();
			return false;
		}
		String txtValidacionConfigs = getPanTablaConfigFormaPagoSindicato().validar();
		if(txtValidacionConfigs != null) {
			mostrarMensajeValidacion(txtValidacionConfigs, "Error", tabDetalle);
			getTxtAnio().requestFocus();
			return false;
		}
		return true;
	}

	@Override
	public void setModoEdicion(boolean estado) {
		getPanTablaConfigFormaPagoSindicato().setModoConsulta(!estado);
		getPanTablaFeriados().setModoConsulta(!estado);
	}

	@Override
	public void limpiarDatos() {
		getTxtAnio().setText(null);
		getPanTablaConfigFormaPagoSindicato().limpiar();
		getPanTablaFeriados().limpiar();
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		this.calendario =  (CalendarioAnualFeriado)lista.getSelectedValue();
		this.calendario = calendarioFacade.getByIdEager(calendario.getAnio());
		limpiarDatos();
		cargarDatosCalendario();
	}

	private void cargarDatosCalendario() {
		getTxtAnio().setValue(calendario.getAnio().longValue());
		getPanTablaConfigFormaPagoSindicato().agregarElementos(calendario.getConfigsFormasPagoSindicatos());
		getPanTablaFeriados().agregarElementos(calendario.getFeriados());
		getPanTablaFeriados().reordenar();
	}

	@Override
	public void itemComboMaestroSeleccionado() {
	}

	private PanTablaConfigFormaPagoSindicato getPanTablaConfigFormaPagoSindicato() {
		if(panTablaConfigFormaPagoSindicato == null) {
			panTablaConfigFormaPagoSindicato = new PanTablaConfigFormaPagoSindicato();
		}
		return panTablaConfigFormaPagoSindicato;
	}

	private PanTablaFeriados getPanTablaFeriados() {
		if(panTablaFeriados == null) {
			panTablaFeriados = new PanTablaFeriados();
		}
		return panTablaFeriados;
	}

	private class PanTablaConfigFormaPagoSindicato extends PanelTabla<ConfigFormaPagoSindicato> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 3;
		private static final int COL_SINDICATO = 0;
		private static final int COL_DESCR_CONFIG = 1;
		private static final int COL_OBJ = 2;

		public PanTablaConfigFormaPagoSindicato() {
			setBorder(BorderFactory.createTitledBorder("Configuración por Defecto de Pagos por Sindicatos/Horas."));
			agregarBotonModificar();
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_SINDICATO, "SINDICATO", 150, 150, true);
			tabla.setMultilineColumn(COL_DESCR_CONFIG, "FORMA DE PAGO POR DIA", 400, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			return tabla;
		}

		public String validar() {
			List<ConfigFormaPagoSindicato> elementos = getElementos();
			List<Sindicato> sindicatos = new ArrayList<Sindicato>();
			for(ConfigFormaPagoSindicato c : elementos) {
				sindicatos.add(c.getSindicato());
			}
			if(new HashSet<Sindicato>(sindicatos).size() != sindicatos.size()) {
				return "Sólo puede haber una configuración por sindicato";
			} else {
				return null;
			}
		}

		@Override
		protected void agregarElemento(ConfigFormaPagoSindicato elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_SINDICATO] = elemento.getSindicato();
			row[COL_DESCR_CONFIG] = toString(elemento.getTotalHorasPagoPorDias());
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		private String toString(List<TotalHorasPagoDia> totalHorasPagoPorDias) {
			List<String> descripciones = new ArrayList<String>();
			for(TotalHorasPagoDia t : totalHorasPagoPorDias) {
				StringBuilder descripcion = new StringBuilder();
				descripcion.append(t.getDia() == null ? "DEFAULT" : t.getDia())
						   .append(" : " + t.getTotalHoras() + " hrs. - ")
						   .append("Discrimina en Rec. de Sueldo: " + (t.isDiscriminaEnRS() ? "SI" : "NO"));
				descripciones.add(descripcion.toString());
			}
			return StringUtil.getCadena(descripciones, "\n");
		}

		@Override
		protected ConfigFormaPagoSindicato getElemento(int fila) {
			return (ConfigFormaPagoSindicato)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarAgregar() {
			ConfigFormaPagoSindicato configFormaPagoSindicato = new ConfigFormaPagoSindicato();
			JDialogCargarConfigFormaPagoSindicato dialogo = new JDialogCargarConfigFormaPagoSindicato(GuiAdministrarCalendarioLaboral.this.getFrame(), configFormaPagoSindicato);
			dialogo.setVisible(true);
			if(dialogo.isAcepto()) {
				agregarElemento(configFormaPagoSindicato);
			}
			return false;
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			ConfigFormaPagoSindicato configFormaPagoSindicato = getElemento(filaSeleccionada);
			JDialogCargarConfigFormaPagoSindicato dialogo = new JDialogCargarConfigFormaPagoSindicato(GuiAdministrarCalendarioLaboral.this.getFrame(), configFormaPagoSindicato);
			dialogo.setVisible(true);
			if(dialogo.isAcepto()) {
				getTabla().setValueAt(toString(configFormaPagoSindicato.getTotalHorasPagoPorDias()), filaSeleccionada, COL_DESCR_CONFIG);
				getTabla().setValueAt(configFormaPagoSindicato.getSindicato(), filaSeleccionada, COL_SINDICATO);
			}
		}

	}

	private class PanTablaFeriados extends PanelTabla<RangoDiasFeriado> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 5;
		private static final int COL_DESCR_FERIADO = 0;
		private static final int COL_FECHA_DESDE = 1;
		private static final int COL_FECHA_HASTA = 2;
		private static final int COL_DETALLE_PAGO_HORAS = 3;
		private static final int COL_OBJ = 4;

		public PanTablaFeriados() {
			setBorder(BorderFactory.createTitledBorder("Feriados"));
			agregarBotonModificar();
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_DESCR_FERIADO, "FERIADO", 170, 170, true);
			tabla.setDateColumn(COL_FECHA_DESDE, "DESDE", 70, true);
			tabla.setDateColumn(COL_FECHA_HASTA, "HASTA", 70, true);
			tabla.setMultilineColumn(COL_DETALLE_PAGO_HORAS, "HORAS A PAGAR POR SINDICATO", 250, true);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			return tabla;
		}

		@Override
		protected void agregarElemento(RangoDiasFeriado elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_DESCR_FERIADO] = elemento.getDescripcion();
			row[COL_FECHA_DESDE] = DateUtil.getAyerSinRedondear(elemento.getDesde());
			row[COL_FECHA_HASTA] = DateUtil.getAyerSinRedondear(elemento.getHasta());
			row[COL_DETALLE_PAGO_HORAS] = toString(elemento.getConfigsFormasPagoSindicatos());
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		private String toString(List<ConfigFormaPagoSindicato> configsFormasPagoSindicatos) {
			List<String> listaDetalle = new ArrayList<String>();
			for(ConfigFormaPagoSindicato c : configsFormasPagoSindicatos) {
				TotalHorasPagoDia t = c.getTotalHorasPagoPorDias().get(0);
				StringBuilder descripcion = new StringBuilder();
				descripcion.append(c.getSindicato().toString())
						   .append(": " + t.getTotalHoras() + " hrs. - ")
						   .append("Discrimina en Rec. de Sueldo: " + (t.isDiscriminaEnRS() ? "SI" : "NO"));
				listaDetalle.add(descripcion.toString());
			}
			return StringUtil.getCadena(listaDetalle, "\n");
		}

		public void reordenar() {
			List<RangoDiasFeriado> feriadosList = new ArrayList<RangoDiasFeriado>();
			for(int i = 0; i < getTabla().getRowCount(); i++) {
				feriadosList.add(getElemento(i));
			}
			Collections.sort(feriadosList);
			getTabla().setNumRows(0);
			agregarElementos(feriadosList);
		}

		@Override
		protected RangoDiasFeriado getElemento(int fila) {
			return (RangoDiasFeriado)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarAgregar() {
			RangoDiasFeriado feriado = new RangoDiasFeriado();
			feriado.setDesde(DateUtil.getHoy());
			feriado.setHasta(DateUtil.getHoy());
			Integer anio = getTxtAnio().getValueWithNull(); 
			if(anio == null){
				anio = calendario.getAnio();
			}
			if(anio == null){
				CLJOptionPane.showErrorMessage(getFrame(), "Debe ingresar el año", "Error");
				return false;
			}
			JDialogCargarFeriado dialogo = new JDialogCargarFeriado(GuiAdministrarCalendarioLaboral.this.getFrame(), anio,  feriado);
			dialogo.setVisible(true);
			if(dialogo.isAcepto()) {
				agregarElemento(feriado);
				reordenar();
			}
			return false;
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			RangoDiasFeriado feriado = getElemento(filaSeleccionada);
			Integer anio = getTxtAnio().getValueWithNull(); 
			if(anio == null){
				anio = calendario.getAnio();
			}
			if(anio == null){
				CLJOptionPane.showErrorMessage(getFrame(), "Debe ingresar el año", "Error");
			}
			JDialogCargarFeriado dialogo = new JDialogCargarFeriado(GuiAdministrarCalendarioLaboral.this.getFrame(), anio, feriado);
			dialogo.setVisible(true);
			if(dialogo.isAcepto()) {
				getTabla().setValueAt(feriado.getDescripcion(), filaSeleccionada, COL_DESCR_FERIADO);
				getTabla().setValueAt(feriado.getDesde(), filaSeleccionada, COL_FECHA_DESDE);
				getTabla().setValueAt(feriado.getHasta(), filaSeleccionada, COL_FECHA_HASTA);
				getTabla().setValueAt(toString(feriado.getConfigsFormasPagoSindicatos()), filaSeleccionada, COL_DETALLE_PAGO_HORAS);
				reordenar();
			}
		}

	}

}
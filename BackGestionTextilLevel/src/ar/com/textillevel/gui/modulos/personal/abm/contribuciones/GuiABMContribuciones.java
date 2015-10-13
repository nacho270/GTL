package ar.com.textillevel.gui.modulos.personal.abm.contribuciones;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.taglibs.string.util.StringW;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.DateUtil;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.fwcommon.util.StringUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.contribuciones.Contribucion;
import ar.com.textillevel.modulos.personal.entidades.contribuciones.PeriodoContribucion;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.facade.api.remote.ContribucionFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.SindicatoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class GuiABMContribuciones extends GuiABMListaTemplate {

	private static final long serialVersionUID = 5490390741310236258L;

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private JTextField txtNombre;
	
	private PanelTablaPeriodos panTablaPeriodos;

	private SindicatoFacadeRemote sindicatoFacade;
	private ContribucionFacadeRemote contribucionFacade;
	
	private Contribucion contribucionActual;

	public GuiABMContribuciones(Integer idModulo){
		super();
		setHijoCreado(true);
		setTitle("Administrar Contribuciones");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Información de la Contribución", getTabDetalle());		
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
			panDetalle.add(new JLabel("Nombre: "), GenericUtils.createGridBagConstraints(0, 0,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNombre(),  GenericUtils.createGridBagConstraints(1, 0,GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getPanTablaPeriodos(), GenericUtils.createGridBagConstraints(0, 1,GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 1, 0));
		}
		return panDetalle;
	}
	
	private PanelTablaPeriodos getPanTablaPeriodos() {
		if(panTablaPeriodos == null) {
			panTablaPeriodos = new PanelTablaPeriodos();
		}
		return panTablaPeriodos;
	}

	@Override
	public void cargarLista() {
		Sindicato sindicatoSeleccionado = (Sindicato)getItemComboMaestroSeleccionado();
		if(sindicatoSeleccionado != null) {
			List<Contribucion> contribucionList = getContribucionFacade().getAllByIdSindicato(sindicatoSeleccionado.getId());
			lista.clear();
			for(Contribucion c : contribucionList) {
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
		Contribucion contribucion = new Contribucion();
		contribucion.setSindicato(sindicato);
		setContribucionActual(contribucion);
		getTxtNombre().requestFocus();
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if(lista.getSelectedIndex() >= 0) {
			if(FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar la Contribución seleccionada?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				getContribucionFacade().remove(getContribucionActual());
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
			FWJOptionPane.showErrorMessage(this, "Debe seleccionar una Contribución", "Error");
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
			Contribucion config = getContribucionFacade().save(getContribucionActual());
			lista.setSelectedValue(config, true);
			return true;
		}
		return false;
	}

	private boolean validar() {
		if(StringUtil.isNullOrEmpty(getTxtNombre().getText().trim())){
			FWJOptionPane.showErrorMessage(this, "Debe ingresar un nombre.", "Error");
			getTxtNombre().requestFocus();
			return false;
		}
		if(getPanTablaPeriodos().getElementos().isEmpty()) {
			FWJOptionPane.showErrorMessage(this, "Debe ingresar al menos un período.", "Error");
			return false;
		}
		List<PeriodoContribucion> periodos = getPanTablaPeriodos().getElementos();
		for(int i = 0; i < periodos.size(); i++) {
			PeriodoContribucion pc = periodos.get(i);
			for(int j = i+1; j < periodos.size(); j++) {
				PeriodoContribucion pc2 = periodos.get(j);
				if(pc.esVigenteEnFecha(pc2.getFechaDesde()) || pc2.esVigenteEnFecha(pc.getFechaDesde())) {
					FWJOptionPane.showErrorMessage(this, StringW.wordWrap("Existen períodos que se solapan."), "Error");
					return false;
				}
			}
		}
		return true;
	}

	private void capturarSetearDatos() {
		getContribucionActual().setNombre(getTxtNombre().getText().trim());
		getContribucionActual().getPeriodos().clear();
		List<PeriodoContribucion> periodos = getPanTablaPeriodos().getElementos();
		Collections.sort(periodos, new Comparator<PeriodoContribucion>() {

			public int compare(PeriodoContribucion o1, PeriodoContribucion o2) {
				return o1.getFechaDesde().compareTo(o2.getFechaDesde())*(-1);
			}

		});
		getContribucionActual().getPeriodos().addAll(periodos);
	}

	@Override
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
		getPanTablaPeriodos().setModoConsulta(!estado);
	}

	@Override
	public void limpiarDatos() {
		getTxtNombre().setText(null);
		getPanTablaPeriodos().limpiar();
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		if(nivelItemSelector != -1) {
			Contribucion contribucion = (Contribucion)lista.getSelectedValue();
			setContribucionActual(getContribucionFacade().getByIdEager(contribucion.getId()));
			limpiarDatos();
			if(getContribucionActual() != null) {
				getTxtNombre().setText(contribucion.getNombre());
				getPanTablaPeriodos().agregarElementos(contribucionActual.getPeriodos());
			}
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
		cargarLista();
	}

	public Contribucion getContribucionActual() {
		return contribucionActual;
	}

	public void setContribucionActual(Contribucion contribucionActual) {
		this.contribucionActual = contribucionActual;
	}

	private JTextField getTxtNombre() {
		if(txtNombre == null) {
			txtNombre = new JTextField();
		}
		return txtNombre;
	}

	private SindicatoFacadeRemote getSindicatoFacade() {
		if(sindicatoFacade == null){
			sindicatoFacade = GTLPersonalBeanFactory.getInstance().getBean2(SindicatoFacadeRemote.class);
		}
		return sindicatoFacade;
	}

	private ContribucionFacadeRemote getContribucionFacade() {
		if(contribucionFacade == null){
			contribucionFacade = GTLPersonalBeanFactory.getInstance().getBean2(ContribucionFacadeRemote.class);
		}
		return contribucionFacade;
	}

	private class PanelTablaPeriodos extends PanelTabla<PeriodoContribucion> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 4;
		private static final int COL_PERIODO = 0;
		private static final int COL_PORCENTAJE = 1;
		private static final int COL_IMPORTE_FIJO = 2;
		private static final int COL_OBJ = 3;

		public PanelTablaPeriodos() {
			agregarBotonModificar();
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_PERIODO, "PERIODO", 200, 200, true);
			tabla.setFloatColumn(COL_IMPORTE_FIJO, "IMPORTE FIJO", 150, true);
			tabla.setFloatColumn(COL_PORCENTAJE, "PORCENTAJE", 150, true);
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
			PeriodoContribucion periodo = getElemento(filaSeleccionada);
			JDialogCargarPeriodoContribucion dialogo = new JDialogCargarPeriodoContribucion(GuiABMContribuciones.this.getFrame(), periodo);
			dialogo.setVisible(true);
			if(dialogo.isAcepto()) {
				updateElemento(filaSeleccionada, dialogo.getPeriodoContribucion());
			}
		}

		private void updateElemento(int selectedRow, PeriodoContribucion periodoContribucion) {
			getTabla().setValueAt(periodoContribucion, selectedRow, COL_OBJ);
			getTabla().setValueAt(DateUtil.dateToString(periodoContribucion.getFechaDesde()) + " - " + DateUtil.dateToString(periodoContribucion.getFechaHasta()), selectedRow, COL_PERIODO);
			getTabla().setValueAt(periodoContribucion.getPorcentaje() == null ? null : GenericUtils.getDecimalFormat().format(periodoContribucion.getPorcentaje().doubleValue()), selectedRow, COL_PORCENTAJE);
			getTabla().setValueAt(periodoContribucion.getImporteFijo() == null ? null : GenericUtils.getDecimalFormat().format(periodoContribucion.getImporteFijo().doubleValue()), selectedRow, COL_IMPORTE_FIJO);
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			handleActionModificar(filaSeleccionada);
		}

		@Override
		protected void agregarElemento(PeriodoContribucion elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_PERIODO] = DateUtil.dateToString(elemento.getFechaDesde()) + " - " + DateUtil.dateToString(elemento.getFechaHasta());
			row[COL_IMPORTE_FIJO] = elemento.getImporteFijo() == null ? null : GenericUtils.getDecimalFormat().format(elemento.getImporteFijo().doubleValue());
			row[COL_PORCENTAJE] = elemento.getPorcentaje() == null ? null : GenericUtils.getDecimalFormat().format(elemento.getPorcentaje().doubleValue());
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected PeriodoContribucion getElemento(int fila) {
			return (PeriodoContribucion)getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarAgregar() {
			PeriodoContribucion catValorPuesto = new PeriodoContribucion();
			JDialogCargarPeriodoContribucion dialogo = new JDialogCargarPeriodoContribucion(GuiABMContribuciones.this.getFrame(), catValorPuesto);
			dialogo.setVisible(true);
			if(dialogo.isAcepto()) {
				catValorPuesto = dialogo.getPeriodoContribucion();
				agregarElemento(catValorPuesto);
			}
			return false;
		}
	
	}

}
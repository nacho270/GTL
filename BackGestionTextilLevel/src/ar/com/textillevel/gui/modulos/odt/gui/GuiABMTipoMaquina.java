package ar.com.textillevel.gui.modulos.odt.gui;

import static ar.com.textillevel.gui.util.GenericUtils.createGridBagConstraints;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.taglibs.string.util.StringW;

import ar.clarin.fwjava.componentes.CLJNumericTextField;
import ar.clarin.fwjava.componentes.CLJOptionPane;
import ar.clarin.fwjava.componentes.CLJTable;
import ar.clarin.fwjava.componentes.CLJTextField;
import ar.clarin.fwjava.componentes.PanelTabla;
import ar.clarin.fwjava.componentes.error.validaciones.ValidacionException;
import ar.clarin.fwjava.templates.GuiABMListaTemplate;
import ar.clarin.fwjava.util.GuiUtil;
import ar.com.textillevel.gui.modulos.odt.gui.procedimientos.JDialogAgregarModificarProcesoTipoMaquina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.TipoMaquina;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.InstruccionProcedimiento;
import ar.com.textillevel.modulos.odt.entidades.maquinas.procesos.ProcesoTipoMaquina;
import ar.com.textillevel.modulos.odt.facade.api.remote.TipoMaquinaFacadeRemote;
import ar.com.textillevel.util.GTLBeanFactory;

public class GuiABMTipoMaquina extends GuiABMListaTemplate {

	private static final long serialVersionUID = 1840795762852110775L;

	private static final int MAX_LONGITUD_NOMBRE = 50;

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private CLJTextField txtNombre;
	private CLJNumericTextField txtOrden;
	private PanProcesoTipoMaquina panelTablaProcesos;

	private TipoMaquinaFacadeRemote tipoMaquinaFacade;
	private TipoMaquina tipoMaquina;

	public GuiABMTipoMaquina(Integer idModulo) {
		super();
		setHijoCreado(true);
		setTitle("Administrar Sectores");
		constructGui();
		setEstadoInicial();
	}

	private void constructGui() {
		panTabs.addTab("Información del Sector", getTabDetalle());
		getBtnAgregar().setVisible(false);
		getBtnEliminar().setVisible(false);
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
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel("Nombre:"), createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNombre(), createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Órden:"), createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtOrden(), createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getPanelTablaProcesos(), createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 2, 1, 0, 0));
		}
		return panDetalle;
	}

	private CLJTextField getTxtNombre() {
		if (txtNombre == null) {
			txtNombre = new CLJTextField(MAX_LONGITUD_NOMBRE);
		}
		return txtNombre;
	}

	private CLJNumericTextField getTxtOrden() {
		if (txtOrden == null) {
			txtOrden = new CLJNumericTextField();
		}
		return txtOrden;
	}

	private PanProcesoTipoMaquina getPanelTablaProcesos() {
		if (panelTablaProcesos == null) {
			this.panelTablaProcesos = new PanProcesoTipoMaquina();
		}
		return panelTablaProcesos;
	}

	@Override
	public void cargarLista() {
		List<TipoMaquina> tipoMaquinaList = getTipoMaquinaFacade().getAllOrderByOrden();
		lista.removeAll();
		for (TipoMaquina tm : tipoMaquinaList) {
			lista.addItem(tm);
		}
	}

	@Override
	public void botonAgregarPresionado(int nivelNodoSeleccionado) {
		limpiarDatos();
		setTipoMaquinaActual(new TipoMaquina());
		getTxtNombre().requestFocus();
	}

	@Override
	public void botonCancelarPresionado(int nivelNodoSeleccionado) {
		setModoEdicion(false);
		itemSelectorSeleccionado(lista.getSelectedIndex());
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if (lista.getSelectedIndex() >= 0) {
			if (CLJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar el sector seleccionado?", "Confirmación") == CLJOptionPane.YES_OPTION) {
				getTipoMaquinaFacade().remove(getTipoMaquinaActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonGrabarPresionado(int nivelNodoSeleccionado) {
		if (validar()) {
			capturarSetearDatos();
			TipoMaquina tipoMaquinaRefresh;
			try {
				tipoMaquinaRefresh = getTipoMaquinaFacade().save(getTipoMaquinaActual());
				lista.setSelectedValue(tipoMaquinaRefresh, true);
			} catch (ValidacionException e) {
				CLJOptionPane.showErrorMessage(GuiABMTipoMaquina.this, StringW.wordWrap(e.getMensajeError()), "Error");
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return false;
	}

	private boolean validar() {
		if (getTxtNombre().getText().trim().length() == 0) {
			CLJOptionPane.showErrorMessage(this, "Debe ingresar el nombre del sector.", this.getTitle());
			return false;
		}

		if (getTxtOrden().getText().trim().length() == 0) {
			CLJOptionPane.showErrorMessage(this, "Debe ingresar el órden.", this.getTitle());
			return false;
		}

		return true;
	}

	private void capturarSetearDatos() {
		getTipoMaquinaActual().setNombre(getTxtNombre().getText().trim().toUpperCase());
		getTipoMaquinaActual().setOrden(getTxtOrden().getValueWithNull().byteValue());
		
		//fix para que graben las instrucciones
		for(ProcesoTipoMaquina proceso : getTipoMaquinaActual().getProcesos()) {
			for(InstruccionProcedimiento ip: proceso.getInstrucciones()) {
				if(ip.getId() < 0) {
					ip.setId(null);
				}
			}
		}
		
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if (nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtNombre().requestFocus();
			setTipoMaquinaActual(getTipoMaquinaFacade().getByIdEager(getTipoMaquinaActual().getId(),TipoMaquinaFacadeRemote.MASK_PROCESOS | TipoMaquinaFacadeRemote.MASK_SUBPROCESOS | TipoMaquinaFacadeRemote.MASK_INSTRUCCIONES));
			getPanelTablaProcesos().setTipoMaquina(tipoMaquina);
			return true;
		} else {
			CLJOptionPane.showErrorMessage(this, "Debe seleccionar un tipo de máquina", "Error");
			return false;
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		TipoMaquina tpActual = (TipoMaquina) lista.getSelectedValue();
		tpActual = getTipoMaquinaFacade().getByIdEager(tpActual.getId(),TipoMaquinaFacadeRemote.MASK_PROCESOS | TipoMaquinaFacadeRemote.MASK_SUBPROCESOS | TipoMaquinaFacadeRemote.MASK_INSTRUCCIONES);
		setTipoMaquinaActual(tpActual);
		limpiarDatos();
		if (getTipoMaquinaActual() != null) {
			getTxtOrden().setValue(Long.valueOf(getTipoMaquinaActual().getOrden().intValue()));
			getTxtNombre().setText(getTipoMaquinaActual().getNombre());
			getPanelTablaProcesos().setTipoMaquina(tpActual);
		}
	}

	@Override
	public void limpiarDatos() {
		getTxtOrden().setText("");
		getTxtNombre().setText("");
		getPanelTablaProcesos().limpiar();
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
	public void setModoEdicion(boolean estado) {
		GuiUtil.setEstadoPanel(getTabDetalle(), estado);
		getPanelTablaProcesos().setModoConsulta(!estado);
	}

	public TipoMaquinaFacadeRemote getTipoMaquinaFacade() {
		if (tipoMaquinaFacade == null) {
			tipoMaquinaFacade = GTLBeanFactory.getInstance().getBean2(TipoMaquinaFacadeRemote.class);
		}
		return tipoMaquinaFacade;
	}

	public TipoMaquina getTipoMaquinaActual() {
		return tipoMaquina;
	}

	public void setTipoMaquinaActual(TipoMaquina tipoMaquina) {
		this.tipoMaquina = tipoMaquina;
	}

	private class PanProcesoTipoMaquina extends PanelTabla<ProcesoTipoMaquina> {

		private static final long serialVersionUID = 1L;

		private static final int CANT_COLS = 2;
		private static final int COL_PROCESO = 0;
		private static final int COL_OBJ = 1;

		private TipoMaquina tipoMaquina;

		public PanProcesoTipoMaquina() {
			agregarBotonModificar();
		}

		@Override
		public void setModoConsulta(boolean modoConsulta) {
			super.setModoConsulta(modoConsulta);
		}

		public void setTipoMaquina(TipoMaquina tipoMaquina) {
			this.tipoMaquina = tipoMaquina;
			getTabla().removeAllRows();
			agregarElementos(tipoMaquina.getProcesos());
		}

		@Override
		protected CLJTable construirTabla() {
			CLJTable tabla = new CLJTable(0, CANT_COLS);
			tabla.setStringColumn(COL_PROCESO, "PROCESO", 560, 560, true);
			tabla.setHeaderAlignment(COL_PROCESO, CLJTable.CENTER_ALIGN);
			tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
			return tabla;
		}

		@Override
		protected ProcesoTipoMaquina getElemento(int fila) {
			return (ProcesoTipoMaquina) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		protected void agregarElemento(ProcesoTipoMaquina elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_PROCESO] = elemento.getNombre();
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}
		
		@Override
		protected void dobleClickTabla(int filaSeleccionada) {
			if(filaSeleccionada>-1){
				botonModificarPresionado(filaSeleccionada);
			}
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			ProcesoTipoMaquina procesoTipoMaquina = getElemento(filaSeleccionada);
			JDialogAgregarModificarProcesoTipoMaquina dialog = new JDialogAgregarModificarProcesoTipoMaquina(GuiABMTipoMaquina.this.getFrame(), procesoTipoMaquina.getProcedimientos(), procesoTipoMaquina, tipoMaquina.getSectorMaquina());
			dialog.setVisible(true);
			if (dialog.isAcepto()) {
				getTipoMaquinaActual().getProcesos().set(filaSeleccionada, dialog.getProcesoActual());
				getTabla().removeAllRows();
				agregarElementos(getTipoMaquinaActual().getProcesos());
			}
		}

		@Override
		public boolean validarAgregar() {
			JDialogAgregarModificarProcesoTipoMaquina dialog = new JDialogAgregarModificarProcesoTipoMaquina(GuiABMTipoMaquina.this.getFrame(), tipoMaquina.getSectorMaquina());
			dialog.setVisible(true);
			if (dialog.isAcepto()) {
				getTipoMaquinaActual().getProcesos().add(dialog.getProcesoActual());
				agregarElemento(dialog.getProcesoActual());
			}
			return false;
		}

		@Override
		public boolean validarQuitar() {
			int filaSeleccionada = getTabla().getSelectedRow();
			tipoMaquina.getProcesos().remove(filaSeleccionada);
			return true;
		}
	}
}
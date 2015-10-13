package ar.com.textillevel.gui.modulos.personal.abm.conceptosrecibosueldo;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ar.com.fwcommon.componentes.FWJOptionPane;
import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.FWJTextField;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.fwcommon.templates.GuiABMListaTemplate;
import ar.com.fwcommon.util.GuiUtil;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.personal.entidades.legajos.tareas.Sindicato;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.ConceptoReciboSueldo;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.ValorConceptoFecha;
import ar.com.textillevel.modulos.personal.entidades.recibosueldo.conceptos.enums.ETipoConceptoReciboSueldo;
import ar.com.textillevel.modulos.personal.facade.api.remote.ConceptoReciboSueldoFacadeRemote;
import ar.com.textillevel.modulos.personal.facade.api.remote.SindicatoFacadeRemote;
import ar.com.textillevel.modulos.personal.utils.GTLPersonalBeanFactory;

public class GuiABMConceptosReciboSueldo extends GuiABMListaTemplate {

	private static final long serialVersionUID = -7936673595253758718L;

	private JPanel tabDetalle;
	private JPanel panDetalle;

	private JComboBox cmbTipoConcepto;
	private FWJTextField txtNombre;
	private PanelTablaValoresConcepto panelTabla;

	private ConceptoReciboSueldoFacadeRemote conceptoFacade;
	private ConceptoReciboSueldo conceptoActual;

	private List<ValorConceptoFecha> valoresElegidos;
	private boolean edicion;

	public GuiABMConceptosReciboSueldo(Integer idModulo) {
		super();
		setHijoCreado(true);
		setTitle("Configuración de conceptos de recibos de sueldo");
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
			panDetalle.setLayout(new GridBagLayout());
			panDetalle.add(new JLabel("Nombre: "), GenericUtils.createGridBagConstraints(0, 0, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getTxtNombre(), GenericUtils.createGridBagConstraints(1, 0, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(new JLabel("Tipo: "), GenericUtils.createGridBagConstraints(0, 1, GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(10, 10, 5, 5), 1, 1, 0, 0));
			panDetalle.add(getCmbTipoConcepto(), GenericUtils.createGridBagConstraints(1, 1, GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL, new Insets(10, 10, 5, 5), 1, 1, 1, 0));
			panDetalle.add(getPanelTabla(), GenericUtils.createGridBagConstraints(0, 2, GridBagConstraints.EAST, GridBagConstraints.BOTH, new Insets(10, 10, 5, 5), 3, 1, 1, 1));
		}
		return panDetalle;
	}

	@Override
	public void cargarLista() {
		Sindicato sindicatoSeleccionado = (Sindicato) getItemComboMaestroSeleccionado();
		if (sindicatoSeleccionado != null) {
			List<ConceptoReciboSueldo> concs = getConceptoFacade().getAllBySindicato(sindicatoSeleccionado);
			lista.clear();
			if (concs != null) {
				for (ConceptoReciboSueldo c : concs) {
					lista.addItem(c);
				}
			}
		}
	}

	@Override
	public void setEstadoInicial() {
		setModoEdicion(false);
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
		ConceptoReciboSueldo conc = ConceptoReciboSueldoFactory.createConcepto((ETipoConceptoReciboSueldo) getCmbTipoConcepto().getSelectedItem());
		conc.setSindicato(sindicato);
		setConceptoActual(conc);
		setValoresElegidos(new ArrayList<ValorConceptoFecha>());
		setEdicion(false);
	}

	@Override
	public void botonEliminarPresionado(int nivelNodoSeleccionado) {
		if (lista.getSelectedIndex() >= 0) {
			if (FWJOptionPane.showQuestionMessage(this, "¿Está seguro que desea eliminar el concepto para el sindicato seleccionado?", "Confirmación") == FWJOptionPane.YES_OPTION) {
				getConceptoFacade().remove(getConceptoActual());
				itemSelectorSeleccionado(-1);
			}
		}
	}

	@Override
	public boolean botonModificarPresionado(int nivelNodoSeleccionado) {
		if (nivelNodoSeleccionado >= 0) {
			setModoEdicion(true);
			getTxtNombre().requestFocus();
			setEdicion(true);
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
			ConceptoReciboSueldo conc = getConceptoFacade().save(getConceptoActual(),isEdicion());
			FWJOptionPane.showInformationMessage(getFrame(), "El concepto se ha grabado con éxito", "Información");
			lista.setSelectedValue(conc, true);
			return true;
		}
		return false;
	}

	private void capturarDatos() {
		Integer idActual = getConceptoActual().getId();
		setConceptoActual(ConceptoReciboSueldoFactory.createConcepto((ETipoConceptoReciboSueldo) getCmbTipoConcepto().getSelectedItem()));
		getConceptoActual().setId(idActual);
		getConceptoActual().setNombre(getTxtNombre().getText().toUpperCase());
		getConceptoActual().setSindicato((Sindicato) getCmbMaestro().getSelectedItem());
		getConceptoActual().setValoresPorFecha(getValoresElegidos());
	}

	private boolean validar() {
		if (getTxtNombre().getText().trim().length() == 0) {
			FWJOptionPane.showErrorMessage(this, "Debe ingresar el nombre del concepto", "Error");
			getTxtNombre().requestFocus();
			return false;
		}
		if (getValoresElegidos().isEmpty()) {
			FWJOptionPane.showErrorMessage(this, "Debe al menos un valor para el concepto", "Error");
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
		getTxtNombre().setText("");
		getCmbTipoConcepto().setSelectedIndex(0);
		getPanelTabla().limpiar();
	}

	private void llenarDatos() {
		getCmbTipoConcepto().setSelectedItem(getConceptoActual().getTipo());
		getTxtNombre().setText(getConceptoActual().getNombre());
		getPanelTabla().refreshTable();
	}

	@Override
	public void itemSelectorSeleccionado(int nivelItemSelector) {
		if(lista.getSelectedValue()!=null){
			ConceptoReciboSueldo selectedValue = (ConceptoReciboSueldo) lista.getSelectedValue();
			setConceptoActual(selectedValue);
			setValoresElegidos(selectedValue.getValoresPorFecha());
			limpiarDatos();
			if (getConceptoActual() != null) {
				llenarDatos();
			}
		}
	}

	@Override
	public void itemComboMaestroSeleccionado() {
		cargarLista();
	}

	public ConceptoReciboSueldoFacadeRemote getConceptoFacade() {
		if (conceptoFacade == null) {
			conceptoFacade = GTLPersonalBeanFactory.getInstance().getBean2(ConceptoReciboSueldoFacadeRemote.class);
		}
		return conceptoFacade;
	}

	public ConceptoReciboSueldo getConceptoActual() {
		return conceptoActual;
	}

	public void setConceptoActual(ConceptoReciboSueldo conceptoActual) {
		this.conceptoActual = conceptoActual;
	}

	public JComboBox getCmbTipoConcepto() {
		if (cmbTipoConcepto == null) {
			cmbTipoConcepto = new JComboBox();
			GuiUtil.llenarCombo(cmbTipoConcepto, Arrays.asList(ETipoConceptoReciboSueldo.values()), true);
		}
		return cmbTipoConcepto;
	}

	public FWJTextField getTxtNombre() {
		if (txtNombre == null) {
			txtNombre = new FWJTextField();
		}
		return txtNombre;
	}

	private class PanelTablaValoresConcepto extends PanelTabla<ValorConceptoFecha> {

		private static final long serialVersionUID = 8676830682590483028L;

		private static final int CANT_COLS = 3;
		private static final int COL_FECHA = 0;
		private static final int COL_VALOR = 1;
		private static final int COL_OBJ = 2;

		public PanelTablaValoresConcepto() {
			agregarBotonModificar();
		}

		public void refreshTable() {
			limpiar();
			Collections.sort(getValoresElegidos(), new Comparator<ValorConceptoFecha>() {

				public int compare(ValorConceptoFecha o1, ValorConceptoFecha o2) {
					return o1.getFechaDesde().compareTo(o2.getFechaDesde()) * -1;
				}
			});
			for (ValorConceptoFecha valor : getValoresElegidos()) {
				agregarElemento(valor);
			}
		}

		@Override
		protected FWJTable construirTabla() {
			FWJTable tabla = new FWJTable(0, CANT_COLS);
			tabla.setDateColumn(COL_FECHA, "Válido desde", 120, true);
			tabla.setStringColumn(COL_VALOR, "Valor", 100, 100, true);
			tabla.setStringColumn(COL_OBJ, "", 0);
			tabla.setReorderingAllowed(false);
			tabla.setAllowHidingColumns(false);
			tabla.setAllowSorting(false);
			return tabla;
		}

		@Override
		protected void agregarElemento(ValorConceptoFecha elemento) {
			Object[] row = new Object[CANT_COLS];
			row[COL_FECHA] = elemento.getFechaDesde();
			NumberFormat decimalFormat = GenericUtils.getDecimalFormat();
			row[COL_VALOR] = (elemento.getValorNumerico() == null ?decimalFormat.format(elemento.getValorPorcentual())+" %" : "$ " + decimalFormat.format(elemento.getValorNumerico()));
			row[COL_OBJ] = elemento;
			getTabla().addRow(row);
		}

		@Override
		protected ValorConceptoFecha getElemento(int fila) {
			return (ValorConceptoFecha) getTabla().getValueAt(fila, COL_OBJ);
		}

		@Override
		protected String validarElemento(int fila) {
			return null;
		}

		@Override
		public boolean validarAgregar() {
			JDialogAgregarModificarValorConceptoFecha dialog = new JDialogAgregarModificarValorConceptoFecha(getFrame(),getValoresElegidos());
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				getValoresElegidos().add(dialog.getValorActual());
				refreshTable();
			}
			return false;
		}

		@Override
		public boolean validarQuitar() {
			int fila = getTabla().getSelectedRow();
			getValoresElegidos().remove(fila);
			refreshTable();
			return false;
		}

		@Override
		protected void botonModificarPresionado(int filaSeleccionada) {
			JDialogAgregarModificarValorConceptoFecha dialog = new JDialogAgregarModificarValorConceptoFecha(getFrame(),getElemento(filaSeleccionada),getValoresElegidos());
			dialog.setVisible(true);
			if(dialog.isAcepto()){
				
			}
		}
	}

	public PanelTablaValoresConcepto getPanelTabla() {
		if (panelTabla == null) {
			panelTabla = new PanelTablaValoresConcepto();
		}
		return panelTabla;
	}

	public List<ValorConceptoFecha> getValoresElegidos() {
		return valoresElegidos;
	}

	public void setValoresElegidos(List<ValorConceptoFecha> valoresElegidos) {
		this.valoresElegidos = valoresElegidos;
	}

	
	public boolean isEdicion() {
		return edicion;
	}

	
	public void setEdicion(boolean edicion) {
		this.edicion = edicion;
	}
}

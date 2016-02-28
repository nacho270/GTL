package ar.com.textillevel.gui.modulos.odt.gui;

import java.awt.Dialog;
import java.util.ArrayList;
import java.util.List;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.fwcommon.componentes.PanelTabla;
import ar.com.textillevel.entidades.ventas.materiaprima.Formulable;
import ar.com.textillevel.gui.util.GenericUtils;
import ar.com.textillevel.modulos.odt.entidades.maquinas.formulas.MateriaPrimaCantidad;

public abstract class PanelTablaMateriaPrimaCantidad<F extends Formulable, T extends MateriaPrimaCantidad<F>> extends PanelTabla<T> {

	private static final long serialVersionUID = -6495780340286054397L;

	private static final int CANT_COLS = 4;
	private static final int COL_MATERIA_PRIMA = 0;
	private static final int COL_CANTIDAD = 1;
	private static final int COL_UNIDAD = 2;
	private static final int COL_OBJ = 3;
	
	private Dialog owner;
	private String descripcionTipoMateriaPrima;
	private List<F> allFormulablesPosibles;

	public PanelTablaMateriaPrimaCantidad(Dialog owner, String descripcionTipoMateriaPrima, List<F> allFormulablesPosibles) {
		this.owner = owner;
		this.descripcionTipoMateriaPrima = descripcionTipoMateriaPrima;
		this.allFormulablesPosibles = allFormulablesPosibles;
		agregarBotonModificar();
	}

	@Override
	protected void agregarElemento(T elemento) {
		getTabla().addRow(new Object[] { elemento.getDescripcion(), GenericUtils.getDecimalFormat2().format(elemento.getCantidad()), elemento.getUnidad(), elemento });
	}

	@Override
	protected void dobleClickTabla(int filaSeleccionada) {
		if (filaSeleccionada > -1) {
			botonModificarPresionado(filaSeleccionada);
		}
	}

	@Override
	protected FWJTable construirTabla() {
		FWJTable tabla = new FWJTable(0, CANT_COLS);
		tabla.setStringColumn(COL_MATERIA_PRIMA, createLabelTipoMateriaPrima(), 200, 200, true);
		tabla.setStringColumn(COL_CANTIDAD, "Proporción (%)", 100, 100, true);
		tabla.setStringColumn(COL_UNIDAD, "Unidad", 70, 70, true);
		tabla.setStringColumn(COL_OBJ, "", 0);
		tabla.setReorderingAllowed(false);
		tabla.setAllowHidingColumns(false);
		tabla.setAllowSorting(false);
		tabla.setHeaderAlignment(COL_MATERIA_PRIMA, FWJTable.CENTER_ALIGN);
		tabla.setHeaderAlignment(COL_CANTIDAD, FWJTable.CENTER_ALIGN);
		tabla.setHeaderAlignment(COL_UNIDAD, FWJTable.CENTER_ALIGN);
		return tabla;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected T getElemento(int fila) {
		return (T) getTabla().getValueAt(fila, COL_OBJ);
	}

	@Override
	protected String validarElemento(int fila) {
		return null;
	}

	private List<F> formulablesAgregados() {
		List<F> formulables = new ArrayList<F>();
		for(int i=0; i < getTabla().getRowCount(); i++) {
			formulables.add(getElemento(i).getMateriaPrima());
		}
		return formulables;
	}

	@Override
	public boolean validarAgregar() {
		JDialogAgregarModificarMateriaPrimaCantidad<F, T> dialog = new JDialogAgregarModificarMateriaPrimaCantidad<F, T>(owner, descripcionTipoMateriaPrima, new ArrayList<F>(allFormulablesPosibles), formulablesAgregados(), createMateriaPrimaCantidad(), modoConsulta);
		dialog.setVisible(true);
		if (dialog.isAcepto()) {
			agregarElemento(dialog.getMatPrimaCantidadActual());
		}
		return false;
	}

	@Override
	public boolean validarQuitar() {
		return true;
	}

	@Override
	protected void botonModificarPresionado(int filaSeleccionada) {
		T mpc = getElemento(filaSeleccionada);
		List<F> formulablesAgregados = formulablesAgregados();
		formulablesAgregados.remove(mpc.getMateriaPrima());
		JDialogAgregarModificarMateriaPrimaCantidad<F, T> dialog = new JDialogAgregarModificarMateriaPrimaCantidad<F, T>(owner, descripcionTipoMateriaPrima, new ArrayList<F>(allFormulablesPosibles), formulablesAgregados, mpc, modoConsulta);
		dialog.setVisible(true);
		if (dialog.isAcepto()) {
			T matPrimaCantidadActual = dialog.getMatPrimaCantidadActual();
			getTabla().setValueAt(GenericUtils.getDecimalFormat2().format(matPrimaCantidadActual.getCantidad()), filaSeleccionada, COL_CANTIDAD);
			getTabla().setValueAt(matPrimaCantidadActual.getDescripcion(), filaSeleccionada, COL_MATERIA_PRIMA);
			getTabla().setValueAt(matPrimaCantidadActual, filaSeleccionada, COL_OBJ);
			getTabla().setValueAt(matPrimaCantidadActual.getUnidad(), filaSeleccionada, COL_UNIDAD);
		}
	}

	public abstract T createMateriaPrimaCantidad();

	public abstract String createLabelTipoMateriaPrima();

}
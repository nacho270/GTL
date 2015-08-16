package ar.com.textillevel.gui.modulos.abm.listaprecios.tenido;

import java.awt.Dialog;

import ar.clarin.fwjava.componentes.CLJTable;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoArticuloTenido;
import ar.com.textillevel.gui.modulos.abm.listaprecios.PanelTablaRango;

public class PanelTablaRangoTenido extends PanelTablaRango<RangoAnchoArticuloTenido> {

	private static final long serialVersionUID = -6110511633595669633L;

	private static final int CANT_COLS = 2;
	private static final int COL_TIPO_ARTICULO = 0;
	private static final int COL_OBJ = 1;
	
	public PanelTablaRangoTenido(Dialog parent) {
		super(parent);
		agregarBotonModificar();
	}

	@Override
	protected CLJTable construirTabla() {
		CLJTable tabla = new CLJTable(0, CANT_COLS);
		tabla.setStringColumn(COL_TIPO_ARTICULO, "TIPO DE ARTICULO", 200, 200, true);
		tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
		tabla.setHeaderAlignment(COL_TIPO_ARTICULO, CLJTable.CENTER_ALIGN);
		tabla.setAlignment(COL_TIPO_ARTICULO, CLJTable.CENTER_ALIGN);
		return tabla;
	}

	@Override
	protected void agregarElemento(RangoAnchoArticuloTenido elemento) {
		Object[] row = new Object[CANT_COLS];
		row[COL_OBJ] = elemento;
		getTabla().addRow(row);
	}

	@Override
	protected RangoAnchoArticuloTenido getElemento(int fila) {
		return (RangoAnchoArticuloTenido) getTabla().getValueAt(fila, COL_OBJ);
	}

	@Override
	protected String validarElemento(int fila) {
		return null;
	}

	@Override
	public boolean validarAgregar() {
		return false;
	}
	
	@Override
	public boolean validarQuitar() {
		return true;
	}
	
	@Override
	protected void botonAgregarPresionado() {

	}
}

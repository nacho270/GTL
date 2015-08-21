package ar.com.textillevel.gui.modulos.abm.listaprecios.comun;

import ar.clarin.fwjava.componentes.CLJTable;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioTipoArticulo;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoComun;
import ar.com.textillevel.gui.modulos.abm.listaprecios.JDialogAgregarModificarDefinicionPrecios;
import ar.com.textillevel.gui.modulos.abm.listaprecios.PanelTablaRango;

public class PanelTablaRangoComun extends PanelTablaRango<RangoAnchoComun, PrecioTipoArticulo> {

	private static final long serialVersionUID = -6110511633595669633L;

	private static final int CANT_COLS = 4;
	private static final int COL_ANCHO = 0;
	private static final int COL_TIPO_ARTICULO = 1;
	private static final int COL_PRECIO = 2;
	private static final int COL_OBJ = 3;
	
	public PanelTablaRangoComun(JDialogAgregarModificarDefinicionPrecios<RangoAnchoComun, PrecioTipoArticulo> parent) {
		super(parent);
	}

	@Override
	protected CLJTable construirTabla() {
		CLJTable tabla = new CLJTable(0, CANT_COLS);
		tabla.setStringColumn(COL_ANCHO, "ANCHO", 150, 150, true);
		tabla.setStringColumn(COL_TIPO_ARTICULO, "TIPO DE ARTICULO", 150, 150, true);
		tabla.setFloatColumn(COL_PRECIO, "PRECIO", 100, true);
		tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
		tabla.setHeaderAlignment(COL_ANCHO, CLJTable.CENTER_ALIGN);
		tabla.setHeaderAlignment(COL_TIPO_ARTICULO, CLJTable.CENTER_ALIGN);
		tabla.setHeaderAlignment(COL_PRECIO, CLJTable.CENTER_ALIGN);
		tabla.setSelectionMode(CLJTable.SINGLE_SELECTION);
		tabla.setAllowHidingColumns(false);
		tabla.setAllowSorting(false);
		tabla.setReorderingAllowed(false);

		return tabla;
	}

	@Override
	protected void agregarElemento(RangoAnchoComun elemento) {
		for(PrecioTipoArticulo pta : elemento.getPrecios()) {
			Object[] row = new Object[CANT_COLS];
			row[COL_ANCHO] = elemento.toString();
			row[COL_TIPO_ARTICULO] = pta.getTipoArticulo().toString();
			row[COL_PRECIO] = pta.getPrecio();
			row[COL_OBJ] = pta;
			getTabla().addRow(row);
		}
	}

	@Override
	protected RangoAnchoComun getElemento(int fila) {
		return (RangoAnchoComun) getTabla().getValueAt(fila, COL_OBJ);
	}

	@Override
	public boolean validarQuitar() {
		return true;
	}

	@Override
	public int getColObj() {
		return COL_OBJ;
	}

}
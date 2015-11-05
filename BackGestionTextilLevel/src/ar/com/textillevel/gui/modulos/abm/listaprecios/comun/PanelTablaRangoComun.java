package ar.com.textillevel.gui.modulos.abm.listaprecios.comun;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioTipoArticulo;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoComun;
import ar.com.textillevel.gui.modulos.abm.listaprecios.JDialogAgregarModificarDefinicionPrecios;
import ar.com.textillevel.gui.modulos.abm.listaprecios.PanelTablaRango;

public class PanelTablaRangoComun extends PanelTablaRango<RangoAnchoComun, PrecioTipoArticulo> {

	private static final long serialVersionUID = -6110511633595669633L;

	private static final int CANT_COLS = 5;
	private static final int COL_ANCHO = 0;
	private static final int COL_TIPO_ARTICULO = 1;
	private static final int COL_ARTICULO = 2;
	private static final int COL_PRECIO = 3;
	private static final int COL_OBJ = 4;
	
	public PanelTablaRangoComun(JDialogAgregarModificarDefinicionPrecios<RangoAnchoComun, PrecioTipoArticulo> parent) {
		super(parent);
	}

	@Override
	protected FWJTable construirTabla() {
		FWJTable tabla = new FWJTable(0, CANT_COLS);
		tabla.setStringColumn(COL_ANCHO, "ANCHO", 150, 150, true);
		tabla.setStringColumn(COL_TIPO_ARTICULO, "TIPO DE ARTICULO", 150, 150, true);
		tabla.setStringColumn(COL_ARTICULO, "ARTICULO", 150, 150, true);
		tabla.setFloatColumn(COL_PRECIO, "PRECIO", 100, true);
		tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
		tabla.setHeaderAlignment(COL_ANCHO, FWJTable.CENTER_ALIGN);
		tabla.setHeaderAlignment(COL_TIPO_ARTICULO, FWJTable.CENTER_ALIGN);
		tabla.setHeaderAlignment(COL_ARTICULO, FWJTable.CENTER_ALIGN);
		tabla.setHeaderAlignment(COL_PRECIO, FWJTable.CENTER_ALIGN);
		tabla.setSelectionMode(FWJTable.SINGLE_SELECTION);
		tabla.setAllowHidingColumns(false);
		tabla.setAllowSorting(false);
		tabla.setReorderingAllowed(false);

		return tabla;
	}

	@Override
	protected void agregarElemento(RangoAnchoComun elemento) {
		for(PrecioTipoArticulo pta : elemento.getPrecios()) {
			Object[] row = new Object[CANT_COLS];
			row[COL_ANCHO] = elemento.toStringConUnidad(EUnidad.METROS);
			row[COL_TIPO_ARTICULO] = pta.getTipoArticulo().toString();
			row[COL_ARTICULO] = pta.getArticulo();
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
		int selectedRow = getTabla().getSelectedRow();
		PrecioTipoArticulo precioTA = (PrecioTipoArticulo)getTabla().getValueAt(selectedRow, COL_OBJ);
		precioTA.deepRemove();
		return true;
	}

	@Override
	public int getColObj() {
		return COL_OBJ;
	}

}
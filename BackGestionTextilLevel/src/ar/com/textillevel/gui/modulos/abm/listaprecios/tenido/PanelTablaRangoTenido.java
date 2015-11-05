package ar.com.textillevel.gui.modulos.abm.listaprecios.tenido;

import ar.com.fwcommon.componentes.FWJTable;
import ar.com.textillevel.entidades.enums.EUnidad;
import ar.com.textillevel.entidades.ventas.cotizacion.GrupoTipoArticulo;
import ar.com.textillevel.entidades.ventas.cotizacion.GrupoTipoArticuloGama;
import ar.com.textillevel.entidades.ventas.cotizacion.PrecioGama;
import ar.com.textillevel.entidades.ventas.cotizacion.RangoAnchoArticuloTenido;
import ar.com.textillevel.gui.modulos.abm.listaprecios.JDialogAgregarModificarDefinicionPrecios;
import ar.com.textillevel.gui.modulos.abm.listaprecios.PanelTablaRango;

public class PanelTablaRangoTenido extends PanelTablaRango<RangoAnchoArticuloTenido, PrecioGama> {

	private static final long serialVersionUID = -6110511633595669633L;

	private static final int CANT_COLS = 6;
	private static final int COL_ANCHO = 0;
	private static final int COL_TIPO_ARTICULO = 1;
	private static final int COL_ARTICULO = 2;
	private static final int COL_GAMA = 3;
	private static final int COL_PRECIO = 4;
	private static final int COL_OBJ = 5;
	
	public PanelTablaRangoTenido(JDialogAgregarModificarDefinicionPrecios<RangoAnchoArticuloTenido, PrecioGama> parent) {
		super(parent);
	}

	@Override
	protected FWJTable construirTabla() {
		FWJTable tabla = new FWJTable(0, CANT_COLS);
		tabla.setStringColumn(COL_ANCHO, "ANCHO", 150, 150, true);
		tabla.setStringColumn(COL_TIPO_ARTICULO, "TIPO DE ARTICULO", 150, 150, true);
		tabla.setStringColumn(COL_ARTICULO, "ARTICULO", 150, 150, true);
		tabla.setStringColumn(COL_GAMA, "GAMA", 230, 230, true);
		tabla.setFloatColumn(COL_PRECIO, "PRECIO", 100, true);
		tabla.setStringColumn(COL_OBJ, "", 0, 0, true);
		tabla.setHeaderAlignment(COL_ANCHO, FWJTable.CENTER_ALIGN);
		tabla.setHeaderAlignment(COL_TIPO_ARTICULO, FWJTable.CENTER_ALIGN);
		tabla.setHeaderAlignment(COL_ARTICULO, FWJTable.CENTER_ALIGN);
		tabla.setHeaderAlignment(COL_GAMA, FWJTable.CENTER_ALIGN);
		tabla.setHeaderAlignment(COL_PRECIO, FWJTable.CENTER_ALIGN);
		tabla.setSelectionMode(FWJTable.SINGLE_SELECTION);
		tabla.setAllowHidingColumns(false);
		tabla.setAllowSorting(false);
		tabla.setReorderingAllowed(false);

		return tabla;
	}

	@Override
	protected void agregarElemento(RangoAnchoArticuloTenido elemento) {
		for(GrupoTipoArticulo grupo : elemento.getGruposGama()) {
			GrupoTipoArticuloGama grupoTenido = (GrupoTipoArticuloGama) grupo;
			for(PrecioGama pg : grupoTenido.getPrecios()) {
				Object[] row = new Object[CANT_COLS];
				row[COL_ANCHO] = elemento.toStringConUnidad(EUnidad.METROS);
				row[COL_TIPO_ARTICULO] = grupoTenido.getTipoArticulo().toString();
				row[COL_ARTICULO] = grupoTenido.getArticulo();
				row[COL_GAMA] = pg.getGamaCliente().toString();
				row[COL_PRECIO] = pg.getPrecio();
				row[COL_OBJ] = pg;
				getTabla().addRow(row);
			}
		}
	}

	@Override
	protected RangoAnchoArticuloTenido getElemento(int fila) {
		return (RangoAnchoArticuloTenido) getTabla().getValueAt(fila, COL_OBJ);
	}

	@Override
	public boolean validarQuitar() {
		int selectedRow = getTabla().getSelectedRow();
		PrecioGama precioGama = (PrecioGama)getTabla().getValueAt(selectedRow, COL_OBJ);
		precioGama.deepRemove();
		return true;
	}
	
	@Override
	public int getColObj() {
		return COL_OBJ;
	}

}
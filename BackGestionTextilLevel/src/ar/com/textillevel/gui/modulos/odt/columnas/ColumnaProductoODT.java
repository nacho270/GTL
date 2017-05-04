package ar.com.textillevel.gui.modulos.odt.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.modulos.odt.to.ODTTO;

public class ColumnaProductoODT extends ColumnaString<ODTTO>{

	public ColumnaProductoODT() {
		super("Producto");
		setAncho(350);
	}

	@Override
	public String getValor(ODTTO item) {
		return item.getProducto();
	}

}

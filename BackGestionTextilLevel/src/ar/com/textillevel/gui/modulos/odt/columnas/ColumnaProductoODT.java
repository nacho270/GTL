package ar.com.textillevel.gui.modulos.odt.columnas;

import ar.clarin.fwjava.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

public class ColumnaProductoODT extends ColumnaString<OrdenDeTrabajo>{

	public ColumnaProductoODT() {
		super("Producto");
		setAncho(350);
	}

	@Override
	public String getValor(OrdenDeTrabajo item) {
		return item.getProducto().getDescripcion();
	}
}

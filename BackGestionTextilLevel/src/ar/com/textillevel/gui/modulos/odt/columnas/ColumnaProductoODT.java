package ar.com.textillevel.gui.modulos.odt.columnas;

import ar.com.fwcommon.templates.modulo.model.tabla.ColumnaString;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.modulos.odt.entidades.OrdenDeTrabajo;

public class ColumnaProductoODT extends ColumnaString<OrdenDeTrabajo>{

	public ColumnaProductoODT() {
		super("Producto");
		setAncho(350);
	}

	@Override
	public String getValor(OrdenDeTrabajo item) {
		ProductoArticulo productoArticulo = item.getProductoArticulo();
		return productoArticulo != null? productoArticulo.toString() : "";
	}

}

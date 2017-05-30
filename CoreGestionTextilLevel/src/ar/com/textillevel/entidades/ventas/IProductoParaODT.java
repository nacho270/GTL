package ar.com.textillevel.entidades.ventas;

import ar.com.textillevel.entidades.enums.ETipoProducto;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;

public interface IProductoParaODT {

	public ETipoProducto getTipo();

	public Articulo getArticulo();

	public String toStringSinProducto();

}

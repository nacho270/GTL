package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;
import ar.com.textillevel.entidades.ventas.productos.Producto;

@Local
public interface ProductoArticuloDAOLocal extends DAOLocal<ProductoArticulo, Integer> {

	public ProductoArticulo getProductoArticulo(Producto prod, Articulo art);

}

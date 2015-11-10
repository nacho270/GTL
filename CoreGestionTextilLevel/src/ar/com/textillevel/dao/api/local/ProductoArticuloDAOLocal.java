package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;

@Local
public interface ProductoArticuloDAOLocal extends DAOLocal<ProductoArticulo, Integer> {

	public ProductoArticulo getProductoArticulo(ProductoArticulo pa);

}

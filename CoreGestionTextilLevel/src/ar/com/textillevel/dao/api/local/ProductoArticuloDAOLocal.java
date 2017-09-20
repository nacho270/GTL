package ar.com.textillevel.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;
import ar.com.textillevel.entidades.ventas.articulos.DibujoEstampado;

@Local
public interface ProductoArticuloDAOLocal extends DAOLocal<ProductoArticulo, Integer> {

	public ProductoArticulo getProductoArticulo(ProductoArticulo pa);
	
	public List<ProductoArticulo> getProductosArticuloByDibujo(DibujoEstampado dibujo);

}

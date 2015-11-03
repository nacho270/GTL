package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.ventas.productos.Producto;

@Local
public interface ProductoDAOLocal extends DAOLocal<Producto, Integer> {

	public Producto getProductoDelMismoTipoYDatos(Producto producto);

}

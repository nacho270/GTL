package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.ProductoDAOLocal;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.facade.api.remote.ProductoFacadeRemote;

@Stateless
public class ProductoFacade implements ProductoFacadeRemote {

	@EJB
	private ProductoDAOLocal productoDao;

	public List<Producto> getAllOrderByName() {
		return productoDao.getAllOrderBy("descripcion");
	}

	public void remove(Producto producto) {
		productoDao.removeById(producto.getId());
	}

	public Producto save(Producto producto) {
		Producto productoDelMismoTipoYDatos = productoDao.getProductoDelMismoTipoYDatos(producto);
		if(productoDelMismoTipoYDatos == null) {
			return productoDao.save(producto);
		}
		return productoDelMismoTipoYDatos;
	}

	public void saveAll(List<? extends Producto> prods) {
		for(Producto p : prods) {
			if(productoDao.getProductoDelMismoTipoYDatos(p) == null) {
				productoDao.save(p);
			}
		}
	}

}
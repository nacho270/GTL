package ar.com.textillevel.facade.impl;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.ProductoDAOLocal;
import ar.com.textillevel.entidades.ventas.productos.Producto;
import ar.com.textillevel.facade.api.remote.ProductoFacadeRemote;

@Stateless
public class ProductoFacade implements ProductoFacadeRemote{

	@EJB
	private ProductoDAOLocal productoDao;
	
	public List<Producto> getAllOrderByName() {
		return productoDao.getAllOrderBy("descripcion");
	}

	public void remove(Producto producto) {
		productoDao.removeById(producto.getId());
	}

	public Producto save(Producto producto) {
		return productoDao.save(producto);
	}

	public void saveAll(List<? extends Producto> prods) {
		for(Producto p : prods){
			if(this.getProductoByNombre(p.getDescripcion()) == null){
				productoDao.save(p);
			}
		}
	}

	public Producto getProductoByNombre(String nombre){
		return productoDao.getProductoByNombre(nombre);
	}
}

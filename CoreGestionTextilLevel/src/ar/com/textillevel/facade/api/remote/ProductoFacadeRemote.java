package ar.com.textillevel.facade.api.remote;

import java.util.List;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.ventas.productos.Producto;

@Remote
public interface ProductoFacadeRemote {
	
	public Producto save(Producto producto);
	public void remove(Producto producto);
	public List<Producto> getAllOrderByName();
	public void saveAll(List<? extends Producto> prods);
	public Producto getById(Integer idProducto);

}

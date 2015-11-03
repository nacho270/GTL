package ar.com.textillevel.facade.api.remote;

import java.util.List;
import javax.ejb.Remote;
import ar.com.textillevel.entidades.ventas.ProductoArticulo;

@Remote
public interface ProductoArticuloFacadeRemote {

	public List<ProductoArticulo> save(List<ProductoArticulo> paList);

}

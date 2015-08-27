package ar.com.textillevel.dao.api.local;

import java.util.List;
import javax.ejb.Local;
import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.ventas.articulos.Articulo;

@Local
public interface ArticuloDAOLocal extends DAOLocal<Articulo, Integer>{

	public List<Articulo> getArticulosConAlgunaPMPConStockInicial();
	
	public List<Articulo> getArticulosConTipoArticuloFetched();
	
}

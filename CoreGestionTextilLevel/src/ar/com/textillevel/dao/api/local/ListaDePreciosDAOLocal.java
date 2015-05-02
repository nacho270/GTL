package ar.com.textillevel.dao.api.local;

import javax.ejb.Local;

import ar.clarin.fwjava.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.ventas.productos.ListaDePrecios;

@Local
public interface ListaDePreciosDAOLocal extends DAOLocal<ListaDePrecios, Integer> {

	public ListaDePrecios getListaByIdCliente(Integer idCliente);

}

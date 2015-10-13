package ar.com.textillevel.dao.api.local;

import java.util.List;

import javax.ejb.Local;

import ar.com.fwcommon.dao.api.local.DAOLocal;
import ar.com.textillevel.entidades.gente.Cliente;
import ar.com.textillevel.entidades.ventas.cotizacion.ListaDePrecios;

@Local
public interface ListaDePreciosDAOLocal extends DAOLocal<ListaDePrecios, Integer> {

	public ListaDePrecios getListaByIdCliente(Integer idCliente);

	public List<Cliente> getClientesConListaDePrecios();

}

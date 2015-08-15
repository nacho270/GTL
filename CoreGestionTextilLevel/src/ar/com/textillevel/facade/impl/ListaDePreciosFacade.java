package ar.com.textillevel.facade.impl;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import ar.com.textillevel.dao.api.local.ListaDePreciosDAOLocal;
import ar.com.textillevel.entidades.ventas.cotizacion.ListaDePrecios;
import ar.com.textillevel.facade.api.remote.ListaDePreciosFacadeRemote;

@Stateless
public class ListaDePreciosFacade implements ListaDePreciosFacadeRemote {

	@EJB
	private ListaDePreciosDAOLocal listaDePreciosDAOLocal;
	
	public ListaDePrecios getListaByIdCliente(Integer idCliente) {
		return listaDePreciosDAOLocal.getListaByIdCliente(idCliente);
	}

	public ListaDePrecios save(ListaDePrecios listaDePreciosActual) {
		return listaDePreciosDAOLocal.save(listaDePreciosActual);
	}

	public void remove(ListaDePrecios listaDePreciosActual) {
		listaDePreciosDAOLocal.removeById(listaDePreciosActual.getId());
	}
}

package ar.com.textillevel.facade.api.remote;

import javax.ejb.Remote;

import ar.com.textillevel.entidades.ventas.cotizacion.ListaDePrecios;

@Remote
public interface ListaDePreciosFacadeRemote {

	public ListaDePrecios getListaByIdCliente(Integer idCliente);

	public ListaDePrecios save(ListaDePrecios listaDePrecios);

	public void remove(ListaDePrecios listaDePrecios);

}
